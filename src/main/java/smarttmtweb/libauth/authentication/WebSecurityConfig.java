package smarttmtweb.libauth.authentication;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

/**
 * clase base de configuracion se spring security , en esta clase se define la logica implicita de sprin security
 */

@Slf4j
@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(AuthLibraryProperties.class)
public class WebSecurityConfig {

    private final UserDetailsService userDetailService;
    private final FilterValidAuthPerRequest jwtAuthorizationFilter;
    private final AuthLibraryProperties authProperties;

    private static  final String URL_AUTH = "/api/auth";

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {

        var urlAut = StringUtils.isEmpty(authProperties.urlAuth) ? URL_AUTH : authProperties.urlAuth;

        RequestCache nullRequestCache = new NullRequestCache();

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//no session
                .requestCache(cache -> cache.requestCache(nullRequestCache))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(urlAut)
                        .permitAll()
                        .anyRequest()
                        .authenticated())//any request
                .authenticationProvider(authenticationProvider()) //cuando se usa provedor personalizado com el de la clase CustomAuthenticationProvider se pone en esta flag
                //y los bean AuthenticationProvider, authenticationManager no son necesarios
                .exceptionHandling(exc ->
                        exc.authenticationEntryPoint((request, response, authEx) ->
                            // No autenticados (401)
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario o clave invalida")
                        )
                        .accessDeniedHandler((request, response, accessEx) ->
                            // Para autenticados sin permisos (403)
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado")
                        ))
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        log.info("AuthenticationProvider");
        final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        log.info("init authenticationManager userDetailService:{}", userDetailService);
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.getOrBuild();
    }

    public static void main(String[] args) {
        log.info("pass:{}", new BCryptPasswordEncoder().encode("123"));
        log.info("SignatureAlgorithm:{}", Keys.secretKeyFor(SignatureAlgorithm.HS256));
    }
}
