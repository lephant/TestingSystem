package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "questions")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Question implements Serializable {

    private long id;
    private Test test;
    private String text;
    private Image image;
    private int value;


    public Question() {
    }

    public Question(Test test, String text, int value) {
        this.test = test;
        this.text = text;
        this.value = value;
    }

    public Question(Test test, String text, int value, Image image) {
        this.test = test;
        this.text = text;
        this.image = image;
        this.value = value;
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

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    @Basic
    @Column(name = "text", nullable = false, length = 1024)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Basic
    @Column(name = "value", nullable = false)
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (id != question.id) return false;
        if (value != question.value) return false;
        if (!test.equals(question.test)) return false;
        if (!text.equals(question.text)) return false;
        return image != null ? image.equals(question.image) : question.image == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + test.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + value;
        return result;
    }
}