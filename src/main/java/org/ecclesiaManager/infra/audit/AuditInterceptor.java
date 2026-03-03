package org.ecclesiaManager.infra.audit;

import io.quarkus.narayana.jta.QuarkusTransaction; // <-- NOVO IMPORT
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.ecclesiaManager.model.AuditLog;
import org.ecclesiaManager.repository.AuditLogRepository;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Loggable
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 1)
public class AuditInterceptor {

    @Inject
    AuditLogRepository auditLogRepository;

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    JsonWebToken jwt;

    @AroundInvoke
    // Remova a anotação @Transactional daqui!
    public Object logAuditoria(InvocationContext context) throws Exception {

        // 1. Deixa a lógica principal (o método do Controller) rodar normalmente
        Object result = context.proceed();

        try {
            Loggable loggable = context.getMethod().getAnnotation(Loggable.class);
            String username = (securityIdentity.isAnonymous()) ? "SISTEMA" : securityIdentity.getPrincipal().getName();
            Long churchId = null;

            try {
                if (jwt != null && jwt.containsClaim("churchId")) {
                    churchId = Long.parseLong(jwt.getClaim("churchId").toString());
                }
            } catch (Exception e) {
                System.err.println("Aviso: Claim churchId não encontrada no JWT.");
            }

            if (churchId == null || churchId == 0L) {
                for (Object arg : context.getParameters()) {
                    if (arg instanceof Long) {
                        churchId = (Long) arg;
                        break;
                    }
                }
            }

            if (churchId == null || churchId == 0L) {
                churchId = 1L; // Fallback final
            }

            AuditLog log = new AuditLog();
            log.setAction(loggable.action());
            log.setEntityName(loggable.entity());
            log.setUsername(username);
            log.setChurchId(churchId);
            log.setDetails("Operação realizada com sucesso no método: " + context.getMethod().getName());

            // 2. A MÁGICA: Abre uma transação isolada EXCLUSIVAMENTE para salvar o log!
            QuarkusTransaction.requiringNew().run(() -> {
                auditLogRepository.persistAndFlush(log);
            });

        } catch (Exception e) {
            System.err.println("Erro ao salvar log de auditoria: " + e.getMessage());
            e.printStackTrace();
        }

        return result; // Retorna a resposta pro React em paz
    }
}