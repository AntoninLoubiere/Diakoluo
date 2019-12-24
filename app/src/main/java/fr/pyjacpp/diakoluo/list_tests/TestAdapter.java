package fr.pyjacpp.diakoluo.list_tests;

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

class TestAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private Context context;
    private TestViewListener listener;

    interface TestViewListener {
        void onItemClick(View view, int position);
        void onPlayButtonClick(View view, int position);
        void onSeeButtonClick(View view, int position);
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        ImageButton playButton;
        ImageButton seeButton;

        View inflatedView;

        TestViewHolder(View v) {
            super(v);

            inflatedView = v;

            title = v.findViewById(R.id.titleTextView);
            description = v.findViewById(R.id.descriptionTextView);
            playButton = v.findViewById(R.id.buttonPlay);
            seeButton = v.findViewById(R.id.buttonSee);
        }
    }

    TestAdapter(Context context) {
        this.context = context;
    }

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
    public void onBindViewHolder(@NonNull TestViewHolder holder, final int position) {
        Test currentTest = DiakoluoApplication.getListTest(context).get(position);

        holder.title.setText(currentTest.getName());
        holder.description.setText(currentTest.getDescription());

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
        holder.inflatedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return DiakoluoApplication.getListTest(context).size();
    }

    public TestViewListener getListener() {
        return listener;
    }

    public void setListener(TestViewListener listener) {
        this.listener = listener;
    }

}
