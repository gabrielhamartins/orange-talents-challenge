package br.com.zup.orangetalentschallenge.repository;

import br.com.zup.orangetalentschallenge.models.Register;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepository extends JpaRepository<Register, Long> {
    Register findById(long id);
}

