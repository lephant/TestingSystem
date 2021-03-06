package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.comparators.DateComparatorForTestOfStudent;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.entities.TestOfStudent;

import java.util.Date;
import java.util.List;

public class StudentStatisticsChartWindowController extends AbstractController {

    @FXML
    private AreaChart<String, Double> areaChart;

    private Stage currentStage;

    private Student student;
    private Subject subject;


    @Override
    public void postInitialize() {
        List<TestOfStudent> testsOfStudent = DaoFacade.getStudentResultsDAOService()
                .getResultsOfStudentBySubject(student, subject);
        testsOfStudent.sort(new DateComparatorForTestOfStudent());
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        for (TestOfStudent testOfStudent : testsOfStudent) {
            Date date = testOfStudent.getDateAndTime();
            Double result = testOfStudent.getResult().doubleValue();
            result = ((int) (result * 10000) / 100D);
            XYChart.Data<String, Double> data = new XYChart.Data<>(date.toString(), result);
            series.getData().add(data);
        }

        areaChart.getData().add(series);
    }


    @FXML
    public void onReturnButtonClicked() {
        currentStage.close();
    }


    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
