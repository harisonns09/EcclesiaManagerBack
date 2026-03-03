package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.ecclesiaManager.model.Evento;
import org.ecclesiaManager.model.Inscricao;
import org.ecclesiaManager.repository.EventoRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ApplicationScoped
public class RelatorioServiceImpl implements IRelatorioService {

    @Inject
    EventoRepository eventoRepository;

    @Override
    @Transactional
    public byte[] gerarPlanilhaInscritos(Long eventoId) {
        Evento evento = eventoRepository.findByIdOptional(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        List<Inscricao> inscricoes = evento.getInscricoes();

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            workbook.setCompressTempFiles(true);

            Sheet sheet = workbook.createSheet("Inscritos");
            ((org.apache.poi.xssf.streaming.SXSSFSheet) sheet).trackAllColumnsForAutoSizing();

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);

            Row headerRow = sheet.createRow(0);
            String[] colunas = {"ID", "Nome Completo", "Email", "Telefone", "Data Inscrição", "Status", "Valor Pago (R$)"};

            for (int i = 0; i < colunas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(colunas[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Inscricao inscricao : inscricoes) {
                Row row = sheet.createRow(rowIdx++);

                createCell(row, 0, inscricao.getNumeroInscricao(), dataStyle);
                createCell(row, 1, inscricao.getNome(), dataStyle);
                createCell(row, 2, inscricao.getEmail(), dataStyle);
                createCell(row, 3, inscricao.getTelefone(), dataStyle);

                String dataFmt = inscricao.getDataInscricao() != null ? inscricao.getDataInscricao().format(formatter) : "-";
                createCell(row, 4, dataFmt, dataStyle);

                createCell(row, 5, inscricao.getStatus(), dataStyle);

                BigDecimal valor = "PAGO".equalsIgnoreCase(inscricao.getStatus()) ? inscricao.getValorPago() : BigDecimal.ZERO;
                createCell(row, 6, valor != null ? valor.toString() : "0.00", dataStyle);
            }

            for (int i = 0; i < colunas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            workbook.dispose();

            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Falha ao gerar arquivo Excel: " + e.getMessage());
        }
    }

    private void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
}