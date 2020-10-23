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

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import fr.pyjacpp.diakoluo.Utils;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.column.Column;


/**
 * A Context that hold a test of a {@link Test}.
 */
public class TestTestContext {

    public static final int PROGRESS_BAR_PRECISION = 100;
    private static final String PREF_PROPORTIONALITY_SCORE_METHOD = "proportionalityScoreMethod";
    private static final String PREF_NUMBER_QUESTION_ASK = "numberQuestionToAsk";
    private static final String PREF_NUMBER_COLUMN_SHOW = "columnShow";
    private static final String PREF_SHOW_COLUMN = "showColumn";
    private static final String PREF_SCORE = "score";
    private static final String PREF_MAX_SCORE = "maxScore";
    private static final String PREF_COLUMN_SCORE_SUM = "columnScoreSum";
    private static final String PREF_COLUMN_SELECTED_SCORE_SUM = "columnSelectedScoreSum";
    private static final String PREF_CURRENT_INDEX = "currentIndex";
    private static final String PREF_ANSWER_GIVE = "answerGive";
    private static final String PREF_LIST_ROW_TO_ASK = "listRowToAsk";
    private final boolean proportionalityScoreMethod;
    private final int numberQuestionToAsk;
    @NonNull
    private final Test test;
    private final HashMap<Column, Object> userAnswer = new HashMap<>();
    private final HashMap<Column, Boolean> showColumn = new HashMap<>();
    private final ArrayList<Column> columnsAskRandom = new ArrayList<>();
    private final ArrayList<Column> columnsAsk = new ArrayList<>();

    private float score;
    private float maxScore;
    private int numberColumnToShowRandom;

    private float columnScoreSum = 0;
    private float columnSelectedScoreSum = 0;
    private ArrayList<DataRow> listRowToAsk;

    private int currentIndex;
    private boolean answerGive;

    /**
     * Create a test.
     *
     * @param test                       the test to test.
     * @param numberQuestionToAsk        the number of questions to ask
     * @param numberColumnToShow         the number of column to show
     * @param proportionalityScoreMethod the score method to use
     */
    public TestTestContext(@NonNull Test test, int numberQuestionToAsk, int numberColumnToShow,
                           boolean proportionalityScoreMethod) {
        this.test = test;
        this.numberQuestionToAsk = numberQuestionToAsk;
        this.proportionalityScoreMethod = proportionalityScoreMethod;
        initialize(numberColumnToShow);
    }

    /**
     * Create a test from a bundle. The bundle must contain a TestTestContext.
     * Use {@link #bundleContainTestTestContext(Bundle)} to verify that.
     *
     * @param test the test to test
     * @param b    the bundle that contain the TestTest context
     * @see #bundleContainTestTestContext(Bundle)
     */
    public TestTestContext(@NonNull Test test, @NonNull Bundle b) {
        this.test = test;

        numberQuestionToAsk = b.getInt(PREF_NUMBER_QUESTION_ASK);
        proportionalityScoreMethod = b.getBoolean(PREF_PROPORTIONALITY_SCORE_METHOD);

        initialize(b.getInt(PREF_NUMBER_COLUMN_SHOW));

        boolean[] showColumn = b.getBooleanArray(PREF_SHOW_COLUMN);

        if (showColumn == null || showColumn.length != test.getListColumn().size())
            throw new RuntimeException(showColumn == null ? "Show column is null !" : "Columns doesn't have the same size");

        ArrayList<Column> listColumn = test.getListColumn();
        for (int i = 0, listColumnSize = listColumn.size(); i < listColumnSize; i++) {
            Column c = listColumn.get(i);
            this.showColumn.put(c, showColumn[i]);
        }

        score = b.getFloat(PREF_SCORE);
        maxScore = b.getFloat(PREF_MAX_SCORE);
        columnScoreSum = b.getFloat(PREF_COLUMN_SCORE_SUM);
        columnSelectedScoreSum = b.getFloat(PREF_COLUMN_SELECTED_SCORE_SUM);
        currentIndex = b.getInt(PREF_CURRENT_INDEX);
        answerGive = b.getBoolean(PREF_ANSWER_GIVE);


        int[] positions = b.getIntArray(PREF_LIST_ROW_TO_ASK);
        ArrayList<DataRow> listDataRow = test.getListRow();

        if (positions == null) {
            throw new RuntimeException("PREF_LIST_ROW_TO_ASK don't exist");
        }

        listRowToAsk.clear();
        for (int pos : positions) {
            listRowToAsk.add(listDataRow.get(pos));
        }
    }

