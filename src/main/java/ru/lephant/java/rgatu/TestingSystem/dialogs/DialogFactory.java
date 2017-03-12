package ru.lephant.java.rgatu.TestingSystem.dialogs;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import ru.lephant.java.rgatu.TestingSystem.reference.ReferenceData;

public class DialogFactory {

    private static final String ERROR_TITLE = "Ошибка!";
    private static final String CONFIRMATION_TITLE = "Подтверждение";
    private static final String VALIDATION_ERROR_HEADER_TEXT = "Ошибка валидации!";
    private static final String AUTHORIZATION_TITLE = "Авторизация";
    private static final String AUTHORIZATION_HEADER_TEXT = "Введите ваши данные:";


    public static Alert createNoSelectedItemAlert(String headerText) {
        Alert alert = createDefaultAlert(Alert.AlertType.ERROR, ERROR_TITLE);
        alert.setHeaderText(headerText);
        return alert;
    }

    public static Alert createDeletingErrorAlert(String headerText) {
        Alert alert = createDefaultAlert(Alert.AlertType.ERROR, ERROR_TITLE);
        alert.setHeaderText(headerText);
        return alert;
    }

    public static Alert createValidationErrorAlert(String contentText) {
        Alert alert = createDefaultAlert(Alert.AlertType.ERROR, ERROR_TITLE);
        alert.setHeaderText(VALIDATION_ERROR_HEADER_TEXT);
        alert.setContentText(contentText);
        return alert;
    }

    public static Alert createConfirmationAlert(String headerText) {
        Alert alert = createDefaultAlert(Alert.AlertType.CONFIRMATION, CONFIRMATION_TITLE);
        alert.setHeaderText(headerText);
        return alert;
    }

    public static Dialog<Pair<String, String>> createLogInDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle(AUTHORIZATION_TITLE);
        dialog.setHeaderText(AUTHORIZATION_HEADER_TEXT);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ReferenceData.getLogoImage());
        dialog.setGraphic(new ImageView(ReferenceData.getLoginImage()));

        ButtonType loginButtonType = new ButtonType("Войти", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Логин");
        PasswordField password = new PasswordField();
        password.setPromptText("Пароль");

        grid.add(new Label("Логин:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Пароль:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(username::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        return dialog;
    }

    public static TextInputDialog createOptionEditDialog(String startValue) {
        TextInputDialog dialog = new TextInputDialog(startValue);
        dialog.setTitle("Редактирование");
        dialog.setHeaderText(null);
        dialog.setContentText("Введите текст варианта ответа:");
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ReferenceData.getLogoImage());
        return dialog;
    }

    public static Alert createAuthorizationErrorAlert() {
        Alert alert = createDefaultAlert(Alert.AlertType.ERROR, ERROR_TITLE);
        alert.setHeaderText("Ошибка авторизации!");
        alert.setContentText("Пользователя с такими данными не существует или он заблокирован!");
        return alert;
    }


    private static Alert createDefaultAlert(Alert.AlertType alertType, String title) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ReferenceData.getLogoImage());
        return alert;
    }

}