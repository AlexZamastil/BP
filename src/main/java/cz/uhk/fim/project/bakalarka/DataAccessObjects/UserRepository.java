package cz.uhk.fim.project.bakalarka.DataAccessObjects;

import cz.uhk.fim.project.bakalarka.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);

    User findUserById(long Id);


    @Query(value = "CALL updateuserstats(?1,?2,?3,?4,?5,?6)",nativeQuery = true)
    @Transactional
    @Modifying
    void updateuserstats(long id, String nickname, LocalDate dateofbirth, String email, double weight, double height);

    @Query(value = "CALL newpassword(?1,?2,?3)",nativeQuery = true)
    @Transactional
    @Modifying
    void newpassword(long id, String oldPassword, String newPassword);


}


