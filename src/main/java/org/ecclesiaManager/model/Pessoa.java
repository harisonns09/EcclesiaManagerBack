package org.ecclesiaManager.model;

import org.ecclesiaManager.model.dto.PessoaRequestDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_pessoas")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "telefone")
    private String telefone;

    @Column(nullable = false)
    private String email;

    private String ministerio;

    @Column(nullable = false)
    private String status;

    @Column(length = 1)
    private String genero;

    @Column(name = "estado_civil")
    private String estadoCivil;

    private String cep;
    private String endereco;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String complemento;

    private String observacao;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_batismo")
    private LocalDate dataBatismo;

    @ManyToOne
    @JoinColumn(name = "igreja_id", nullable = false)
    private Igreja igreja;

    @Embedded
    private TrilhaIntegracao trilha = new TrilhaIntegracao();


    public Pessoa() {
        super();
    }

    public Pessoa(PessoaRequestDTO dto) {
        this.nome = dto.nome();
        this.dataNascimento = dto.dataNascimento();
        this.telefone = dto.telefone();
        this.email = dto.email();
        this.ministerio = dto.ministerio();
        this.status = dto.status();
        this.genero = dto.genero();
        this.estadoCivil = dto.estadoCivil();
        this.cep = dto.cep();
        this.endereco = dto.endereco();
        this.numero = dto.numero();
        this.bairro = dto.bairro();
        this.cidade = dto.cidade();
        this.estado = dto.estado();
        this.complemento = dto.complemento();
        this.dataBatismo = dto.dataBatismo();
        if (dto.igrejaId() != null) {
            this.igreja = new Igreja();
            this.igreja.setId(dto.igrejaId());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
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

    public String getMinisterio() {
        return ministerio;
    }

    public void setMinisterio(String ministerio) {
        this.ministerio = ministerio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Igreja getIgreja() {
        return igreja;
    }

    public void setIgreja(Igreja igreja) {
        this.igreja = igreja;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getDataBatismo() {
        return dataBatismo;
    }

    public void setDataBatismo(LocalDate dataBatismo) {
        this.dataBatismo = dataBatismo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getComplemento() {

        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public TrilhaIntegracao getTrilha() {
        return trilha;
    }

    public void setTrilha(TrilhaIntegracao trilha) {
        this.trilha = trilha;
    }
}