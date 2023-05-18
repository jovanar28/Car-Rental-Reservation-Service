package com.example.sherlock_chan_car_rental_service.service.impl;

import com.example.sherlock_chan_car_rental_service.domain.Company;
import com.example.sherlock_chan_car_rental_service.dto.CompanyCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.CompanyDto;
import com.example.sherlock_chan_car_rental_service.dto.CompanyUpdateDto;
import com.example.sherlock_chan_car_rental_service.exception.NotFoundException;
import com.example.sherlock_chan_car_rental_service.mapper.CompanyMapper;
import com.example.sherlock_chan_car_rental_service.repository.CompanyRepository;
import com.example.sherlock_chan_car_rental_service.security.CheckSecurity;
import com.example.sherlock_chan_car_rental_service.security.service.TokenService;
import com.example.sherlock_chan_car_rental_service.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImplementation implements CompanyService {

    private TokenService tokenService;
    private CompanyRepository companyRepository;
    private CompanyMapper companyMapper;

    public CompanyServiceImplementation(TokenService tokenService, CompanyRepository companyRepository, CompanyMapper companyMapper){
        this.tokenService = tokenService;
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public Page<CompanyDto> findAll(Pageable pageable) {
        return (Page<CompanyDto>) companyRepository.findAll(pageable).map(companyMapper::companyToCompanyDto);
    }

    @Override
    public CompanyDto findById(Long id) {
        return companyRepository
                .findById(id)
                .map(companyMapper::companyToCompanyDto)
                .orElseThrow(() -> new NotFoundException(String.format("Company with provided id %d cannot be found", id)));
    }

    @Override
    public CompanyDto updateCompany(Long id, CompanyUpdateDto companyUpdateDto) {
        Company company = companyRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Company with provided id %d has not been found",id)
                ));

        company.setAddress(companyMapper.companyUpdateDtoToAddress(companyUpdateDto));
        company.setDescription(companyUpdateDto.getDescription());
        company.setName(companyUpdateDto.getName());
        company.setNumVehicls(companyUpdateDto.getNumVehicles());
        company.setId(id);

        companyRepository.save(company);

        return companyMapper.companyToCompanyDto(companyRepository.save(company));
    }

    @Override
    public CompanyDto createCompany(CompanyCreateDto companyCreateDto) {
        Company company = companyMapper.companyCreateDtoToCompany(companyCreateDto);
        companyRepository.save(company);

        return companyMapper.companyToCompanyDto(company);
    }

    @Override
    public CompanyDto deleteCompany(Long id) {
        Company company = companyRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Company with provided id %d cannot be found", id)));

        companyRepository.delete(company);

        return companyMapper.companyToCompanyDto(company);
    }

    @Override
    public CompanyDto findCompanyByName(String name) {

        List<Company> companyList = new ArrayList<>();
        companyList = companyRepository.findAll();

        Company companyret = null;

        for(Company company : companyList){
            if(company.getName().equals(name)){
                companyret = company;
            }
        }

        if(companyret == null)
            return new CompanyDto();

        return companyMapper.companyToCompanyDto(companyret);
    }
}
