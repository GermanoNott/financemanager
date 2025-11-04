package br.com.germanott.financemananger.security;

import br.com.germanott.financemananger.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.Security;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SecurityFilter securityFilter;

    // Codificador de senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Busca de usuarios
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    //Provedor de autenticação
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    //Gerenciador de Autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //Filtros de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilitar CSRF (não precisamos em APIs stateless)
                .csrf(csrf -> csrf.disable())

                // 2. Definir a política de sessão como STATELESS (sem estado)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Isso captura falhas de autenticação (como senha errada, usuário não encontrado)
                            // e força o retorno 401 Unauthorized
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Autenticação falhou ou credenciais inválidas");
                        })
                )
                // 3. Configurar as regras de autorização
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso público aos endpoints de autenticação e registro
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/registro").permitAll()
                        .requestMatchers("/usuarios").permitAll() // Registro de usuário

                        // Permite acesso ao Console H2 (apenas para desenvolvimento)
                        .requestMatchers("/h2-console/**").permitAll()

                        // Exige autenticação para qualquer outra requisição
                        .anyRequest().authenticated()
                )
                // 4. Adiciona o provedor de autenticação (Bean 3)
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        // (Vamos adicionar nosso filtro JWT aqui no próximo passo)

        // 5. Necessário para o H2 Console funcionar
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }


}

