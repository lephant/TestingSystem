package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tests")
public class Test implements Serializable {

    private long id;
    private String name;
    private String subject;
    private String teacherFio;
    private boolean randomOrder;
    private List<Question> questions;


    public Test() {
    }

    public Test(String name, String subject, String teacherFio, boolean randomOrder) {
        this.name = name;
        this.subject = subject;
        this.teacherFio = teacherFio;
        this.randomOrder = randomOrder;
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

    @Basic
    @Column(name = "subject", nullable = false, length = 128)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Basic
    @Column(name = "teacher_fio", nullable = false, length = 255)
    public String getTeacherFio() {
        return teacherFio;
    }

    public void setTeacherFio(String teacherFio) {
        this.teacherFio = teacherFio;
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
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
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
        if (!teacherFio.equals(test.teacherFio)) return false;
        return questions != null ? questions.equals(test.questions) : test.questions == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + subject.hashCode();
        result = 31 * result + teacherFio.hashCode();
        result = 31 * result + (randomOrder ? 1 : 0);
        result = 31 * result + (questions != null ? questions.hashCode() : 0);
        return result;
    }
}