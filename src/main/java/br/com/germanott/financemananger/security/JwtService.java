package br.com.germanott.financemananger.security;

import br.com.germanott.financemananger.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String CHAVE_SECRETA;

    private static final long TEMPO_EXPIRACAO = 1000 * 60 * 60;

    // Gera um token JWT para o usuário
    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TEMPO_EXPIRACAO))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrai o email do token
    public String getEmailFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }

    //Verifica se o token é valido
    public boolean isTokenValido(String token, String userEmail){
        final String email = getEmailFromToken(token);
        return email.equals(userEmail) && !isTokenExpirado(token);
    }

    private Key getSigningKey(){
        byte[] keyBytes = CHAVE_SECRETA.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpirado(String token){
        return getExpiration(token).before(new Date());
    }

    private Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }


}
