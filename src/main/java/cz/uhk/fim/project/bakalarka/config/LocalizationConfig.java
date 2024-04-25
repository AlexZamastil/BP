package cz.uhk.fim.project.bakalarka.config;


import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Configuration class for setting up LocaleResolver.
 * This class defines beans for managing language of the project and message sources for the actual texts.
 *
 * @author Alex Zamastil
 */

@Configuration
public class LocalizationConfig {
    /**
     * Definition for locale resolver, that is set to Czech as default language
     *
     * @return LocaleResolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(new Locale.Builder().setLanguage("cs").setRegion("CZ").build());
        return resolver;
    }

    /**
     * Definition of source of the localized messages, that are being sent to frontend part of project
     *
     * @return messageSource
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


}
