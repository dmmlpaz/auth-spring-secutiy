package smarttmtweb.libauth.authentication;

import io.micrometer.common.util.StringUtils;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import smarttmtweb.libauth.utils.Constantes;
import smarttmtweb.libauth.utils.LeeCadena;

/*Clase con funcion implicita de spring security para carga de los datos del usuario que viene de la
base de datos y se asigna UserDetails (Objeto usuario implicito de spring security)*/

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String itemsCredenciales) throws UsernameNotFoundException {

        log.info("init loadUserByUsername itemsCredenciales:{}", itemsCredenciales);

        if (StringUtils.isEmpty(itemsCredenciales)) {
            throw new IllegalArgumentException("itemsCredenciales no puede nulos");
        }

        String pw = LeeCadena.funLeerItems(itemsCredenciales, Constantes.PW);

        log.info("init loadUserByUsername password:{}", pw);

        return new User(itemsCredenciales, pw, Collections.emptyList());

    }




}
