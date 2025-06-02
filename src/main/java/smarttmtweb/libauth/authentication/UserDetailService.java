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
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {

        log.info("init loadUserByUsername usuario:{}", usuario);

        if (StringUtils.isEmpty(usuario)) {
            throw new IllegalArgumentException("itemsCredenciales no puede nulos");
        }

        //con el usuario se onbienen las credeciales de la base datos ejemplo
        //se llama da la base de datos para otenera la clave de la base de datos con el parametro usuario, la clave de estar encriptada con bycript
        String pw = "$2a$10$awCDlOc.vf.DaA9sBrbfZutYY2G2KhJxohZU8t9eI7sQ5lDLRlaHa" ;


        return new User(usuario, pw, Collections.emptyList());

    }




}
