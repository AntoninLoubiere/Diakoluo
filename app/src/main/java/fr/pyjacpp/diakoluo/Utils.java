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

import java.util.ArrayDeque;

public final class Utils {
    public static String removeUselessSpaces(String s) {
        final int length = s.length();
        int realStart = 0;
        int realEnd = length - 1;

        while (realStart < length &&
                (s.charAt(realStart) == ' ' ||
                        s.charAt(realStart) == '\n' || s.charAt(realStart) == '\t')) {
            realStart++;
        }

        if (realStart >= length) {
            return "";
        }

        while (s.charAt(realEnd) == ' ' || s.charAt(realEnd) == '\n' || s.charAt(realEnd) == '\t') {
            realEnd--;
        }

        return s.substring(realStart, realEnd + 1);
    }

    public static int map(int var, int inMin, int inMax, int outMin, int outMax) {
        return (var - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static class ExtremeDeceleratorInterpolator implements Interpolator {

        @Override
        public float getInterpolation(float v) {
            float v1 = v - 1;
            return -(v1 * v1 * v1 * v1) + 1;
        }
    }

    public static class EditValidator {
        private final boolean error;
        private final boolean warning;
        private final Integer errorMessageResourceId;

        public EditValidator() {
            errorMessageResourceId = null;
            warning = false;
            error = false;
        }

        public EditValidator(Integer errorMessageResourceId) {
            this.errorMessageResourceId = errorMessageResourceId;
            warning = false;
            error = true;
        }

        public EditValidator(Integer errorMessageResourceId, boolean warning) {
            this.errorMessageResourceId = errorMessageResourceId;
            this.warning = warning;
            error = true;
        }

        public Integer getErrorMessageResourceId() {
            return errorMessageResourceId;
        }

        public boolean isError() {
            return error;
        }

        public boolean isWarning() {
            return warning;
        }
    }

    public static class VerifyAndAsk {

        private final Context context;
        private final Runnable success;
        private boolean errorInDeque;
        private ArrayDeque<EditValidator> errorValidatorDeque;

        public VerifyAndAsk(Context context, Runnable success) {
            this.context = context;
            this.success = success;
            this.errorInDeque = false;
        }

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

        private void verifyAndAsk() {
            if (errorValidatorDeque.isEmpty()) {
                if (!errorInDeque) success.run();
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
