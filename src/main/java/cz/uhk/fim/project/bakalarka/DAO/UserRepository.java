package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.Training;
import cz.uhk.fim.project.bakalarka.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for accessing User-related data from the database.
 *
 * @author Alex Zamastil
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user to find.
     * @return The user entity matching the email, if found; otherwise, null.
     */
    User findUserByEmail(String email);
    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user to find.
     * @return The user entity matching the ID, if found; otherwise, null.
     */
    User findUserById(long id);
    /**
     * Finds a user by their token.
     *
     * @param token The token of the user to find.
     * @return The user entity matching the token, if found; otherwise, null.
     */
    User findUserByToken(String token);
    /**
     * Deletes days associated with a user.
     *
     * @param user The user entity for which days should be deleted.
     */
    @Transactional
    @Modifying
    @Query("DELETE  FROM Day d WHERE d.user = :user")
    void deleteDaysByUser(User user);
    /**
     * Deletes trainings associated with a user.
     *
     * @param user The user entity for which trainings should be deleted.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Training t WHERE t.user = :user")
    void deleteTrainingByUser(User user);

    /**
     * Deletes user statistics associated with a user.
     *
     * @param user The user entity for which statistics should be deleted.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM UserStats u WHERE u.user = :user")
    void deleteStatsByUser(User user);
    /**
     * Deletes a user entity.
     *
     * @param user The user entity to be deleted.
     */

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u = :user")
    void deleteUser(User user);
    /**
     * Deletes a user entity along with its associated data (days, trainings, and stats).
     *
     * @param user The user entity to be deleted along with its associated data.
     */
    default void deleteUserWithData(User user) {
        deleteDaysByUser(user);
        deleteTrainingByUser(user);
        deleteStatsByUser(user);
        deleteUser(user);
    }

}