    /**
     * Initialize fields.
     *
     * @param numberColumnToShow the number of columns to show
     */
    private void initialize(int numberColumnToShow) {
        for (Column c : test.getListColumn()) {
            boolean canHide = c.isInSettings(Column.SET_CAN_BE_HIDE);
            boolean canShow = c.isInSettings(Column.SET_CAN_BE_SHOW);

            float score = c.getScoreRight();
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

    /**
     * Get the bundle that save this TestTestContext.
     *
     * @return a bundle that hold this TestTestContext
     */
    @NonNull
    public Bundle getBundle() {
        Bundle b = new Bundle();
        b.putBoolean(PREF_PROPORTIONALITY_SCORE_METHOD, proportionalityScoreMethod);
        b.putInt(PREF_NUMBER_QUESTION_ASK, numberQuestionToAsk);

        ArrayList<Column> listColumn = test.getListColumn();
        boolean[] showColumn = new boolean[listColumn.size()];

        int numberColumnToShow = numberColumnToShowRandom;

        for (int i = 0, size = listColumn.size(); i < size; i++) {
            Column c = listColumn.get(i);
            if (c.isInSettings(Column.SET_CAN_BE_SHOW) && !c.isInSettings(Column.SET_CAN_BE_HIDE)) {
                numberColumnToShow++;
            }
            Boolean bool = this.showColumn.get(c);
            showColumn[i] = bool != null && bool;
        }

        int[] dataRowPositions = new int[listRowToAsk.size()];
        ArrayList<DataRow> listRow = test.getListRow();
        for (int i = 0, listRowSize = listRow.size(); i < listRowSize; i++) {
            DataRow target = listRow.get(i);

            // test if listRowToAsk contain target and save the pos in the correct order
            for (int j = 0, listRowToAskSize = listRowToAsk.size(); j < listRowToAskSize; j++) {
                DataRow r = listRowToAsk.get(j);
                if (target.equals(r)) {
                    dataRowPositions[j] = i;
                    break;
                }
            }
        }
        b.putIntArray(PREF_LIST_ROW_TO_ASK, dataRowPositions);
        b.putBooleanArray(PREF_SHOW_COLUMN, showColumn);
        b.putInt(PREF_NUMBER_COLUMN_SHOW, numberColumnToShow);
        b.putFloat(PREF_SCORE, score);
        b.putFloat(PREF_MAX_SCORE, maxScore);
        b.putFloat(PREF_COLUMN_SCORE_SUM, columnScoreSum);
        b.putFloat(PREF_COLUMN_SELECTED_SCORE_SUM, columnSelectedScoreSum);
        b.putInt(PREF_CURRENT_INDEX, currentIndex);
        b.putBoolean(PREF_ANSWER_GIVE, answerGive);
        return b;
    }

    /**
     * Select randoms columns
     */
    private void selectShowColumn() {
        ArrayList<Column> columnsToChoose = new ArrayList<>(columnsAskRandom);
        Random random = new Random();

        for (int i = 0; i < numberColumnToShowRandom; i++) {
            int index = random.nextInt(columnsToChoose.size());
            Column column = columnsToChoose.get(index);
            Boolean previous = showColumn.get(column);
            if (previous == null || !previous) columnSelectedScoreSum -= column.getScoreRight();
            showColumn.put(column, true);
            columnsToChoose.remove(index);
        }

        for (Column column : columnsToChoose) {
            Boolean previous = showColumn.get(column);
            if (previous != null && previous) columnSelectedScoreSum += column.getScoreRight();
            showColumn.put(column, false);
        }
    }

    /**
     * Reset all variables. Useful to restart the test with the same parameters.
     */
    public void reset() {
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
            if (previous != null && previous) columnSelectedScoreSum += column.getScoreRight();
            showColumn.put(column, false);
        }
        selectShowColumn();
    }

    /**
     * Get the maximum score obtainable.
     *
     * @return the maximum score
     * @see #getMaxProgressScore()
     */
    public float getMaxScore() {
        return maxScore;
    }

    /**
     * Get the score of the user.
     *
     * @return the score of the user
     * @see #getProgressScore()
     */
    public float getScore() {
        return score;
    }

    /**
     * Get the score of the user to be used in a progress bar (with some more precision for it).
     *
     * @return the score for a progress bar
     * @see #PROGRESS_BAR_PRECISION
     * @see #getScore()
     */
    public int getProgressScore() {
        return (int) (score * PROGRESS_BAR_PRECISION);
    }

    /**
     * Get the maximum score obtainable to be used in a progress bar (with some more precision for
     * it).
     *
     * @return the maximum score for a progress bar
     * @see #PROGRESS_BAR_PRECISION
     * @see #getMaxScore()
     */
    public int getMaxProgressScore() {
        return (int) (maxScore * PROGRESS_BAR_PRECISION);
    }

    /**
     * Add to score points from a column.
     *
     * @param score    the score obtain by the user
     * @param maxScore the maximum score that the user could have obtain
     */
    public void addScore(float score, float maxScore) {
        if (columnSelectedScoreSum > 0) {
            if (proportionalityScoreMethod) {
                this.score += score * columnScoreSum / columnSelectedScoreSum;
                this.maxScore += maxScore * columnScoreSum / columnSelectedScoreSum;
            } else {
                this.score += score;
                this.maxScore += maxScore;
            }
        }
    }

    /*public int getNumberColumnToShow() {
        return numberColumnToShow;
    }

    public void setNumberColumnToShow(int numberColumnToShow) {
        this.numberColumnToShow = numberColumnToShow;
    }*/

    /**
     * Get the current index of the test (the n° of the question).
     *
     * @return the current index
     * @see #incrementCurrentIndex()
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Increment by one the currentIndex. Pass to the next question. And select column randomly and
     * remove the user input.
     *
     * @return if the current index could be increment (if we are already to the last question)
     * @see #getCurrentIndex()
     * @see #selectShowColumn()
     * @see #removeUserInput()
     */
    public boolean incrementCurrentIndex() {
        if (currentIndex < listRowToAsk.size() - 1) {
            currentIndex++;
            selectShowColumn();
            removeUserInput();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get if te the answer is given to the user.
     *
     * @return if the answer is given to the user
     */
    public boolean isAnswerGive() {
        return answerGive;
    }

    /**
     * Set if the answer is given.
     *
     * @param answerGive if the answer is given
     */
    public void setAnswerGive(boolean answerGive) {
        this.answerGive = answerGive;
    }

    /**
     * Get what the user has inputted
     *
     * @return data that the user has inputted.
     */
    public HashMap<Column, Object> getUserAnswer() {
        return userAnswer;
    }

    /**
     * Get the test that represent this test.
     *
     * @return the test attached to this test
     */
    @NonNull
    public Test getTest() {
        return test;
    }

    /**
     * Get what columns are showed
     *
     * @return what columns are showed (or not)
     */
    public HashMap<Column, Boolean> getShowColumn() {
        return showColumn;
    }

    /**
     * Get the current progress of the test. Like {@link #getCurrentIndex()} but for progress bar.
     *
     * @return the progress of the test for progress bars
     * @see #getCurrentIndex()
     * @see #PROGRESS_BAR_PRECISION
     */
    public int getProgress() {
        return currentIndex * PROGRESS_BAR_PRECISION;
    }

    /**
     * Get the current max progress of the test.
     *
     * @return the progress max of the test for progress bars
     * @see #PROGRESS_BAR_PRECISION
     */
    public int getMaxProgress() {
        return numberQuestionToAsk * PROGRESS_BAR_PRECISION;
    }

    /**
     * Get the row that is currently showed.
     *
     * @return the current row
     */
    public DataRow getCurrentRow() {
        return listRowToAsk.get(currentIndex);
    }

    /**
     * Remove all the data that the user has inputted (for example if we change of question).
     */
    private void removeUserInput() {
        userAnswer.clear();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof TestTestContext) {
            TestTestContext c = ((TestTestContext) obj);
            return score == c.score &&
                    maxScore == c.maxScore &&
                    proportionalityScoreMethod == c.proportionalityScoreMethod &&
                    numberQuestionToAsk == c.numberQuestionToAsk &&
                    test.equals(c.test) &&
//                    userAnswer.equals(c.userAnswer) && The userAnswer isn't save now (22 october 2020)
                    showColumn.equals(c.showColumn) &&
                    columnsAskRandom.equals(c.columnsAskRandom) &&
                    columnsAsk.equals(c.columnsAsk) &&
                    numberColumnToShowRandom == c.numberColumnToShowRandom &&
                    columnScoreSum == c.columnScoreSum &&
                    columnSelectedScoreSum == c.columnSelectedScoreSum &&
                    Utils.dataRowArrayEquals(listRowToAsk, test.getListColumn(), c.listRowToAsk,
                            test.getListColumn()) &&
                    currentIndex == c.currentIndex &&
                    answerGive == c.answerGive;
        } else {
            return false;
        }
    }
}
