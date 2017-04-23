package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.imagefilevalidators;

import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageExtensionValidator implements Validator<File> {

    private List<String> supportingFileExtensions;

    public ImageExtensionValidator() {
        supportingFileExtensions = new ArrayList<>();
        supportingFileExtensions.add(".jpg");
        supportingFileExtensions.add(".jpeg");
        supportingFileExtensions.add(".bmp");
        supportingFileExtensions.add(".png");
        supportingFileExtensions.add(".gif");
    }


    @Override
    public boolean validate(File file) {
        String lowerFilename = file.getName().toLowerCase();
        int dotIndex = lowerFilename.lastIndexOf(".");
        String fileExtension = lowerFilename.substring(dotIndex);
        for (String supportingExtension : supportingFileExtensions) {
            if (supportingExtension.equals(fileExtension)) return true;
        }
        return false;
    }

    @Override
    public String getMessage() {
        return "Выбранный файл должен иметь расширение jpg, jpeg, bmp, png или gif!";
    }

}