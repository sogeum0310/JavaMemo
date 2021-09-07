package com.sogeum0310.javamemo;

import android.provider.BaseColumns;

public class MemoData {
    public static final class Memolist implements BaseColumns{
        public static final String tablename = "memo";
        public static final String content= "content";
        public static final String date = "date";
        public static final String feel = "feel";
        public static final String arlam = "arlam";
    }

}
