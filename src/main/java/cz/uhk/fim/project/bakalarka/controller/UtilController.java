package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")
public class UtilController {
    private final MessageSource messageSource;
    public UtilController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired

    @PostMapping(value = "initConnection")
    public ResponseEntity<?> initConnection(){
        return MessageHandler.success(messageSource.getMessage("success.init", null, LocaleContextHolder.getLocale()));
    }

}
