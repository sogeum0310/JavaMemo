package com.sogeum0310.javamemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.sogeum0310.javamemo.MemoData.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MaterialCalendarView materialCalendarView;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    DateFormat format = new SimpleDateFormat("YYYY-MM-dd");
    TextView test;
    private RecyclerView recyclerView;
    MemoAdapter adapter;
    private EditText added;
    private Button addbt;
    private LinearLayout addll;
    InputMethodManager inputMethodManager;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.layout.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.layout.fab_close);

        fab = (FloatingActionButton) findViewById(R.id.fabMain);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        test = findViewById(R.id.test);
        added = findViewById(R.id.added);
        addbt = findViewById(R.id.addbtn);
        addll = findViewById(R.id.addll);


        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        //db가져오기===================================================================================
        DatabaseHelper helper;
        SQLiteDatabase db;
        helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();
//        helper.onCreate(db);

        long todayl = System.currentTimeMillis();

        //플로팅 버튼
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                added.requestFocus();

//                anim();
//                db.execSQL(insert);
            }
        });

        added.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (added.hasFocus()){
                    addll.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.GONE);
                    inputMethodManager.showSoftInput(added, 0);
                }
            }
        });

        addll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                added.clearFocus();
                addll.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                inputMethodManager.hideSoftInputFromWindow(added.getWindowToken(),0);
            }
        });


        addbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addll.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                String con = added.getText().toString();
                if (added.length() != 0){
                    db.execSQL("INSERT INTO "+Memolist.tablename+" VALUES ("+ null +", "+"'"+con+ "'"+" , "+ todayl+",1, 0)");
                }
                added.setText("");
                added.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(added.getWindowToken(),0);
            }
        });

        String sql = "select " + Memolist.content +", "+Memolist.feel+", "+"id, "+Memolist.arlam +" from "+ Memolist.tablename;
//        Cursor c = db.query("memo", null,null,null,null,null,null,null);
        Cursor c = db.rawQuery(sql,null);


        //리사이클러뷰 시작 =============================================================================
        recyclerView = findViewById(R.id.recyclerview);
        ArrayList<MemoData> list = new ArrayList<>();
        adapter = new MemoAdapter(this, c);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //달력 시작=====================================================================================

        materialCalendarView = findViewById(R.id.calendarView);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                test.setText(date.getYear()+"-"+date.getMonth()+"-"+date.getDay());
            }
        });

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2018,0,1))
                .setMaximumDate(CalendarDay.from(2040,11,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.setSelectedDate(CalendarDay.today());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
//            case R.id.fabMain:
//                anim();
//                Toast.makeText(this, "Floating Action Button", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.fab1:
                anim();
                Toast.makeText(this, "Button1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab2:
                anim();
                Toast.makeText(this, "Button2", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

}