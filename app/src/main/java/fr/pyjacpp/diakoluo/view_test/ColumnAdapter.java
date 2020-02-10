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

class ColumnAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<ColumnAdapter.ColumnViewHolder> {
    private final Context context;

    static class ColumnViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView description;

        ColumnViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.titleTextView);
            description = v.findViewById(R.id.descriptionTextView);
        }
    }

    ColumnAdapter(Context context) {
        this.context = context;
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
        Test currentTest = DiakoluoApplication.getCurrentTest(context);

        holder.title.setText(currentTest.getListColumn().get(position).getName());
        holder.description.setText(currentTest.getListColumn().get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return DiakoluoApplication.getCurrentTest(context).getNumberColumn();
    }

}
