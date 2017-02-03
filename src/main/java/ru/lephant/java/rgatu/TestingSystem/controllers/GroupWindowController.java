package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.hibernate.Session;
import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

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