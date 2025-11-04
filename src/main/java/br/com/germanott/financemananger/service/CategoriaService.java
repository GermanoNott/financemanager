package br.com.germanott.financemananger.service;

import br.com.germanott.financemananger.dto.CategoriaDTO;
import br.com.germanott.financemananger.model.Categoria;
import br.com.germanott.financemananger.model.Usuario;
import br.com.germanott.financemananger.repository.CategoriaRepository;
import br.com.germanott.financemananger.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<CategoriaDTO> listarCategoriasPorUsuario(Authentication authentication) {
        // 1. Pega o usuário logado
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        // 2. Usa o ID dele
        return categoriaRepository.findByUsuarioId(usuarioLogado.getId())
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO criarCategoria(Authentication authentication, CategoriaDTO dto) {
        // 1. Pega o usuário logado
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        // 2. Cria a entidade
        Categoria novaCategoria = new Categoria();
        novaCategoria.setNome(dto.getNome());
        novaCategoria.setUsuario(usuarioLogado); // Associa ao usuário logado

        // 3. Salva no banco
        Categoria categoriaSalva = categoriaRepository.save(novaCategoria);

        // 4. Retorna o DTO
        return converterParaDTO(categoriaSalva);
    }

    private CategoriaDTO converterParaDTO(Categoria entidade) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(entidade.getId());
        dto.setNome(entidade.getNome());
        return dto;
    }


}
