package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;

@Entity
@Table(name = "questions")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Question implements Serializable {

    private long id;
    private Test test;
    private String text;
    private byte[] image;
    private int value;


    public Question() {
        value = 1;
    }

    public Question(Test test, String text, int value) {
        this.test = test;
        this.text = text;
        this.value = value;
    }

    public Question(Test test, String text, int value, byte[] image) {
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

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "image")
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
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
        if (test != null ? !test.equals(question.test) : question.test != null) return false;
        if (text != null ? !text.equals(question.text) : question.text != null) return false;
        return Arrays.equals(image, question.image);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (test != null ? test.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(image);
        result = 31 * result + value;
        return result;
    }
}