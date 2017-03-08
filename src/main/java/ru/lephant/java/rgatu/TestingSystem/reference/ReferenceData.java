package ru.lephant.java.rgatu.TestingSystem.reference;

import javafx.scene.image.Image;

public class ReferenceData {

    private static Image logoImage = new Image("/test.png");
    private static Image loginImage = new Image("/login.png");


    private ReferenceData() {
    }


    public static Image getLogoImage() {
        return logoImage;
    }

    public static Image getLoginImage() {
        return loginImage;
    }
}