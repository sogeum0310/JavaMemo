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
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;

import com.sogeum0310.javamemo.MemoData.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialCalendarView materialCalendarView;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private TextView test;
    private RecyclerView recyclerView;
    private MemoAdapter adapter;
    private EditText added;
    private Button addbt;
    private LinearLayout addll;
    private FrameLayout mainlay;
    private InputMethodManager inputMethodManager;
    private ArrayList<MemoArray> list = new ArrayList<>();
    private String selday, today;
    TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
    private final Calendar calendar = Calendar.getInstance(tz);
    private Cursor c;
    private Date ddd;
    Collection<CalendarDay> collection = new ArrayList<>();
    private long backBtnTime = 0;
    GestureDetector gestureDetector;
    Swipelistener swipelistener;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DebugDB.getAddressLog();

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.layout.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.layout.fab_close);

        fab = findViewById(R.id.fabMain);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        test = findViewById(R.id.test);
        added = findViewById(R.id.added);
        addbt = findViewById(R.id.addbtn);
        addll = findViewById(R.id.addll);
        mainlay = findViewById(R.id.mainlayout);
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
        //helper.getWritableDatabase();에 oncreate 포함

//        calendar.add(Calendar.MONTH,+1);
        today = format.format(calendar.getTime());
        selday = today;
        test.setText(today);

        EventDeco eventDeco = new EventDeco(Color.parseColor("#000000"), Collections.singletonList(CalendarDay.from(calendar)));

        String sql2 = "select " + Memolist.content + ", " + Memolist.feel + ", " + Memolist.arlam + " , " + Memolist.date + ", id from " + Memolist.tablename + "order by id";
//                +" where "+Memolist.date +" = '" + today+"'";
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                test.setText(format.format(date.getDate()));
                ddd = date.getDate();
                selday = format.format(ddd);
                String sql2 = "select " + Memolist.content + ", " + Memolist.feel + ", " + Memolist.arlam + " , " + Memolist.date + ", id from " + Memolist.tablename
                        + " where " + Memolist.date + " = '" + selday + "'";
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

        //메모 클릭 이벤트
        adapter.setOnItemClickListener(new MemoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                int id = list.get(position).getId();

                System.out.println(id);

                Intent intent = new Intent(MainActivity.this, MemodetailActivity.class);
                intent.putExtra("id", id + "");
                startActivity(intent);
            }
        });
//플로팅 버튼
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                added.requestFocus();
                materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();

                anim();
