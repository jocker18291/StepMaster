package com.example.fitnessstepper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbConnect extends SQLiteOpenHelper {

    private static String dbName = "stepMasterManager";
    private static String dbTable = "stepMasterUsers";
    private static int dbVersion = 1;

    private static String ID = "id";
    private static String emailAddress = "emailAddress";
    private static String password = "password";


    private static String dbTableHistory = "UsersStepHistory";
    private static String stepDate = "stepDate";
    private static String steps = "steps";

    public dbConnect(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + dbTable + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + emailAddress + " TEXT, " + password + " TEXT)";
        String queryHistory = "CREATE TABLE " + dbTableHistory + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + emailAddress + " TEXT, " + stepDate + " TEXT, " + steps + " INTEGER)";

        db.execSQL(query);
        db.execSQL(queryHistory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + dbTable);
        db.execSQL("DROP TABLE IF EXISTS " + dbTableHistory);
        onCreate(db);
    }

    public void setSteps(String email, String date, int Steps) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT " + steps + " FROM " + dbTableHistory + " WHERE " + emailAddress + " = ? AND " + stepDate + " = ?";
        Cursor cursor = db.rawQuery(query, new String[] { email, date });
        ContentValues values = new ContentValues();
        values.put(steps, Steps);
        values.put(emailAddress, email);
        values.put(stepDate, date);


        if(cursor.getCount() > 0) {
            db.update(dbTableHistory, values, emailAddress + " = ? AND " + stepDate + " = ?", new String[]{ email, date });
        } else {
            db.insert(dbTableHistory, null, values);
        }
        cursor.close();

    }

    public int getSteps(String email, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        int currentSteps = 0;

        String query = "SELECT " + steps + " FROM " + dbTableHistory + " WHERE " +emailAddress + " = ? AND " + stepDate + " = ?";
        Cursor cursor = db.rawQuery(query, new String[] { email, date });

        if(cursor.moveToFirst()) {
            currentSteps = cursor.getInt(0);
        }
        cursor.close();
        return currentSteps;
    }

    public boolean checkPassword(String email, String inputPassword) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + password + " FROM " + dbTable + " WHERE " + emailAddress + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{ email });

        boolean passwordMatch = false;

        if(cursor.moveToFirst()) {
            String savedPassword = cursor.getString(0);

            if(savedPassword.equals(inputPassword)) {
                passwordMatch = true;
            }
        }

        return passwordMatch;
    }

    public boolean searchUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + dbTable + " WHERE " + emailAddress + " = ?";
        String[] selectionQuery = { email };

        Cursor cursor = db.rawQuery(query, selectionQuery);

        int count = cursor.getCount();

        cursor.close();

        if(count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void addUser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(emailAddress, user.getEmailAddress());
        values.put(password, user.getPassword());
        db.insert(dbTable, null, values);
    }
}
