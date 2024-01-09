package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.Exercise;
import cz.uhk.fim.project.bakalarka.model.Swimming;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SwimmingRepository extends JpaRepository<Swimming, Long> {
    Optional<Swimming> findByExercise(Exercise e);
}
