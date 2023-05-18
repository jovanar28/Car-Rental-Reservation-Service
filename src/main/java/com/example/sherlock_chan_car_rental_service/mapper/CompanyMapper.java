package com.example.sherlock_chan_car_rental_service.mapper;

import com.example.sherlock_chan_car_rental_service.domain.Address;
import com.example.sherlock_chan_car_rental_service.domain.Company;
import com.example.sherlock_chan_car_rental_service.dto.CompanyCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.CompanyDto;
import com.example.sherlock_chan_car_rental_service.dto.CompanyUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public CompanyDto companyToCompanyDto(Company company){
        CompanyDto companyDto=new CompanyDto();
        companyDto.setId(company.getId());
        companyDto.setName(company.getName());
        companyDto.setDescription(company.getDescription());
        companyDto.setNumVehicles(company.getNumVehicls());
        return companyDto;
    }

    public Company companyCreateDtoToCompany(CompanyCreateDto companyCreateDto){
        Company company=new Company();
        company.setName(companyCreateDto.getName());
        company.setDescription(companyCreateDto.getDescription());
        company.setNumVehicls(companyCreateDto.getNumVehicles());
        //adresa
        Address address=new Address();
        address.setCountry(companyCreateDto.getAddressDto().getCountry());
        address.setCity(companyCreateDto.getAddressDto().getCity());
        address.setStreet(companyCreateDto.getAddressDto().getStreet());
        address.setPostcode(companyCreateDto.getAddressDto().getPostcode());
        company.setAddress(address);
        return company;
    }

    public Address companyUpdateDtoToAddress(CompanyUpdateDto companyUpdateDto){
        Address address = new Address();
        address.setCity(companyUpdateDto.getAddressDto().getCity());
        address.setCountry(companyUpdateDto.getAddressDto().getCountry());
        address.setPostcode(companyUpdateDto.getAddressDto().getPostcode());
        address.setStreet(companyUpdateDto.getAddressDto().getStreet());

        return address;
    }

    public Company companyDtoToCompany(CompanyDto companyDto){
        Company company = new Company();
        company.setId(companyDto.getId());
        company.setNumVehicls(companyDto.getNumVehicles());
        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
//        Address address = new Address();
//        address.setCountry(companyDto.getAddressDto().getCountry());
//        address.setCity(companyDto.getAddressDto().getCity());
//        address.setStreet(companyDto.getAddressDto().getStreet());
//        address.setPostcode(companyDto.getAddressDto().getPostcode());
//        company.setAddress(address);
        return company;
    }
}
