package com.patcomp.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText eitEditText;
    int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String currentItemName = getIntent().getExtras().getString("currentItemName");
        itemPosition = getIntent().getExtras().getInt("itemPosition", 0);
        eitEditText = (EditText) findViewById(R.id.eitEditText);
        eitEditText.setText(currentItemName);
        eitEditText.setSelection(eitEditText.getText().length());
    }

    public void onSave(View view) {
        Intent data = new Intent();
        data.putExtra("newItemName", eitEditText.getText().toString());
        data.putExtra("itemPosition", itemPosition);
        setResult(RESULT_OK, data);
        finish();
    }
}
