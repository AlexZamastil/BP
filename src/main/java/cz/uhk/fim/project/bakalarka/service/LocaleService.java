package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Service
@Log4j2
public class LocaleService {
    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;

    public LocaleService(LocaleResolver localeResolver, MessageSource messageSource) {
        this.localeResolver = localeResolver;
        this.messageSource = messageSource;
    }

    public ResponseEntity<?> switchLanguage(String language, HttpServletRequest request, HttpServletResponse response) {
        if (language.contains("cs")) {
            LocaleContextHolder.setLocale(Locale.FRANCE);
            localeResolver.setLocale(request, response, Locale.FRANCE);
        } else

        if (language.contains("en")) {
            LocaleContextHolder.setLocale(Locale.US);
            localeResolver.setLocale(request, response, Locale.US);
        } else return MessageHandler.success(messageSource.getMessage("error.language.wrongCode", null, LocaleContextHolder.getLocale()));
        return MessageHandler.success(messageSource.getMessage("success.language.switched", null, LocaleContextHolder.getLocale()));
    }
}