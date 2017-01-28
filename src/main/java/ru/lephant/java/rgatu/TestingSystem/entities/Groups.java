package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Groups {
    private long id;
    private String name;
    private Collection<Students> studentsesById;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Groups groups = (Groups) o;

        if (id != groups.id) return false;
        if (name != null ? !name.equals(groups.name) : groups.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "groupsByGroupId")
    public Collection<Students> getStudentsesById() {
        return studentsesById;
    }

    public void setStudentsesById(Collection<Students> studentsesById) {
        this.studentsesById = studentsesById;
    }
}
