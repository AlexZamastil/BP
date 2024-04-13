package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.Exercise;
import cz.uhk.fim.project.bakalarka.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
/**
 * Repository interface for accessing Exercise-related data from the database.
 *
 * @author Alex Zamastil
 */
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findExerciseById(Long id);

    /**
     *
     * @param exerciseId The ID of the exercise to retrieve tags for.
     * @return A set of Tags bonded with the exercise.
     */
    @Query("SELECT e.tagexercise FROM Exercise e WHERE e.id = :exerciseId")
    Set<Tag> findTagsByExerciseId(@Param("exerciseId") Long exerciseId);



}

