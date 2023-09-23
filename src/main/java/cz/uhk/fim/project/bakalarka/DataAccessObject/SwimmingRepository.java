package cz.uhk.fim.project.bakalarka.DataAccessObject;

import cz.uhk.fim.project.bakalarka.model.GymWorkout;
import cz.uhk.fim.project.bakalarka.model.Swimming;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingRepository extends JpaRepository<Swimming, Long> {
}
