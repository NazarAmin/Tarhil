package com.example.higooo.tarhil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SQLHelperClass extends SQLiteOpenHelper {

    private static final String DB_NAME = "Trip_DB";
    private static final int DB_VERSION = 1;

    /*
    int AvgS;
int MaxS;
int DisS;
String StartS;
String EndS;
int ID;
     */

    public SQLHelperClass(Context context) {
        super(context, DB_NAME, null, DB_VERSION);


    }
    private static final String TABLE_NAME = "Trips_TB";
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create Table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "AvgS INTEGER, DisS INTEGER, MaxS INTEGER, StartS TEXT, EndS TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql22 = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql22);
    }

    public void addEmployee(Trip emp){

        ContentValues values = new ContentValues();
        values.put("AvgS", emp.getAvgS());
        values.put("DisS", emp.getDisS());
        values.put("MaxS", emp.getMaxS());
        values.put("StartS", emp.getStartS());
        values.put("EndS", emp.getEndS());


        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NAME,null,values);
        db.close();
    }
    public void updateemployee(Trip emp){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("AvgS", emp.getAvgS());
        values.put("DisS", emp.getDisS());
        values.put("MaxS", emp.getMaxS());
        values.put("StartS", emp.getStartS());
        values.put("EndS", emp.getEndS());

        db.update(TABLE_NAME, values, "id=?" + new String[]{String.valueOf(emp.getID())},null);
        db.close();
    }

    public ArrayList<Trip> getEmployees(){
        ArrayList<Trip> empList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()){
            do{

                Trip employee = new Trip();
                employee.setID(cursor.getInt(0));
                employee.setAvgS(cursor.getInt(1));
                employee.setMaxS(cursor.getInt(2));
                employee.setDisS(cursor.getInt(3));
                employee.setStartS(cursor.getString(4));
                employee.setEndS(cursor.getString(5));



                empList.add(employee);

            }while(cursor.moveToNext());
        }
        return empList;
    }

}

