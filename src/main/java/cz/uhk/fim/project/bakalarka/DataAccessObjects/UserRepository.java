package cz.uhk.fim.project.bakalarka.DataAccessObjects;

import cz.uhk.fim.project.bakalarka.model.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);

    User findUserById(long Id);

}


