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

package fr.pyjacpp.diakoluo.tests.column;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.data.AnswerValidEnum;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

/**
 * A column that hold a list of values.
 */
public class ColumnList extends Column {
    private static final String TAG_VALUES = "values";
    private static final String TAG_VALUE = "value";
    @NonNull
    private ArrayList<String> values = new ArrayList<>();
    private int defaultValue = -1;
    // private static final int SET_DEFAULT = Column.SET_DEFAULT;

    /**
     * Default constructor
     */
    protected ColumnList() {
        super(ColumnInputType.List);
    }

    @Override
    public void initialize() {
        super.initialize();
        defaultValue = -1;
    }

    @Override
    public void initialize(String name, String description) {
        super.initialize(name, description);
        defaultValue = 0;
        // settings = SET_DEFAULT;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = (int) defaultValue;
    }

    public String getStringValue(Context context, int valueIndex) {
        if (valueIndex < 0 || valueIndex >= values.size()) {
            if (context == null)
                return "";
            return context.getString(R.string.default_string_list);
        }
        return values.get(valueIndex);
    }

    public int setValueFromCsvGetIndex(String lineCell) {
        int valuesSize = values.size();
        for (int i = 0; i < valuesSize; i++) {
            if (lineCell.equals(values.get(i))) {
                return i;
            }
        }
        values.add(lineCell);
        return valuesSize;
    }

    public int getMigrationIndex(String migration) {
        int valuesSize = values.size();
        for (int i = 0; i < valuesSize; i++) {
            if (migration.equals(values.get(i))) {
                return i;
            }
        }
        values.add(migration);
        return valuesSize;
    }

    @Override
    public AnswerValidEnum verifyAnswer(DataCell dataCell, Object answer) {
        int value = ((int) answer);
        if (value < 0) return AnswerValidEnum.SKIPPED;
        else if (value == ((int) dataCell.getValue())) return AnswerValidEnum.RIGHT;
        else return AnswerValidEnum.WRONG;
    }

