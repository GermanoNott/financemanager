package br.com.germanott.financemananger.service;

import br.com.germanott.financemananger.dto.TransacaoDTO;
import br.com.germanott.financemananger.model.Categoria;
import br.com.germanott.financemananger.model.Transacao;
import br.com.germanott.financemananger.model.Usuario;
import br.com.germanott.financemananger.repository.CategoriaRepository;
import br.com.germanott.financemananger.repository.TransacaoRepository;
import br.com.germanott.financemananger.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoService {

    // Injeção de dependências
    @Autowired
    private TransacaoRepository transacaoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    // Metodo para listar transações por usuário
    public List<TransacaoDTO> listarTransacoesPorUsuario(Authentication authentication) {
        Usuario usuarioLogado = (Usuario)  authentication.getPrincipal();

        return transacaoRepository.findByUsuarioId(usuarioLogado.getId())
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Metodo para criar uma nova transação
    public TransacaoDTO criarTransacao(Authentication authentication, TransacaoDTO dto) {
        // 1. Pega o usuário logado
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId()).orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if(!categoria.getUsuario().getId().equals(usuario.getId())){
            throw new RuntimeException("Usuario não autorizado!");
        }

        Transacao novaTransacao = new Transacao();
        novaTransacao.setDescricao(dto.getDescricao());
        novaTransacao.setValor(dto.getValor());
        novaTransacao.setData(dto.getData());
        novaTransacao.setTipo(dto.getTipo());
        novaTransacao.setCategoria(categoria);
        novaTransacao.setUsuario(usuario);

        Transacao salva = transacaoRepository.save(novaTransacao);
        return converterParaDTO(salva);
    }

    // MeTODO ATUALIZAR (Com verificação de segurança)
    public TransacaoDTO atualizarTransacao(Long id, TransacaoDTO dto, Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // 1. Busca a transação e verifica se ela pertence ao usuário
        Transacao transacao = verificarDonoTransacao(id, usuario.getId());

        // 2. (Opcional) Verifica a categoria se ela mudou
        if (!dto.getCategoriaId().equals(transacao.getCategoria().getId())) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada!"));
            if (!categoria.getUsuario().getId().equals(usuario.getId())) {
                throw new RuntimeException("Usuário não autorizado a usar esta categoria.");
            }
            transacao.setCategoria(categoria);
        }

        // 3. Atualiza os dados
        transacao.setDescricao(dto.getDescricao());
        transacao.setValor(dto.getValor());
        transacao.setData(dto.getData());
        transacao.setTipo(dto.getTipo());

        Transacao salva = transacaoRepository.save(transacao);
        return converterParaDTO(salva);
    }

    // MeTODO DELETAR (Com verificação de segurança)
    public void deletarTransacao(Long id, Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // 1. Busca a transação e verifica se ela pertence ao usuário
        Transacao transacao = verificarDonoTransacao(id, usuario.getId());

        // 2. Se for o dono, deleta
        transacaoRepository.delete(transacao);
    }

    // Metodo de segurança: Verifica se a transação 'id' pertence ao 'usuarioId'
    private Transacao verificarDonoTransacao(Long transacaoId, Long usuarioId) {
        Transacao transacao = transacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada!"));

        if (!transacao.getUsuario().getId().equals(usuarioId)) {
            // Se o ID do dono da transação for diferente do ID do usuário logado...
            throw new RuntimeException("Acesso negado. Esta transação pertence a outro usuário.");
        }
        return transacao;
    }

    // Metodo auxiliar para converter entidade Transacao para DTO
    private TransacaoDTO converterParaDTO(Transacao entidade) {
        TransacaoDTO dto = new TransacaoDTO();
        dto.setId(entidade.getId());
        dto.setDescricao(entidade.getDescricao());
        dto.setValor(entidade.getValor());
        dto.setData(entidade.getData());
        dto.setTipo(entidade.getTipo());
        dto.setCategoriaId(entidade.getCategoria().getId());
        dto.setCategoriaNome(entidade.getCategoria().getNome());
        return dto;
    }


}
