package smarttmtweb.libauth.authentication;

import lombok.Data;

/*clase modelo para obtener el user y password que viene desde el post que hace el cliente*/

@Data
public class AuthCredentials {

    private String user;
    private String password;

}
