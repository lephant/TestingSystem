package ru.lephant.java.rgatu.TestingSystem.controllers;

import javafx.fxml.Initializable;
import javafx.stage.Stage;
import ru.lephant.java.rgatu.TestingSystem.interfaces.Controller;
import ru.lephant.java.rgatu.TestingSystem.interfaces.PostInitializable;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractController implements Controller, Initializable, PostInitializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Empty body
    }

    @Override
    public void postInitialize() {
        // Empty body
    }

    @Override
    public void changePositionOfStage(Stage source, Stage target) {
        double sourceWidth = source.getWidth();
        double sourceHeight = source.getHeight();
        double sourceX = source.getX();
        double sourceY = source.getY();

        double targetWidth = target.getWidth();
        double targetHeight = target.getHeight();

        double sourceCenterX = sourceX + (sourceWidth / 2.0);
        double sourceCenterY = sourceY + (sourceHeight / 2.0);

        double targetX = sourceCenterX - (targetWidth / 2.0);
        double targetY = sourceCenterY - (targetHeight / 2.0);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        targetX = targetX < 0 ? 0 : targetX;
        targetX = targetX + targetWidth > dimension.getWidth() ? dimension.getWidth() - targetWidth : targetX;

        targetY = targetY < 0 ? 0 : targetY;
        targetY = targetY + targetHeight > dimension.getHeight() ? dimension.getHeight() - targetHeight : targetY;

        target.setX(targetX);
        target.setY(targetY);
    }

}