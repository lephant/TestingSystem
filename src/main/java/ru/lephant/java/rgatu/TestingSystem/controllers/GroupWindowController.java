package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.dao.DaoFacade;
import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.interfaces.RefreshableController;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GroupWindowController extends AbstractController implements RefreshableController {

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
    public void postInitialize() {
        currentStage.setOnCloseRequest(event -> {
            changePositionOfStage(currentStage, mainStage);
            mainStage.show();
        });
    }

    @Override
    public void refreshData() {
        groups.setAll(DaoFacade.getGroupDAOService().getList());
    }


    @FXML
    public void onAddButtonClicked() {
        Group group = new Group();
        Stage groupSaveStage = TransitionFacade.getGroupTransitionService().createGroupSaveStage(currentStage, "Добавление группы", group, this);
        changePositionOfStage(currentStage, groupSaveStage);
        groupSaveStage.show();
    }

    @FXML
    public void onEditButtonClicked() {
        int index = groupListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert("Не выбрана группа для редактирования!");
            noSelectedItemAlert.show();
            return;
        }
        Group group = groups.get(index);
        Stage groupSaveStage = TransitionFacade.getGroupTransitionService().createGroupSaveStage(currentStage, "Редактирование группы", group, this);
        changePositionOfStage(currentStage, groupSaveStage);
        groupSaveStage.show();
    }

    @FXML
    public void onDeleteButtonClicked() {
        Group group = groupListView.getSelectionModel().getSelectedItem();
        if (group != null) {
            Alert confirmationAlert = DialogFactory.createConfirmationAlert("Вы уверены, что хотите удалить выбранную группу?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (DaoFacade.getGroupDAOService().delete(group)) {
                    groups.remove(group);
                } else {
                    Alert deletingErrorAlert = DialogFactory.createDeletingErrorAlert("Невозможно удалить эту группу!\nВозможно в ней есть студенты.");
                    deletingErrorAlert.show();
                }
            }
        } else {
            Alert noSelectedItemAlert = DialogFactory.createNoSelectedItemAlert("Не выбрана группа для удаления!");
            noSelectedItemAlert.show();
        }
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}