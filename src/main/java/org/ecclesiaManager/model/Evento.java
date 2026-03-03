package org.ecclesiaManager.model;

import org.ecclesiaManager.model.dto.EventoRequestDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_evento", nullable = false, unique = true)
    private String nomeEvento;

    @Column(name = "data_evento", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEvento;

    @Column(name = "ministerio_responsavel", nullable = false)
    private String ministerioResponsavel;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Inscricao> inscricoes = new ArrayList<>();

    @Column(name = "horario")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horario;

    @Column(name = "local_evento")
    private String local;

    @Column(name = "preco")
    private BigDecimal preco;

    @Column(name = "preco_promocional")
    private BigDecimal precoPromocional;

    @ManyToOne
    @JoinColumn(name = "igreja_id", nullable = false)
    private Igreja igreja;

    public Evento() {
        super();
    }

    public Evento(EventoRequestDTO dto) {
        this.nomeEvento = dto.nomeEvento();
        this.dataEvento = dto.dataEvento();
        this.ministerioResponsavel = dto.ministerioResponsavel();
        this.descricao = dto.descricao();
        this.horario = dto.horario();
        this.local = dto.local();
        this.preco = dto.preco();


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public LocalDate getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(LocalDate dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getMinisterioResponsavel() {
        return ministerioResponsavel;
    }

    public void setMinisterioResponsavel(String ministerioResponsavel) {
        this.ministerioResponsavel = ministerioResponsavel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Inscricao> getInscricoes() {
        return inscricoes;
    }

    public void setInscricoes(List<Inscricao> inscricoes) {
        this.inscricoes = inscricoes;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Igreja getIgreja() {
        return igreja;
    }
    public void setIgreja(Igreja igreja) {
        this.igreja = igreja;
    }

    public BigDecimal getPrecoPromocional() {
        return precoPromocional;
    }
    public BigDecimal setPrecoPromocional(BigDecimal precoPromocional){
        return this.precoPromocional = precoPromocional;
    }
}