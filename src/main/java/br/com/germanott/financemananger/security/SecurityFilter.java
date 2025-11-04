package br.com.germanott.financemananger.security;

import br.com.germanott.financemananger.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Recuperar o token do cabeçalho
        String token = recuperarToken(request);

        if (token != null) {
            // 2. Validar o token e buscar o email
            String email = jwtService.getEmailFromToken(token);

            // 3. Buscar o usuário no banco
            UserDetails usuario = usuarioRepository.findByEmail(email).orElse(null);

            if (usuario != null && jwtService.isTokenValido(token, usuario.getUsername())) {
                // 4. Se válido, autenticar o usuário no contexto do Spring
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        usuario,
                        null, // Não precisamos de credenciais (senha) aqui
                        usuario.getAuthorities()
                );

                // 5. Salva a autenticação no "contexto" da requisição
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 6. Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }

    // Metodo auxiliar para pegar o "Authorization" header
    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        // Extrai apenas o token (remove o "Bearer ")
        return authHeader.substring(7);
    }


}
