package com.leonard.app58;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MySqliteHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "computer.db";
    private static final String TABLE_NAME = "computers";

    private static final String  COLUMN_ID = "id";
    private static final String  COLUMN_COMPUTER_NAME = "computerName";
    private static final String  COLUMN_COMPUTER_TYPE = "computerType";


    String CREATE_COMPUTER_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY, " + COLUMN_COMPUTER_NAME + " TEXT, "
            + COLUMN_COMPUTER_TYPE + " TEXT" + ")";

    public MySqliteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_COMPUTER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    //Adding VAlues
    public void addComputer (Computer computer) {

        SQLiteDatabase database = MySqliteHandler.this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_COMPUTER_NAME, computer.getComputerName());
        values.put(COLUMN_COMPUTER_TYPE, computer.getComputerType());

        database.insert(TABLE_NAME, null, values);
        database.close();
    }

    public Computer getComputer ( int id) {

        SQLiteDatabase database = MySqliteHandler.this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_NAME, new String[] { COLUMN_ID, COLUMN_COMPUTER_NAME, COLUMN_COMPUTER_TYPE},
                COLUMN_ID + "=?", new String[] {String.valueOf(id)},
                null, null, null);


        if (cursor != null) {

            cursor.moveToFirst();
        }

        Computer computer = new Computer(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        return computer;
    }

    public List<Computer> getAllComputers () {

        List<Computer> computerList = new ArrayList<>();

        String selectAllQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase database = MySqliteHandler.this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectAllQuery, null);

        if (cursor.moveToFirst()) {

            do {

                Computer computer = new Computer();
                computer.setId(Integer.parseInt(cursor.getString(0)));
                computer.setComputerName(cursor.getString(1));
                computer.setComputerType(cursor.getString(2));

                computerList.add(computer);

            } while (cursor.moveToNext());
        }

        return computerList;
    }


    public int updateComputer (Computer computer) {

        SQLiteDatabase database = MySqliteHandler.this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPUTER_NAME, computer.getComputerName());
        values.put(COLUMN_COMPUTER_TYPE, computer.getComputerType());

        return database.update(TABLE_NAME, values, COLUMN_ID + "=?",
                new String[] {String.valueOf(computer.getId())});

    }

    public void deleteComputer (Computer computer) {

        SQLiteDatabase database = MySqliteHandler.this.getWritableDatabase();
        database.delete(TABLE_NAME, COLUMN_ID + "=?",
                new String[] {String.valueOf(computer.getId())});

        database.close();
    }

    public int getComputerCount () {

        SQLiteDatabase database = MySqliteHandler.this.getReadableDatabase();
        String computerCount = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = database.rawQuery(computerCount, null);
        cursor.close();

        return cursor.getCount();

    }
}
