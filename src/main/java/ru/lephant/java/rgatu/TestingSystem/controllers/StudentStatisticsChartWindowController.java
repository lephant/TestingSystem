package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.lephant.java.rgatu.TestingSystem.comparators.DateComporatorForTestOfStudent;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.entities.TestOfStudent;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

import java.util.Date;
import java.util.List;

public class StudentStatisticsChartWindowController {

    @FXML
    private AreaChart<String, Double> areaChart;

    private Stage modalStage;

    private Student student;
    private Subject subject;


    public void fillChart() {
        List<TestOfStudent> testsOfStudent = getDataFromDB();
        testsOfStudent.sort(new DateComporatorForTestOfStudent());
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

    @SuppressWarnings("unchecked")
    private List<TestOfStudent> getDataFromDB() {
        Session session = null;
        List<TestOfStudent> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createCriteria(TestOfStudent.class)
                    .add(Restrictions.eq("student", student))
                    .createAlias("test", "test")
                    .add(Restrictions.eq("test.subject", subject))
                    .list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }


    public void onReturnButtonClicked(ActionEvent event) {
        modalStage.close();
    }

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
