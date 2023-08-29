package cz.uhk.fim.project.bakalarka.DataAccessObject;

import cz.uhk.fim.project.bakalarka.model.User;
import cz.uhk.fim.project.bakalarka.model.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatsRepository extends JpaRepository<UserStats, Long> {
    UserStats findUserStatsByUserIs(long id);

    UserStats findUserStatsByUser(User user);

    UserStats findUserStatsById(Long id);
}
