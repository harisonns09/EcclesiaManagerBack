package org.ecclesiaManager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class TrilhaIntegracao {

    @Column(name = "trilha_decidiu_membro")
    private Boolean decidiuSerMembro = false;

    @Column(name = "trilha_data_decisao")
    private LocalDate dataDecisao;

    @Column(name = "trilha_cafe_concluido")
    private Boolean cafePastorConcluido = false;

    @Column(name = "trilha_celula_concluida")
    private Boolean visitaCelulaConcluida = false;

    @Column(name = "trilha_classe_concluida")
    private Boolean classeIntegracaoConcluida = false;

    @Column(name = "trilha_data_apresentacao")
    private LocalDate dataApresentacao;

    // Método Mágico da Gamificação: Calcula a % de avanço em tempo real
    public int calcularProgresso() {
        if (!Boolean.TRUE.equals(decidiuSerMembro)) return 0;

        int progresso = 25; // Já ganha 25% por ter decidido começar a trilha
        if (Boolean.TRUE.equals(cafePastorConcluido)) progresso += 25;
        if (Boolean.TRUE.equals(visitaCelulaConcluida)) progresso += 25;
        if (Boolean.TRUE.equals(classeIntegracaoConcluida)) progresso += 25;

        return progresso;
    }

    // Getters e Setters
    public Boolean getDecidiuSerMembro() { return decidiuSerMembro; }
    public void setDecidiuSerMembro(Boolean decidiuSerMembro) { this.decidiuSerMembro = decidiuSerMembro; }
    public LocalDate getDataDecisao() { return dataDecisao; }
    public void setDataDecisao(LocalDate dataDecisao) { this.dataDecisao = dataDecisao; }
    public Boolean getCafePastorConcluido() { return cafePastorConcluido; }
    public void setCafePastorConcluido(Boolean cafePastorConcluido) { this.cafePastorConcluido = cafePastorConcluido; }
    public Boolean getVisitaCelulaConcluida() { return visitaCelulaConcluida; }
    public void setVisitaCelulaConcluida(Boolean visitaCelulaConcluida) { this.visitaCelulaConcluida = visitaCelulaConcluida; }
    public Boolean getClasseIntegracaoConcluida() { return classeIntegracaoConcluida; }
    public void setClasseIntegracaoConcluida(Boolean classeIntegracaoConcluida) { this.classeIntegracaoConcluida = classeIntegracaoConcluida; }
    public LocalDate getDataApresentacao() { return dataApresentacao; }
    public void setDataApresentacao(LocalDate dataApresentacao) { this.dataApresentacao = dataApresentacao; }
}