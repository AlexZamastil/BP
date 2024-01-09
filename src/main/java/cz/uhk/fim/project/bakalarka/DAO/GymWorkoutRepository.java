package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.Exercise;
import cz.uhk.fim.project.bakalarka.model.GymWorkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GymWorkoutRepository extends JpaRepository<GymWorkout, Long> {
    Optional<GymWorkout> findByExercise(Exercise e);
}
