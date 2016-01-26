package com.patcomp.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int EDIT_CODE = 20;
    private final int ADD_CODE = 30;
    ArrayList<Item> itemsList;
    ItemAdapter aToDoAdapter;
    ListView lvItems;
    EditText etEditText;
    ItemsDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHelper = ItemsDatabaseHelper.getInstance(this);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);



        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                Item item = new Item(itemsList.get(position).Name);
                databaseHelper.deleteItem(item);
                itemsList.remove(position);
                aToDoAdapter.notifyDataSetChanged();

                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("currentItemName", itemsList.get(position).Name.toString());
                i.putExtra("itemPosition", position);
                i.putExtra("itemId", itemsList.get(position).Id);
                i.putExtra("itemDueDate", itemsList.get(position).dueDate.toString());
                i.putExtra("itemNotes", itemsList.get(position).Notes.toString());
                i.putExtra("itemLevel", itemsList.get(position).level.toString());
                i.putExtra("itemStatus", itemsList.get(position).Status.toString());
                startActivityForResult(i, EDIT_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_CODE) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            int position;
            Item item = new Item("");
            position = data.getExtras().getInt("itemPosition", 0);
            String oldname = itemsList.get(position).Name;

            item.Name = data.getExtras().getString("newItemName");

            item.Id = data.getExtras().getInt("itemId", 0);
            try {
                item.dueDate = dateFormat.parse(data.getExtras().getString("itemDueDate"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            item.Notes = data.getExtras().getString("itemNotes");
            item.level = data.getExtras().getString("itemLevel");
            item.Status = data.getExtras().getString("itemStatus");

            itemsList.set(position, item);
            aToDoAdapter.notifyDataSetChanged();
            databaseHelper.updateItem(item, oldname);
        }
    }

    public void populateArrayItems() {
        itemsList = databaseHelper.getAllItems();
        aToDoAdapter = new ItemAdapter(this, itemsList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.actionAdd) {
            Intent i = new Intent(MainActivity.this, EditItemActivity.class);
            startActivityForResult(i, ADD_CODE);
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
        String NewitemName = etEditText.getText().toString();
        Item item = new Item(NewitemName);
        itemsList.add(item);
        aToDoAdapter.notifyDataSetChanged();
        etEditText.setText("");

        databaseHelper.addItem(item);
    }




}
