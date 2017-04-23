package ru.lephant.java.rgatu.TestingSystem.validators.impl;

import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.imagefilevalidators.ImageExtensionValidator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.imagefilevalidators.ImageSizeValidator;

import java.io.File;

public class ImageFileValidator implements Validator<File> {

    private ImageExtensionValidator imageExtensionValidator = new ImageExtensionValidator();
    private ImageSizeValidator imageSizeValidator = new ImageSizeValidator();

    private String message;

    @Override
    public boolean validate(File file) {
        if (!imageExtensionValidator.validate(file)) {
            message = imageExtensionValidator.getMessage();
            return false;
        }
        if (!imageSizeValidator.validate(file)) {
            message = imageSizeValidator.getMessage();
            return false;
        }
        return true;
    }

    @Override
    public String getMessage() {
        return message;
    }

}