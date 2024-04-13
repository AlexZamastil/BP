package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.Exercise;
import cz.uhk.fim.project.bakalarka.model.GymWorkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Repository interface for accessing GymWorkout-related data from the database.
 *
 * @author Alex Zamastil
 */
public interface GymWorkoutRepository extends JpaRepository<GymWorkout, Long> {
    /**
     *
     * @param e Exercise object bonded with the GymWorkout object, that is being returned.
     * @return GymWorkout object bonded with an Exercise object (1:1 relation).
     */
    Optional<GymWorkout> findByExercise(Exercise e);
}
