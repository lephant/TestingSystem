package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "students")
public class Student implements Serializable {

    private long id;
    private String fio;
    private Group group;
    private List<TestOfStudent> testOfStudentList;


    public Student() {
    }

    public Student(String fio) {
        this.fio = fio;
    }

    public Student(String fio, Group group) {
        this.fio = fio;
        this.group = group;
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
    @Column(name = "fio", nullable = false, length = 255)
    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", orphanRemoval = true, cascade = CascadeType.ALL)
    public List<TestOfStudent> getTestOfStudentList() {
        return testOfStudentList;
    }

    public void setTestOfStudentList(List<TestOfStudent> testOfStudentList) {
        this.testOfStudentList = testOfStudentList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != student.id) return false;
        if (!fio.equals(student.fio)) return false;
        return group.equals(student.group);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + fio.hashCode();
        result = 31 * result + group.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return fio + " (" + group + ")";
    }
}