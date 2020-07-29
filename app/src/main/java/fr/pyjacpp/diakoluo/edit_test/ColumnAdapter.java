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

package fr.pyjacpp.diakoluo.edit_test;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;

class ColumnAdapter extends RecyclerView.Adapter<ColumnAdapter.ColumnViewHolder> {
    private final Context context;
    private final ColumnViewListener listener;

    interface ColumnViewListener {
        void onItemClickListener(View view, View itemView);
        void onDeleteClickListener(View view, View itemView);
    }

    static class ColumnViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView description;
        final ImageButton deleteImageButton;

        ColumnViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.titleTextView);
            description = v.findViewById(R.id.descriptionTextView);
            deleteImageButton = v.findViewById(R.id.delete);
        }
    }

    ColumnAdapter(Context context, ColumnViewListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColumnViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_edit_column, parent, false);
        return new ColumnViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ColumnViewHolder holder, int position) {
        Test currentTest = DiakoluoApplication.getCurrentEditTest(context);

        holder.title.setText(currentTest.getListColumn().get(position).getName());
        holder.description.setText(currentTest.getListColumn().get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClickListener(view, holder.itemView);
                }
            }
        });

        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDeleteClickListener(view, holder.itemView);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        Test currentEditTest = DiakoluoApplication.getCurrentEditTest(context);
        if (currentEditTest == null) {
            return 0;
        } else {
            return currentEditTest.getNumberColumn();
        }
    }

}
