package com.example.panel1;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.GridView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
public class SecondPanel extends AppCompatActivity{

    GridView gridView;
    ArrayList<panel> list;
    PanelListAdapter adapter = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel_list_activity);

        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new PanelListAdapter(this, R.layout.panel_items, list);
        gridView.setAdapter(adapter);

        // get all data from sqlite
        Cursor cursor = AddActivity.sqLiteHelper.getData("SELECT * FROM PANEL");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nume = cursor.getString(1);
            String promotie = cursor.getString(2);
            String clasa = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            list.add(new panel(nume, promotie,clasa, image, id));
        }
        adapter.notifyDataSetChanged();

    }



}