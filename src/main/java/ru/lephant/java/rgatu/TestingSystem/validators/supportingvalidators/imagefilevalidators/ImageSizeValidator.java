package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.imagefilevalidators;

import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

import java.io.File;

public class ImageSizeValidator implements Validator<File> {

    private static final long MAX_IMAGE_SIZE = 4194304L;

    @Override
    public boolean validate(File file) {
        return file.length() < MAX_IMAGE_SIZE;
    }

    @Override
    public String getMessage() {
        return "Размер изображения не должен быть больше 4MB!";
    }

}