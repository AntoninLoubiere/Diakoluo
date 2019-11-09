package fr.pyjacpp.diakoluo.list_tests;

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

class TestAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private Context context;

    static class TestViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;

        TestViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.titleTextView);
            description = v.findViewById(R.id.descriptionTextView);
        }
    }

    TestAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_test, parent, false);
        return new TestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Test currentTest = DiakoluoApplication.getListTest(context).get(position);

        holder.title.setText(currentTest.getName());
        holder.description.setText(currentTest.getDescription());

    }

    @Override
    public int getItemCount() {
        return DiakoluoApplication.getListTest(context).size();
    }

}
