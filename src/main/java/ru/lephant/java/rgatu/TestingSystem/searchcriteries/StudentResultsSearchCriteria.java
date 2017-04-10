package ru.lephant.java.rgatu.TestingSystem.searchcriteries;

import ru.lephant.java.rgatu.TestingSystem.entities.Group;

import java.util.Date;
import java.util.List;

public class StudentResultsSearchCriteria {

    private List<Group> groups;

    private Date date;


    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}