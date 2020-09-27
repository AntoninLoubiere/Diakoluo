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

package fr.pyjacpp.diakoluo.view_test;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;

class ColumnAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<ColumnAdapter.ColumnViewHolder> {
    private final Test currentTest;

    static class ColumnViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView description;

        ColumnViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.titleTextView);
            description = v.findViewById(R.id.descriptionTextView);
        }
    }

    ColumnAdapter(Test currentTest) {
        this.currentTest = currentTest;
    }

    @NonNull
    @Override
    public ColumnViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_view_column, parent, false);
        return new ColumnViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ColumnViewHolder holder, int position) {

        holder.title.setText(currentTest.getListColumn().get(position).getName());
        holder.description.setText(currentTest.getListColumn().get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return currentTest.getNumberColumn();
    }

}
