package mx.uaemex.fi.ico.linc21.security;

import java.util.Set;
import org.eclipse.microprofile.jwt.Claims;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import static mx.uaemex.fi.ico.linc21.security.AllowedEntities.*;

@ApplicationScoped
public class TokenIssuer {
	
	public TokenIssuer() {}
	
	public String generateUserToken(String user) {
		return generateToken(user, CLIENTE_DEL_BANCO);
	}
	
	private String generateToken(String user, String... roles) {
		return Jwt.issuer("mx.uaemex.fi")
	             .groups(Set.of(roles))
	             .subject(user)
	             .claim(Claims.exp.name(), System.currentTimeMillis() + 2629800000l)
	             .sign();
	}

}
