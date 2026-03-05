package org.ecclesiaManager.model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.ecclesiaManager.model.dto.TransacaoRequestDTO;
import org.ecclesiaManager.util.CriptografiaConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_transacoes")
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "igreja_id")
    private Igreja igreja;

    @Convert(converter = CriptografiaConverter.class)
    private String descricao;

    private BigDecimal valor;

    private String tipo;

    private String categoria;

    private LocalDateTime dataRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id")
    private Evento evento;

    public Transacao() {
        super();
    }

    public Transacao(TransacaoRequestDTO dto) {
        this.tipo = dto.tipo();
        this.valor = dto.valor();
        this.descricao = dto.descricao();
        this.categoria = dto.categoria();
        this.dataRegistro = dto.dataRegistro();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Igreja getIgreja() {
        return igreja;
    }

    public void setIgreja(Igreja igreja) {
        this.igreja = igreja;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}