    @Override
    public void getViewColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        super.getViewColumnSettings(layoutInflater, parent);
        View inflatedView = layoutInflater.inflate(R.layout.fragment_column_settings_view_list,
                parent, true);
        RecyclerView recyclerView = inflatedView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        ColumnAdapter adapter = new ColumnAdapter(false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void getEditColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        super.getEditColumnSettings(layoutInflater, parent);
        View inflatedView = layoutInflater.inflate(R.layout.fragment_column_settings_edit_list,
                parent, true);
        final RecyclerView recyclerView = inflatedView.findViewById(R.id.recyclerView);
        Button addButton = inflatedView.findViewById(R.id.addOptionButton);
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        final ColumnAdapter adapter = new ColumnAdapter(true);
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                values.add("");
                int size = values.size();
                adapter.notifyItemInserted(size - 1);
                adapter.requestFocus(true);
            }
        });
    }

    @Override
    public void setEditColumnSettings(ViewGroup parent) {
        super.setEditColumnSettings(parent);
        RecyclerView recyclerView = parent.findViewById(R.id.recyclerView);

        for (int i = 0, valuesSize = values.size(); i < valuesSize; i++) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager != null) {
                View layout = layoutManager.findViewByPosition(i);
                if (layout != null) {
                    EditText editText = layout.findViewById(R.id.editText);
                    values.set(i, editText.getText().toString());
                }
            }
        }
    }

    @Override
    public View showEditValueView(Context context, @Nullable Object defaultValue) {
        Spinner spinner = new Spinner(context);
        spinner.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, values));
        if (defaultValue != null)
            spinner.setSelection((int) defaultValue);
        return spinner;
    }

    @Override
    public Object getValueFromView(View view) {
        Spinner spinner = (Spinner) view;
        return spinner.getSelectedItemPosition();
    }

    @Override
    public View showEditValueTestView(Context context, @Nullable Object defaultValue) {
        Spinner spinner = new Spinner(context);
        ArrayList<String> values = new ArrayList<>(this.values);
        values.add(0, context.getString(R.string.column_list_skipped_item));
        spinner.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, values));
        if (defaultValue != null)
            spinner.setSelection((int) defaultValue);
        return spinner;
    }

    @Override
    public Object getValueFromTestView(View view) {
        Spinner spinner = (Spinner) view;
        return spinner.getSelectedItemPosition() - 1;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && defaultValue >= 0;
    }

    @Override
    public void writeXmlInternal(OutputStream fileOutputStream) throws IOException {
        super.writeXmlInternal(fileOutputStream);
        XmlSaver.writeData(fileOutputStream, FileManager.TAG_DEFAULT_VALUE, defaultValue);

        XmlSaver.writeStartBeacon(fileOutputStream, TAG_VALUES);
        for (int i = 0, valuesSize = values.size(); i < valuesSize; i++) {
            XmlSaver.writeData(fileOutputStream, TAG_VALUE, values.get(i));
        }
        XmlSaver.writeEndBeacon(fileOutputStream, TAG_VALUES);
    }

    @Override
    protected void readColumnXmlTag(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        switch (parser.getName()) {
            case TAG_VALUES:
                readXmlValuesTag(parser);
                break;

            case FileManager.TAG_DEFAULT_VALUE:
                defaultValue = XmlLoader.readInt(parser);
                break;

            default:
                super.readColumnXmlTag(parser);
                break;
        }

    }

    private void readXmlValuesTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, TAG_VALUES);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            if (parser.getName().equals(TAG_VALUE)) {
                values.add(XmlLoader.readText(parser));
            } else {
                XmlLoader.skip(parser);
            }
        }
    }

    @Override
    public Column copyColumn() {
        Column column = new ColumnList();
        copyColumn(column);
        return column;
    }

    @Override
    protected void copyColumn(Column newColumn) {
        super.copyColumn(newColumn);
        ColumnList column = (ColumnList) newColumn;
        column.defaultValue = defaultValue;
        column.values = new ArrayList<>(values);
    }

    public void setValues(@NonNull ArrayList<String> values) {
        this.values = values;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof ColumnList && super.equals(obj)) {
            ColumnList cL = (ColumnList) obj;
            return cL.defaultValue == defaultValue &&
                    cL.values.equals(values);
        }
        return false;
    }

    private class ColumnAdapter extends RecyclerView.Adapter<ColumnAdapter.ColumnViewHolder> {

        private final boolean editable;
        private boolean requestFocus;

        private ColumnAdapter(boolean editable) {
            super();
            this.editable = editable;
        }

        @NonNull
        @Override
        public ColumnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v;
            if (editable) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_holder_column_settings_list_edit, parent, false);
            } else {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_holder_column_settings_list_view, parent, false);
            }
            return new ColumnViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ColumnViewHolder holder, int position) {
            if (editable) {
                holder.editText.setText(values.get(position));
                holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        int index = holder.getAdapterPosition();
                        if (index > 0) values.set(index, holder.editText.getText().toString());
                    }
                });
                if (requestFocus) {
                    holder.editText.requestFocus();
                    requestFocus = false;
                }

                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        values.remove(position);
                        notifyItemRemoved(position);
                    }
                });

                holder.upButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        if (position > 0) {
                            Collections.swap(values, position, position - 1);
                            notifyItemMoved(position, position - 1);
                        }
                    }
                });

                holder.downButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        if (position < values.size() - 1) {
                            Collections.swap(values, position, position + 1);
                            notifyItemMoved(position, position + 1);
                        }
                    }
                });
            } else {
                holder.textView.setText(values.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return values.size();
        }

        public void requestFocus(boolean b) {
            requestFocus = b;
        }

        private class ColumnViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            EditText editText;
            ImageButton deleteButton;
            ImageButton upButton;
            ImageButton downButton;

            public ColumnViewHolder(@NonNull View itemView) {
                super(itemView);
                if (editable) {
                    editText = itemView.findViewById(R.id.editText);
                    deleteButton = itemView.findViewById(R.id.delete);
                    upButton = itemView.findViewById(R.id.upButton);
                    downButton = itemView.findViewById(R.id.downButton);
                } else {
                    textView = itemView.findViewById(R.id.textView);
                }
            }
        }
    }
}
