package br.com.zup.orangetalentschallenge.resources;

import br.com.zup.orangetalentschallenge.models.Car;
import br.com.zup.orangetalentschallenge.models.Register;
import br.com.zup.orangetalentschallenge.models.User;
import br.com.zup.orangetalentschallenge.repository.CarRepository;
import br.com.zup.orangetalentschallenge.repository.RegisterRepository;
import br.com.zup.orangetalentschallenge.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@Api(value = "API REST Registros")
@CrossOrigin(origins = "*")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class RegisterResource {
    @Autowired
    RegisterRepository registerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CarRepository carRepository;

    @GetMapping(value = "/registers")
    @ApiIgnore
    public List<Register> listRegisters(){
        return registerRepository.findAll();
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Registra um carro para um usuário")
    public Register saveRegister(@RequestParam long userId, @RequestParam long carId){
        User user = userRepository.findById(userId);
        Car car = carRepository.findById(carId);
        Register rgt = Register.builder().car(car).user(user).build();
        return registerRepository.save(rgt);
    }

    @PutMapping(value = "/register")
    @ApiIgnore
    public Register updateRegister(@RequestParam long id, @RequestParam long userId, @RequestParam long carId){
        User user = userRepository.findById(userId);
        Car car = carRepository.findById(carId);
        Register rgt = registerRepository.findById(id);
        rgt = Register.builder().id(id).user(user).car(car).build();
        return registerRepository.save(rgt);
    }

    @DeleteMapping(value = "/register")
    @ApiIgnore
    public void deleteRegister(@RequestParam long id){
        Register rgt = registerRepository.findById(id);
        registerRepository.delete(rgt);
    }
}
