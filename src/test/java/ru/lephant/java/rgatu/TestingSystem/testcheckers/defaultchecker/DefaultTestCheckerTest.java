package ru.lephant.java.rgatu.TestingSystem.testcheckers.defaultchecker;

import javafx.util.Pair;
import org.junit.Before;
import ru.lephant.java.rgatu.TestingSystem.entities.*;
import ru.lephant.java.rgatu.TestingSystem.testcheckers.TestChecker;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DefaultTestCheckerTest {

    private TestChecker testChecker;

    @Before
    public void setUp() throws Exception {
        testChecker = new DefaultTestChecker();
    }

    private Test generateTest() {
        Test test = new Test();
        test.setName("Test's name");
        test.setSubject(new Subject("Subject's name"));
        test.setTeacher(new Teacher("Teacher's name"));
        test.setQuestions(new ArrayList<>());
        return test;
    }

    private List<Choice> generateChoices(Question question, boolean... isCorrect) {
        List<Choice> choices = new ArrayList<>(isCorrect.length);
        for (int i = 0; i < isCorrect.length; i++) {
            Choice choice = new Choice(question);
            choice.setText(String.format("Choice%d's text", i));
            choice.setCorrectIt(isCorrect[i]);
            choices.add(choice);
        }
        return choices;
    }

    private List<MissingPossibleAnswer> generatePossibleAnswers(Question question, String... possibleAnswers) {
        List<MissingPossibleAnswer> answers = new ArrayList<>(possibleAnswers.length);
        for (String possibleAnswer : possibleAnswers) {
            MissingPossibleAnswer answer = new MissingPossibleAnswer((MissingWordQuestion) question);
            answer.setText(possibleAnswer);
            answers.add(answer);
        }
        return answers;
    }

    private boolean isFullyCorrectTest(Test test) {
        Pair<Double, Integer> testResult = testChecker.check(test);
        return testResult.getKey() == 1.0 && testResult.getValue() == 5;
    }

    private boolean isTestCorrectBy(Test test, double result, int mark) {
        Pair<Double, Integer> testResult = testChecker.check(test);
        return testResult.getKey() == result && testResult.getValue() == mark;
    }

    private void clearAnswers(SingleChoiceQuestion question) {
        for (Choice choice : question.getChoices()) {
            choice.setMarked(false);
        }
    }

    private void clearAnswers(MultiChoiceQuestion question) {
        for (Choice choice : question.getChoices()) {
            choice.setMarked(false);
        }
    }

    private void clearAnswers(MissingWordQuestion question) {
        question.setAnswer("");
    }

    @org.junit.Test
    public void checkSingleChoiceQuestion() throws Exception {
        Test test = generateTest();

        SingleChoiceQuestion question = new SingleChoiceQuestion();
        question.setTest(test);
        question.setText("Question's text");
        question.setValue(1);
        test.getQuestions().add(question);

        question.setChoices(generateChoices(question, false, true, false));
        Choice choice1 = question.getChoices().get(0);
        Choice choice2 = question.getChoices().get(1);
        Choice choice3 = question.getChoices().get(2);

        clearAnswers(question);
        assertFalse(isFullyCorrectTest(test));

        clearAnswers(question);
        choice1.setMarked(true);
        assertFalse(isFullyCorrectTest(test));

        clearAnswers(question);
        choice2.setMarked(true);
        assertTrue(isFullyCorrectTest(test));

        clearAnswers(question);
        choice3.setMarked(true);
        assertFalse(isFullyCorrectTest(test));
    }

    @org.junit.Test
    public void checkMultiChoiceQuestion() throws Exception {
        Test test = generateTest();

        MultiChoiceQuestion question = new MultiChoiceQuestion();
        question.setTest(test);
        question.setText("Question's text");
        question.setValue(1);
        test.getQuestions().add(question);

        question.setChoices(generateChoices(question, false, true, true));
        Choice choice1 = question.getChoices().get(0);
        Choice choice2 = question.getChoices().get(1);
        Choice choice3 = question.getChoices().get(2);

        clearAnswers(question);
        assertFalse(isFullyCorrectTest(test));

        clearAnswers(question);
        choice3.setMarked(true);
        assertFalse(false);

        clearAnswers(question);
        choice2.setMarked(true);
        assertFalse(isFullyCorrectTest(test));

        clearAnswers(question);
        choice2.setMarked(true);
        choice3.setMarked(true);
        assertTrue(isFullyCorrectTest(test));

        clearAnswers(question);
        choice1.setMarked(true);
        assertFalse(isFullyCorrectTest(test));

        clearAnswers(question);
        choice1.setMarked(true);
        choice3.setMarked(true);
        assertFalse(isFullyCorrectTest(test));

        clearAnswers(question);
        choice1.setMarked(true);
        choice2.setMarked(true);
        assertFalse(isFullyCorrectTest(test));

        clearAnswers(question);
        choice1.setMarked(true);
        choice2.setMarked(true);
        choice3.setMarked(true);
        assertFalse(isFullyCorrectTest(test));
    }

    @org.junit.Test
    public void checkMissingWordQuestion() throws Exception {
        Test test = generateTest();

        MissingWordQuestion question = new MissingWordQuestion();
        question.setTest(test);
        question.setText("Question's text");
        question.setValue(1);
        test.getQuestions().add(question);

        question.setPossibleAnswers(generatePossibleAnswers(question, "Answer", "Question", "Test"));

        clearAnswers(question);
        assertFalse(isFullyCorrectTest(test));

        clearAnswers(question);
        question.setAnswer("Something");
        assertFalse(isFullyCorrectTest(test));

        clearAnswers(question);
        question.setAnswer("answer");
        assertTrue(isFullyCorrectTest(test));

        clearAnswers(question);
        question.setAnswer("Answer");
        assertTrue(isFullyCorrectTest(test));

        clearAnswers(question);
        question.setAnswer("Answernot");
        assertFalse(isFullyCorrectTest(test));

        clearAnswers(question);
        question.setAnswer("Almost answer");
        assertFalse(isFullyCorrectTest(test));

        clearAnswers(question);
        question.setAnswer("Almostanswer");
        assertFalse(isFullyCorrectTest(test));

        clearAnswers(question);
        question.setAnswer("Question");
        assertTrue(isFullyCorrectTest(test));

        clearAnswers(question);
        question.setAnswer("Test");
        assertTrue(isFullyCorrectTest(test));
    }

    //Метод проверяет работу параметра value у вопросов, чтобы оценка ставилась в соответствии с ними.
    @org.junit.Test
    public void checkWorkableOfFewQuestionsWithDifferentValue() throws Exception {
        Test test = generateTest();

        SingleChoiceQuestion question1 = new SingleChoiceQuestion();
        question1.setTest(test);
        question1.setText("Question1's text");
        question1.setValue(1);
        question1.setChoices(generateChoices(question1, true, false, false));
        test.getQuestions().add(question1);

        SingleChoiceQuestion question2 = new SingleChoiceQuestion();
        question2.setTest(test);
        question2.setText("Question2's text");
        question2.setValue(3);
        question2.setChoices(generateChoices(question1, true, false, false));
        test.getQuestions().add(question2);

        SingleChoiceQuestion question3 = new SingleChoiceQuestion();
        question3.setTest(test);
        question3.setText("Question3's text");
        question3.setValue(6);
        question3.setChoices(generateChoices(question1, true, false, false));
        test.getQuestions().add(question3);


        clearAnswers(question1);
        clearAnswers(question2);
        clearAnswers(question3);
        assertTrue(isTestCorrectBy(test, 0d, 2));

        clearAnswers(question1);
        clearAnswers(question2);
        clearAnswers(question3);
        question1.getChoices().get(0).setMarked(true);
        assertTrue(isTestCorrectBy(test, 0.1, 2));

        clearAnswers(question1);
        clearAnswers(question2);
        clearAnswers(question3);
        question2.getChoices().get(0).setMarked(true);
        assertTrue(isTestCorrectBy(test, 0.3, 2));

        clearAnswers(question1);
        clearAnswers(question2);
        clearAnswers(question3);
        question3.getChoices().get(0).setMarked(true);
        assertTrue(isTestCorrectBy(test, 0.6, 3));

        clearAnswers(question1);
        clearAnswers(question2);
        clearAnswers(question3);
        question1.getChoices().get(0).setMarked(true);
        question2.getChoices().get(0).setMarked(true);
        assertTrue(isTestCorrectBy(test, 0.4, 2));

        clearAnswers(question1);
        clearAnswers(question2);
        clearAnswers(question3);
        question1.getChoices().get(0).setMarked(true);
        question3.getChoices().get(0).setMarked(true);
        assertTrue(isTestCorrectBy(test, 0.7, 3));

        clearAnswers(question1);
        clearAnswers(question2);
        clearAnswers(question3);
        question2.getChoices().get(0).setMarked(true);
        question3.getChoices().get(0).setMarked(true);
        assertTrue(isTestCorrectBy(test, 0.9, 4));

        clearAnswers(question1);
        clearAnswers(question2);
        clearAnswers(question3);
        question1.getChoices().get(0).setMarked(true);
        question2.getChoices().get(0).setMarked(true);
        question3.getChoices().get(0).setMarked(true);
        assertTrue(isTestCorrectBy(test, 1d, 5));
    }
}