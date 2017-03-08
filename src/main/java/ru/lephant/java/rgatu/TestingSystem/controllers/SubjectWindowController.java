package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.NoSelectedItemAlert;
import ru.lephant.java.rgatu.TestingSystem.entities.Subject;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SubjectWindowController implements Initializable, RefreshableController {

    @FXML
    private ListView<Subject> subjectListView;
    private ObservableList<Subject> subjects = FXCollections.observableArrayList();

    private Stage mainStage;
    private Stage currentStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subjectListView.setItems(subjects);
        subjects.setAll(DaoFacade.getSubjectDAOService().getList());
    }

    @Override
    public void refreshData() {
        subjects.setAll(DaoFacade.getSubjectDAOService().getList());
    }


    @FXML
    public void onAddButtonClicked() {
        Subject subject = new Subject();
        Stage subjectSaveStage = TransitionFacade.getSubjectTransitionService().createSubjectSaveStage(currentStage, "Добавление предмета", subject, this);
        subjectSaveStage.show();
    }

    @FXML
    public void onEditButtonClicked() {
        int index = subjectListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            new NoSelectedItemAlert("Не выбран предмет для редактирования!");
            return;
        }
        Subject subject = subjects.get(index);
        Stage subjectSaveStage = TransitionFacade.getSubjectTransitionService().createSubjectSaveStage(currentStage, "Редактирование предмета", subject, this);
        subjectSaveStage.show();
    }

    @FXML
    public void onDeleteButtonClicked() {
        Subject subject = subjectListView.getSelectionModel().getSelectedItem();
        if (subject != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText("Вы уверены, что хотите удалить выбранный предмет?");
            alert.setContentText(null);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                DaoFacade.getSubjectDAOService().delete(subject);
                subjects.remove(subject);
                alert.close();
            } else {
                alert.close();
            }
        } else {
            new NoSelectedItemAlert("Не выбран предмет для удаления!");
        }
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}