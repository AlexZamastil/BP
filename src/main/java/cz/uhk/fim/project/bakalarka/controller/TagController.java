package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling tag-related endpoints.
 *
 * @author Alex Zamastil
 */
@RestController
@RequestMapping("/api")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Endpoint for retrieving exercise tags.
     *
     * @return ResponseEntity with exercise tags.
     */
    @GetMapping(value = "tag/getExerciseTags")
    public ResponseEntity<?> getExerciseTags() {
        return tagService.getExerciseTags();
    }

    /**
     * Endpoint for retrieving food tags.
     *
     * @return ResponseEntity with food tags.
     */
    @GetMapping(value = "tag/getFoodTags")
    public ResponseEntity<?> getFoodTags() {
        return tagService.getFoodTags();
    }

    /**
     * Endpoint for retrieving timing tags.
     *
     * @return ResponseEntity with timing tags.
     */
    @GetMapping(value = "tag/getTimingTags")
    public ResponseEntity<?> getTimingTags() {
        return tagService.getTimingTags();
    }
}
