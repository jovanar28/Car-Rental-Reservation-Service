package com.example.sherlock_chan_car_rental_service.service.impl;

import com.example.sherlock_chan_car_rental_service.domain.Company;
import com.example.sherlock_chan_car_rental_service.domain.Reservation;
import com.example.sherlock_chan_car_rental_service.domain.Vehicle;
import com.example.sherlock_chan_car_rental_service.dto.*;
import com.example.sherlock_chan_car_rental_service.dto.mailDtos.ReservationCancelMailDto;
import com.example.sherlock_chan_car_rental_service.dto.mailDtos.ReservationMailDto;
import com.example.sherlock_chan_car_rental_service.dto.mailDtos.ReservationReminderDto;
import com.example.sherlock_chan_car_rental_service.exception.NotFoundException;
import com.example.sherlock_chan_car_rental_service.listener.helper.MessageHelper;
import com.example.sherlock_chan_car_rental_service.mapper.CompanyMapper;
import com.example.sherlock_chan_car_rental_service.mapper.ModelMapper;
import com.example.sherlock_chan_car_rental_service.mapper.ReservationMapper;
import com.example.sherlock_chan_car_rental_service.mapper.VehicleMapper;
import com.example.sherlock_chan_car_rental_service.repository.CompanyRepository;
import com.example.sherlock_chan_car_rental_service.repository.ReservationRepository;
import com.example.sherlock_chan_car_rental_service.repository.VehicleRepository;
import com.example.sherlock_chan_car_rental_service.service.ReservationService;
import com.example.sherlock_chan_car_rental_service.user_service.dtos.ClientDto;
import com.example.sherlock_chan_car_rental_service.user_service.dtos.DiscountDto;
import com.example.sherlock_chan_car_rental_service.user_service.dtos.UserDto;
import io.github.resilience4j.retry.Retry;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class ReservationServiceImplementation implements ReservationService {

    private ReservationMapper reservationMapper;
    private VehicleMapper vehicleMapper;
    private CompanyMapper companyMapper;
    private ReservationRepository reservationRepository;
    private CompanyRepository companyRepository;
    private VehicleRepository vehicleRepository;
    private RestTemplate userServiceRestTemplate;
    private Retry userServiceRetry;
    private JmsTemplate jmsTemplate;
    private String createReservationDest;
    private String cancelReservationDest;
    private String notifyCustomerDest;
    private MessageHelper messageHelper;

    private Dekorator dekorator;
    public ReservationServiceImplementation(ReservationMapper reservationMapper, CompanyRepository companyRepository,
                                            ReservationRepository reservationRepository, VehicleRepository vehicleRepository,
                                            VehicleMapper vehicleMapper, RestTemplate userServiceRestTemplate,
                                            CompanyMapper companyMapper, JmsTemplate jmsTemplate,
                                            @Value("${destination.reservation_create}") String createReservationDest,
                                            @Value("${destination.cancel_reservation}") String cancelReservationDest,
                                            @Value("${destination.notify_customer_new}") String notifyCustomerDest,
                                            MessageHelper messageHelper, Retry userServiceRetry){
        this.reservationMapper = reservationMapper;
        this.companyRepository=companyRepository;
        this.reservationRepository = reservationRepository;
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.userServiceRestTemplate = userServiceRestTemplate;
        this.companyMapper = companyMapper;
        this.jmsTemplate = jmsTemplate;
        this.createReservationDest = createReservationDest;
        this.messageHelper = messageHelper;
        this.cancelReservationDest = cancelReservationDest;
        this.notifyCustomerDest = notifyCustomerDest;
        this.userServiceRetry = userServiceRetry;
    }

    @Override
    public Page<ReservationDto> findAll(Pageable pageable) {
        return (Page<ReservationDto>) reservationRepository
                .findAll(pageable)
                .map(reservationMapper::reservationToReservationDto);
    }

    @Override
    public List<ReservationDto> findByCity(String city_name, boolean sort_by_price) {
        return  reservationRepository
                .findAll().stream()
                .filter(reservation -> reservation.getCompany().getAddress().getCity().equalsIgnoreCase(city_name))
                .map(reservationMapper::reservationToReservationDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<ReservationDto> findByCompany(String company_name, boolean sort_by_price) {
       // Optional<Company> company=companyRepository.findCompanyByName(company_name);
        return reservationRepository
                .findAll().stream()
                .filter(reservation -> reservation.getCompany().getName().equalsIgnoreCase(company_name))
                .map(reservationMapper::reservationToReservationDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<ReservationDto> findByDate(LocalDate start_date, LocalDate end_date, boolean sort_by_price) {

        List<Reservation> fetchedReservations = reservationRepository.findAll();
        List<Reservation> preResult = new ArrayList<>();
        List<ReservationDto> resultList = new ArrayList<>();

        for (Reservation currentReservation : fetchedReservations) {
            if ((start_date.isAfter(currentReservation.getStarting_date()) || start_date.isEqual(currentReservation.getStarting_date())
                    && (end_date.isEqual(currentReservation.getEnding_date()) || end_date.isBefore(currentReservation.getEnding_date())))
                    && currentReservation.getIs_active() == 0) {
                if (!preResult.contains(currentReservation)) {
                    preResult.add(currentReservation);
                }
            }
        }

        preResult.sort(new Comparator<Reservation>() {
            @Override
            public int compare(Reservation o1, Reservation o2) {
                if (sort_by_price) {
                    return o1.getVehicle().getModel().getPrice().compareTo(o2.getVehicle().getModel().getPrice());
                } else {
                    return (-1) * o1.getVehicle().getModel().getPrice().compareTo(o2.getVehicle().getModel().getPrice());
                }
            }
        });

        for (Reservation reservation : preResult) {
            resultList.add(reservationMapper.reservationToReservationDto(reservation));
        }

        return resultList;
    }

    @Override
    public List<ReservationDto> filterByAll(String vehicle_type, String city_name, String company_name, LocalDate start_date, LocalDate end_date, boolean sort_by_price) {
        List<Reservation> fetchedReservations = reservationRepository.findAll();
        List<Reservation> preResult = new ArrayList<>();
        List<ReservationDto> resultList = new ArrayList<>();

        for(int i = 0;i<fetchedReservations.size();i++){
            Reservation currentReservation = fetchedReservations.get(i);
            // filter by company

            if(currentReservation.getVehicle().getType().getName().equalsIgnoreCase(vehicle_type)){
                if(currentReservation.getCompany().getName().equalsIgnoreCase(company_name)){
                    if(currentReservation.getCompany().getAddress().getCity().equalsIgnoreCase(city_name)){
                        if ((start_date.isAfter(currentReservation.getStarting_date()) || start_date.isEqual(currentReservation.getStarting_date())
                                && (end_date.isEqual(currentReservation.getEnding_date()) || end_date.isBefore(currentReservation.getEnding_date())))
                                && currentReservation.getIs_active() == 0) {
                            if(!preResult.contains(currentReservation)){
                                preResult.add(currentReservation);
                            }
                        }
                    }
                }
            }
        }

        preResult.sort(new Comparator<Reservation>() {
            @Override
            public int compare(Reservation o1, Reservation o2) {
                if(sort_by_price){
                    return o1.getVehicle().getModel().getPrice().compareTo(o2.getVehicle().getModel().getPrice());
                }else{
                    return (-1) * o1.getVehicle().getModel().getPrice().compareTo(o2.getVehicle().getModel().getPrice());
                }
            }
        });

        for(Reservation reservation : preResult){
            resultList.add(reservationMapper.reservationToReservationDto(reservation));
        }

        return resultList;
    }

    @Override
    public ReservationDto createReservationByModel(ReservationCreateByModelDto reservationCreateByModelDto) {
        LocalDate start_date = reservationCreateByModelDto.getStarting_date();
        LocalDate end_date = reservationCreateByModelDto.getEnding_date();
        Long modelId = reservationCreateByModelDto.getModel_id();
        Long company_id = reservationCreateByModelDto.getCompany_id();

        System.out.println("USER ID " + reservationCreateByModelDto.getUser_id());

//        int user_discount = getDiscountByUserId(reservationCreateByModelDto.getUser_id());

        int user_discount = Retry.decorateSupplier(userServiceRetry, () -> getDiscountByUserId(reservationCreateByModelDto.getUser_id())).get();

        List<Vehicle> availableVehicles = listAvailableVehiclesNew(start_date, end_date)
                .stream()
                .map(vehicleMapper::vehicleDtoToVehicle)
                .collect(Collectors.toList());

        boolean isAvailable = false;

        Vehicle vehicleToReserve = null;

        for(Vehicle vehicle : availableVehicles){
            if((vehicle.getModel().getId().equals(modelId)) && (vehicle.getCompany().getId().equals(company_id))){
                isAvailable = true;
                vehicleToReserve = vehicle;
                break;
            }
        }

        if(isAvailable){
            long datesDiff = ChronoUnit.DAYS.between(start_date, end_date);

            double total_price = 0.0;
            double per_day = vehicleToReserve.getModel().getPrice();

            if(user_discount != 0){
                total_price = ((100 - user_discount) * per_day * datesDiff) / 100;
            }else{
                total_price = per_day * datesDiff;
            }

            Reservation reservation = new Reservation();

            Company company = companyRepository.findById(company_id)
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Company with provided id %d cannot be found", company_id)
                    ));

            reservation.setUser_id(reservationCreateByModelDto.getUser_id());
            reservation.setCompany(company);
            reservation.setIs_active(1);
            reservation.setTotal_price(total_price);
            reservation.setStarting_date(start_date);
            reservation.setEnding_date(end_date);
            reservation.setVehicle(vehicleToReserve);

            callIncrease(reservationCreateByModelDto.getUser_id());

            return reservationMapper.reservationToReservationDto(reservationRepository.save(reservation));
        }else{
            System.out.println("Cannot make reservation");
            return null;
        }
    }

    @Override
    public ReservationDto reserveVehicle(ReservationCreateDto reservationCreateDto) {
        LocalDate start_date = reservationCreateDto.getStarting_date();
        LocalDate end_date = reservationCreateDto.getEnding_date();


      //  int user_discount = Retry.decorateSupplier(userServiceRetry, () -> getDiscountByUserId(reservationCreateDto.getUser_id())).get();
         int user_discount=dekorator.discountRetry(userServiceRestTemplate,reservationCreateDto.getUser_id());
        Vehicle vehicle = vehicleRepository
                .findById(reservationCreateDto.getVehicle_id())
                .orElseThrow(() -> new NotFoundException(String.format("Vehicle with id %d cannot be found", reservationCreateDto.getVehicle_id())));

        double total_price = 0.0;
        double per_day = vehicle.getModel().getPrice();
        long datesDiff = ChronoUnit.DAYS.between(start_date, end_date);

        if(user_discount != 0){
            total_price = ((100 - user_discount) * per_day * datesDiff) / 100;
        }else{
            total_price = per_day * datesDiff;
        }

        Company company = vehicle.getCompany();

        Reservation reservation = new Reservation();
        reservation.setUser_id(reservationCreateDto.getUser_id());
        reservation.setCompany(company);
        reservation.setVehicle(vehicle);
        reservation.setIs_active(1);
        reservation.setStarting_date(start_date);
        reservation.setEnding_date(end_date);
        reservation.setTotal_price(total_price);

        callIncrease(reservationCreateDto.getUser_id());

//        UserDto uDto = Retry.decorateSupplier(userServiceRetry, ()->getUserById(reservationCreateDto.getUser_id())).get();
        UserDto uDto=dekorator.userRetry(userServiceRestTemplate,reservationCreateDto.getUser_id());
     //   System.out.println("Company id " + company.getId());
//        UserDto mDto = getManagerById(company.getId());
     //   UserDto mDto = Retry.decorateSupplier(userServiceRetry, ()->getManagerById(company.getId())).get();
        UserDto mDto=dekorator.managerRetry(userServiceRestTemplate,company.getId());

        if(uDto == null || mDto == null)
            return new ReservationDto();

        reservationRepository.save(reservation);

        System.out.println("Manager name " + mDto.getFirst_name());

        ReservationMailDto reservationMailDto = new ReservationMailDto();
        String vehicleName = vehicle.getModel().getName() + " " + vehicle.getType().getName();
        reservationMailDto.setVehicle(vehicleName);
        reservationMailDto.setStart_date(start_date);
        reservationMailDto.setEnd_date(end_date);
        reservationMailDto.setDiscount(user_discount);
        reservationMailDto.setTotal_price(total_price);
        reservationMailDto.setCompany_name(company.getName());
        reservationMailDto.setFirstName(uDto.getFirst_name());
        reservationMailDto.setLast_name(uDto.getLast_name());
        reservationMailDto.setEmail(uDto.getEmail());

        reservationMailDto.setCompany_manager_name(mDto.getFirst_name());
        reservationMailDto.setCompany_manager_lastname(mDto.getLast_name());
        reservationMailDto.setCompany_manager_email(mDto.getEmail());

        jmsTemplate.convertAndSend(createReservationDest, messageHelper.createTextMessage(reservationMailDto));
        return reservationMapper.reservationToReservationDto(reservation);
    }

    @Override
    public ReservationDto createReservationByType(ReservationCreateByTypeDto reservationCreateByTypeDto) {

        /** FUNKCIJA KOJA TREBA DA SE ZAVRSI
         *
         *  Korisnik u reservationCreateDto salje i userid, i vehicle id i company id i datum
         *  Treba da docupam listu dostupnih vozila i da proverim da li se vozilo sa typeId
         *  i sa kompani id iz createDto nalazi u listi dostupnih vozila. Ako se nalazi,
         *  izvrsiti kreiranje rezervacije.
         *
         * */
        LocalDate start_date = reservationCreateByTypeDto.getStarting_date();
        LocalDate end_date = reservationCreateByTypeDto.getEnding_date();
        Long typeId = reservationCreateByTypeDto.getType_id();
        Long company_id = reservationCreateByTypeDto.getCompany_id();

//        System.out.println("USER ID " + reservationCreateDto.getUser_id());

//        int user_discount = getDiscountByUserId(reservationCreateByTypeDto.getUser_id());

        int user_discount = Retry.decorateSupplier(userServiceRetry, () -> getDiscountByUserId(reservationCreateByTypeDto.getUser_id())).get();

        List<Vehicle> availableVehicles = listAvailableVehiclesNew(start_date, end_date)
                .stream()
                .map(vehicleMapper::vehicleDtoToVehicle)
                .collect(Collectors.toList());

        boolean isAvailable = false;

        Vehicle vehicleToReserve = null;

        for(Vehicle vehicle : availableVehicles){
            // treba razmotriti da li se rezervacija po tipu odnosi na to da nadjem prvi slobodan tip
            // za ovaj termin bez obzira na kompaniju
            if((vehicle.getType().getId().equals(typeId)) && vehicle.getCompany().getId().equals(company_id)){
                isAvailable = true;
                vehicleToReserve = vehicle;
                break;
            }
        }

        if(isAvailable){
            long datesDiff = ChronoUnit.DAYS.between(start_date, end_date);

            double total_price = 0.0;
            double per_day = vehicleToReserve.getModel().getPrice();

//            System.out.println(vehicleToReserve.getModel().getPrice());

            if(user_discount != 0){
                total_price = ((100 - user_discount) * per_day * datesDiff) / 100;
            }else{
                total_price = per_day * datesDiff;
            }

            Reservation reservation = new Reservation();
            Company company = companyRepository.findById(company_id).orElseThrow(() -> new NotFoundException(
                    String.format("Company with id %d not found", company_id)
            ));

            reservation.setUser_id(reservationCreateByTypeDto.getUser_id());
            reservation.setCompany(company);
            reservation.setIs_active(1);
            reservation.setTotal_price(total_price);
            reservation.setStarting_date(start_date);
            reservation.setEnding_date(end_date);
            reservation.setVehicle(vehicleToReserve);

            // Povecati korisniku broj dosadasnjih iznajmljivanja
            callIncrease(reservation.getUser_id());

            return reservationMapper.reservationToReservationDto(reservationRepository.save(reservation));
        }else{
            System.out.println("Cannot make reservation !");
            return null;
        }
    }

    @Override
    public ReservationDto cancelReservation(Long reservation_id) {
        Reservation reservation = reservationRepository
                .findById(reservation_id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Reservation with provided id %d cannot be found", reservation_id)
                ));

        System.out.println(reservation.getId());

        Vehicle vehicle = reservation.getVehicle();
        Company company = reservation.getCompany();
//        UserDto userDto = getUserById(reservation.getUser_id());
        UserDto userDto = Retry.decorateSupplier(userServiceRetry, () -> getUserById(reservation.getUser_id())).get();
//        UserDto managerDto = getManagerById(company.getId());
        UserDto managerDto = Retry.decorateSupplier(userServiceRetry, () -> getUserById(reservation.getUser_id())).get();

        if(userDto == null || managerDto == null)
            return new ReservationDto();

        ReservationCancelMailDto reservationCancelMailDto = new ReservationCancelMailDto();
        reservationCancelMailDto.setManager_email(managerDto.getEmail());
        reservationCancelMailDto.setManager_name(managerDto.getFirst_name());
        reservationCancelMailDto.setVehicle_name(vehicle.getModel().getName() + " " + vehicle.getType().getName());
        reservationCancelMailDto.setStart_date(reservation.getStarting_date());
        reservationCancelMailDto.setEnd_date(reservation.getEnding_date());
        reservationCancelMailDto.setUser_email(userDto.getEmail());
        reservationCancelMailDto.setUser_name(userDto.getFirst_name());
        reservationCancelMailDto.setUser_last_name(userDto.getLast_name());

        reservationRepository.delete(reservation);
        callDecrease(reservation.getUser_id());

        jmsTemplate.convertAndSend(cancelReservationDest, messageHelper.createTextMessage(reservationCancelMailDto));

        return reservationMapper.reservationToReservationDto(reservation);
    }

    private void callIncrease(Long user_id){
        ResponseEntity<ClientDto> userResponseEntity = null;
        try{
            userResponseEntity = userServiceRestTemplate.exchange("/user/increaseReservations/" + user_id, HttpMethod.GET, null, ClientDto.class);
        }catch (HttpClientErrorException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new NotFoundException(String.format("User with id %d not found " , user_id));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void callDecrease(Long user_id){
        ResponseEntity<ClientDto> userResponseEntity = null;

        try{
            userResponseEntity = userServiceRestTemplate.exchange("/user/decreaseReservations/" + user_id, HttpMethod.GET, null, ClientDto.class);
        }catch (HttpClientErrorException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new NotFoundException(String.format("User with id %d not found ", user_id));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private int getDiscountByUserId(Long user_id) {
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

    private UserDto getManagerById(Long company_id){
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

    private UserDto getUserById(Long user_id){
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

    public List<VehicleDto> listAvailableVehicles(){
        List<Vehicle> vehiclesResultSet = vehicleRepository.findAll();
        List<Reservation> reservationsResultSet = reservationRepository.findAll();
        List<Vehicle> resultVehicles = new ArrayList<>();
        List<VehicleDto> resultDtos = new ArrayList<>();

        System.out.println(reservationsResultSet.size());

        for(Reservation reservation : reservationsResultSet){
            if(!resultVehicles.contains(reservation.getVehicle()))
                resultVehicles.add(reservation.getVehicle());
        }

        vehiclesResultSet.removeAll(resultVehicles);

        for(Vehicle vehicle : vehiclesResultSet){
            resultDtos.add(vehicleMapper.vehicleToVehicleDto(vehicle));
        }

        return resultDtos;
    }

    @Override
    public List<VehicleDto> listAvailableVehiclesNew(LocalDate startDate, LocalDate endDate){
        List<Vehicle> vehiclesResultSet = vehicleRepository.findAll();
        List<Reservation> reservationsResultSet = reservationRepository.findAll();

        for(Reservation reservation : reservationsResultSet){
            Vehicle vehicle = reservation.getVehicle();
            boolean res = checkDateRange(reservation.getStarting_date(), startDate);

            if(res){
                vehiclesResultSet.remove(vehicle);
            }
        }

        return vehiclesResultSet
                .stream()
                .map(vehicleMapper::vehicleToVehicleDto)
                .collect(Collectors.toList());
    }

    private boolean checkDateRange(LocalDate res_start_date, LocalDate user_start_date){
        if(res_start_date.isEqual(user_start_date)){
            return true;
        }
        return false;
    }

    @Scheduled(cron = "0 0 * * * *")
    private void checkReservations(){
        List<Reservation> reservations = reservationRepository.findAll();
        List<Reservation> reservationsToNotify = new ArrayList<>();

        LocalDate current_date = LocalDate.now();

        for(Reservation reservation : reservations){
            long datesDiff = ChronoUnit.DAYS.between(current_date, reservation.getStarting_date());
            System.out.println(datesDiff);
            if(datesDiff <= 3){
                reservationsToNotify.add(reservation);
            }
        }

        for(Reservation reservation : reservationsToNotify){
            ReservationReminderDto reservationReminderDto = new ReservationReminderDto();
//            UserDto userDto = getUserById(reservation.getUser_id());
            UserDto userDto = Retry.decorateSupplier(userServiceRetry, () -> getUserById(reservation.getUser_id())).get();

            System.out.println(userDto.getEmail());

            if(userDto == null)
                continue;

            reservationReminderDto.setCustomerEmail(userDto.getEmail());
            reservationReminderDto.setCustomerName(userDto.getFirst_name());
            reservationReminderDto.setVehicleToReserve(reservation.getVehicle().getModel().getName() + " " + reservation.getVehicle().getType().getName());
            reservationReminderDto.setDaysLeft((int) ChronoUnit.DAYS.between(reservation.getStarting_date(), LocalDate.now()));

            jmsTemplate.convertAndSend(notifyCustomerDest, messageHelper.createTextMessage(reservationReminderDto));
        }
    }

    public ReservationServiceImplementation(){

    }

    public Dekorator getDekorator() {
        return dekorator;
    }

    public void setReservationMapper(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    public void setVehicleMapper(VehicleMapper vehicleMapper) {
        this.vehicleMapper = vehicleMapper;
    }

    public void setCompanyMapper(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void setVehicleRepository(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void setUserServiceRestTemplate(RestTemplate userServiceRestTemplate) {
        this.userServiceRestTemplate = userServiceRestTemplate;
    }

    public void setUserServiceRetry(Retry userServiceRetry) {
        this.userServiceRetry = userServiceRetry;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setCreateReservationDest(String createReservationDest) {
        this.createReservationDest = createReservationDest;
    }

    public void setCancelReservationDest(String cancelReservationDest) {
        this.cancelReservationDest = cancelReservationDest;
    }

    public void setNotifyCustomerDest(String notifyCustomerDest) {
        this.notifyCustomerDest = notifyCustomerDest;
    }

    public void setMessageHelper(MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }

    public void setDekorator(Dekorator dekorator) {
        this.dekorator = dekorator;
    }
}
