package ru.lephant.java.rgatu.TestingSystem;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.util.HibernateUtil;
import ru.lephant.java.rgatu.TestingSystem.transitions.TransitionFacade;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage unusedStage) throws Exception {
        if (HibernateUtil.checkConnection()) {
            Stage testSelectionStage = TransitionFacade.getTestTransitionService().createTestSelectionStage();
            testSelectionStage.show();
        } else {
            Stage connectionSettingStage = TransitionFacade.getConnectionTransitionService().createConnectionSettingStage();
            connectionSettingStage.show();
        }
    }

}