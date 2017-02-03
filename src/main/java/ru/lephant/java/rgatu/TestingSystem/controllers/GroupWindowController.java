package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GroupWindowController implements Initializable {

    @FXML
    private ListView<Group> groupListView;
    private ObservableList<Group> groups = FXCollections.observableArrayList();

    private Stage mainStage;
    private Stage modalStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        groupListView.setItems(groups);
        initGroups();
    }


    @FXML
    public void onAddButtonClicked() {
        Group group = new Group();
        boolean needToSave = showGroupChangingDialog("Добавление группы", group);
        if (needToSave) {
            saveGroup(group);
            groups.add(group);
        }
    }

    @FXML
    public void onEditButtonClicked() {
        int index = groupListView.getSelectionModel().getSelectedIndex();
        Group group = groups.get(index);
        if (group == null) {
            showNoSelectedGroupAlert();
            return;
        }
        boolean needToSave = showGroupChangingDialog("Редактирование группы", group);
        if (needToSave) {
            saveGroup(group);
            groups.set(index, group);
        }
    }

    private void showNoSelectedGroupAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка!");
        alert.setHeaderText("Не выбрана группа для редактирования!");
        alert.setContentText(null);
        alert.show();
    }

    @FXML
    public void onDeleteButtonClicked() {
        Group group = groupListView.getSelectionModel().getSelectedItem();
        deleteGroup(group);
        groups.remove(group);
    }


    private boolean showGroupChangingDialog(String title, Group group) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/group_save_window.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(modalStage);
            dialogStage.setScene(new Scene(root));

            GroupSaveWindowController groupSaveWindowController = loader.getController();
            groupSaveWindowController.setModalStage(dialogStage);
            groupSaveWindowController.setGroup(group);
            groupSaveWindowController.fillFields();

            dialogStage.showAndWait();

            return groupSaveWindowController.isNeedToSave();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveGroup(Group group) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(group);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void deleteGroup(Group group) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(group);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initGroups() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            List<Group> list = session.createCriteria(Group.class).list();
            groups.setAll(list);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }
}