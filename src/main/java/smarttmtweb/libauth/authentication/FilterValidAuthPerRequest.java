package smarttmtweb.libauth.authentication;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import smarttmtweb.libauth.utils.Constantes;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * clase que se usa como middleware o filtro en cada peticion valida autenticiacion y token valido
 */

@RequiredArgsConstructor
@Slf4j
@Component
public class FilterValidAuthPerRequest extends OncePerRequestFilter {

    private final JwtsUtils jwtUtils;
    private final UserDetailService userDetailsService;
    //private final DatosExternosModel datosExternosModel;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        log.info("init doFilterInternal");

        final String headerAuth = request.getHeader(AUTHORIZATION);

        log.info("init doFilterInternal headerAuth:{}",headerAuth);

        if (headerAuth == null || !headerAuth.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            final String jwtToken = headerAuth.substring(7);
            log.info("init doFilterInternal jwtToken.{}",jwtToken);

            if (!jwtUtils.isTokenValid(jwtToken)) {
                String jsonError = "{\"error\": \"" + Constantes.MENSAJE_TOKEN_INVALIDO + "\"}";
                response.setContentType("application/json");
                response.setStatus(HttpStatus.OK.value());
                response.getWriter().write(jsonError);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                final String subjectJwt = jwtUtils.extractUsername(jwtToken);
                // 3. Cargar el usuario desde la base de datos
                var userDetails = userDetailsService.loadUserByUsername(subjectJwt);

                // 4. Crear Authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                "USER",
                                null,
                                userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (JWTokenException | UsernameNotFoundException ex) {
            String jsonError = "{\"error\": \"" + Constantes.MENSAJE_TOKEN_INVALIDO + "\"}";
            response.setContentType("application/json");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(jsonError);
            return;
        }

        filterChain.doFilter(request, response);

    }
}
