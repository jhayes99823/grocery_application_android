package com.example.hayes.mygrocerylist.Activites;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hayes.mygrocerylist.Data.DatabaseHandler;
import com.example.hayes.mygrocerylist.Model.Grocery;
import com.example.hayes.mygrocerylist.R;
import com.example.hayes.mygrocerylist.UI.RecycleViewAdapter;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

public class DetailedActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView quantity;
    private TextView dateAdded;
    private int groceryId;

    private Button editButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        itemName = (TextView) findViewById(R.id.itemNameDet);
        quantity = (TextView) findViewById(R.id.quantityDet);
        dateAdded = (TextView) findViewById(R.id.dateAddedDet);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            itemName.setText(bundle.getString("name"));
            quantity.setText(bundle.getString("quantity"));
            dateAdded.setText(bundle.getString("date"));
            groceryId = bundle.getInt("id");
        }

        editButton = (Button) findViewById(R.id.editButtonDet);
        deleteButton = (Button) findViewById(R.id.deleteButtonDet);

    }

}
