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
import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GroupWindowController implements Initializable, RefreshableController {

    @FXML
    private ListView<Group> groupListView;
    private ObservableList<Group> groups = FXCollections.observableArrayList();

    private Stage mainStage;
    private Stage currentStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        groupListView.setItems(groups);
        groups.setAll(DaoFacade.getGroupDAOService().getList());
    }

    @Override
    public void refreshData() {
        groups.setAll(DaoFacade.getGroupDAOService().getList());
    }


    @FXML
    public void onAddButtonClicked() {
        Group group = new Group();
        Stage groupSaveStage = TransitionFacade.getGroupTransitionService().createGroupSaveStage(currentStage, "Добавление группы", group, this);
        groupSaveStage.show();
    }

    @FXML
    public void onEditButtonClicked() {
        int index = groupListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            new NoSelectedItemAlert("Не выбрана группа для редактирования!");
            return;
        }
        Group group = groups.get(index);
        Stage groupSaveStage = TransitionFacade.getGroupTransitionService().createGroupSaveStage(currentStage, "Редактирование группы", group, this);
        groupSaveStage.show();
    }

    @FXML
    public void onDeleteButtonClicked() {
        Group group = groupListView.getSelectionModel().getSelectedItem();
        if (group != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText("Вы уверены, что хотите удалить выбранную группу?");
            alert.setContentText(null);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                DaoFacade.getGroupDAOService().delete(group);
                groups.remove(group);
                alert.close();
            } else {
                alert.close();
            }
        } else {
            new NoSelectedItemAlert("Не выбрана группа для удаления!");
        }
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}