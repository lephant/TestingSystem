package ru.lephant.java.rgatu.TestingSystem.entities;

import javafx.scene.control.ToggleGroup;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "single_choice_questions")
@PrimaryKeyJoinColumn(name = "id")
public class SingleChoiceQuestion extends Question implements Serializable {

    private List<Choice> choices = new ArrayList<>();

    private ToggleGroup toggleGroup;


    public SingleChoiceQuestion() {
    }

    public SingleChoiceQuestion(Test test, String text, int value) {
        super(test, text, value);
    }

    public SingleChoiceQuestion(Test test, String text, int value, Image image) {
        super(test, text, value, image);
    }


    @OrderBy("id")
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SingleChoiceQuestion that = (SingleChoiceQuestion) o;

        return choices != null ? choices.equals(that.choices) : that.choices == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (choices != null ? choices.hashCode() : 0);
        return result;
    }

    @Transient
    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }

    public void setToggleGroup(ToggleGroup toggleGroup) {
        this.toggleGroup = toggleGroup;
    }
}