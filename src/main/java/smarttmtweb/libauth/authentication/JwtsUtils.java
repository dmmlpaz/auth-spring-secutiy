package smarttmtweb.libauth.authentication;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


/*clase para control, creacion y validacion del token*/

@Slf4j
@Component
public class JwtsUtils {

    private Environment env;

    private String jwtSingningKey = "$2a$10$y2DJveXOsNNEtvoHI9.LtuKjAc2w/S2M431aoABYvZCGD/bVxJOLa";
    private static final String EXPIRO_TOKEN = "Expiro el token";
    private static final  String TOKEN_INVALIDO = "Token inválido";


    public String keyToHexByte(String cadena, String hexByte) {
        return (switch (hexByte) {
            case "1" -> DigestUtils.sha1Hex(cadena);
            case "384" -> DigestUtils.sha384Hex(cadena);
            case "512" -> DigestUtils.sha512Hex(cadena);
            default -> DigestUtils.sha256Hex(cadena);
        });
    }

    public Date extractExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extracAllClaims(token);
        return claims == null ? null : claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenUserValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username != null && (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Claims extracAllClaims(String token) {
        String key = keyToHexByte(jwtSingningKey, "256");
        Jws<Claims> parsedJwt = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key.getBytes()))
                .build()
                .parseClaimsJws(token);
        return parsedJwt.getBody();
    }

    /*
     *Antes de un tilizar un metodo de extracion de los claims primero se debe validar y
     * ejecutar el metodo isTokenValid para verficar que no lance excepcion
     */
    public boolean isTokenValid(String token) throws JWTokenException {
        try {
            String key = keyToHexByte(jwtSingningKey, "256");
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(key.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("Error extracAllClaims Exception:{}", ex.toString());
            throw new JWTokenException(EXPIRO_TOKEN);
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new JWTokenException(TOKEN_INVALIDO);
        }
    }

    public String generaToken(String user,long tiempoExpiracion) {
        String key = keyToHexByte(jwtSingningKey, "256");
        Map<String, Object> claims = new HashMap<>();
        return io.jsonwebtoken.Jwts.builder().setClaims(claims)
                .setSubject(user)
                //.claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(tiempoExpiracion)))
                .signWith(Keys.hmacShaKeyFor(key.getBytes())).compact();
    }

}
