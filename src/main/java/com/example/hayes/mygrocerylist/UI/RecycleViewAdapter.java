package com.example.hayes.mygrocerylist.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.hayes.mygrocerylist.Activites.DetailedActivity;
import com.example.hayes.mygrocerylist.Data.DatabaseHandler;
import com.example.hayes.mygrocerylist.Model.Grocery;
import com.example.hayes.mygrocerylist.R;

import org.w3c.dom.Text;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{

    private Context context;
    private List<Grocery> groceryList;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater inflater;

    public RecycleViewAdapter(Context context, List<Grocery> groceryList) {
        this.context = context;
        this.groceryList = groceryList;
    }

    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.ViewHolder viewHolder, int i) {
        Grocery grocery = groceryList.get(i);

        viewHolder.groceryItemName.setText(grocery.getName());
        viewHolder.quantity.setText(grocery.getQuantity());
        viewHolder.dateAdded.setText(grocery.getDateItemAdded());
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

            groceryItemName = (TextView) view.findViewById(R.id.name);
            quantity = (TextView) view.findViewById(R.id.quantity);
            dateAdded = (TextView) view.findViewById(R.id.dateAdded);

            editButton = (Button) view.findViewById(R.id.editButton);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Go to next screen - DetailedActivity\
                    int pos = getAdapterPosition();

                    Grocery grocery = groceryList.get(pos);
                    Intent intent = new Intent(context, DetailedActivity.class);
                    intent.putExtra("name", grocery.getName());
                    intent.putExtra("quantity", grocery.getQuantity());
                    intent.putExtra("id", grocery.getId());
                    intent.putExtra("date", grocery.getDateItemAdded());

                    context.startActivity(intent);

                }
            });

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.editButton:
                    int pos = getAdapterPosition();
                    Grocery grocery = groceryList.get(pos);
                    editItem(grocery);
                    break;
                case R.id.deleteButton:
                    pos = getAdapterPosition();
                    grocery = groceryList.get(pos);
                    deleteItem(grocery.getId());
                    break;
            }
        }

        public void deleteItem(final int id) {
            // create alertDialog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);

                    // delete item
                    db.deleteGrocery(id);
                    groceryList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    alertDialog.dismiss();
                }
            });
        }

        public void editItem(final Grocery grocery) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup, null);

            final EditText groceryItem = (EditText) view.findViewById(R.id.groceryItem);
            final EditText quantity = (EditText) view.findViewById(R.id.groceryQty);
            final TextView title = (TextView) view.findViewById(R.id.tile);
            Button saveButton = (Button) view.findViewById(R.id.saveButton);

            groceryItem.setHint(grocery.getName());
            quantity.setHint(grocery.getQuantity());

            title.setText("Edit Grocery");

            alertDialogBuilder.setView(view);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);

                    // update item
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity("Qty: " + quantity.getText().toString());

                    if (!groceryItem.getText().toString().isEmpty() &&
                            !quantity.getText().toString().isEmpty()) {
                        db.updateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(), grocery);
                    } else {
                        Snackbar.make(v, "Add grocery and Quantity", Snackbar.LENGTH_LONG).show();
                    }

                    alertDialog.dismiss();
                }
            });
        }
    }
}
