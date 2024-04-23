package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.Training;
import cz.uhk.fim.project.bakalarka.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
/**
 * Repository interface for accessing Training-related data from the database.
 *
 * @author Alex Zamastil
 */
public interface TrainingRepository extends JpaRepository<Training, Long> {
    /**
     * Retrieves all trainings containing a specific user.
     *
     * @param userId The ID of the user to find trainings for.
     * @return A list of trainings containing the specified user.
     */
    @Query("SELECT t FROM Training t JOIN t.user u WHERE u.id = :userId")
    List<Training> findTrainingsContainingUser(@Param("userId") Long userId);
    /**
     * Finds a training associated with a user and occurring after a specific date.
     *
     * @param user The user associated with the training.
     * @param date The date after which the training occurred.
     * @return The training entity matching the criteria, if found; otherwise, null.
     */
    Training findTrainingByUserAndRacedayIsAfter(User user, LocalDate date);

    /**
     * Deletes days associated with a training.
     *
     * @param training The training entity for which days should be deleted.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Day d WHERE d.training = :training")
    void deleteDaysByTraining(Training training);


    /**
     * Deletes a training entity.
     *
     * @param training The training entity to be deleted.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Training t WHERE t = :training")
    void deleteTraining(Training training);
    /**
     * Deletes a training entity along with its associated days.
     *
     * @param training The training entity to be deleted along with its associated days.
     */
    default void deleteTrainingWithData(Training training) {
        deleteDaysByTraining(training);
        deleteTraining(training);
    }

}