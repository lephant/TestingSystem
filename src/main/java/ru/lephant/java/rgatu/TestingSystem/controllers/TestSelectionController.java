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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;
import ru.lephant.java.rgatu.TestingSystem.hibernate.HibernateUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TestSelectionController implements Initializable {

    private ObservableList<Test> tests = FXCollections.observableArrayList();

    @FXML
    private TableView<Test> testTableView;

    @FXML
    private TableColumn<Test, String> nameTableColumn;

    @FXML
    private TableColumn<Test, String> teacherTableColumn;

    @FXML
    private TableColumn<Test, String> subjectTableColumn;

    private Stage mainStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();

        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        teacherTableColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        subjectTableColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));

        testTableView.setItems(tests);
    }


    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            testSelected();
        }
    }

    public void onMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            testSelected();
        }
    }

    private void testSelected() {
        Test test = testTableView.getSelectionModel().getSelectedItem();
        if (test != null) {
            openStudentSelectionModalStage(test);
        }
    }

    private void openStudentSelectionModalStage(Test test) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/student_selection.fxml"));
            Parent root = loader.load();

            StudentSelectionController studentSelectionController = loader.getController();
            studentSelectionController.setMainStage(mainStage);
            studentSelectionController.setModalStage(stage);
            studentSelectionController.setTest(test);

            stage.setScene(new Scene(root));
            stage.setTitle("Выбор студента");
            stage.getIcons().add(new Image("/test.png"));

            stage.setResizable(true);
            stage.setWidth(438D);
            stage.setHeight(314D);
            stage.setMinWidth(438D);
            stage.setMinHeight(314D);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            List<Test> list = session
                    .createCriteria(Test.class)
                    .list();
            tests.addAll(list);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void selectTest() {
        testSelected();
    }

}
