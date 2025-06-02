package smarttmtweb.libauth.authentication;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String MENSAJE_AUTH_INVALIDO = "Usuario o contraseña incorrectos";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // 1. Obtener el Usuadmin desde tu servicio o credenciales del uisuario des la base de datos
        String usuadmin = null;//inicioSesionAdminService.geUsuaAdmi(username);//llamado credenciales de la base de datos
        String claveBaseDatos = null;

        // 2. Verificar existencia del usuario
        if (usuadmin == null) {
            throw new UsernameNotFoundException(MENSAJE_AUTH_INVALIDO);
        }

        // 3. Validar contraseña (asegúrate de usar el mismo PasswordEncoder)
        if (!passwordEncoder.matches(password, claveBaseDatos)) {
            throw new BadCredentialsException(MENSAJE_AUTH_INVALIDO);
        }

        // 4. Crear Authentication con el Usuadmin completo como principal
        return new UsernamePasswordAuthenticationToken(
                usuadmin, // <-- Aquí pasas el objeto completo
                null,
                Collections.emptyList() // O tus authorities si las tienes
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}