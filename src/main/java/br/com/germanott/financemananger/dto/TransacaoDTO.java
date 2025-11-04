package br.com.germanott.financemananger.dto;

import br.com.germanott.financemananger.model.TipoTransacao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransacaoDTO {

    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private TipoTransacao tipo;
    private Long categoriaId;

    private Long id;
    private String categoriaNome;

}
