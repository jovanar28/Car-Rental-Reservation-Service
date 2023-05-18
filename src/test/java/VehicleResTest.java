import com.example.sherlock_chan_car_rental_service.domain.Company;
import com.example.sherlock_chan_car_rental_service.domain.Model;
import com.example.sherlock_chan_car_rental_service.domain.Type;
import com.example.sherlock_chan_car_rental_service.domain.Vehicle;
import com.example.sherlock_chan_car_rental_service.dto.ReservationCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.ReservationDto;
import com.example.sherlock_chan_car_rental_service.listener.helper.MessageHelper;
import com.example.sherlock_chan_car_rental_service.mapper.*;
import com.example.sherlock_chan_car_rental_service.repository.*;
import com.example.sherlock_chan_car_rental_service.service.impl.Dekorator;
import com.example.sherlock_chan_car_rental_service.service.impl.ReservationServiceImplementation;
import com.example.sherlock_chan_car_rental_service.user_service.dtos.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VehicleResTest {

    @Spy
    @Autowired
    private ReservationServiceImplementation reservationServiceImplementation;

    @Mock
    private Dekorator dekorator;

    @Mock
    private RestTemplate userServiceRestTemplate;

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private MessageHelper messageHelper;

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ModelRepository modelRepository;
    @Mock
    private TypeRepository typeRepository;

    private CompanyMapper companyMapper;
    private VehicleMapper vehicleMapper;
    private TypeMapper typeMapper;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp(){
        companyMapper=new CompanyMapper();
        typeMapper=new TypeMapper();
        modelMapper=new ModelMapper();
        vehicleMapper=new VehicleMapper(modelRepository,typeRepository,modelMapper,typeMapper,companyMapper,companyRepository);
        reservationMapper=new ReservationMapper(companyRepository,vehicleRepository,companyMapper,vehicleMapper);
        reservationServiceImplementation.setDekorator(dekorator);
        reservationServiceImplementation.setUserServiceRestTemplate(userServiceRestTemplate);
        reservationServiceImplementation.setVehicleRepository(vehicleRepository);
        reservationServiceImplementation.setReservationRepository(reservationRepository);
        reservationServiceImplementation.setJmsTemplate(jmsTemplate);
        reservationServiceImplementation.setReservationMapper(reservationMapper);
        reservationServiceImplementation.setMessageHelper(messageHelper);

        Vehicle vehicle=new Vehicle();
        vehicle.setId(1l);
        Model model=new Model();
        model.setPrice(10.0);
        Type type=new Type();
        type.setId(1L);
        vehicle.setType(type);
        vehicle.setModel(model);
        Company company=new Company();
        company.setId(1L);
        vehicle.setCompany(company);
        UserDto userDto=new UserDto();
        userDto.setId(1L);

        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(vehicle));
        when(dekorator.userRetry(userServiceRestTemplate,1L)).thenReturn(userDto);
        when(dekorator.managerRetry(userServiceRestTemplate,1L)).thenReturn(userDto);

    }

    @Test
    public void noVehicle(){
        ReservationCreateDto reservationCreateDto=new ReservationCreateDto();
        reservationCreateDto.setVehicle_id(2L);
        reservationCreateDto.setUser_id(1L);
        reservationCreateDto.setStarting_date(LocalDate.of(2023,2,20));
        reservationCreateDto.setEnding_date(LocalDate.of(2023,2,23));

        ReservationDto rDto=reservationServiceImplementation.reserveVehicle(reservationCreateDto);



    }

    @Test
    public void noDiscount(){
        ReservationCreateDto reservationCreateDto=new ReservationCreateDto();
        reservationCreateDto.setStarting_date(LocalDate.of(2023, 1, 13));
        reservationCreateDto.setEnding_date(LocalDate.of(2023, 1, 17));
        reservationCreateDto.setVehicle_id(1L);
        reservationCreateDto.setUser_id(1L);

        ReservationDto rDto= reservationServiceImplementation.reserveVehicle(reservationCreateDto);

        assertEquals(40,rDto.getTotal_price());
        assertEquals(LocalDate.of(2023,1,13),rDto.getStarting_date());
        assertEquals(LocalDate.of(2023,1,17),rDto.getEnding_date());
        assertEquals(1L,rDto.getUser_id());
        assertEquals(1L,rDto.getCompanyDto().getId());

        verify(vehicleRepository,times(1)).findById(anyLong());
        verify(dekorator,times(1)).userRetry(any(),anyLong());
        verify(dekorator,times(1)).managerRetry(any(),anyLong());

    }

    @Test
    public void discount(){
        ReservationCreateDto reservationCreateDto=new ReservationCreateDto();
        reservationCreateDto.setStarting_date(LocalDate.of(2023, 1, 13));
        reservationCreateDto.setEnding_date(LocalDate.of(2023, 1, 17));
        reservationCreateDto.setVehicle_id(1L);
        reservationCreateDto.setUser_id(1L);
    }

}
