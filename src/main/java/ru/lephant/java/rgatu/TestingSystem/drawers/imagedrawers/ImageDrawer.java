package ru.lephant.java.rgatu.TestingSystem.drawers.imagedrawers;

import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.lephant.java.rgatu.TestingSystem.entities.Question;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ImageDrawer {

    private static final double IMAGE_VIEW_DEFAULT_WIDTH = 450D;


    public void drawImage(Question question, VBox choiceBox, int position) {
        InputStream stream = new ByteArrayInputStream(question.getImage());
        javafx.scene.image.Image imageFX = new javafx.scene.image.Image(stream);
        double imageWidth = imageFX.getWidth();
        double imageHeight = imageFX.getHeight();
        double imageViewHeight = (IMAGE_VIEW_DEFAULT_WIDTH * imageHeight) / imageWidth;

        ImageView imageView = new ImageView(imageFX);
        imageView.setFitWidth(IMAGE_VIEW_DEFAULT_WIDTH);
        imageView.setFitHeight(imageViewHeight);

        choiceBox.getChildren().add(position, imageView);
    }

}