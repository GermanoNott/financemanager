package br.com.germanott.financemananger.controller;

import br.com.germanott.financemananger.dto.LoginRequestDTO;
import br.com.germanott.financemananger.dto.LoginResponseDTO;
import br.com.germanott.financemananger.model.Usuario;
import br.com.germanott.financemananger.repository.UsuarioRepository;
import br.com.germanott.financemananger.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager; // O Gerenciador (Bean 4)

    @Autowired
    private JwtService jwtService; // O Serviço de Token

    @Autowired
    private UsuarioRepository usuarioRepository; // O Repositório

    @Autowired
    private PasswordEncoder passwordEncoder; // O Codificador (Bean 1)

    @PostMapping("/registro")
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) {
        // 1. Cria um "pacote" de autenticação com email e senha
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());

        // 2. O Spring Security (AuthenticationManager) tenta autenticar
        // (Ele vai usar nosso UserDetailsService e PasswordEncoder)
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 3. Se deu certo, o 'auth' contém o usuário. Geramos o token.
        Usuario usuarioAutenticado = (Usuario) auth.getPrincipal();
        String token = jwtService.gerarToken(usuarioAutenticado);

        return new LoginResponseDTO(token);
    }


}

