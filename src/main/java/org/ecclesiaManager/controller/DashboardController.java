package org.ecclesiaManager.controller;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.model.dto.EventoResponseDTO;
import org.ecclesiaManager.model.dto.dashboard.*;
import org.ecclesiaManager.repository.*;

import java.time.LocalDate;
import java.util.List;

@Path("/api/igrejas/{igrejaId}/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardController {

    @Inject PessoaRepository pessoaRepository;
    @Inject MinisterioRepository ministerioRepository;
    @Inject EventoRepository eventoRepository;

    @GET
    public Response getDashboard(@PathParam("igrejaId") Long igrejaId) {
        LocalDate hoje = LocalDate.now();
        int mesAtual = hoje.getMonthValue();

        // 1. Resumo Numérico
        DashboardResumoDTO resumo = new DashboardResumoDTO(
                pessoaRepository.countMembrosByIgreja(igrejaId),
                pessoaRepository.countVisitantesByIgreja(igrejaId),
                ministerioRepository.countByIgreja(igrejaId),
                eventoRepository.countEventosFuturosByIgreja(igrejaId, hoje)
        );

        // 2. Aniversariantes e Eventos
        List<AniversarianteDTO> aniversariantes = pessoaRepository.findAniversariantesDoMes(igrejaId, mesAtual)
                .stream().map(AniversarianteDTO::new).toList();

        List<EventoResponseDTO> proximosEventos = eventoRepository.findProximosEventos(igrejaId, hoje)
                .stream().map(EventoResponseDTO::new).toList();

        return Response.ok(new DashboardResponseDTO(resumo, aniversariantes, proximosEventos)).build();
    }
}
