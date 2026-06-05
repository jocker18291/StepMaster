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
    private static String steps = "steps";

    public dbConnect(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + dbTable + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + emailAddress + " TEXT, " + password + " TEXT, " + steps + " INTEGER)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + dbTable);
        onCreate(db);
    }

    public void setSteps(String email, int Steps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(steps, Steps);
        db.update(dbTable, values, emailAddress + " = ?", new String[]{ email });
    }

    public int getSteps(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int currentSteps = 0;

        String query = "SELECT " + steps + " FROM " + dbTable + " WHERE " +emailAddress + " = ?";
        Cursor cursor = db.rawQuery(query, new String[] { email });

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
        values.put(steps, 0);
        db.insert(dbTable, null, values);
    }
}
