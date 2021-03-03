package com.example.panel1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class AddActivity extends AppCompatActivity {


    Button btnList2, btnLog;
    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        sqLiteHelper = new SQLiteHelper(this, "PanelDB.sqlite", null, 1);

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS panel(Id INTEGER PRIMARY KEY AUTOINCREMENT, nume VARCHAR, promotie VARCHAR, clasa VARCHAR, image BLOB)");

        btnLog = (Button) findViewById(R.id.btnLog);
        btnList2 = (Button) findViewById(R.id.btnList2);


        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });

        btnList2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this, SecondPanel.class);
                startActivity(intent);
            }
        });

    }
}



