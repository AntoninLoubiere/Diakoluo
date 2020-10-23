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
import android.text.Editable;
import android.view.View;
import android.view.ViewParent;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public final class ViewUtils {
    /**
     * When a edit text with a layout is clicked send focus change to the parent so it can be
     * receive from other class
     */
    public static final View.OnFocusChangeListener TRANSFER_TO_PARENT_FOCUS_LISTENER =
            new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    ViewParent parent = v.getParent();
                    if (parent instanceof View) {
                        View parent1 = (View) parent;
                        View.OnFocusChangeListener onFocusChangeListener =
                                parent1.getOnFocusChangeListener();
                        if (onFocusChangeListener != null)
                            onFocusChangeListener.onFocusChange(parent1, true);
                    }
                }
            };

    public static void setBooleanView(Context context, MaterialTextView materialTextView, boolean value) {
        materialTextView.setCompoundDrawablesWithIntrinsicBounds(
                ResourcesCompat.getDrawable(context.getResources(),
                        value ? R.drawable.ic_check_coloured_24 : R.drawable.ic_close_coloured_24dp,
                        context.getTheme()), null, null, null);
    }

    public static int getIntFromEditText(TextInputEditText editText, int defaultInt) {
        int result = defaultInt;
        try {
            Editable text = editText.getText();
            if (text != null) {
                result = Integer.parseInt(text.toString());
            }
        } catch (NumberFormatException ignored) {
        }
        return result;
    }

    public static float getFloatFromEditText(TextInputEditText editText, float defaultFloat) {
        float result = defaultFloat;
        try {
            Editable text = editText.getText();
            if (text != null) {
                result = Float.parseFloat(text.toString());
            }
        } catch (NumberFormatException ignored) {
        }
        return result;
    }
}
