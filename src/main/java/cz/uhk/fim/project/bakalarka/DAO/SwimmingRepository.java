package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.Exercise;
import cz.uhk.fim.project.bakalarka.model.Swimming;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Repository interface for accessing swimming-related data from the database.
 *
 * @author Alex Zamastil
 */
public interface SwimmingRepository extends JpaRepository<Swimming, Long> {
    /**
     *
     * @param e Exercise object bonded with the Swimming object, that is being returned.
     * @return Run object bonded with a Swimming object (1:1 relation).
     */
    Optional<Swimming> findByExercise(Exercise e);
}
