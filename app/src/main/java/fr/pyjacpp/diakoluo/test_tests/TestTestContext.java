package fr.pyjacpp.diakoluo.test_tests;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;

public class TestTestContext {

    static int PROGRESS_BAR_PRECISION = 100;

    private int score = 0;
    private int maxScore;

    private final int numberQuestionToAsk;
    private final int numberColumnToShow;

    private ArrayList<DataRow> listRowToAsk = new ArrayList<>();

    private final Test test;

    private int currentIndex = 0;
    private boolean answerGive = false;

    private HashMap<Column, Object> userAnswer = new HashMap<>();
    private HashMap<Column, Boolean> showColumn = new HashMap<>();



    TestTestContext(Context context, int numberQuestionToAsk, int numberColumnToShow) {
        this.numberQuestionToAsk = numberQuestionToAsk;
        this.numberColumnToShow = numberColumnToShow;

        test = DiakoluoApplication.getCurrentTest(context);
        Random random = new Random();

        maxScore = numberQuestionToAsk * (test.getNumberColumn() - numberColumnToShow);

        ArrayList<DataRow> dataRowsToChoose = new ArrayList<>(test.getListRow());

        for (int i = 0; i < numberQuestionToAsk; i++) {
            int index = random.nextInt(dataRowsToChoose.size());
            listRowToAsk.add(dataRowsToChoose.get(index));
            dataRowsToChoose.remove(index);
        }

        for (Column column : test.getListColumn()) {
            userAnswer.put(column, null);
            showColumn.put(column, false);
        }
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    public int getProgressScore() {
        return score * PROGRESS_BAR_PRECISION;
    }

    public int getMaxProgressScore() {
        return maxScore * PROGRESS_BAR_PRECISION;
    }

    public void addScore(int score) {
        this.score += score;
    }

    ArrayList<DataRow> getListRowToAsk() {
        return listRowToAsk;
    }

    /*public int getNumberColumnToShow() {
        return numberColumnToShow;
    }

    public void setNumberColumnToShow(int numberColumnToShow) {
        this.numberColumnToShow = numberColumnToShow;
    }*/

    int getCurrentIndex() {
        return currentIndex;
    }

    boolean incrementCurrentIndex() {
        if (currentIndex < listRowToAsk.size() - 1) {
            currentIndex++;
            return true;
        } else {
            return false;
        }
    }

    boolean isAnswerGive() {
        return answerGive;
    }

    void setAnswerGive(boolean answerGive) {
        this.answerGive = answerGive;
    }

    HashMap<Column, Object> getUserAnswer() {
        return userAnswer;
    }

    public Test getTest() {
        return test;
    }

    HashMap<Column, Boolean> getShowColumn() {
        return showColumn;
    }

    void selectShowColumn() {
        ArrayList<Column> columnsToChoose = new ArrayList<>(test.getListColumn());
        Random random = new Random();

        for (int i = 0; i < numberColumnToShow; i++) {
            int index = random.nextInt(columnsToChoose.size());
            showColumn.put(columnsToChoose.get(index), true);
            columnsToChoose.remove(index);
        }

        for (Column column : columnsToChoose) {
            showColumn.put(column, false);
        }
    }

    DataRow getCurrentRow() {
        return listRowToAsk.get(currentIndex);
    }

    void removeUserInput() {
        userAnswer.clear();
    }

    void reset() {
        score = 0;
        maxScore = numberQuestionToAsk * (test.getNumberColumn() - numberColumnToShow);
        listRowToAsk = new ArrayList<>();
        currentIndex = 0;
        answerGive = false;

        userAnswer = new HashMap<>();
        showColumn = new HashMap<>();

        ArrayList<DataRow> dataRowsToChoose = new ArrayList<>(test.getListRow());

        Random random = new Random();
        for (int i = 0; i < numberQuestionToAsk; i++) {
            int index = random.nextInt(dataRowsToChoose.size());
            listRowToAsk.add(dataRowsToChoose.get(index));
            dataRowsToChoose.remove(index);
        }

        for (Column column : test.getListColumn()) {
            userAnswer.put(column, null);
            showColumn.put(column, false);
        }
    }
}
