package org.ecclesiaManager.enums;

public enum StatusPagamento {
    PENDENTE("Pendente"),
    PAGO("Pago");

    private String statusPagamento;

    StatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }
}
