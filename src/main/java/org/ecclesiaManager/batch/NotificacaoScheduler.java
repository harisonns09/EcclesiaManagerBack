package org.ecclesiaManager.batch;

import io.quarkus.scheduler.Scheduled;
import jakarta.batch.operations.JobOperator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.ecclesiaManager.infra.audit.Loggable;

import java.util.Properties;

@ApplicationScoped
public class NotificacaoScheduler {

    @Inject
    JobOperator jobOperator;

    // A expressão "0 0 15 * * ?" significa:
    // 0 segundos, 0 minutos, 15 horas (4 da tarde), todos os dias, todos os meses
//    @Scheduled(cron = "0 0 15 * * ?", timeZone = "America/Sao_Paulo")
    @Scheduled(cron = "0 0 9 * * ?", timeZone = "America/Sao_Paulo")
    @Loggable(action = "EXECUCAO_BATCH_NOTIFICACOES", entity = "SISTEMA")
    void dispararJobDiario() {

        jobOperator.start("notificacoes-job", new Properties());
    }
}
