package br.com.germanott.financemananger.controller;

import br.com.germanott.financemananger.dto.TransacaoDTO;
import br.com.germanott.financemananger.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
    @Autowired
    private TransacaoService transacaoService;

    @PostMapping
    public TransacaoDTO criarTransacao(@RequestBody TransacaoDTO dto, Authentication authentication) {
        return transacaoService.criarTransacao(authentication, dto);
    }

    @GetMapping
    public List<TransacaoDTO> listarTransacoesPorUsuario(Authentication authentication) {
       return transacaoService.listarTransacoesPorUsuario(authentication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTransacao(@PathVariable Long id, Authentication authentication) {
        transacaoService.deletarTransacao(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public TransacaoDTO atualizarTransacao(@PathVariable Long id, @RequestBody TransacaoDTO dto, Authentication authentication){
        return transacaoService.atualizarTransacao(id, dto, authentication);
    }

}
