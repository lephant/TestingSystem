package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;

@Entity
@Table(name = "teachers")
public class Teacher {
    private long id;
    private String fio;


    public Teacher() {
    }

    public Teacher(String fio) {
        this.fio = fio;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Teacher teacher = (Teacher) o;

        if (id != teacher.id) return false;
        if (fio != null ? !fio.equals(teacher.fio) : teacher.fio != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (fio != null ? fio.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return fio;
    }
}