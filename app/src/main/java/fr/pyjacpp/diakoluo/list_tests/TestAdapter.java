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

package fr.pyjacpp.diakoluo.list_tests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.CompactTest;

class TestAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private final Context context;
    private TestViewListener listener;

    interface TestViewListener {
        void onItemClick(View view, int position);
        void onPlayButtonClick(View view, int position);
        void onSeeButtonClick(View view, int position);
        void onDeleteMenuItemClick(View view, int position);
        void onEditMenuItemClick(View view, int position);
        void onExportMenuItemClick(View view, int position);
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView description;
        final ImageButton playButton;
        final ImageButton seeButton;

        TestViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.titleTextView);
            description = v.findViewById(R.id.descriptionTextView);
            playButton = v.findViewById(R.id.buttonPlay);
            seeButton = v.findViewById(R.id.buttonSee);
        }
    }

    /*TestAdapter(Context context) {
        this.context = context;
    }*/

    TestAdapter(Context context, TestViewListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_view_test, parent, false);
        return new TestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final TestViewHolder holder, final int position) {
        final CompactTest currentTest = DiakoluoApplication.get(context).getListTest().get(position);

        holder.title.setText(currentTest.getName());
        holder.description.setText(currentTest.getCompactDescription());

        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onPlayButtonClick(view, position);
                }
            }
        });
        holder.seeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSeeButtonClick(view, position);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(view, position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                PopupMenu popup = new PopupMenu(context, holder.itemView);
                popup.inflate(R.menu.menu_list_test_item);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (listener != null) {
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    listener.onEditMenuItemClick(view, position);
                                    return true;

                                case R.id.delete:
                                    listener.onDeleteMenuItemClick(view, position);
                                    return true;

                                case R.id.export:
                                    listener.onExportMenuItemClick(view, position);

                                default:
                                    return false;
                            }
                        } else {
                            return false;
                        }
                    }
                });


                popup.show();
                return true;
            }
        });

        if (!currentTest.isPlayable()) {
            holder.playButton.setImageDrawable(
                    ResourcesCompat.getDrawable(context.getResources(),
                            R.drawable.ic_play_arrow_gray_24dp, null));
        }
    }

    @Override
    public int getItemCount() {
        return DiakoluoApplication.get(context).getListTest().size();
    }

    public TestViewListener getListener() {
        return listener;
    }

    public void setListener(TestViewListener listener) {
        this.listener = listener;
    }

}
