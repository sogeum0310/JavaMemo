package com.sogeum0310.javamemo;

import static java.util.Calendar.SUNDAY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Debug;
import android.provider.Settings;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.sogeum0310.javamemo.MemoData.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialCalendarView materialCalendarView;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private DateFormat format = new SimpleDateFormat("YYYY-M-dd");
    private TextView test;
    private RecyclerView recyclerView;
    private MemoAdapter adapter;
    private EditText added;
    private Button addbt;
    private LinearLayout addll;
    private InputMethodManager inputMethodManager;
    private ArrayList<MemoArray> list = new ArrayList<MemoArray>();
    private String selday, selday2, today, today2;
    private Calendar calendar = Calendar.getInstance();
    private Cursor c;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DebugDB.getAddressLog();

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.layout.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.layout.fab_close);

        fab = (FloatingActionButton) findViewById(R.id.fabMain);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        test = findViewById(R.id.test);
        added = findViewById(R.id.added);
        addbt = findViewById(R.id.addbtn);
        addll = findViewById(R.id.addll);
        materialCalendarView = findViewById(R.id.calendarView);


        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        //db가져오기===================================================================================
        DatabaseHelper helper;
        SQLiteDatabase db;
        helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();
//        helper.onCreate(db);

//        calendar.add(Calendar.MONTH,+1);
        today = format.format(calendar.getTime());
        selday = today;
        test.setText(today);


        String sql2 = "select " + Memolist.content +", "+Memolist.feel+", "+Memolist.arlam +" , "+ Memolist.date + ", id from "+ Memolist.tablename +"order by id";
//                +" where "+Memolist.date +" = '" + today+"'";

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                test.setText(date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDay());
                selday = date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDay();
                selday2 = date.getYear()+"-"+(date.getMonth()+1)+"-"+(date.getDay()+1);
                String sql2 = "select " + Memolist.content +", "+Memolist.feel+", "+Memolist.arlam +" , "+ Memolist.date + ", id from "+ Memolist.tablename
                        +" where "+Memolist.date +" = '" + selday+"'";
                list.clear();
               select(sql2);
               adapter.notifyDataSetChanged();
            }
        });
        materialCalendarView.setSelectedDate(CalendarDay.today());


        //리사이클러뷰 시작 =============================================================================
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new MemoAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //플로팅 버튼
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                added.requestFocus();
                materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();

//                anim();
//                db.execSQL(insert);
            }
        });

        //메모 클릭 이벤트
        adapter.setOnItemClickListener(new MemoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                int id = list.get(position).getId();

                System.out.println(id);

                Intent intent = new Intent(MainActivity.this, MemodetailActivity.class);
                intent.putExtra("id",id+"");
                startActivity(intent);
            }
        });


        //플로팅 버튼
        added.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (added.hasFocus()){
                    addll.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.GONE);
                    inputMethodManager.showSoftInput(added, 0);
                    added.clearFocus();
                }
            }
        });

        //메모중 다른곳클릭
        addll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                added.clearFocus();
                addll.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                inputMethodManager.hideSoftInputFromWindow(added.getWindowToken(),0);
                materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
            }
        });

        //메모 작성후 버튼
        addbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addll.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                String con = added.getText().toString();
                if (added.length() != 0){

                    String insert = "INSERT INTO "+Memolist.tablename+" VALUES ("+ null +", "+"'"+con+ "'"+" , '"+test.getText().toString() +"', 1, 0)";
                    c =db.rawQuery(insert, null);
                    c.moveToLast();
                    System.out.println("222222222222222222222222222222222222222222222222222222" +insert);
                    String sql = "select " + Memolist.content +", "+Memolist.feel+", "+Memolist.arlam +" , "+ Memolist.date + ", id from "+ Memolist.tablename;
//                            +" order by date desc limit 1";
                    c= db.rawQuery(sql,null);
                    c.moveToLast();
                    System.out.println(sql);
                    list.add(new MemoArray(c.getString(0), c.getInt(1), c.getString(2), c.getInt(3), c.getInt(4)));
                    adapter.notifyDataSetChanged();
                }
                added.setText("");
                added.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(added.getWindowToken(),0);
                materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
            }
        });



        //달력=====================================================================================

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(SUNDAY)
                .setMinimumDate(CalendarDay.from(2018,0,1))
                .setMaximumDate(CalendarDay.from(2040,11,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.addDecorators(
//                new sundayDeco(),
                new todayDeco()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        String sql = "select " + Memolist.content +", "+Memolist.feel+", "+Memolist.arlam +" , "+ Memolist.date + ", id from "+ Memolist.tablename
                +" where "+Memolist.date +" = '" + selday+"'" ;
        select(sql);
        adapter.notifyDataSetChanged();

    }

    static class sundayDeco implements DayViewDecorator {
        private final Calendar calendar = Calendar.getInstance();

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            return weekday == SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view .addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    private void select(String sql){
        DatabaseHelper helper;
        SQLiteDatabase db;
        helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();
        c = db.rawQuery(sql, null);
        if (c.moveToFirst()){
            do {
                MemoArray memoArray = new MemoArray();

                memoArray.setContent(c.getString(0));
                memoArray.setFeel(c.getInt(1));
                memoArray.setDate(c.getString(2));
                memoArray.setArlam(c.getInt(3));
                memoArray.setId(c.getInt(4));
                list.add(memoArray);
            } while (c.moveToNext());
        }
    }

    class todayDeco implements DayViewDecorator{
        private CalendarDay date;

        public todayDeco() {
            date = CalendarDay.today();
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.4f));
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }

        public void setDate(Date date) {
            this.date = CalendarDay.from(date);
        }
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