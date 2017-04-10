package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.entities.TestOfStudent;
import ru.lephant.java.rgatu.TestingSystem.searchcriteries.StudentResultsSearchCriteria;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

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
    private TableColumn<TestOfStudent, Integer> markTableColumn;

    private Stage mainStage;
    private Stage currentStage;

    private StudentResultsSearchCriteria searchCriteria;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchCriteria = new StudentResultsSearchCriteria();

        groups.setAll(DaoFacade.getGroupDAOService().getList());
        groupListView.setItems(groups);
        groupListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        fioTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getStudent().getFio()));
        groupTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getStudent().getGroup().getName()));
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
    public void onResetButtonClicked() {
        groupListView.getSelectionModel().clearSelection();
        datePicker.setValue(null);
    }

    @FXML
    public void onSearchButtonClicked() {
        configureSearchCriteria();
        List<TestOfStudent> resultList = DaoFacade.getStudentResultsDAOService().getResultsByCriteria(searchCriteria);
        results.setAll(resultList);
        accordion.setExpandedPane(resultsTab);
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


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}