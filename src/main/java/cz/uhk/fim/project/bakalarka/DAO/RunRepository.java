package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.Exercise;
import cz.uhk.fim.project.bakalarka.model.Run;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Repository interface for accessing run-related data from the database.
 *
 * @author Alex Zamastil
 */
public interface RunRepository extends JpaRepository<Run, Long> {
    /**
     *
     * @param e Exercise object bonded with the Run object, that is being returned.
     * @return Run object bonded with an Exercise object (1:1 relation).
     */
    Optional<Run> findByExercise(Exercise e);
}
