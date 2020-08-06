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
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
    private final Context context;

    private final AnswerViewListener listener;

    interface AnswerViewListener {
        void onItemClick(View view, View itemView);
        void onDeleteClick(View view, View itemView);
    }


    static class AnswerViewHolder extends RecyclerView.ViewHolder {

        final TextView textView;
        final ImageButton deleteImageButton;

        AnswerViewHolder(View v) {
            super(v);

            textView = v.findViewById(R.id.textView);
            deleteImageButton = v.findViewById(R.id.delete);
        }
    }

    AnswerAdapter(Context context, AnswerViewListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_edit_answer, parent, false);
        return new AnswerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnswerViewHolder holder, final int position) {
        Test currentTest = DiakoluoApplication.getCurrentEditTest(context);

        if (currentTest.getNumberColumn() >= 1) {
            Column column = currentTest.getListColumn().get(0);
            DataCell dataCell = currentTest.getListRow().get(position).getListCells().get(
                    column
            );
            if (dataCell != null)
                holder.textView.setText(
                        dataCell.getStringValue(context, column)
                );
            else
                holder.textView.setText(
                        String.valueOf(position + 1)
                );
        } else {
            holder.textView.setText(String.valueOf(position + 1));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(view, holder.itemView);
                }
            }
        });

        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDeleteClick(view, holder.itemView);
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
            return currentEditTest.getNumberRow();
        }
    }
}
