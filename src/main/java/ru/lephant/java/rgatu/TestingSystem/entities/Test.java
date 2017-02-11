package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tests")
public class Test implements Serializable {

    private long id;
    private String name;
    private Subject subject;
    private Teacher teacher;
    private boolean randomOrder;
    private List<Question> questions;
    private List<TestOfStudent> testsOfStudents;


    public Test() {
    }

    public Test(String name, Subject subject, Teacher teacher, boolean randomOrder) {
        this.name = name;
        this.subject = subject;
        this.teacher = teacher;
        this.randomOrder = randomOrder;
    }


    public double calculateResult() {
        int receivedScores = 0;
        int totalScores = 0;

        for (Question question : questions) {
            totalScores += question.getValue();
            if (question.checkAnswer()) {
                receivedScores += question.getValue();
            }
        }

        return (double) receivedScores / (double) totalScores;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 128)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Basic
    @Column(name = "random_order", nullable = false)
    public boolean isRandomOrder() {
        return randomOrder;
    }

    public void setRandomOrder(boolean randomOrder) {
        this.randomOrder = randomOrder;
    }

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy("id")
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    public List<TestOfStudent> getTestsOfStudents() {
        return testsOfStudents;
    }

    public void setTestsOfStudents(List<TestOfStudent> testsOfStudents) {
        this.testsOfStudents = testsOfStudents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Test test = (Test) o;

        if (id != test.id) return false;
        if (randomOrder != test.randomOrder) return false;
        if (!name.equals(test.name)) return false;
        if (!subject.equals(test.subject)) return false;
        if (!teacher.equals(test.teacher)) return false;
        return questions != null ? questions.equals(test.questions) : test.questions == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + subject.hashCode();
        result = 31 * result + teacher.hashCode();
        result = 31 * result + (randomOrder ? 1 : 0);
        result = 31 * result + (questions != null ? questions.hashCode() : 0);
        return result;
    }
}