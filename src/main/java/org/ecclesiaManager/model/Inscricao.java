package org.ecclesiaManager.model;

import org.ecclesiaManager.enums.StatusPagamento;
import org.ecclesiaManager.model.dto.InscricaoRequestDTO;
import org.ecclesiaManager.model.dto.InscricaoResponseDTO;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_inscricoes")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "numero_inscricao", unique = true)
    private String numeroInscricao;

    @Column(name = "data_inscricao")
    private LocalDateTime dataInscricao;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "cpf")
    private String cpf;

    @Column(name="comprovante")
    private String comprovante;

    @Column(name="tipo_pagamento")
    private String tipoPagamento;

    @Column(name = "tipo_valor_pagamento")
    private String tipoValorPagamento;

    @Column(name = "valor_pago")
    private BigDecimal valorPago;

    public Inscricao() {
        super();
    }

    public Inscricao(InscricaoRequestDTO dto, Evento evento){
        this.nome = dto.nome();
        this.email = dto.email();
        this.telefone = dto.telefone();
        this.dataInscricao = LocalDateTime.now();
        this.evento = evento;
        this.status = StatusPagamento.PENDENTE.getStatusPagamento();
        this.tipoPagamento = "Dinheiro";
        this.cpf = dto.cpf();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public String getNumeroInscricao() {
        return numeroInscricao;
    }

    public void setNumeroInscricao(String numeroInscricao) {
        this.numeroInscricao = numeroInscricao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getComprovante() {
        return comprovante;
    }

    public void setComprovante(String comprovante) {
        this.comprovante = comprovante;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public String getTipoValorPagamento() {
        return tipoValorPagamento;
    }

    public void setTipoValorPagamento(String tipoValorPagamento) {
        this.tipoValorPagamento = tipoValorPagamento;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }
}
