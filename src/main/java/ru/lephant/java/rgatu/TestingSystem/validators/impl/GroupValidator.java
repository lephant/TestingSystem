package ru.lephant.java.rgatu.TestingSystem.validators.impl;

import ru.lephant.java.rgatu.TestingSystem.entities.Group;
import ru.lephant.java.rgatu.TestingSystem.validators.Validator;
import ru.lephant.java.rgatu.TestingSystem.validators.supportingvalidators.groupvalidators.GroupNameValidator;

public class GroupValidator implements Validator<Group> {

    private GroupNameValidator groupNameValidator = new GroupNameValidator();


    @Override
    public boolean validate(Group group) {
        if (!groupNameValidator.validate(group)) return false;
        return true;
    }

}