package ru.lephant.java.rgatu.TestingSystem.validators.impl;

import ru.lephant.java.rgatu.TestingSystem.dialogs.DialogFactory;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.imagefilevalidators.ImageExtensionValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.imagefilevalidators.ImageSizeValidator;

import java.io.File;

public class ImageFileValidator implements Validator<File> {

    private ImageExtensionValidator imageExtensionValidator = new ImageExtensionValidator();
    private ImageSizeValidator imageSizeValidator = new ImageSizeValidator();

    @Override
    public boolean validate(File file) {
        if (!imageExtensionValidator.validate(file)) {
            DialogFactory.createValidationErrorAlert(imageExtensionValidator.getMessage()).show();
            return false;
        }
        if (!imageSizeValidator.validate(file)) {
            DialogFactory.createValidationErrorAlert(imageSizeValidator.getMessage()).show();
            return false;
        }
        return true;
    }

    @Override
    public String getMessage() {
        return "Ошибка валидации файла изображения!";
    }

}