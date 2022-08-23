package com.cognizant.pms.login.util;


import java.util.Date;
import java.util.function.Function;

import com.cognizant.pms.login.exception.LoginException;
import com.cognizant.pms.login.securityConfiguration.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtil {

	private final JwtConfig config;

	public JwtUtil(JwtConfig config) {
		this.config = config;
	}

	public String generateToken(String id) {
		Claims claims = Jwts.claims().setSubject(id);
		long nowMillis = System.currentTimeMillis();
		long expMillis = nowMillis + config.getValidity() * 1000 * 60;
		Date exp = new Date(expMillis);
		return Jwts.builder().setClaims(claims).setIssuedAt(new Date(nowMillis)).setExpiration(exp)
				.signWith(SignatureAlgorithm.HS512, config.getSecret()).compact();
	}
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(config.getSecret()).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public boolean validateToken(final String header) throws LoginException {
		try {
			String[] parts = header.split(" ");
			if (parts.length != 2 || !"Bearer".equals(parts[0])) {
				log.error("Incorrect Authentication Structure");
				throw new LoginException("Incorrect Authentication Structure");
			}
			String username = extractUsername(parts[1]);
			log.info("username:"+username);
			return (!isTokenExpired(parts[1]));


		} catch (SignatureException ex) {
			throw new LoginException("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			throw new LoginException("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			throw new LoginException("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			throw new LoginException("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			throw new LoginException("JWT claims string is empty.");
		}
	}
}
