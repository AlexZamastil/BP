package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Supportive controller class for haldling utility endpoints.
 *
 * @author Alex Zamastil
 */
@RestController
@RequestMapping("/api")
public class UtilController {
    private final MessageSource messageSource;
    MessageHandler<String> messageHandler = new MessageHandler<>();
    public UtilController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Utility endpoint for initializing connection.
     *
     * @return ResponseEntity with the result of the operation.
     */
    @Autowired
    @PostMapping(value = "initConnection")
    public ResponseEntity<?> initConnection(){
        return messageHandler.success(messageSource.getMessage("success.init", null, LocaleContextHolder.getLocale()));
    }

}
