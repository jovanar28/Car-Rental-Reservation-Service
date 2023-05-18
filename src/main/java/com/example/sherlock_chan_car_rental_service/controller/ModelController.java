package com.example.sherlock_chan_car_rental_service.controller;

import com.example.sherlock_chan_car_rental_service.dto.*;
import com.example.sherlock_chan_car_rental_service.security.CheckSecurity;
import com.example.sherlock_chan_car_rental_service.service.ModelService;
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
@RequestMapping("/model")
public class ModelController {

    private ModelService modelService;

    public ModelController(ModelService modelService){
        this.modelService=modelService;
    }

    @ApiOperation(value = "Get all models")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping
    @CheckSecurity(userTypes = {"Admin", "Manager", "Client"})
    public ResponseEntity<Page<ModelDto>> findAllTypes(@RequestHeader("Authorization") String authorization,
                                                       @ApiIgnore Pageable pageable){
        return new ResponseEntity<>(modelService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/listById/{id}")
    @CheckSecurity(userTypes = {"Admin","Manager","Client"})
    public ResponseEntity<ModelDto> findById(@RequestHeader("Authorization") String authorization,
                                             @PathVariable("id") Long id) {
        return new ResponseEntity<>(modelService.findById(id), HttpStatus.OK);
    }
    @GetMapping("/listByName/{id}")
    @CheckSecurity(userTypes = {"Admin","Manager","Client"})
    public ResponseEntity<String> findModelNameById(@RequestHeader("Authorization") String authorization,
                                                    @PathVariable("id") Long id){
        return new ResponseEntity<>(modelService.findModelNameById(id),HttpStatus.OK);
    }
    @PostMapping("/createModel")
    @CheckSecurity(userTypes = {"Manager"})
    public ResponseEntity<ModelDto> createModel(@RequestHeader("Authorization") String authorization,
                                                @RequestBody @Valid ModelCreateDto modelCreateDto) {
        return new ResponseEntity<>(modelService.createModel(modelCreateDto), HttpStatus.CREATED);
    }
    @PutMapping("/edit/{id}")
    @CheckSecurity(userTypes = {"Manager"})
    public ResponseEntity<ModelDto> updateModel(@RequestHeader("Authorization") String authorization,
                                                @PathVariable("id") Long id,
                                              @RequestBody @Valid ModelUpdateDto modelUpdateDto) {
        return new ResponseEntity<>(modelService.updateModel(id,modelUpdateDto), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    @CheckSecurity(userTypes = {"Manager"})
    void delete(@RequestHeader("Authorization") String authorization,
                @PathVariable("id") Long id) {
        modelService.deleteById(id);

    }
}
