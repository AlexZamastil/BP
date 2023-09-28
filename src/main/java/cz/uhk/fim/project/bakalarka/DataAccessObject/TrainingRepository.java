package cz.uhk.fim.project.bakalarka.DataAccessObject;

import cz.uhk.fim.project.bakalarka.model.Training;
import cz.uhk.fim.project.bakalarka.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    @Query("SELECT t FROM Training t JOIN t.user u WHERE u.id = :userId")
    List<Training> findTrainingsContainingUser(@Param("userId") Long userId);

}
