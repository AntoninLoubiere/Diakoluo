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

package fr.pyjacpp.diakoluo.widgets.accordion;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.textview.MaterialTextView;

import fr.pyjacpp.diakoluo.R;

/**
 * An accordion view that could expand views
 */
public class AccordionView {
    private boolean expanded;
    private boolean animation;
    private LinearLayout rootView;
    private LinearLayout parentView;
    private MaterialTextView expandTextView;
    private Drawable expandDrawable;
    private Drawable expandedDrawable;

    /**
     * Initialise the view with animation true.
     *
     * @param context the app context
     */
    public AccordionView(Context context) {
        parentView = new LinearLayout(context);
        expanded = false;
        animation = true;
        initialise(context);
    }

    /**
     * Initialise the view.
     *
     * @param context   the app context
     * @param animation if the view should be animated
     */
    public AccordionView(Context context, boolean animation) {
        parentView = new LinearLayout(context);
        expanded = false;
        this.animation = animation;
        initialise(context);
    }

    /**
     * Initialise the view.
     *
     * @param context    the app context
     * @param animation  if the view should be animated
     * @param parentView the parent view that contain view that could be hide and show
     */
    public AccordionView(Context context, boolean animation, LinearLayout parentView) {
        this.parentView = parentView;
        expanded = false;
        this.animation = animation;
        initialise(context);
    }

    /**
     * Initialise the view
     *
     * @param context the app context
     */
    private void initialise(Context context) {
        rootView = new LinearLayout(context);
        rootView.setOrientation(LinearLayout.VERTICAL);
        expandTextView = new MaterialTextView(context);
        expandTextView.setTextAppearance(context, R.style.TextAppearance_MaterialComponents_Body1);
        expandTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

        Resources resources = context.getResources();
        Resources.Theme theme = context.getTheme();
        expandDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_expand_arrow,
                theme);
        expandedDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_drop_down_24,
                theme);

        updateExpandDrawable();
        expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleExpand();
            }
        });
        rootView.addView(expandTextView);

        parentView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMarginStart(resources.getDimensionPixelSize(R.dimen.accordion_view_offset));
        rootView.addView(parentView, layoutParams);

        collapse();
    }

    /**
     * Update the expand drawable
     */
    private void updateExpandDrawable() {
        expandTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                expanded ? expandedDrawable : expandDrawable, null, null, null
        );
    }

    /**
     * Set the text of the accordion (always show)
     *
     * @param res the string res of the text
     * @see #setText(CharSequence)
     */
    public void setText(@StringRes int res) {
        expandTextView.setText(res);
    }

    /**
     * Set the text of the accordion (always show)
     *
     * @param sequence the sequence of the text
     * @see #setText(int)
     */
    public void setText(CharSequence sequence) {
        expandTextView.setText(sequence);
    }

    /**
     * Toggle expand and collapse.
     *
     * @see #expand()
     * @see #collapse()
     */
    public void toggleExpand() {
        if (expanded) {
            collapse();
        } else {
            expand();
        }
    }

    /**
     * Expand the view
     */
    public void expand() {
        expanded = true;
        if (animation) {
            ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f,
                    0f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0f);
            scaleAnimation.setFillAfter(true);
            scaleAnimation.setDuration(300);
            parentView.startAnimation(scaleAnimation);
            parentView.setVisibility(View.VISIBLE);
            updateExpandDrawable();
        }
    }

    /**
     * Collapse the view
     */
    public void collapse() {
        expanded = false;
        if (animation) {
            ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f,
                    1f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0f);
            scaleAnimation.setFillAfter(true);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    parentView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            scaleAnimation.setDuration(300);
            parentView.startAnimation(scaleAnimation);
            updateExpandDrawable();
        }
    }

    /**
     * Get if the accordion is expanded.
     *
     * @return if expanded
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * Get if animation are activated.
     *
     * @return if animation are activated
     */
    public boolean isAnimation() {
        return animation;
    }

    /**
     * Set if the view should do animations.
     *
     * @param animation the new value
     */
    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    /**
     * Get the root view to add.
     *
     * @return the root view
     */
    public LinearLayout getRootView() {
        return rootView;
    }

    /**
     * Get the parent view of the data that could be hide or show.
     *
     * @return the parent view
     */
    public LinearLayout getParentView() {
        return parentView;
    }
}
