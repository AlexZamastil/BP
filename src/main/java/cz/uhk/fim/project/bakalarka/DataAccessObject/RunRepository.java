package cz.uhk.fim.project.bakalarka.DataAccessObject;

import cz.uhk.fim.project.bakalarka.model.GymWorkout;
import cz.uhk.fim.project.bakalarka.model.Run;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunRepository extends JpaRepository<Run, Long> {
}
