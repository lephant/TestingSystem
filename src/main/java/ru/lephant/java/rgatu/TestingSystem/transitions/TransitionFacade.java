package ru.lephant.java.rgatu.TestingSystem.transitions;

import ru.lephant.java.rgatu.TestingSystem.transitions.services.*;

public class TransitionFacade {

    private static ConnectionTransitionService connectionTransitionService;

    private static TestTransitionService testTransitionService;

    private static StudentTransitionService studentTransitionService;

    private static GroupTransitionService groupTransitionService;

    private static SubjectTransitionService subjectTransitionService;

    private static TeacherTransitionService teacherTransitionService;


    private TransitionFacade() {
    }


    public static ConnectionTransitionService getConnectionTransitionService() {
        if (connectionTransitionService == null) {
            connectionTransitionService = new ConnectionTransitionService();
        }
        return connectionTransitionService;
    }

    public static TestTransitionService getTestTransitionService() {
        if (testTransitionService == null) {
            testTransitionService = new TestTransitionService();
        }
        return testTransitionService;
    }

    public static StudentTransitionService getStudentTransitionService() {
        if (studentTransitionService == null) {
            studentTransitionService = new StudentTransitionService();
        }
        return studentTransitionService;
    }

    public static GroupTransitionService getGroupTransitionService() {
        if (groupTransitionService == null) {
            groupTransitionService = new GroupTransitionService();
        }
        return groupTransitionService;
    }

    public static SubjectTransitionService getSubjectTransitionService() {
        if (subjectTransitionService == null) {
            subjectTransitionService = new SubjectTransitionService();
        }
        return subjectTransitionService;
    }

    public static TeacherTransitionService getTeacherTransitionService() {
        if (teacherTransitionService == null) {
            teacherTransitionService = new TeacherTransitionService();
        }
        return teacherTransitionService;
    }

}