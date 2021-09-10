package br.com.zup.orangetalentschallenge.resources;

import br.com.zup.orangetalentschallenge.models.Car;
import br.com.zup.orangetalentschallenge.models.FipeResponse;
import br.com.zup.orangetalentschallenge.repository.CarRepository;
import br.com.zup.orangetalentschallenge.repository.FipeClient;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@Api(value = "API REST Carros")
@CrossOrigin(origins = "*")
public class CarResource {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private FipeClient fipeClient;

    @GetMapping(value = "/cars")
    @ApiOperation(value = "Retorna uma lista com os carros registrados")
    public List<Car> listCars(){
        return checkIsRotating(carRepository.findAll());
    }

    @GetMapping(value = "/car/{id}")
    @ApiOperation(value = "Retorna um carro específico")
    public Car listUniqueCar(@PathVariable(value = "id") long id){
        return checkIsRotating(carRepository.findById(id));
    }

    @PostMapping(value = "/car")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Registra um carro")
    public Car saveCar(@RequestBody Car car){
        car = getCarValue(car);
        car = getRotationDay(car);
        car = checkIsRotating(car);
        return carRepository.save(car);
    }

    @PostMapping(value = "cars")
    @ApiIgnore
    public List<Car> saveCar(@RequestBody List<Car> cars){
        for(Car car : cars){
            car = getCarValue(car);
            car = getRotationDay(car);
            car = checkIsRotating(car);
        }
        return carRepository.saveAll(cars);
    }

    @DeleteMapping(value = "/car/{id}")
    @ApiOperation(value = "Deleta um carro")
    public void deleteCar(@PathVariable(value = "id") long id){
        carRepository.deleteById(id);
    }

    @PutMapping(value = "/car")
    @ApiOperation(value = "Atualiza um carro")
    public Car updateCar (@RequestBody Car car){
        car = getCarValue(car);
        car = getRotationDay(car);
        car = checkIsRotating(car);
        return carRepository.save(car);
    }

    public Car getCarValue(Car car){
        List<FipeResponse> fipeResponse = fipeClient.getBrands();
        String brandId = "";
        String modelId= "";
        for(FipeResponse fr1 : fipeResponse){
            if(fr1.getNome().equals(car.getBrand())){
                brandId = fr1.getCodigo();
                break;
            }
        }
        FipeResponse mandy = fipeClient.getModels(brandId);
        List<FipeResponse> models = mandy.getModelos();
        for(FipeResponse fr2 : models){
            if(fr2.getNome().equals(car.getModel())){
                modelId = fr2.getCodigo();
                break;
            }
        }
        FipeResponse carValue = fipeClient.getValue(brandId, modelId, car.getYear());
        car.setValue(carValue.getValor());
        return car;
    }

    public Car getRotationDay(Car car){
        int yearLastDigit = Integer.parseInt(String.valueOf(car.getYear().charAt(3)));
        if(yearLastDigit == 0 || yearLastDigit == 1){
            car.setRotationDay("Segunda-feira");
        }
        if(yearLastDigit == 2 || yearLastDigit == 3){
            car.setRotationDay("Terça-feira");
        }
        if(yearLastDigit == 4 || yearLastDigit == 5){
            car.setRotationDay("Quarta-feira");
        }
        if(yearLastDigit == 6 || yearLastDigit == 7){
            car.setRotationDay("Quinta-feira");
        }
        if(yearLastDigit == 8 || yearLastDigit == 9){
            car.setRotationDay("Sexta-feira");
        }
        return car;
    }

    public static Car checkIsRotating(Car car){
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, 1);
        int dayOftheWeek = today.get(Calendar.DAY_OF_WEEK);
        if(car.getRotationDay().equals("Segunda-feira") && dayOftheWeek == 2){
            car.setRotating(true);
        }
        else if(car.getRotationDay() == "Terça-feira" && dayOftheWeek == 3){
            car.setRotating(true);
        }

        else if(car.getRotationDay() == "Quarta-feira" && dayOftheWeek == 4){
            car.setRotating(true);
        }

        else if(car.getRotationDay() == "Quinta-feira" && dayOftheWeek == 5){
            car.setRotating(true);
        }

        else if(car.getRotationDay() == "Sexta-feira" && dayOftheWeek == 6){
            car.setRotating(true);
        }
        else{
            car.setRotating(false);
        }
        return car;
    }

    public static List<Car> checkIsRotating(List<Car> cars){
        for(Car car : cars){
            checkIsRotating(car);
        }
        return cars;
    }
}
