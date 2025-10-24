package com.app.socialmediacontentmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "social_content_manager.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_CONTENT_TYPES = "content_types";
    public static final String TABLE_CONTENT_ITEMS = "content_items";

    // Category Table Columns
    public static final String KEY_CATEGORY_ID = "id";
    public static final String KEY_CATEGORY_NAME = "name";
    public static final String KEY_CATEGORY_ICON = "icon_res_id";

    // ContentType Table Columns
    public static final String KEY_CONTENT_TYPE_ID = "id";
    public static final String KEY_CONTENT_TYPE_CATEGORY_ID = "category_id";
    public static final String KEY_CONTENT_TYPE_NAME = "name";

    // ContentItem Table Columns
    public static final String KEY_CONTENT_ITEM_ID = "id";
    public static final String KEY_CONTENT_ITEM_TYPE_ID = "type_id";
    public static final String KEY_CONTENT_ITEM_TITLE = "title";
    public static final String KEY_CONTENT_ITEM_DESCRIPTION = "description";
    public static final String KEY_CONTENT_ITEM_CREATION_DATE = "creation_date";
    public static final String KEY_CONTENT_ITEM_LAST_UPDATED = "last_updated";
    public static final String KEY_CONTENT_ITEM_TAGS = "tags";
    public static final String KEY_CONTENT_ITEM_STATUS = "status";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES +
                "(" +
                KEY_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_CATEGORY_NAME + " TEXT NOT NULL," +
                KEY_CATEGORY_ICON + " INTEGER" +
                ")";

        String CREATE_CONTENT_TYPES_TABLE = "CREATE TABLE " + TABLE_CONTENT_TYPES +
                "(" +
                KEY_CONTENT_TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_CONTENT_TYPE_CATEGORY_ID + " INTEGER," +
                KEY_CONTENT_TYPE_NAME + " TEXT NOT NULL," +
                "FOREIGN KEY(" + KEY_CONTENT_TYPE_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + KEY_CATEGORY_ID + ")" +
                ")";

        String CREATE_CONTENT_ITEMS_TABLE = "CREATE TABLE " + TABLE_CONTENT_ITEMS +
                "(" +
                KEY_CONTENT_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_CONTENT_ITEM_TYPE_ID + " INTEGER," +
                KEY_CONTENT_ITEM_TITLE + " TEXT NOT NULL," +
                KEY_CONTENT_ITEM_DESCRIPTION + " TEXT," +
                KEY_CONTENT_ITEM_CREATION_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                KEY_CONTENT_ITEM_LAST_UPDATED + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                KEY_CONTENT_ITEM_TAGS + " TEXT," +
                KEY_CONTENT_ITEM_STATUS + " INTEGER DEFAULT 0," +
                "FOREIGN KEY(" + KEY_CONTENT_ITEM_TYPE_ID + ") REFERENCES " + TABLE_CONTENT_TYPES + "(" + KEY_CONTENT_TYPE_ID + ")" +
                ")";

        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_CONTENT_TYPES_TABLE);
        db.execSQL(CREATE_CONTENT_ITEMS_TABLE);

        // Insert initial data
        initializeData(db);
    }

    private void initializeData(SQLiteDatabase db) {
        // Insert default categories
        String[] categories = {"YouTube", "Instagram", "Twitter", "Facebook", "Telegram", "Snapchat"};

        for (String category : categories) {
            ContentValues values = new ContentValues();
            values.put(KEY_CATEGORY_NAME, category);
            db.insert(TABLE_CATEGORIES, null, values);
        }

        // Insert content types for each category
        // YouTube (category_id = 1)
        insertContentType(db, 1, "YouTube Post Ideas");
        insertContentType(db, 1, "YouTube Shorts Ideas");
        insertContentType(db, 1, "YouTube Long Form Videos");

        // Instagram (category_id = 2)
        insertContentType(db, 2, "Instagram Post Ideas");
        insertContentType(db, 2, "Instagram Reels Ideas");
        insertContentType(db, 2, "Instagram Stories Ideas");

        // Twitter (category_id = 3)
        insertContentType(db, 3, "Tweet Ideas");
        insertContentType(db, 3, "Thread Ideas");
        insertContentType(db, 3, "Twitter Spaces Ideas");

        // Facebook (category_id = 4)
        insertContentType(db, 4, "Facebook Post Ideas");
        insertContentType(db, 4, "Facebook Story Ideas");
        insertContentType(db, 4, "Facebook Reel Ideas");

        // Telegram (category_id = 5)
        insertContentType(db, 5, "Telegram Post Ideas");
        insertContentType(db, 5, "Telegram Channel Ideas");
        insertContentType(db, 5, "Telegram Story Ideas");

        // Snapchat (category_id = 6)
        insertContentType(db, 6, "Snapchat Snap Ideas");
        insertContentType(db, 6, "Snapchat Story Ideas");
        insertContentType(db, 6, "Snapchat Spotlight Ideas");
    }

    private void insertContentType(SQLiteDatabase db, int categoryId, String name) {
        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT_TYPE_CATEGORY_ID, categoryId);
        values.put(KEY_CONTENT_TYPE_NAME, name);
        db.insert(TABLE_CONTENT_TYPES, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENT_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENT_TYPES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
            onCreate(db);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}