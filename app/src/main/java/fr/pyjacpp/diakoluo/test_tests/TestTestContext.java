/*
 * Copyright (c) 2020 LOUBIERE Antonin <https://www.github.com/AntoninLoubiere/>
 *
 * This file is part of Diakôluô project <https://www.github.com/AntoninLoubiere/Diakoluo/>.
 *
 *     Diakôluô is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Diakôluô is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     A copy of the license is available in the root folder of Diakôluô, under the
 *     name of LICENSE.md. You could find it also at <https://www.gnu.org/licenses/gpl-3.0.html>.
 */

package fr.pyjacpp.diakoluo.test_tests;

import android.content.Context;

import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.column.Column;

public class TestTestContext {

    static final int PROGRESS_BAR_PRECISION = 100;

    private float score;
    private float maxScore;
    private final boolean proportionalityScoreMethod;

    private final int numberQuestionToAsk;
    private int numberColumnToShowRandom;
    private float columnScoreSum = 0;
    private float columnSelectedScoreSum = 0;

    private ArrayList<DataRow> listRowToAsk;

    private final Test test;

    private int currentIndex;
    private boolean answerGive;

    private final HashMap<Column, Object> userAnswer = new HashMap<>();
    private final HashMap<Column, Boolean> showColumn = new HashMap<>();

    private final ArrayList<Column> columnsAskRandom = new ArrayList<>();
    private final ArrayList<Column> columnsAsk = new ArrayList<>();

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    protected TestTestContext(Test test, int numberQuestionToAsk, int numberColumnToShow,
                              boolean proportionalityScoreMethod) {
        this.test = test;
        this.numberQuestionToAsk = numberQuestionToAsk;
        this.proportionalityScoreMethod = proportionalityScoreMethod;
        initialize(numberColumnToShow);
    }

    TestTestContext(Context context, int numberQuestionToAsk, int numberColumnToShow,
                    boolean proportionalityScoreMethod) {
        this.numberQuestionToAsk = numberQuestionToAsk;

        test = DiakoluoApplication.getCurrentTest(context);
        this.proportionalityScoreMethod = proportionalityScoreMethod;
        initialize(numberColumnToShow);
    }

    private void initialize(int numberColumnToShow) {
        for (Column c : test.getListColumn()) {
            boolean canHide = c.isInSettings(Column.SET_CAN_BE_HIDE);
            boolean canShow = c.isInSettings(Column.SET_CAN_BE_SHOW);

            int score = c.getScore();
            if (canHide && canShow) {
                columnsAskRandom.add(c);
                columnsAsk.add(c);
                columnSelectedScoreSum += score; // null showColumn is considered false
                columnScoreSum += score;
            } else if (canHide) {
                columnsAsk.add(c);
                showColumn.put(c, false);
                columnSelectedScoreSum += score;
                columnScoreSum += score;
            } else if (canShow) {
                showColumn.put(c, true);
                numberColumnToShow--;
            }
        }
        numberColumnToShowRandom = numberColumnToShow;
        reset();
    }

    public float getMaxScore() {
        return maxScore;
    }

    public float getScore() {
        return score;
    }

    int getProgressScore() {
        return (int) (score * PROGRESS_BAR_PRECISION);
    }

    int getMaxProgressScore() {
        return (int) (maxScore * PROGRESS_BAR_PRECISION);
    }

    public void addScore(int score, int maxScore) {
        if (proportionalityScoreMethod) {
            this.score += score * columnScoreSum / columnSelectedScoreSum;
            this.maxScore += maxScore * columnScoreSum / columnSelectedScoreSum;
        } else {
            this.score += score;
            this.maxScore += maxScore;
        }
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
        ArrayList<Column> columnsToChoose = new ArrayList<>(columnsAskRandom);
        Random random = new Random();

        for (int i = 0; i < numberColumnToShowRandom; i++) {
            int index = random.nextInt(columnsToChoose.size());
            Column column = columnsToChoose.get(index);
            Boolean previous = showColumn.get(column);
            if (previous == null || !previous) columnSelectedScoreSum -= column.getScore();
            showColumn.put(column, true);
            columnsToChoose.remove(index);
        }

        for (Column column : columnsToChoose) {
            Boolean previous = showColumn.get(column);
            if (previous != null && previous) columnSelectedScoreSum += column.getScore();
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
        maxScore = 0;
        listRowToAsk = new ArrayList<>();
        currentIndex = 0;
        answerGive = false;

        ArrayList<DataRow> dataRowsToChoose = new ArrayList<>(test.getListRow());

        Random random = new Random();
        for (int i = 0; i < numberQuestionToAsk; i++) {
            int index = random.nextInt(dataRowsToChoose.size());
            listRowToAsk.add(dataRowsToChoose.get(index));
            dataRowsToChoose.remove(index);
        }

        for (Column column : columnsAsk) {
            userAnswer.put(column, null);
            Boolean previous = showColumn.get(column);
            if (previous != null && previous) columnSelectedScoreSum += column.getScore();
            showColumn.put(column, false);
        }
        selectShowColumn();
    }

    int getProgress() {
        return currentIndex * PROGRESS_BAR_PRECISION;
    }

    int getMaxProgress() {
        return numberQuestionToAsk * PROGRESS_BAR_PRECISION;
    }
}
