package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Students {
    private long id;
    private String fio;
    private long groupId;
    private Groups groupsByGroupId;
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
    @Column(name = "fio", nullable = false, length = 255)
    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    @Basic
    @Column(name = "group_id", nullable = false)
    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Students students = (Students) o;

        if (id != students.id) return false;
        if (groupId != students.groupId) return false;
        if (fio != null ? !fio.equals(students.fio) : students.fio != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (fio != null ? fio.hashCode() : 0);
        result = 31 * result + (int) (groupId ^ (groupId >>> 32));
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    public Groups getGroupsByGroupId() {
        return groupsByGroupId;
    }

    public void setGroupsByGroupId(Groups groupsByGroupId) {
        this.groupsByGroupId = groupsByGroupId;
    }

    @OneToMany(mappedBy = "studentsByStudentId")
    public Collection<StudentsTests> getStudentsTestsesById() {
        return studentsTestsesById;
    }

    public void setStudentsTestsesById(Collection<StudentsTests> studentsTestsesById) {
        this.studentsTestsesById = studentsTestsesById;
    }
}
