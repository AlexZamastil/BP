package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Autowired

    @GetMapping(value = "tag/getExerciseTags")
    public ResponseEntity<?> getExerciseTags(){
        return tagService.getExerciseTags();
    }
    @GetMapping(value = "tag/getFoodTags")
    public ResponseEntity<?> getFoodTags(){
        return tagService.getFoodTags();
    }
    @GetMapping(value = "tag/getTimingTags")
    public ResponseEntity<?> getTimingTags(){
        return tagService.getTimingTags();
    }
}
