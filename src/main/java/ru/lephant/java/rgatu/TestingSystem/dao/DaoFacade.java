package ru.lephant.java.rgatu.TestingSystem.dao;

import ru.lephant.java.rgatu.TestingSystem.dao.services.impl.*;

public class DaoFacade {

    private static GroupDAOService groupDAOService;

    private static StudentDAOService studentDAOService;

    private static StudentResultsDAOService studentResultsDAOService;

    private static SubjectDAOService subjectDAOService;

    private static TeacherDAOService teacherDAOService;

    private static TestDAOService testDAOService;

    private static UserDAOService userDAOService;


    private DaoFacade() {
    }


    public static GroupDAOService getGroupDAOService() {
        if (groupDAOService == null) {
            groupDAOService = new GroupDAOService();
        }
        return groupDAOService;
    }

    public static StudentDAOService getStudentDAOService() {
        if (studentDAOService == null) {
            studentDAOService = new StudentDAOService();
        }
        return studentDAOService;
    }

    public static StudentResultsDAOService getStudentResultsDAOService() {
        if (studentResultsDAOService == null) {
            studentResultsDAOService = new StudentResultsDAOService();
        }
        return studentResultsDAOService;
    }

    public static SubjectDAOService getSubjectDAOService() {
        if (subjectDAOService == null) {
            subjectDAOService = new SubjectDAOService();
        }
        return subjectDAOService;
    }

    public static TeacherDAOService getTeacherDAOService() {
        if (teacherDAOService == null) {
            teacherDAOService = new TeacherDAOService();
        }
        return teacherDAOService;
    }

    public static TestDAOService getTestDAOService() {
        if (testDAOService == null) {
            testDAOService = new TestDAOService();
        }
        return testDAOService;
    }

    public static UserDAOService getUserDAOService() {
        if (userDAOService == null) {
            userDAOService = new UserDAOService();
        }
        return userDAOService;
    }
}