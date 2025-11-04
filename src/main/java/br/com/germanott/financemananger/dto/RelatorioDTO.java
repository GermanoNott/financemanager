package br.com.germanott.financemananger.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class RelatorioDTO {
    private BigDecimal totalReceitas;
    private BigDecimal totalDespesas;
    private BigDecimal saldoFinal;
    private Map<String, BigDecimal> gastosPorCategoria;
}
