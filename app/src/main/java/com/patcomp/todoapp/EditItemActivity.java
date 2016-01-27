package com.patcomp.todoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditItemActivity extends AppCompatActivity {

    ListView listView;
    EditText eitEditName;
    EditText etDueDate;
    EditText etLevel;
    EditText etStatus;
    EditText etNotes;
    int itemPosition;
    int itemId;
    Calendar myCalendar = Calendar.getInstance();
    String[] listValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        itemPosition = getIntent().getExtras().getInt("itemPosition", 0);
        itemId = getIntent().getExtras().getInt("itemId", 0);

        DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");

        Item item = new Item();
        item.Name = getIntent().getExtras().getString("currentItemName");
        item.Status = getIntent().getExtras().getString("itemStatus");
        try {
            item.dueDate = format.parse(getIntent().getExtras().getString("itemDueDate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        item.level = getIntent().getExtras().getString("itemLevel");
        item.Notes = getIntent().getExtras().getString("itemNotes");

        eitEditName = (EditText) findViewById(R.id.eitEditName);
        etDueDate = (EditText) findViewById(R.id.etDueDate);
        etLevel = (EditText) findViewById(R.id.etLevel);
        etStatus = (EditText) findViewById(R.id.etStatus);
        etNotes = (EditText) findViewById(R.id.etNotes);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        eitEditName.setText(item.Name);
        etStatus.setText(item.Status);
        etDueDate.setText(dateFormat.format(item.dueDate));
        etLevel.setText(item.level);
        etNotes.setText(item.Notes);

        eitEditName.setSelection(eitEditName.getText().length());
        etStatus.setSelection(etStatus.getText().length());
        etDueDate.setSelection(etDueDate.getText().length());
        etLevel.setSelection(etLevel.getText().length());
        etNotes.setSelection(etNotes.getText().length());

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        etDueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditItemActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        etLevel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listValues = new String[]{"LOW","MEDIUM", "HIGH"};
                SearchList();
            }
        });

        etStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listValues = new String[]{"TO DO", "DONE"};
                SearchList();
            }
        });
    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDueDate.setText(sdf.format(myCalendar.getTime()));
    }


    public void SearchList() {
        final Dialog dialog = new Dialog(EditItemActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.search_list);
        listView = (ListView) dialog.findViewById(R.id.lvLevel);

        dialog.show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listValues);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listView
                        .getItemAtPosition(position);

                if (listValues[0].equals("LOW"))
                    etLevel.setText(itemValue);
                else
                    etStatus.setText(itemValue);

                dialog.cancel();

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.actionSave) {
            Intent data = new Intent();
            data.putExtra("newItemName", eitEditName.getText().toString());
            data.putExtra("itemPosition", itemPosition);
            data.putExtra("itemId", itemId);
            data.putExtra("itemDueDate", etDueDate.getText().toString());
            data.putExtra("itemNotes", etNotes.getText().toString());
            data.putExtra("itemLevel", etLevel.getText().toString());
            data.putExtra("itemStatus", etStatus.getText().toString());
            setResult(RESULT_OK, data);
            finish();
            return true;
        }
        else if (id == R.id.actionExit) {
            Intent data = new Intent();
            setResult(RESULT_CANCELED, data);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
