package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Questions {
    private long id;
    private long testId;
    private String text;
    private Long imageId;
    private int value;
    private Collection<Choices> choicesById;
    private MissingWordQuestions missingWordQuestionsById;
    private MultiChoiceQuestions multiChoiceQuestionsById;
    private Tests testsByTestId;
    private Images imagesByImageId;
    private SingleChoiceQuestions singleChoiceQuestionsById;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "test_id", nullable = false)
    public long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    @Basic
    @Column(name = "text", nullable = false, length = 1024)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "image_id", nullable = true)
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
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

        Questions questions = (Questions) o;

        if (id != questions.id) return false;
        if (testId != questions.testId) return false;
        if (value != questions.value) return false;
        if (text != null ? !text.equals(questions.text) : questions.text != null) return false;
        if (imageId != null ? !imageId.equals(questions.imageId) : questions.imageId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (testId ^ (testId >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (imageId != null ? imageId.hashCode() : 0);
        result = 31 * result + value;
        return result;
    }

    @OneToMany(mappedBy = "questionsByChoiceQuestionId")
    public Collection<Choices> getChoicesById() {
        return choicesById;
    }

    public void setChoicesById(Collection<Choices> choicesById) {
        this.choicesById = choicesById;
    }

    @OneToOne(mappedBy = "questionsById")
    public MissingWordQuestions getMissingWordQuestionsById() {
        return missingWordQuestionsById;
    }

    public void setMissingWordQuestionsById(MissingWordQuestions missingWordQuestionsById) {
        this.missingWordQuestionsById = missingWordQuestionsById;
    }

    @OneToOne(mappedBy = "questionsById")
    public MultiChoiceQuestions getMultiChoiceQuestionsById() {
        return multiChoiceQuestionsById;
    }

    public void setMultiChoiceQuestionsById(MultiChoiceQuestions multiChoiceQuestionsById) {
        this.multiChoiceQuestionsById = multiChoiceQuestionsById;
    }

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id", nullable = false)
    public Tests getTestsByTestId() {
        return testsByTestId;
    }

    public void setTestsByTestId(Tests testsByTestId) {
        this.testsByTestId = testsByTestId;
    }

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    public Images getImagesByImageId() {
        return imagesByImageId;
    }

    public void setImagesByImageId(Images imagesByImageId) {
        this.imagesByImageId = imagesByImageId;
    }

    @OneToOne(mappedBy = "questionsById")
    public SingleChoiceQuestions getSingleChoiceQuestionsById() {
        return singleChoiceQuestionsById;
    }

    public void setSingleChoiceQuestionsById(SingleChoiceQuestions singleChoiceQuestionsById) {
        this.singleChoiceQuestionsById = singleChoiceQuestionsById;
    }
}
