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
import fr.pyjacpp.diakoluo.tests.data.DataCell;

class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
    private final Context context;

    private AnswerViewListener listener;

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
            DataCell dataCell = currentTest.getListRow().get(position).getListCells().get(
                    currentTest.getListColumn().get(0)
            );
            if (dataCell != null)
                holder.textView.setText(
                        dataCell.getStringValue()
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
