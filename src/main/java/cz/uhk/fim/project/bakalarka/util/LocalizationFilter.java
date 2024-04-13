package cz.uhk.fim.project.bakalarka.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.util.Locale;
/**
 * Filter for setting the localization of the application based on the request header.
 * Filter code is executed before each request, in this case it sets up the localization based on "Localization" header.
 *
 * @author Alex Zamastil
 */
@Component
public class LocalizationFilter implements Filter {


    private final LocaleResolver localeResolver;

    public LocalizationFilter(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }
    /**
     * Sets the localization of the application based on the request header "Localization".
     * @param request The ServletRequest object.
     * @param response The ServletResponse object.
     * @param filterChain The FilterChain object for invoking the next filter in the chain.
     * @throws IOException if an I/O error occurs during the filtering process.
     * @throws ServletException if any servlet-related error occurs during the filtering process.
     */
   @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String localizationCookie = httpRequest.getHeader("Localization");
        Locale czech = new Locale.Builder().setLanguage("cs").setRegion("CZ").build();

       if (localizationCookie.equals("en")) {
           LocaleContextHolder.setLocale(Locale.US);
           localeResolver.setLocale((HttpServletRequest) request, (HttpServletResponse) response, Locale.US);
       } else {
           LocaleContextHolder.setLocale(czech);

           localeResolver.setLocale((HttpServletRequest) request, (HttpServletResponse) response, czech);
       }
        filterChain.doFilter(request, response);

    }
}
