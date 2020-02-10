package fr.pyjacpp.diakoluo.list_tests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;

class TestAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private final Context context;
    private TestViewListener listener;

    interface TestViewListener {
        void onItemClick(View view, int position);
        void onPlayButtonClick(View view, int position);
        void onSeeButtonClick(View view, int position);
        void onDeleteMenuItemClick(View view, int position);
        void onEditMenuItemClick(View view, int position);
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView description;
        final ImageButton playButton;
        final ImageButton seeButton;

        final View inflatedView;

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
    public void onBindViewHolder(@NonNull final TestViewHolder holder, final int position) {
        final Test currentTest = DiakoluoApplication.getListTest(context).get(position);

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

        holder.inflatedView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                PopupMenu popup = new PopupMenu(context, holder.inflatedView);
                popup.inflate(R.menu.menu_list_test_item);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                if (listener != null) {
                                    listener.onEditMenuItemClick(view, position);
                                }
                                return true;
                            case R.id.delete:
                                if (listener != null) {
                                    listener.onDeleteMenuItemClick(view, position);
                                }
                                return true;

                            default:
                                return false;
                        }
                    }
                });


                MenuPopupHelper menuHelper = new MenuPopupHelper(context, (MenuBuilder) popup.getMenu(), holder.inflatedView);
                menuHelper.setForceShowIcon(true);
                menuHelper.show();
                return true;
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
