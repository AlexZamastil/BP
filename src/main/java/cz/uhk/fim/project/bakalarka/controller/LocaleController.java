package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.service.LocaleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")
public class LocaleController {
    private final LocaleService localeService;
    public LocaleController(LocaleService localeService) {
        this.localeService = localeService;
    }

    @PostMapping("/language/switch")
    public ResponseEntity<?> switchLanguage(@RequestBody String language,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
     return localeService.switchLanguage(language, request,response);

    }



}