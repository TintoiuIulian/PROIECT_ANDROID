package com.example.panel1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;



public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String nume, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nume,  factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String nume, String promotie,String clasa, byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO panel VALUES (NULL,?, ?, ? ,?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, nume);
        statement.bindString(2, promotie);
        statement.bindString(3, clasa);
        statement.bindBlob(4, image);

        statement.executeInsert();
    }

    public void updateData(String nume, String promotie,String clasa, byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE panel SET nume = ?, promotie = ?, clasa = ?, image = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, nume);
        statement.bindString(2, promotie);
        statement.bindString(3, clasa);
        statement.bindBlob(4, image);
        statement.bindDouble(5, (double)id);

        statement.execute();
        database.close();
    }

    public  void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM panel WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}