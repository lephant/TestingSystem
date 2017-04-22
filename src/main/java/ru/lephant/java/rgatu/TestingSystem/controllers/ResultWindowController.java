package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.entities.TestOfStudent;
import ru.lephant.java.rgatu.TestingSystem.searchcriteries.StudentResultsSearchCriteria;
import ru.lephant.java.rgatu.TestingSystem.writers.ReportBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

public class ResultWindowController extends AbstractController {

    @FXML
    private Accordion accordion;

    @FXML
    private TitledPane searchTab;

    @FXML
    private TitledPane resultsTab;

    @FXML
    private ListView<Group> groupListView;
    private ObservableList<Group> groups = FXCollections.observableArrayList();

    @FXML
    private DatePicker datePicker;

    @FXML
    private CheckBox dateCheckBox;

    @FXML
    private TableView<TestOfStudent> resultsTableView;
    private ObservableList<TestOfStudent> results = FXCollections.observableArrayList();

    @FXML
    private TableColumn<TestOfStudent, String> fioTableColumn;

    @FXML
    private TableColumn<TestOfStudent, String> groupTableColumn;

    @FXML
    private TableColumn<TestOfStudent, String> computerTableColumn;

    @FXML
    private TableColumn<TestOfStudent, String> dateAndTimeTableColumn;

    @FXML
    private TableColumn<TestOfStudent, Integer> markTableColumn;

    private Stage mainStage;
    private Stage currentStage;

    private StudentResultsSearchCriteria searchCriteria;
    private ReportBuilder reportBuilder;
    private DateFormat dateFormat;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchCriteria = new StudentResultsSearchCriteria();
        reportBuilder = new ReportBuilder();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        groups.setAll(DaoFacade.getGroupDAOService().getList());
        groupListView.setItems(groups);
        groupListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        fioTableColumn.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue().getStudent().getFio()));
        groupTableColumn.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue().getStudent().getGroup().getName()));
        computerTableColumn.setCellValueFactory(new PropertyValueFactory<>("computerName"));
        dateAndTimeTableColumn.setCellValueFactory(param ->
                new ReadOnlyStringWrapper(dateFormat.format(param.getValue().getDateAndTime())));
        markTableColumn.setCellValueFactory(new PropertyValueFactory<>("mark"));

        resultsTableView.setItems(results);
    }

    @Override
    public void postInitialize() {
        currentStage.setOnCloseRequest(event -> {
            changePositionOfStage(currentStage, mainStage);
            mainStage.show();
        });

        accordion.expandedPaneProperty().addListener((observable, oldValue, newValue) -> {
            if (searchTab.equals(newValue)) {
                currentStage.setMaxWidth(412D);
                currentStage.setMaxHeight(260D);
            } else if (resultsTab.equals(newValue)) {
                currentStage.setMaxWidth(Double.MAX_VALUE);
                currentStage.setMaxHeight(Double.MAX_VALUE);
            }
        });

        accordion.setExpandedPane(searchTab);
    }


    @FXML
    public void onSearchButtonClicked() {
        configureSearchCriteria();
        List<TestOfStudent> resultList = DaoFacade.getStudentResultsDAOService().getResultsByCriteria(searchCriteria);
        results.setAll(resultList);
        accordion.setExpandedPane(resultsTab);
    }

    @FXML
    public void onResetButtonClicked() {
        groupListView.getSelectionModel().clearSelection();
        datePicker.setValue(null);
    }

    @FXML
    public void onReportComposeButtonClicked() {
        if (results.size() > 0) {
            String fileName = generateFileName();
            try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
                Map<String, Integer> commonData = fillCommonData();
                XWPFDocument document = reportBuilder.createReport(commonData, results);
                document.write(outputStream);
                Alert informationAlert = DialogFactory.createInformationAlert("Сообщение", "Отчет составлен успешно и сохранен в папку /reports.");
                informationAlert.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert(
                    "Отчет можно составить только в том случае, если найден хотя бы 1 студент!");
            noSelectedItemAlert.show();
        }
    }

    @FXML
    public void onDeleteMenuItemClicked() {
        TestOfStudent selectedItem = resultsTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            results.remove(selectedItem);
        } else {
            Alert deletingErrorAlert = DialogFactory.createDeletingErrorAlert("Не выбрана запись для удаления из списка!");
            deletingErrorAlert.showAndWait();
        }
    }


    private Map<String, Integer> fillCommonData() {
        int countOfA = 0, countOfB = 0, countOfC = 0, countOfD = 0;
        for (TestOfStudent testOfStudent : results) {
            switch (testOfStudent.getMark()) {
                case 5:
                    countOfA++;
                    break;
                case 4:
                    countOfB++;
                    break;
                case 3:
                    countOfC++;
                    break;
                case 2:
                    countOfD++;
                    break;
            }
        }
        Map<String, Integer> commonData = new LinkedHashMap<>();
        commonData.put("Количество оценок \"5\": ", countOfA);
        commonData.put("Количество оценок \"4\": ", countOfB);
        commonData.put("Количество оценок \"3\": ", countOfC);
        commonData.put("Количество оценок \"2\": ", countOfD);
        return commonData;
    }

    private void configureSearchCriteria() {
        if (dateCheckBox.isSelected() && datePicker.getValue() != null) {
            searchCriteria.setDate(Date.from(datePicker.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        } else {
            searchCriteria.setDate(null);
        }
        ObservableList<Group> selectedItems = groupListView.getSelectionModel().getSelectedItems();
        if (selectedItems != null && !selectedItems.isEmpty()) {
            searchCriteria.setGroups(selectedItems);
        } else {
            searchCriteria.setGroups(null);
        }
    }

    private String generateFileName() {
        Calendar calendar = Calendar.getInstance();
        String stringDate = calendar.get(Calendar.YEAR)
                + "-" + calendar.get(Calendar.MONTH)
                + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                + "-" + calendar.get(Calendar.HOUR_OF_DAY)
                + "-" + calendar.get(Calendar.MINUTE)
                + "-" + calendar.get(Calendar.SECOND);
        File file = new File("reports");
        if (!file.exists() || !file.isDirectory()) file.mkdir();
        return "reports/report-" + stringDate + ".doc";
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}