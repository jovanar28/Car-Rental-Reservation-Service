package com.example.sherlock_chan_car_rental_service.service.impl;

import com.example.sherlock_chan_car_rental_service.exception.NotFoundException;
import com.example.sherlock_chan_car_rental_service.user_service.dtos.DiscountDto;
import com.example.sherlock_chan_car_rental_service.user_service.dtos.UserDto;
import io.github.resilience4j.retry.Retry;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class Dekorator {

    private Retry userServiceRetry;

        private int getDiscountByUserId(Long user_id,RestTemplate userServiceRestTemplate) {
        System.out.println(user_id);
        ResponseEntity<DiscountDto> discountResponseEntity = null;
        try{
            discountResponseEntity = userServiceRestTemplate.exchange("/user/discount/" + user_id, HttpMethod.GET, null, DiscountDto.class);
        }catch (HttpClientErrorException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new NotFoundException(String.format("User with id %d not found ", user_id));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(discountResponseEntity.getBody().getDiscount() == null){
            return 0;
        }

        return discountResponseEntity.getBody().getDiscount();
    }

     public int discountRetry(RestTemplate userServiceRestTemplate,Long id){
         return Retry.decorateSupplier(userServiceRetry, () -> getDiscountByUserId(id,userServiceRestTemplate)).get();
     }
    private UserDto getUserById(Long user_id, RestTemplate userServiceRestTemplate){
        ResponseEntity<UserDto> userDtoResponseEntity = null;

        try{
            userDtoResponseEntity = userServiceRestTemplate.exchange("/user/findUserById/" + user_id, HttpMethod.GET, null, UserDto.class);
        }catch (HttpClientErrorException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new NotFoundException(String.format("User with id %d not found", user_id));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        if(userDtoResponseEntity != null){
            UserDto userDto = new UserDto();
            userDto.setId(userDtoResponseEntity.getBody().getId());
            userDto.setEmail(userDtoResponseEntity.getBody().getEmail());
            userDto.setFirst_name(userDtoResponseEntity.getBody().getFirst_name());
            userDto.setLast_name(userDtoResponseEntity.getBody().getLast_name());
            userDto.setUsername(userDtoResponseEntity.getBody().getUsername());
            return userDto;
        }

        return null;
    }

     public UserDto userRetry(RestTemplate userServiceRestTemplate,Long id){
            return  Retry.decorateSupplier(userServiceRetry, ()->getUserById(id,userServiceRestTemplate)).get();
     }

    private UserDto getManagerById(RestTemplate userServiceRestTemplate,Long company_id){
        ResponseEntity<UserDto> userDtoResponseEntity = null;

        try{
            userDtoResponseEntity = userServiceRestTemplate.exchange("/user/findManager/" + company_id, HttpMethod.GET, null, UserDto.class);
        }catch (HttpClientErrorException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new NotFoundException(String.format("Manager with company id %d not found", company_id));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        if(userDtoResponseEntity != null){
            UserDto userDto = new UserDto();
            userDto.setId(userDtoResponseEntity.getBody().getId());
            userDto.setEmail(userDtoResponseEntity.getBody().getEmail());
            userDto.setFirst_name(userDtoResponseEntity.getBody().getFirst_name());
            userDto.setLast_name(userDtoResponseEntity.getBody().getLast_name());
            userDto.setUsername(userDtoResponseEntity.getBody().getUsername());
            return userDto;
        }

        return null;
    }

    public UserDto managerRetry(RestTemplate userServiceRestTemplate,Long id){
            return Retry.decorateSupplier(userServiceRetry, ()->getManagerById(userServiceRestTemplate,id)).get();
    }

}


