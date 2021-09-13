package com.sogeum0310.javamemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.sogeum0310.javamemo.MemoData.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MemodetailActivity extends AppCompatActivity {

    String id, content, date;
    int feel, arlam;
    TextView cancel, submit, txdate, tomorrow, testid;
    RadioGroup radio;
    RadioButton go, nogo, complete;
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
        radio = findViewById(R.id.radio);
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
        if(feel ==1){
            radio.check(R.id.go);
        }else if (feel ==2){
            radio.check(R.id.nogo);
        }else {
            radio.check(R.id.compl);
        }
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

        //기한 설정
        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                txdate.setText(String.format("%d-%d-%d", i, i1 + 1, i2));

            }
        };
        txdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MemodetailActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
            }
        });

        //취소
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //삭제버튼
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL(delete);


                finish();
            }
        });

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (id){
                    case R.id.go:
                        feel = 1;
                        break;
                    case R.id.nogo:
                        feel = 2;
                        break;
                    case R.id.compl:
                        feel = 3;
                        break;

                }
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