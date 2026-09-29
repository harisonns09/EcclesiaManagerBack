package org.ecclesiaManager.batch;

import jakarta.batch.api.chunk.AbstractItemReader;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.ecclesiaManager.model.Pessoa;
import org.ecclesiaManager.repository.PessoaRepository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Iterator;

@Named("aniversarianteReader")
@ApplicationScoped
public class AniversarianteReader extends AbstractItemReader {
    @Inject
    PessoaRepository repository;
    private Iterator<Pessoa> iterator;


    @Override
    public void open(Serializable checkpoint) {
        LocalDate hoje = LocalDate.now();
        iterator = repository.findAniversariantesDoDia(hoje.getMonthValue(), hoje.getDayOfMonth()).iterator();
    }

    @Override
    public Object readItem() {
        return iterator.hasNext() ? iterator.next() : null;

    }
}
