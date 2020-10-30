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

package fr.pyjacpp.diakoluo.tests.score.view_creator.parameters;

import android.content.Context;
import android.text.InputType;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import fr.pyjacpp.diakoluo.R;

public class FloatParameter extends BaseParameter {
    private float value;
    private TextInputEditText editText;

    /**
     * Default constructor.
     *
     * @param name         the name of the parameter
     * @param description  the description of the parameter
     * @param defaultValue the default value of the view
     */
    public FloatParameter(int name, int description, float defaultValue) {
        super(name, description);
        value = defaultValue;
    }

    /**
     * Generate the view to view the value of this parameter.
     *
     * @param context the context of the application
     * @return the view generated
     * @see #getEditView(Context)
     */
    @NonNull
    @Override
    public View getViewView(@NonNull Context context) {
        MaterialTextView view = new MaterialTextView(context);
        view.setText(context.getString(R.string.float_formatter, value));
        return view;
    }

    /**
     * Generate the view to view the value of this parameter.
     *
     * @param context the context of the application
     * @return the view generated
     * @see #getEditValue()
     * @see #getViewView(Context)
     */
    @NonNull
    @Override
    public View getEditView(@NonNull Context context) {
        TextInputLayout layout = new TextInputLayout(context, null,
                R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);

        editText = new TextInputEditText(context);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setHint(name);
        editText.setText(context.getString(R.string.float_formatter, value));
        layout.addView(editText);

        return layout;
    }

    /**
     * Get the value inputted by the user.
     *
     * @return the value inputted by the user
     * @see #getEditView(Context)
     */
    @Override
    public Object getEditValue() {
        return Float.valueOf(String.valueOf(editText.getText()));
    }
}
