package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;

@Entity
public class Images {
    private long id;
    private byte[] content;
    private Collection<Questions> questionsesById;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "content", nullable = false)
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Images images = (Images) o;

        if (id != images.id) return false;
        if (!Arrays.equals(content, images.content)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }

    @OneToMany(mappedBy = "imagesByImageId")
    public Collection<Questions> getQuestionsesById() {
        return questionsesById;
    }

    public void setQuestionsesById(Collection<Questions> questionsesById) {
        this.questionsesById = questionsesById;
    }
}
