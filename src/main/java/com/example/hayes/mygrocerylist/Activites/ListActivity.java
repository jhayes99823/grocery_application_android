package com.example.hayes.mygrocerylist.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hayes.mygrocerylist.Data.DatabaseHandler;
import com.example.hayes.mygrocerylist.Model.Grocery;
import com.example.hayes.mygrocerylist.R;
import com.example.hayes.mygrocerylist.UI.RecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecycleViewAdapter recycleViewAdapter;
    private List<Grocery> groceryList;
    private List<Grocery> listItems;
    private DatabaseHandler db;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                  .setAction("Action", null).show();
                createPopupDialog();
            }
        });

        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList = new ArrayList<>();
        listItems = new ArrayList<>();

        // get items from db
        groceryList = db.getAllGroceries();

        for (Grocery g : groceryList) {

            Grocery grocery = new Grocery();

            grocery.setName(g.getName());
            grocery.setQuantity("Qty: " + g.getQuantity());
            grocery.setDateItemAdded("Added on: " + g.getDateItemAdded());
            grocery.setId(g.getId());

            listItems.add(grocery);
        }

        recycleViewAdapter = new RecycleViewAdapter(this, listItems);
        recyclerView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.notifyDataSetChanged();
    }

    private void createPopupDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        final EditText groceryItem = (EditText) view.findViewById(R.id.groceryItem);
        final EditText quantity = (EditText) view.findViewById(R.id.groceryQty);
        Button saveButton = (Button) view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Grocery grocery = new Grocery();

                db = new DatabaseHandler(ListActivity.this);

                // update item
                grocery.setName(groceryItem.getText().toString());
                grocery.setQuantity("Qty: " + quantity.getText().toString());

                if (!groceryItem.getText().toString().isEmpty() &&
                        !quantity.getText().toString().isEmpty()) {
                    db.addGrocery(grocery);
                    startActivity(new Intent(ListActivity.this, ListActivity.class));
                } else {
                    Snackbar.make(v, "Add grocery and Quantity", Snackbar.LENGTH_LONG).show();
                }

                dialog.dismiss();

            }
        });


    }

}