//                db.execSQL(insert);
            }
        });

        //포커스 변경확인
        added.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (added.hasFocus()) {
                    addll.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.GONE);
                    inputMethodManager.showSoftInput(added, 0);

                } else {
                    addll.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    added.setText("");
                    inputMethodManager.hideSoftInputFromWindow(added.getWindowToken(), 0);
                    materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                }


            }
        });

        //메모중 다른곳클릭
        addll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                added.clearFocus();
            }
        });

        //메모 작성후 버튼
        addbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String con = added.getText().toString();
                if (added.length() != 0) {

                    String insert = "INSERT INTO " + Memolist.tablename + " VALUES (" + null + ", " + "'" + con + "'" + " , '" + test.getText().toString() + "', 1, 0, " + null + " )";
                    c = db.rawQuery(insert, null);
                    c.moveToLast();
                    String sql = "select " + Memolist.content + ", " + Memolist.feel + ", " + Memolist.arlam + " , " + Memolist.date + ", id, arlamtime from " + Memolist.tablename;
                    c = db.rawQuery(sql, null);
                    c.moveToLast();
                    list.add(new MemoArray(c.getString(0), c.getInt(1), c.getString(2), c.getInt(3), c.getInt(4), c.getString(5)));

                    try {
                        ddd = format.parse(selday);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    CalendarDay calendarDay = CalendarDay.from(ddd);
                    collection.add(calendarDay);
                    materialCalendarView.addDecorator(new EventDeco(Color.parseColor("#000000"), collection));
                    added.clearFocus();
                    adapter.notifyDataSetChanged();

                }

            }
        });


        //달력초기 세팅
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(SUNDAY)
                .setMinimumDate(CalendarDay.from(2018, 0, 1))
                .setMaximumDate(CalendarDay.from(2040, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.removeDecorators();

        swipelistener = new Swipelistener(mainlay);

//        gestureDetetor = new GestureDetector(this, mGestureDetector);

        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent evnent) {
                return gestureDetector.onTouchEvent(evnent);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseHelper helper;
        SQLiteDatabase db;
        helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();

        collection.clear();
        String datesql = "Select distinct date from " + Memolist.tablename;
        Cursor cursor = db.rawQuery(datesql, null);
        CalendarDay calendarDay = null;

        try {
            if (cursor.moveToFirst()) {
                do {
                    MemoArray memoArray = new MemoArray();
                    memoArray.setDate(cursor.getString(0));
                    ddd = format.parse(memoArray.getDate());
                    calendar.setTime(ddd);
                    calendarDay = CalendarDay.from(calendar);
                    collection.add(calendarDay);
                } while (cursor.moveToNext());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        String sql = "select " + Memolist.content + ", " + Memolist.feel + ", " + Memolist.arlam + " , " + Memolist.date + ", id from " + Memolist.tablename
                + " where " + Memolist.date + " = '" + selday + "'";
        select(sql);

        materialCalendarView.removeDecorators();
        materialCalendarView.addDecorators(new todayDeco(),
                new EventDeco(Color.parseColor("#000000"), collection));

        adapter.notifyDataSetChanged();

    }


    //날짜 데코
    public class EventDeco implements DayViewDecorator {

        private int color;
        private HashSet<CalendarDay> dates;

        public EventDeco(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(10, color));
        }
    }

    class todayDeco implements DayViewDecorator {
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
//            view.addSpan(new StyleSpan(Typeface.BOLD));
//            view.addSpan(new RelativeSizeSpan(1.4f));
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }

        public void setDate(Date date) {
            this.date = CalendarDay.from(date);
        }
    }


    //db조회
    private void select(String sql) {
        DatabaseHelper helper;
        SQLiteDatabase db;
        helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();
        c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
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

    //플로팅버튼
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fabMain:
                anim();
                break;
//            case R.id.fab1:
//                anim();
//                break;
            case R.id.fab2:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                anim();

                break;
        }
    }

    //플로팅버튼 애니메이션
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

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (added.requestFocus()) {
            added.clearFocus();
            addll.setVisibility(View.GONE);
        } else if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }


    }

    private class Swipelistener implements View.OnTouchListener {
        Swipelistener(View view) {
            int threshold = 100;
            int velocity = 100;

            GestureDetector.SimpleOnGestureListener listener =
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onDown(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            float xDiff = e2.getX() - e1.getX();
                            float yDiff = e2.getY() - e1.getY();

                            try {
                                if (Math.abs(xDiff) > Math.abs(yDiff)) {
                                    if (Math.abs(xDiff) > threshold && Math.abs(velocityY) > velocity) {
                                        if (xDiff > 0) {
//                                            Toast.makeText(getApplicationContext(), "Swipe right", Toast.LENGTH_SHORT).show();
                                        } else {
//                                            Toast.makeText(getApplicationContext(), "Swipe left", Toast.LENGTH_SHORT).show();
                                        }
                                        return true;
                                    }
                                } else {
                                    if (Math.abs(yDiff) > threshold && Math.abs(velocityY) > velocity) {
                                        if (yDiff > 0) {
                                            //down
//                                            Toast.makeText(getApplicationContext(), "Swipe down", Toast.LENGTH_SHORT).show();
                                            materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();

                                        } else {
                                            //up
//                                            Toast.makeText(getApplicationContext(), "Swipe up", Toast.LENGTH_SHORT).show();
                                            materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                                        }
                                        return true;

                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return false;
                        }
                    };
            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
    }
}