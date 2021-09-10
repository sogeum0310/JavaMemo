package com.sogeum0310.javamemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.sogeum0310.javamemo.MemoData.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MemodetailActivity extends AppCompatActivity {

    String id, content, date;
    int feel, arlam;
    TextView cancel, submit, txdate, go, nogo, complete, tomorrow, testid;
    EditText edmemo;
    Button del;
    Switch arlamsw;
    Calendar calendar;
    ArrayList<MemoArray> list = new ArrayList<>();
    private DateFormat format = new SimpleDateFormat("YYYY-M-dd");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memodetail);

        cancel = findViewById(R.id.cancel);
        submit = findViewById(R.id.summit);
        txdate = findViewById(R.id.date);
        go = findViewById(R.id.go);
        nogo = findViewById(R.id.nogo);
        complete = findViewById(R.id.compl);
        tomorrow = findViewById(R.id.tomorrow);
        edmemo = findViewById(R.id.edmemo);
        arlamsw = findViewById(R.id.arlam);
        del = findViewById(R.id.del);
        testid = findViewById(R.id.testid);


        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        DatabaseHelper helper;
        SQLiteDatabase db;
        helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();

        String select = "select " + Memolist.content + ", " + Memolist.feel +" , " + Memolist.date + ", " + Memolist.arlam +  ", id from " + Memolist.tablename
                + " where id = " + id;
        select(select);

        String delete = "delete from " + Memolist.tablename + " where ID= " + id;

        System.out.println(select);
        testid.setText(id);
        content = list.get(0).getContent();
        edmemo.setText(content);
        date = list.get(0).getDate();
        txdate.setText(date);
        feel = list.get(0).getFeel();
        arlam = list.get(0).getArlam();
        if(arlam == 1){
            arlamsw.setChecked(true);
        }

        arlamsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (arlamsw.isChecked()) {
                    System.out.println("check");
                } else {

                }
            }
        });

        txdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL(delete);


                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = edmemo.getText().toString();
                if (arlamsw.isChecked()) {
                    arlam = 1;

                } else {
                    arlam = 0;
                }
                date = txdate.getText().toString();

                String update = "UPDATE " + Memolist.tablename + " SET " + Memolist.content + " = '" + content + "', " + Memolist.date + " = '" + date
                        + "', " + Memolist.feel + " = '" + feel + "', " + Memolist.arlam + " = '" + arlam + "' WHERE ID = " + id;
                db.execSQL(update);
                System.out.println("11111111111111111111111111111111" + id + update);
                finish();
            }
        });

        edmemo.setText(content);


    }

    private void select(String sql) {
        Cursor c;
        DatabaseHelper helper;
        SQLiteDatabase db;
        helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();
        c = db.rawQuery(sql, null);
        c.moveToFirst();
        list.add(new MemoArray(c.getString(0), c.getInt(1), c.getString(2), c.getInt(3), c.getInt(4)));

    }
}