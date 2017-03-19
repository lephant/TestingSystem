package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.entities.Student;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class StudentStatisticsWindowController extends AbstractController {

    private static final String SUBJECT_KEY = "subject";
    private static final String RESULT_KEY = "result";


    @FXML
    private TableView<Map> statisticsTableView;
    private ObservableList<Map> statistics = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Map, Subject> subjectColumn;

    @FXML
    private TableColumn<Map, String> averageResultColumn;

    private Stage currentStage;

    private Student student;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subjectColumn.setCellValueFactory(new MapValueFactory<>(SUBJECT_KEY));
        averageResultColumn.setCellValueFactory(new MapValueFactory<>(RESULT_KEY));

        statisticsTableView.setItems(statistics);
    }

    @Override
    public void postInitialize() {
        fillStatisticsList(DaoFacade.getStudentResultsDAOService().getAllResultsOfStudent(student));
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
            Subject subject = (Subject) currentMap.get(SUBJECT_KEY);
            Stage studentStatisticsChartStage = TransitionFacade.getStudentTransitionService().createStudentStatisticsChartStage(currentStage, student, subject);
            changePositionOfStage(currentStage, studentStatisticsChartStage);
            studentStatisticsChartStage.show();
        } else {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert("Не выбрана запись!");
            noSelectedItemAlert.show();
        }
    }

    @FXML
    public void onReturnButtonClicked() {
        currentStage.close();
    }


    private void fillStatisticsList(List<Object[]> list) {
        for (Object[] obj : list) {
            Map<String, Object> dataRow = new HashMap<>();
            Subject subject = (Subject) obj[0];
            Double res = (Double) obj[1];
            res = (((int) (res * 10000)) / 100D);
            String result = String.valueOf(res + "%");
            dataRow.put(SUBJECT_KEY, subject);
            dataRow.put(RESULT_KEY, result);
            statistics.add(dataRow);
        }
    }


    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}