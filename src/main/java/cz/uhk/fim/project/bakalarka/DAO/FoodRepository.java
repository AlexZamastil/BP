package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.Food;
import cz.uhk.fim.project.bakalarka.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for accessing Food-related data from the database.
 *
 * @author Alex Zamastil
 */
public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findFoodById(long id);

    /**
     *
     * @param foodId The ID of the food to retrieve tags for.
     * @return A set of Tags bonded with the food.
     */
    @Query("SELECT f.tagfood FROM Food f WHERE f.id = :foodId")
    Set<Tag> findTagsByFoodId(@Param("foodId") Long foodId);
}
