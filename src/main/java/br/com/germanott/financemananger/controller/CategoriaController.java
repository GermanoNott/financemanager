package br.com.germanott.financemananger.controller;

import br.com.germanott.financemananger.dto.CategoriaDTO;
import br.com.germanott.financemananger.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public CategoriaDTO criarCategoria(
            @RequestBody CategoriaDTO dto,
            Authentication authentication // <-- O Spring injeta o usuário logado
    ) {
        return categoriaService.criarCategoria(authentication, dto);
    }

    @GetMapping
    public List<CategoriaDTO> listarCategorias(Authentication authentication) {
        return categoriaService.listarCategoriasPorUsuario(authentication);
    }


}
