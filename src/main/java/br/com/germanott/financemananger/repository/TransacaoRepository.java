package br.com.germanott.financemananger.repository;

import br.com.germanott.financemananger.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByUsuarioId(Long usuarioId);
    @Query("SELECT t FROM Transacao t WHERE t.usuario.id = :usuarioId AND YEAR(t.data) = :ano AND MONTH(t.data) = :mes")
    List<Transacao> findByUsuarioIdAndData(
            @Param("usuarioId") Long usuarioId,
            @Param("ano") int ano,
            @Param("mes") int mes
    );
}
