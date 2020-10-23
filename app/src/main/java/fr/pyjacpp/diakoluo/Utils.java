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

package fr.pyjacpp.diakoluo;

import android.content.Context;
import android.content.DialogInterface;
import android.view.animation.Interpolator;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;

import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.column.Column;

/**
 * A utils class that hold useful methods.
 */
public final class Utils {

    public static final DecimalFormat SCORE_DECIMAL_FORMATTER = new DecimalFormat("+0.##;-0.##");

    /**
     * Remove useless spaces around a string
     *
     * @param s the string to process
     * @return the string clean up
     */
    public static String removeUselessSpaces(String s) {
        final int length = s.length();
        int realStart = 0;
        int realEnd = length - 1;

        while (realStart < length &&
                (s.charAt(realStart) == ' ' ||
                        s.charAt(realStart) == '\n' || s.charAt(realStart) == '\t' || s.charAt(realStart) == '\r')) {
            realStart++;
        }

        if (realStart >= length) {
            return "";
        }

        while (s.charAt(realEnd) == ' ' || s.charAt(realEnd) == '\n' || s.charAt(realEnd) == '\t' ||
                s.charAt(realEnd) == '\r') {
            realEnd--;
        }

        return s.substring(realStart, realEnd + 1);
    }

    /**
     * Map a var in interval [inMin; inMax] to [outMin; outMax]. (If the var doesn't respect the
     * interval, the out will not respect the interval too but proportions will be respect.
     * From Arduino map code (appendix:
     * https://www.arduino.cc/reference/en/language/functions/math/map/#_example_code).
     *
     * @param var    the var to process
     * @param inMin  the min of the var
     * @param inMax  the max of the var
     * @param outMin the min of the return
     * @param outMax the max of the return
     * @return the mapped var
     */
    public static int map(int var, int inMin, int inMax, int outMin, int outMax) {
        return (var - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    /**
     * Remove useless space at the en of a line
     *
     * @param stringBuilder the string builder of the string to process
     * @return the string processed
     */
    public static String removeUselessSpacesEnd(StringBuilder stringBuilder) {
        while (stringBuilder.length() > 0) {
            char c = stringBuilder.charAt(stringBuilder.length() - 1);
            if (c == ' ' || c == '\n' || c == '\t' || c == '\r') {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            } else {
                break;
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Copy a stream into another stream
     *
     * @param inputStream  the input stream to copy
     * @param outputStream the output stream where to copy
     * @throws IOException if while writing or reading a stream an exception occur.
     */
    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[2048];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
    }

    public static String formatScore(float score) {
        return SCORE_DECIMAL_FORMATTER.format(score);
    }

    /**
     * Return if two lists of {@link fr.pyjacpp.diakoluo.tests.DataRow} are equals
     *
     * @param first         the first list of DataRow to test
     * @param firstColumns  list of columns of first
     * @param second        the second list of DataRow to test
     * @param secondColumns list of columns of second columns
     * @return if the two lists of {@link fr.pyjacpp.diakoluo.tests.DataRow} are equals
     */
    public static boolean dataRowArrayEquals(ArrayList<DataRow> first, ArrayList<Column> firstColumns,
                                             ArrayList<DataRow> second, ArrayList<Column> secondColumns) {
        if (first.size() != second.size()) {
            return false;
        }

        for (int i = 0; i < first.size(); i++) {
            DataRow df = first.get(i);
            DataRow ds = second.get(i);
            if (!df.equals(ds, secondColumns, firstColumns)) {
                return false;
            }
        }
        return true;
    }

    /**
     * A extreme decelerator that start rapid and slow at the end (quadratic function upside-down).
     */
    public static class ExtremeDeceleratorInterpolator implements Interpolator {

        /**
         * The interpolation
         *
         * @param v thr value to map
         * @return the mapped value
         */
        @Override
        public float getInterpolation(float v) {
            float v1 = v - 1;
            return -(v1 * v1 * v1 * v1) + 1;
        }
    }

    /**
     * A validator of edit that hold if there is an error or a warning, and the error message.
     */
    public static class EditValidator {
        private final boolean error;
        private final boolean warning;
        private final Integer errorMessageResourceId;

        /**
         * Default constructor that create a no-error, no-warning validator.
         */
        public EditValidator() {
            errorMessageResourceId = null;
            warning = false;
            error = false;
        }

        /**
         * Constructor that create an error validator.
         *
         * @param errorMessageResourceId the resource id of the error message
         */
        public EditValidator(Integer errorMessageResourceId) {
            this.errorMessageResourceId = errorMessageResourceId;
            warning = false;
            error = true;
        }

        /**
         * Constructor that create an error or warning validator.
         *
         * @param errorMessageResourceId the resource id of the message
         * @param warning                if it is a warning and not an error
         */
        public EditValidator(Integer errorMessageResourceId, boolean warning) {
            this.errorMessageResourceId = errorMessageResourceId;
            this.warning = warning;
            error = true;
        }

        /**
         * Get the error message resource id.
         *
         * @return the error message resource id
         */
        public Integer getErrorMessageResourceId() {
            return errorMessageResourceId;
        }

        /**
         * Get if the validator contain an error (or a warning !).
         *
         * @return if the validator contain an error or a warning
         */
        public boolean isError() {
            return error;
        }

        /**
         * If the validator contain a warning
         *
         * @return if the validator contain a warning
         */
        public boolean isWarning() {
            return warning;
        }
    }

    /**
     * Verify and ask validators.
     */
    public static class VerifyAndAsk {

        private final Context context;
        private final Runnable success;
        private boolean errorInDeque;
        private ArrayDeque<EditValidator> errorValidatorDeque;

        /**
         * Default constructor
         *
         * @param context the context of the application or the activity
         * @param success the code to execute in case of success
         */
        public VerifyAndAsk(Context context, Runnable success) {
            this.context = context;
            this.success = success;
            this.errorInDeque = false;
        }

        /**
         * Run validation on the list of validator.
         *
         * @param errorValidatorDeque the list of validator to test
         */
        public void run(ArrayDeque<EditValidator> errorValidatorDeque) {
            this.errorValidatorDeque = errorValidatorDeque;
            for (EditValidator validator : errorValidatorDeque) {
                if (validator.error && !validator.warning) {
                    errorInDeque = true;
                    break;
                }
            }

            verifyAndAsk();
        }

        /**
         * Recursive method that show error dialogs and warning dialog and might call the success
         * method.
         */
        private void verifyAndAsk() {
            if (errorValidatorDeque.isEmpty()) {
                if (!errorInDeque) success.run();
                errorValidatorDeque = null;
            } else {
                EditValidator validator = errorValidatorDeque.pop();

                if (validator.error && !validator.warning) {
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
                    materialAlertDialogBuilder
                            .setTitle(R.string.error)
                            .setMessage(validator.getErrorMessageResourceId())
                            .setIcon(R.drawable.ic_error_red_24dp)
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    dialogInterface.dismiss();
                                    verifyAndAsk();
                                }
                            })
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    verifyAndAsk();
                                }
                            })
                            .show();

                } else if (validator.warning && !errorInDeque) {
                    new MaterialAlertDialogBuilder(context)
                            .setTitle(R.string.warning)
                            .setMessage(validator.getErrorMessageResourceId())
                            .setIcon(R.drawable.ic_warning_yellow_24dp)
                            .setPositiveButton(R.string.continue_text, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    verifyAndAsk();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    errorValidatorDeque = null;
                                }
                            })
                            .show();

                } else {
                    verifyAndAsk();
                }
            }
        }
    }


}
