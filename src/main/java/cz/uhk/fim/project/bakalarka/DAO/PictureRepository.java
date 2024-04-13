package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository interface for accessing Picture-related data from the database.
 *
 * @author Alex Zamastil
 */
public interface PictureRepository extends JpaRepository<Picture, Long> {
}
