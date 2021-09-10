package br.com.zup.orangetalentschallenge.repository;

import br.com.zup.orangetalentschallenge.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
    Car findById(long id);
}
