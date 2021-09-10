package br.com.zup.orangetalentschallenge.resources;

import br.com.zup.orangetalentschallenge.models.Car;
import br.com.zup.orangetalentschallenge.models.User;
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
@Api(value = "API REST Usuários")
@CrossOrigin(origins = "*")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UserResource {
    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/users")
    @ApiOperation(value = "Retorna uma lista com todos os usuários")
    public List<User> listUsers(){
        return checkRotation(userRepository.findAll());
    }

    @GetMapping(value = "/user/{id}")
    @ApiOperation(value = "Retorna um usuário específico")
    public User listUniqueUser(@PathVariable(value = "id") long id){
        return checkRotation(userRepository.findById(id));
    }

    @GetMapping(value = "/userCars/{id}")
    @ApiOperation(value = "Retorna apenas os carros de um usuário específico")
    public List<Car> listUserCars(@PathVariable(value = "id") long id){
        User user = userRepository.findById(id);
        return user.getCars();
    }

    @PostMapping(value = "/user")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Registra um usuário")
    public User saveUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @PostMapping(value = "/users")
    @ApiIgnore
    public List<User> saveUser(@RequestBody List<User> users){
        return userRepository.saveAll(users);
    }

    @DeleteMapping(value = "/user/{id}")
    @ApiOperation(value = "Deleta um usuário")
    public void deleteUser(@PathVariable(value = "id") long id){
        userRepository.deleteById(id);
    }

    @PutMapping(value = "/user")
    @ApiOperation(value = "Atualiza um usuário")
    public User updateUser (@RequestBody User user){
        return userRepository.save(user);
    }

    public List<User> checkRotation(List<User> users){
        for(User user : users){
            CarResource.checkIsRotating(user.getCars());
        }
        return users;
    }
    public User checkRotation(User user){

        CarResource.checkIsRotating(user.getCars());
        return user;
    }
}
