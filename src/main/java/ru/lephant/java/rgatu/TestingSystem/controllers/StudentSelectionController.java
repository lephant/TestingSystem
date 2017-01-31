package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.entities.Test;

public class StudentSelectionController {

    private Stage mainStage;
    private Stage modalStage;
    private Test test;


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
