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

package fr.pyjacpp.diakoluo.tests.score.view_creator;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.material.textview.MaterialTextView;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.score.view_creator.parameters.BaseParameter;
import fr.pyjacpp.diakoluo.widgets.accordion.AccordionView;

/**
 * A view creator that create view to show the object represented. It has also parameters views that
 * will edit parameters of the object represented.
 *
 * @see fr.pyjacpp.diakoluo.tests.score.condition.BaseCondition
 * @see fr.pyjacpp.diakoluo.tests.score.action.BaseAction
 * @see fr.pyjacpp.diakoluo.tests.score.view_creator.parameters.BaseParameter
 */
public class ViewCreator {
    @StringRes
    private final int name;
    @StringRes
    private final int description;

    private BaseParameter[] parameters;
    private boolean useAccordion = true;

    /**
     * Default constructor.
     *
     * @param name        the name of the object
     * @param description the description of the object
     */
    public ViewCreator(@StringRes int name, @StringRes int description) {
        this.name = name;
        this.description = description;
        parameters = new BaseParameter[0];
    }

    /**
     * Construct with parameters.
     *
     * @param name        the name of the object
     * @param description the description of the object
     */
    public ViewCreator(@StringRes int name, @StringRes int description, BaseParameter[] parameters) {
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }

    /**
     * Get a view to view (not edit) the object represented.
     *
     * @param context the context of the application
     * @return the view to add
     * @see #getEditView(Context)
     */
    public View getViewView(@NonNull Context context) {
        LinearLayout parentView;
        LinearLayout rootView;

        if (useAccordion) {
            AccordionView accordion = new AccordionView(context);
            accordion.setText(name);
            parentView = accordion.getParentView();
            rootView = accordion.getRootView();
        } else {
            rootView = new LinearLayout(context);

            MaterialTextView textView = new MaterialTextView(context);
            textView.setText(name);
            textView.setTextAppearance(context, R.style.TextAppearance_MaterialComponents_Body1);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            rootView.addView(textView);

            parentView = rootView;
        }

        for (BaseParameter baseParameter : parameters) {
            parentView.addView(baseParameter.getViewView(context));
        }
        return rootView;
    }

    /**
     * Get a view to edit the object represented.
     *
     * @param context the context of the application
     * @return the view to add
     * @see #getViewView(Context)
     */
    public View getEditView(@NonNull Context context) {
        LinearLayout parentView;
        LinearLayout rootView;

        if (useAccordion) {
            AccordionView accordion = new AccordionView(context);
            accordion.setText(name);
            parentView = accordion.getParentView();
            rootView = accordion.getRootView();
        } else {
            rootView = new LinearLayout(context);

            MaterialTextView textView = new MaterialTextView(context);
            textView.setText(name);
            textView.setTextAppearance(context, R.style.TextAppearance_MaterialComponents_Body1);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            rootView.addView(textView);

            parentView = rootView;
        }

        for (BaseParameter baseParameter : parameters) {
            parentView.addView(baseParameter.getEditView(context));
        }
        return rootView;
    }

    /**
     * Get the name of the object.
     *
     * @return the name
     */
    @StringRes
    public int getName() {
        return name;
    }

    /**
     * Get the description of the object.
     *
     * @return the description
     */
    @StringRes
    public int getDescription() {
        return description;
    }

    /**
     * Get parameters
     * @return the list of parameters
     */
    public BaseParameter[] getParameters() {
        return parameters;
    }

    /**
     * Set if the view generated should use an accordion (if the view should have a drop down).
     * @param b if the view generate should use an accordion
     */
    public void setUseAccordion(boolean b) {
        useAccordion = b;
    }

    /**
     * Get if the view use an accordion
     * @return if the view should use an accordion
     */
    public boolean isUseAccordion() {
        return useAccordion;
    }
}
