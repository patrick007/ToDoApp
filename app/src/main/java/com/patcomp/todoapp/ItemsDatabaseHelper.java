package com.patcomp.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by patrick on 1/24/16.
 */
public class ItemsDatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "itemsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ITEMS = "items";

    // Post Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_NAME = "name";
    private static final String KEY_ITEM_DUE_DATE = "due_date";
    private static final String KEY_ITEM_NOTES = "notes";
    private static final String KEY_ITEM_LEVEL = "level";
    private static final String KEY_ITEM_STATUS = "status";

    private static final String TAG = "error";
    private static ItemsDatabaseHelper sInstance;


    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_NAME + " TEXT," +
                KEY_ITEM_DUE_DATE + " DATE," +
                KEY_ITEM_NOTES + " TEXT," +
                KEY_ITEM_LEVEL + " TEXT," +
                KEY_ITEM_STATUS + " TEXT" +
                ")";

        db.execSQL(CREATE_ITEMS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

    public static synchronized ItemsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new ItemsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private ItemsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Insert a post into the database
    public void addItem(Item item) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_NAME, item.Name);
            values.put(KEY_ITEM_DUE_DATE, dateFormat.format(item.dueDate));
            values.put(KEY_ITEM_NOTES, item.Notes);
            values.put(KEY_ITEM_LEVEL, item.level);
            values.put(KEY_ITEM_STATUS, item.Status);


            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_ITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Item> getAllItems() {
        ArrayList<Item> items = new ArrayList<>();

        String ITEMS_SELECT_QUERY = String.format("SELECT * FROM %s ", TABLE_ITEMS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                 Item newItem = new Item("");
                    newItem.Name = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME));
                    String sdate = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DUE_DATE));
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    newItem.dueDate = format.parse(sdate);
                    newItem.Notes = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NOTES));
                    newItem.level = cursor.getString(cursor.getColumnIndex(KEY_ITEM_LEVEL));
                    newItem.Status = cursor.getString(cursor.getColumnIndex(KEY_ITEM_STATUS));

                    items.add(newItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }

    public int updateItem(Item item, String oldname) {

        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, item.Name);
        values.put(KEY_ITEM_DUE_DATE, dateFormat.format(item.dueDate));
        values.put(KEY_ITEM_NOTES, item.Notes);
        values.put(KEY_ITEM_LEVEL, item.level);
        values.put(KEY_ITEM_STATUS, item.Status);

        return db.update(TABLE_ITEMS, values, KEY_ITEM_NAME + " = ?",
                new String[] { String.valueOf(oldname) });
    }

    public void deleteItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_ITEMS, KEY_ITEM_NAME + " = ?", new String[] {item.Name});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

}
