package br.com.germanott.financemananger.service;

import br.com.germanott.financemananger.dto.RelatorioDTO;
import br.com.germanott.financemananger.model.TipoTransacao;
import br.com.germanott.financemananger.model.Transacao;
import br.com.germanott.financemananger.model.Usuario;
import br.com.germanott.financemananger.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {
    @Autowired
    private TransacaoRepository transacaoRepository;

    public RelatorioDTO gerarRalatorioMensal(Authentication authentication, int ano, int mes){
        Usuario usuario = (Usuario) authentication.getPrincipal();

        List<Transacao> transacoes = transacaoRepository.findByUsuarioIdAndData(usuario.getId(),  ano, mes);

        //Calcular Receitas
        BigDecimal totalReceitas = transacoes.stream()
                .filter(t -> t.getTipo() == TipoTransacao.RECEITA)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Calcular Despesas
        BigDecimal totalDespesas = transacoes.stream()
                .filter(t -> t.getTipo() == TipoTransacao.DESPESA)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Calcular Saldo
        BigDecimal saldoFinal = totalReceitas.subtract(totalDespesas);

        //Calcular Gastos por Categoria
        Map<String, BigDecimal> gastosPorCategoria = transacoes.stream()
                .filter(t -> t.getTipo() == TipoTransacao.DESPESA)
                .collect(Collectors.groupingBy(
                        t -> t.getCategoria().getNome(), 
                        Collectors.mapping(Transacao::getValor,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)))
                );
        
        //Montar DTO de resposta
        RelatorioDTO relatorioDTO = new RelatorioDTO();
        relatorioDTO.setTotalReceitas(totalReceitas);
        relatorioDTO.setTotalDespesas(totalDespesas);
        relatorioDTO.setSaldoFinal(saldoFinal);
        relatorioDTO.setGastosPorCategoria(gastosPorCategoria);
        
        return relatorioDTO;
    }
}
