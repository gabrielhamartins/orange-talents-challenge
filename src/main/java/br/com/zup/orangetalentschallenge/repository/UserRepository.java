package br.com.zup.orangetalentschallenge.repository;

import br.com.zup.orangetalentschallenge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);

}
