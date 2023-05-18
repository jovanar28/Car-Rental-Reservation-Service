package com.example.sherlock_chan_car_rental_service.service.impl;

import com.example.sherlock_chan_car_rental_service.domain.Company;
import com.example.sherlock_chan_car_rental_service.domain.Review;
import com.example.sherlock_chan_car_rental_service.dto.*;
import com.example.sherlock_chan_car_rental_service.exception.NotFoundException;
import com.example.sherlock_chan_car_rental_service.mapper.CompanyMapper;
import com.example.sherlock_chan_car_rental_service.mapper.ReviewMapper;
import com.example.sherlock_chan_car_rental_service.repository.CompanyRepository;
import com.example.sherlock_chan_car_rental_service.repository.ReviewRepository;
import com.example.sherlock_chan_car_rental_service.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.round;

@Service
public class ReviewServiceImplementation implements ReviewService {
    private ReviewRepository reviewRepository;
    private ReviewMapper reviewMapper;

    private CompanyRepository companyRepository;

    private CompanyMapper companyMapper;

    public ReviewServiceImplementation(ReviewRepository reviewRepository, ReviewMapper reviewMapper,
                                       CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.companyRepository=companyRepository;
        this.companyMapper=companyMapper;
    }

    @Override
    public Page<ReviewDto> findAll(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(reviewMapper::reviewToReviewDto);
    }

    @Override
    public List<CompanyDto> getCompaniesByReview() {
        List<Review> reviews= reviewRepository.findAll();
        List<Company> companies=new ArrayList<>();
        List<CompanyDto> companyDtos=new ArrayList<>();

        for(Review r:reviews){
            if(!(companies.contains(r.getCompany())))
                companies.add(r.getCompany());
        }

        for(int i=0;i<companies.size();i++){
            Company company=companies.get(i);
            double sum=0.0;
            double rank=0.0;
            int cnt=0;

            for(int j=0;j<reviews.size();j++){
                Review review=reviews.get(j);
                if(review.getCompany().equals(company)){
                    sum+=review.getStar();
                    cnt++;
                }
            }
            rank=(double) sum/cnt;
            company.setRank(rank);
        }

        companies.sort(new Comparator<Company>() {
            @Override
            public int compare(Company o1, Company o2) {
                return (-1)*o1.getRank().compareTo(o2.getRank());
            }
        });

        for(Company company:companies){
            companyDtos.add(companyMapper.companyToCompanyDto(company));
        }
        return companyDtos;
    }

    @Override
    public ReviewDto findById(Long id) {
        return reviewRepository.findById(id)
                .map(reviewMapper::reviewToReviewDto)
                .orElseThrow(()->
                        new NotFoundException(String.format("Review with id %d doesnt exist",id)));
    }

    @Override
    public List<ReviewDto> findByCity(String city_name){
        return reviewRepository
                .findAll().stream()
                .filter(review -> review.getCompany().getAddress().getCity().equals(city_name))
                .map(reviewMapper::reviewToReviewDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<ReviewDto> findByCompany(String company_name) {
        return reviewRepository
                .findAll().stream()
                .filter(review->review.getCompany().getName().equals(company_name))
                .map(reviewMapper::reviewToReviewDto)
                .collect(Collectors.toList());
    }
    @Override
    public ReviewDto createReview(ReviewCreateDto reviewCreateDto) {
        Review review=reviewMapper.reviewCreateDtoToReview(reviewCreateDto);
        reviewRepository.save(review);

        Company company = companyRepository
                .findById(reviewCreateDto.getCompany_id())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Company with provided id %d cannot be found", reviewCreateDto.getCompany_id())
                ));

        double companyRank = calculateRankForCompany(company.getName());
        company.setRank(companyRank);

        companyRepository.save(company);

        return reviewMapper.reviewToReviewDto(review);
    }

    private double calculateRankForCompany(String company_name){
        List<ReviewDto> reviews = findByCompany(company_name);

        double rank = 0.0;
        double sum = 0.0;

        for(ReviewDto review : reviews){
            sum += review.getStar();
        }

        rank = sum / reviews.size();

        System.out.println("Rank for company " + company_name + " is " + rank);

        return rank;
    }

    @Override
    public ReviewDto updateReview(Long id, ReviewUpdateDto reviewUpdateDto) {
        Review review=reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Review with id: %d not found",id)));

        review.setId(id);
        review.setStar(reviewUpdateDto.getStar());
        review.setComment(reviewUpdateDto.getComment());

        reviewRepository.save(review);

        // Zaokruzivanje na dve decimale mora da se resi
        // Ili ovo ili prelazak na BigDecimal
        double rank = calculateRankForCompany(review.getCompany().getName());
        review.getCompany().setRank(rank);

        // Ponovno racunanje ranka jer smo promenili velicinu ocene
        companyRepository.save(review.getCompany());

        return reviewMapper.reviewToReviewDto(review);
    }

    @Override
    public void deleteById(Long id) {
        Review review = reviewRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Review with id %d cannot be found", id)));

        Company company = review.getCompany();

        reviewRepository.deleteById(id);

        double rank = calculateRankForCompany(review.getCompany().getName());
        company.setRank(rank);

        companyRepository.save(company);
    }
}
