package smarttmtweb.libauth.authentication;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ConfigurationProperties(prefix = "auth.lib")
public class AuthLibraryProperties {
    String urlAuth;
    String tiempoToken;
}
