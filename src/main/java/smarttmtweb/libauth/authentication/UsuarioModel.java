package smarttmtweb.libauth.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioModel {

    private String usuario;
    private String clave;

}
