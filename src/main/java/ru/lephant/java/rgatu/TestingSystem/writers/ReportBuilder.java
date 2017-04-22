package ru.lephant.java.rgatu.TestingSystem.writers;

import org.apache.poi.xwpf.usermodel.*;
import ru.lephant.java.rgatu.TestingSystem.entities.TestOfStudent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ReportBuilder {

    public XWPFDocument createReport(Map<String, Integer> commonData, List<TestOfStudent> results) {
        XWPFDocument document = new XWPFDocument();
        addHeader(document);
        addCommonData(document, commonData);
        addTableData(document, results);
        return document;
    }

    private void addHeader(XWPFDocument document) {
        XWPFParagraph header = document.createParagraph();
        header.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun headerRun = header.createRun();
        headerRun.setBold(true);
        headerRun.setFontSize(24);
        headerRun.setText("Отчет");
        headerRun.addBreak();
    }

    private void addCommonData(XWPFDocument document, Map<String, Integer> commonData) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        for (Map.Entry<String, Integer> entry : commonData.entrySet()) {
            XWPFRun run = paragraph.createRun();
            run.setText(entry.getKey() + entry.getValue());
            run.addBreak();
        }
        paragraph.createRun().addBreak();
    }

    private void addTableData(XWPFDocument document, List<TestOfStudent> data) {
        XWPFTable table = document.createTable(data.size() + 1, 5);
        table.setCellMargins(100, 100, 100, 100);

        XWPFTableRow headerRow = table.getRow(0);
        headerRow.getCell(0).setText("ФИО студента");
        headerRow.getCell(1).setText("Группа");
        headerRow.getCell(2).setText("Имя компьютера");
        headerRow.getCell(3).setText("Дата и время");
        headerRow.getCell(4).setText("Оценка");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        for (int i = 0; i < data.size(); i++) {
            XWPFTableRow row = table.getRow(i + 1);

            TestOfStudent testOfStudent = data.get(i);
            List<XWPFTableCell> tableCells = row.getTableCells();

            XWPFTableCell fioTableCell = tableCells.get(0);
            fioTableCell.setText(testOfStudent.getStudent().getFio());
            fioTableCell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTH);

            XWPFTableCell groupTableCell = tableCells.get(1);
            groupTableCell.setText(testOfStudent.getStudent().getGroup().getName());
            groupTableCell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTH);

            XWPFTableCell computerNameTableCell = tableCells.get(2);
            computerNameTableCell.setText(testOfStudent.getComputerName());
            computerNameTableCell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTH);

            XWPFTableCell dateAndTimeTableCell = tableCells.get(3);
            dateAndTimeTableCell.setText(dateFormat.format(testOfStudent.getDateAndTime()));
            dateAndTimeTableCell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTH);

            XWPFTableCell markTableCell = tableCells.get(4);
            markTableCell.setText(String.valueOf(testOfStudent.getMark()));
            markTableCell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTH);
        }
    }

}