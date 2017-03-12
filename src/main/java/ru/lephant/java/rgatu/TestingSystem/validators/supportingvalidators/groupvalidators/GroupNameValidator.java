package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.groupvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

public class GroupNameValidator implements Validator<Group> {

    @Override
    public boolean validate(Group group) {
        String groupName = group.getName();
        if (groupName == null) return false;
        if (groupName.trim().length() < 1) return false;
        if (groupName.length() > 45) return false;
        return true;
    }

    @Override
    public String getMessage() {
        return "Имя группы должно быть от 1 до 45 символов!";
    }

}