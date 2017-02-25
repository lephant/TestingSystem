package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import ru.lephant.java.rgatu.TestingSystem.dialogs.NoSelectedItemAlert;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.entities.TestOfStudent;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class StudentStatisticsWindowController implements Initializable {

    private static final String SUBJECT_KEY = "subject";
    private static final String RESULT_KEY = "result";
    @FXML
    private TableView<Map> statisticsTableView;
    private ObservableList<Map> statistics = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Map, Subject> subjectColumn;

    @FXML
    private TableColumn<Map, String> averageResultColumn;

    private Stage mainStage;
    private Stage modalStage;

    private Student student;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subjectColumn.setCellValueFactory(new MapValueFactory<>(SUBJECT_KEY));
        averageResultColumn.setCellValueFactory(new MapValueFactory<>(RESULT_KEY));

        statisticsTableView.setItems(statistics);
    }


    public void fillContent() {
        List list = getStatisticsFromDB();
        fillStatisticsList(list);
    }


    @FXML
    public void onStatisticsTableViewKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            showStudentStatisticsChartStage();
        }
    }

    @FXML
    public void onStatisticsTableViewMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            showStudentStatisticsChartStage();
        }
    }

    @FXML
    public void onMoreDetailsButtonClicked() {
        showStudentStatisticsChartStage();
    }

    private void showStudentStatisticsChartStage() {
        Map currentMap = statisticsTableView.getSelectionModel().getSelectedItem();
        if (currentMap != null) {
            try {
                Subject subject = (Subject) currentMap.get(SUBJECT_KEY);

                Stage stage = new Stage();

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/student_statistics_chart_window.fxml"));
                Parent root = loader.load();

                StudentStatisticsChartWindowController statisticsChartWindowController = loader.getController();
                statisticsChartWindowController.setModalStage(stage);
                statisticsChartWindowController.setStudent(student);
                statisticsChartWindowController.setSubject(subject);
                statisticsChartWindowController.fillChart();

                stage.setScene(new Scene(root));
                stage.setTitle("Статистика студента");
                stage.getIcons().add(new Image("/test.png"));

                stage.setResizable(true);
                stage.setWidth(600D);
                stage.setHeight(600D);
                stage.setMinWidth(500D);
                stage.setMinHeight(500D);

                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(modalStage);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new NoSelectedItemAlert("Не выбрана запись!");
        }
    }

    @FXML
    public void onReturnButtonClicked() {
        modalStage.close();
    }


    private List getStatisticsFromDB() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TestOfStudent.class);
        criteria.add(Restrictions.eq("student", student));
        criteria.createAlias("test", "test");
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.groupProperty("test.subject"));
        projectionList.add(Projections.avg("result"));
        criteria.setProjection(projectionList);
        return criteria.list();
    }

    private void fillStatisticsList(List list) {
        for (Object aList : list) {
            Map<String, Object> dataRow = new HashMap<>();
            Object[] obj = (Object[]) aList;
            Subject subject = (Subject) obj[0];
            Double res = (Double) obj[1];
            res = (((int) (res * 10000)) / 100D);
            String result = String.valueOf(res + "%");
            dataRow.put(SUBJECT_KEY, subject);
            dataRow.put(RESULT_KEY, result);
            statistics.add(dataRow);
        }
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}