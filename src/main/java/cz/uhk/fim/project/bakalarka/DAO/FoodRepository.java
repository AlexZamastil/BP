package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository interface for accessing Food-related data from the database.
 *
 * @author Alex Zamastil
 */
public interface FoodRepository extends JpaRepository<Food, Long> {
}
