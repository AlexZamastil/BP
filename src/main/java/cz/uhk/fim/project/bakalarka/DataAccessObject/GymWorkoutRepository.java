package cz.uhk.fim.project.bakalarka.DataAccessObject;

import cz.uhk.fim.project.bakalarka.model.GymWorkout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymWorkoutRepository extends JpaRepository<GymWorkout, Long> {
}
