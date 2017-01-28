package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Tests {
    private long id;
    private String name;
    private String subject;
    private String teacherFio;
    private byte randomOrder;
    private Collection<Questions> questionsesById;
    private Collection<StudentsTests> studentsTestsesById;

    @Id
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
    public byte getRandomOrder() {
        return randomOrder;
    }

    public void setRandomOrder(byte randomOrder) {
        this.randomOrder = randomOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tests tests = (Tests) o;

        if (id != tests.id) return false;
        if (randomOrder != tests.randomOrder) return false;
        if (name != null ? !name.equals(tests.name) : tests.name != null) return false;
        if (subject != null ? !subject.equals(tests.subject) : tests.subject != null) return false;
        if (teacherFio != null ? !teacherFio.equals(tests.teacherFio) : tests.teacherFio != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (teacherFio != null ? teacherFio.hashCode() : 0);
        result = 31 * result + (int) randomOrder;
        return result;
    }

    @OneToMany(mappedBy = "testsByTestId")
    public Collection<Questions> getQuestionsesById() {
        return questionsesById;
    }

    public void setQuestionsesById(Collection<Questions> questionsesById) {
        this.questionsesById = questionsesById;
    }

    @OneToMany(mappedBy = "testsByTestId")
    public Collection<StudentsTests> getStudentsTestsesById() {
        return studentsTestsesById;
    }

    public void setStudentsTestsesById(Collection<StudentsTests> studentsTestsesById) {
        this.studentsTestsesById = studentsTestsesById;
    }
}
