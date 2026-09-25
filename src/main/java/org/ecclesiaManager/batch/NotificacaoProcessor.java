package org.ecclesiaManager.batch;

import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.ecclesiaManager.model.Inscricao;
import org.ecclesiaManager.model.Pessoa;

import java.time.LocalDate;
import java.time.Period;

@Named("notificacaoProcessor")
@ApplicationScoped
public class NotificacaoProcessor implements ItemProcessor {
    @Override
    public Object processItem(Object item) {
        if (item instanceof Pessoa p) {

            int idade = Period.between(p.getDataNascimento(), LocalDate.now()).getYears();
            String msg = "Hoje e aniversario de " + p.getNome() + "! " + idade + " anos.";

            return msg;
        } else if (item instanceof Inscricao i) {
            return "Olá " + i.getNome() + ", lembramos que o evento " + i.getEvento().getNomeEvento() + " será em 2 dias!";
        }
        return null;
    }
}