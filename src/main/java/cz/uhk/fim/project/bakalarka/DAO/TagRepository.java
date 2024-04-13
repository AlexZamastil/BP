package cz.uhk.fim.project.bakalarka.DAO;

import cz.uhk.fim.project.bakalarka.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository interface for accessing Tag-related data from the database.
 *
 * @author Alex Zamastil
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    /**
     * Retrieves a tag by its text.
     *
     * @param text The text of the tag to find.
     * @return The tag entity with the specified text, if found; otherwise, null.
     */
    Tag findTagByText(String text);

}
