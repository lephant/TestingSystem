package ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.groupvalidators;

import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;

public class GroupNameValidator implements Validator<Group> {

    @Override
    public boolean validate(Group group) {
        String groupName = group.getName();
        if (groupName.trim().length() < 1) return false;
        if (groupName.length() > 45) return false;
        return true;
    }

}