package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.User;
import cz.uhk.fim.project.bakalarka.model.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository interface for accessing UserStats-related data from the database.
 *
 * @author Alex Zamastil
 */
public interface UserStatsRepository extends JpaRepository<UserStats, Long> {
    /**
     * Finds user stats by user ID.
     *
     * @param id The ID of the user.
     * @return The user stats entity associated with the user ID.
     */
    UserStats findUserStatsByUserIs(long id);
    /**
     * Finds user stats by user entity.
     *
     * @param user The user entity.
     * @return The user stats entity associated with the user.
     */
    UserStats findUserStatsByUser(User user);
    /**
     * Finds user stats by ID.
     *
     * @param id The ID of the user stats entity.
     * @return The user statistics entity with the specified ID.
     */
    UserStats findUserStatsById(Long id);
}
