package com.example.sherlock_chan_car_rental_service.controller;

import com.example.sherlock_chan_car_rental_service.dto.CompanyCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.CompanyDto;
import com.example.sherlock_chan_car_rental_service.dto.CompanyUpdateDto;
import com.example.sherlock_chan_car_rental_service.dto.ModelDto;
import com.example.sherlock_chan_car_rental_service.security.CheckSecurity;
import com.example.sherlock_chan_car_rental_service.service.CompanyService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @ApiOperation(value = "Get all COMPANIES")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping
    @CheckSecurity(userTypes = {"Admin", "Manager", "Client"})
    public ResponseEntity<Page<CompanyDto>> findAllCompanies(@RequestHeader("Authorization") String authorization,
                                                             @ApiIgnore Pageable pageable){
        return new ResponseEntity<>(companyService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/listById/{id}")
    @CheckSecurity(userTypes = {"Admin","Manager","Client"})
    public ResponseEntity<CompanyDto> findById(@RequestHeader("Authorization") String authorization,
                                             @PathVariable("id") Long id) {
        return new ResponseEntity<>(companyService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<CompanyDto> findById(@PathVariable("name") String name) {
        return new ResponseEntity<>(companyService.findCompanyByName(name), HttpStatus.OK);
    }

    @PutMapping("/updateCompany/{id}")
    @CheckSecurity(userTypes = {"Manager"})
    public ResponseEntity<CompanyDto> updateCompany(@RequestHeader("Authorization") String authorization,
                                                    @RequestBody @Valid CompanyUpdateDto companyUpdateDto,
                                                    @PathVariable("id") Long id){
        return new ResponseEntity<>(companyService.updateCompany(id, companyUpdateDto), HttpStatus.OK);
    }

    @PostMapping("/createCompany/")
    @CheckSecurity(userTypes = {"Manager"})
    public ResponseEntity<CompanyDto> createCompany(@RequestHeader("Authorization") String authorization,
                                                    @RequestBody @Valid CompanyCreateDto companyCreateDto){
        return new ResponseEntity<>(companyService.createCompany(companyCreateDto), HttpStatus.OK);
    }

    @DeleteMapping("/deleteCompany/{id}")
    @CheckSecurity(userTypes = {"Manager"})
    public ResponseEntity<CompanyDto> deleteCompany(@RequestHeader("Authorization") String authorization,
                                                    @PathVariable("id") Long id){
        return new ResponseEntity<>(companyService.deleteCompany(id), HttpStatus.OK);
    }
}
