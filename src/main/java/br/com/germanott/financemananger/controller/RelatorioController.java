package br.com.germanott.financemananger.controller;


import br.com.germanott.financemananger.dto.RelatorioDTO;
import br.com.germanott.financemananger.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/mensal")
    public RelatorioDTO getRelatorioMensal(
            Authentication authentication,
            @RequestParam int ano,
            @RequestParam int mes) {
        return relatorioService.gerarRalatorioMensal(authentication, ano, mes);
    }

}
