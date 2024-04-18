package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.Day;
import cz.uhk.fim.project.bakalarka.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for accessing Day-related data from the database.
 *
 * @author Alex Zamastil
 */
public interface DayRepository extends JpaRepository<Day, Long> {
    /**
     * Retrieves all days that are bonded with a given training.
     *
     * @param training The training entity to retrieve days for.
     * @return A list of Day entities bonded with the training.
     */
    @Query("SELECT d FROM Day d WHERE d.training = :training")
    List<Day> findAllByTraining(@Param("training") Training training);
    /**
     * Retrieves the next 7 days that are bonded with a given training.
     *
     * @param training The training entity to retrieve days for.
     * @return A list of Day entities bonded with the training.
     */

    @Query("SELECT d FROM Day d WHERE d.training = :training AND d.date >= CURRENT_DATE AND d.date < CURRENT_DATE + 7")
    List<Day> findUpcomingTrainingDays(@Param("training") Training training);

}
