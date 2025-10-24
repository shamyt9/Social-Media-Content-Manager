package com.app.socialmediacontentmanager.utils;

public class Constants {

    // Intent extras
    public static final String EXTRA_CATEGORY_ID = "CATEGORY_ID";
    public static final String EXTRA_CATEGORY_NAME = "CATEGORY_NAME";
    public static final String EXTRA_CONTENT_TYPE_ID = "CONTENT_TYPE_ID";
    public static final String EXTRA_CONTENT_TYPE_NAME = "CONTENT_TYPE_NAME";
    public static final String EXTRA_CONTENT_ITEM_ID = "CONTENT_ITEM_ID";

    // Content status
    public static final int STATUS_DRAFT = 0;
    public static final int STATUS_PUBLISHED = 1;

    // Database constants
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "social_content_manager.db";

    // SharedPreferences keys
    public static final String PREF_FIRST_RUN = "first_run";
    public static final String PREF_THEME = "app_theme";
}