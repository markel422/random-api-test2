package com.example.mike0.w3d5_quiz2;

import android.provider.BaseColumns;

/**
 * Created by mike0 on 8/6/2017.
 */

public class FeedReaderContract {

    public FeedReaderContract() {

    }

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "random_api";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_IMAGE = "image";
    }
}
