package fr.pyjacpp.diakoluo.view_test;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;

class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
    private Context context;

    static class AnswerViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        AnswerViewHolder(View v) {
            super(v);

            textView = v.findViewById(R.id.textView);
        }
    }

    AnswerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_answer, parent, false);
        return new AnswerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Test currentTest = DiakoluoApplication.getListTest(context).get(position);

        if (currentTest.getNumberColumn() >= 1) {
            holder.textView.setText(
                    (String) currentTest.getListRow().get(position).getListCells().get(
                    currentTest.getListColumn().get(0)
                    ).getValue()
            );
        } else {
            holder.textView.setText(String.valueOf(position + 1));
        }


    }

    @Override
    public int getItemCount() {
        return DiakoluoApplication.getCurrentTest(context).getNumberRow();
    }

}
