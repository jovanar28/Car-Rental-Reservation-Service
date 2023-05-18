package com.example.sherlock_chan_car_rental_service.mapper;

import com.example.sherlock_chan_car_rental_service.domain.Company;
import com.example.sherlock_chan_car_rental_service.domain.Review;
import com.example.sherlock_chan_car_rental_service.dto.ReviewCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.ReviewDto;
import com.example.sherlock_chan_car_rental_service.exception.NotFoundException;
import com.example.sherlock_chan_car_rental_service.repository.CompanyRepository;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private CompanyRepository companyRepository;
    private CompanyMapper companyMapper;

    public ReviewMapper(CompanyRepository companyRepository, CompanyMapper companyMapper){
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    public ReviewDto reviewToReviewDto(Review review){
        ReviewDto reviewDto=new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setComment(review.getComment());
        reviewDto.setStar(review.getStar());
        reviewDto.setCompanyDto(companyMapper.companyToCompanyDto(review.getCompany()));
        return reviewDto;
    }

    public Review reviewCreateDtoToReview(ReviewCreateDto reviewCreateDto){
        Review review = new Review();
        review.setComment(reviewCreateDto.getComment());
        review.setStar(reviewCreateDto.getStar());

        review.setCompany(companyRepository
                .findById(reviewCreateDto.getCompany_id())
                .orElseThrow(() ->
                        new NotFoundException(String.format("Company with provided id %d has not been found", reviewCreateDto.getCompany_id()))));

        return review;
    }

}
