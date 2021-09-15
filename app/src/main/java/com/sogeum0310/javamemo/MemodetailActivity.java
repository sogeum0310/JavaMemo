package com.sogeum0310.javamemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.widget.TimePicker;

import com.sogeum0310.javamemo.MemoData.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MemodetailActivity extends AppCompatActivity {

    String id, content, date, stime;
    int feel, arlam;
    TextView cancel, submit, txdate, tomorrow, testid, time;
    RadioGroup radio;
    RadioButton go, nogo, complete;
    EditText edmemo;
    Button del;
    Switch arlamsw;
    Calendar calendar;
    ArrayList<MemoArray> list = new ArrayList<>();
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private final DateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd hh-mm");


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
        time = findViewById(R.id.time);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        DatabaseHelper helper;
        SQLiteDatabase db;
        helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();

        String select = "select " + Memolist.content + ", " + Memolist.feel +" , " + Memolist.date + ", " + Memolist.arlam +  ", id, arlamtime from " + Memolist.tablename
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
        time.setText("");

        //알람
        arlam = list.get(0).getArlam();

        if(arlam == 1){
            arlamsw.setChecked(true);
            time.setText(list.get(0).getArlamtime());
            time.setVisibility(View.VISIBLE);
            stime = time.getText().toString();
        }

        arlamsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
                Calendar c = Calendar.getInstance(tz);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                try {
                    Date d = format.parse(date);
                    c.setTime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (arlamsw.isChecked()) {
                    TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            if (timePicker.isShown()){
                                c.set(Calendar.HOUR_OF_DAY, i);
                                c.set(Calendar.MINUTE, i1);
                                time.setText(i+" 시 "+ i1+" 분");
                                Date d = c.getTime();
                                stime = timeformat.format(d);
                            }
                        }
                    };


                    TimePickerDialog dialog = new TimePickerDialog(MemodetailActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, listener,hour,minute,false);

                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            arlamsw.setChecked(true);
                        }
                    });
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            arlamsw.setChecked(false);
                        }
                    });


                    dialog.setTitle("시간 설정");
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                    dialog.show();
                    time.setVisibility(View.VISIBLE);
                } else {
                    time.setText("");
                    time.setVisibility(View.GONE);
                }
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
                Calendar c = Calendar.getInstance(tz);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                try {
                    Date d = format.parse(date);
                    c.setTime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (arlamsw.isChecked()) {
                    System.out.println("check");
                    TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            if (timePicker.isShown()){
                                c.set(Calendar.HOUR_OF_DAY, i);
                                c.set(Calendar.MINUTE, i1);
                                time.setText(i+" 시 "+ i1+" 분");
                                Date d = c.getTime();
                                stime = timeformat.format(d);
                            }
                        }
                    };
                    TimePickerDialog dialog = new TimePickerDialog(MemodetailActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, listener,hour,minute,false);
                    dialog.setTitle("시간 설정");
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.show();
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
                String update;
                date = txdate.getText().toString();
                content = edmemo.getText().toString();
                if (arlamsw.isChecked()) {
                    arlam = 1;
                    update = "UPDATE " + Memolist.tablename + " SET " + Memolist.content + " = '" + content + "', " + Memolist.date + " = '" + date
                            + "', " + Memolist.feel + " = '" + feel + "', " + Memolist.arlam + " = '" + arlam + "', " + Memolist.arlamtime + " = '" + stime +"' WHERE ID = " + id;
                } else {
                    arlam = 0;
                    update = "UPDATE " + Memolist.tablename + " SET " + Memolist.content + " = '" + content + "', " + Memolist.date + " = '" + date
                            + "', " + Memolist.feel + " = '" + feel + "', " + Memolist.arlam + " = '" + arlam + "', " + Memolist.arlamtime + " = " + null +" WHERE ID = " + id;
                }

                db.execSQL(update);
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
        list.add(new MemoArray(c.getString(0), c.getInt(1), c.getString(2), c.getInt(3), c.getInt(4),c.getString(5)));

    }
}