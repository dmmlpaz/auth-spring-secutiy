package smarttmtweb.libauth.authentication;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import smarttmtweb.libauth.utils.Constantes;

@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
@RestController
public class ControllerDummy {

    private final AuthenticationManager authenticationManager;
    private final JwtsUtils jwtUtils;
    private final AuthLibraryProperties authProperties;

    @PostMapping("/auth")
    public ResponseEntity<?> Authdummy(@RequestBody UsuarioModel usuarioModel) {
      log.info("init Authdummy");
        var usDB = "USER";
        var pwDB = "$2a$10$awCDlOc.vf.DaA9sBrbfZutYY2G2KhJxohZU8t9eI7sQ5lDLRlaHa";
        var itemsCredenciales =     Constantes.ITEM_US + usuarioModel.getUsuario() + "|" + Constantes.ITEM_PW + pwDB;
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(itemsCredenciales, usuarioModel.getClave()));
        var tiempToken = StringUtils.isEmpty(authProperties.tiempoToken) ? "1" : authProperties.urlAuth;
        return ResponseEntity.ok(jwtUtils.generaToken(usuarioModel.getUsuario() ,Long.parseLong(tiempToken)));
    }

    @GetMapping
    public ResponseEntity<?> getDummy() {
        return ResponseEntity.ok("OK");
    }

}
