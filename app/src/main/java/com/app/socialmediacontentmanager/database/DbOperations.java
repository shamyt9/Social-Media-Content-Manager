package com.app.socialmediacontentmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.socialmediacontentmanager.models.Category;
import com.app.socialmediacontentmanager.models.ContentType;
import com.app.socialmediacontentmanager.models.ContentItem;

import java.util.ArrayList;
import java.util.List;

public class DbOperations {
    private DatabaseHelper dbHelper;

    public DbOperations(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // Category Operations
    public long addCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.KEY_CATEGORY_NAME, category.getName());
            values.put(DatabaseHelper.KEY_CATEGORY_ICON, category.getIconResId());

            id = db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);
        } catch (Exception e) {
            Log.e("DbOperations", "Error adding category: " + e.getMessage());
        } finally {
            db.close();
        }
        return id;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(DatabaseHelper.TABLE_CATEGORIES,
                    null, null, null, null, null, DatabaseHelper.KEY_CATEGORY_NAME + " ASC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Category category = new Category();
                    category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CATEGORY_ID)));
                    category.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CATEGORY_NAME)));
                    category.setIconResId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CATEGORY_ICON)));
                    categories.add(category);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DbOperations", "Error getting categories: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return categories;
    }

    public Category getCategoryById(int categoryId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Category category = null;
        Cursor cursor = null;

        try {
            cursor = db.query(DatabaseHelper.TABLE_CATEGORIES,
                    null,
                    DatabaseHelper.KEY_CATEGORY_ID + " = ?",
                    new String[]{String.valueOf(categoryId)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CATEGORY_ID)));
                category.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CATEGORY_NAME)));
                category.setIconResId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CATEGORY_ICON)));
            }
        } catch (Exception e) {
            Log.e("DbOperations", "Error getting category: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return category;
    }

    // ContentType Operations
    public List<ContentType> getContentTypesByCategory(int categoryId) {
        List<ContentType> contentTypes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(DatabaseHelper.TABLE_CONTENT_TYPES,
                    null,
                    DatabaseHelper.KEY_CONTENT_TYPE_CATEGORY_ID + " = ?",
                    new String[]{String.valueOf(categoryId)},
                    null, null, DatabaseHelper.KEY_CONTENT_TYPE_NAME + " ASC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ContentType contentType = new ContentType();
                    contentType.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_TYPE_ID)));
                    contentType.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_TYPE_CATEGORY_ID)));
                    contentType.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_TYPE_NAME)));
                    contentTypes.add(contentType);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DbOperations", "Error getting content types: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return contentTypes;
    }

    public ContentType getContentTypeById(int typeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentType contentType = null;
        Cursor cursor = null;

        try {
            cursor = db.query(DatabaseHelper.TABLE_CONTENT_TYPES,
                    null,
                    DatabaseHelper.KEY_CONTENT_TYPE_ID + " = ?",
                    new String[]{String.valueOf(typeId)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                contentType = new ContentType();
                contentType.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_TYPE_ID)));
                contentType.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_TYPE_CATEGORY_ID)));
                contentType.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_TYPE_NAME)));
            }
        } catch (Exception e) {
            Log.e("DbOperations", "Error getting content type: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return contentType;
    }

    // ContentItem Operations
    public long addContentItem(ContentItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.KEY_CONTENT_ITEM_TYPE_ID, item.getTypeId());
            values.put(DatabaseHelper.KEY_CONTENT_ITEM_TITLE, item.getTitle());
            values.put(DatabaseHelper.KEY_CONTENT_ITEM_DESCRIPTION, item.getDescription());
            values.put(DatabaseHelper.KEY_CONTENT_ITEM_TAGS, item.getTags());
            values.put(DatabaseHelper.KEY_CONTENT_ITEM_STATUS, item.getStatus());

            id = db.insert(DatabaseHelper.TABLE_CONTENT_ITEMS, null, values);
        } catch (Exception e) {
            Log.e("DbOperations", "Error adding content item: " + e.getMessage());
        } finally {
            db.close();
        }
        return id;
    }

    public List<ContentItem> getContentItemsByType(int typeId) {
        List<ContentItem> contentItems = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(DatabaseHelper.TABLE_CONTENT_ITEMS,
                    null,
                    DatabaseHelper.KEY_CONTENT_ITEM_TYPE_ID + " = ?",
                    new String[]{String.valueOf(typeId)},
                    null, null, DatabaseHelper.KEY_CONTENT_ITEM_LAST_UPDATED + " DESC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ContentItem item = new ContentItem();
                    item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_ID)));
                    item.setTypeId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_TYPE_ID)));
                    item.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_TITLE)));
                    item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_DESCRIPTION)));
                    item.setCreationDate(new java.util.Date(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_CREATION_DATE))));
                    item.setLastUpdated(new java.util.Date(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_LAST_UPDATED))));
                    item.setTags(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_TAGS)));
                    item.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_STATUS)));
                    contentItems.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DbOperations", "Error getting content items: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return contentItems;
    }

    public ContentItem getContentItemById(int itemId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentItem item = null;
        Cursor cursor = null;

        try {
            cursor = db.query(DatabaseHelper.TABLE_CONTENT_ITEMS,
                    null,
                    DatabaseHelper.KEY_CONTENT_ITEM_ID + " = ?",
                    new String[]{String.valueOf(itemId)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                item = new ContentItem();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_ID)));
                item.setTypeId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_TYPE_ID)));
                item.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_TITLE)));
                item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_DESCRIPTION)));
                item.setCreationDate(new java.util.Date(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_CREATION_DATE))));
                item.setLastUpdated(new java.util.Date(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_LAST_UPDATED))));
                item.setTags(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_TAGS)));
                item.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_STATUS)));
            }
        } catch (Exception e) {
            Log.e("DbOperations", "Error getting content item: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return item;
    }

    public int updateContentItem(ContentItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.KEY_CONTENT_ITEM_TITLE, item.getTitle());
            values.put(DatabaseHelper.KEY_CONTENT_ITEM_DESCRIPTION, item.getDescription());
            values.put(DatabaseHelper.KEY_CONTENT_ITEM_TAGS, item.getTags());
            values.put(DatabaseHelper.KEY_CONTENT_ITEM_STATUS, item.getStatus());
            values.put(DatabaseHelper.KEY_CONTENT_ITEM_LAST_UPDATED, System.currentTimeMillis());

            rowsAffected = db.update(DatabaseHelper.TABLE_CONTENT_ITEMS,
                    values,
                    DatabaseHelper.KEY_CONTENT_ITEM_ID + " = ?",
                    new String[]{String.valueOf(item.getId())});
        } catch (Exception e) {
            Log.e("DbOperations", "Error updating content item: " + e.getMessage());
        } finally {
            db.close();
        }
        return rowsAffected;
    }

    public int deleteContentItem(int itemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = 0;

        try {
            rowsAffected = db.delete(DatabaseHelper.TABLE_CONTENT_ITEMS,
                    DatabaseHelper.KEY_CONTENT_ITEM_ID + " = ?",
                    new String[]{String.valueOf(itemId)});
        } catch (Exception e) {
            Log.e("DbOperations", "Error deleting content item: " + e.getMessage());
        } finally {
            db.close();
        }
        return rowsAffected;
    }

    public List<ContentItem> searchContentItems(String query) {
        List<ContentItem> contentItems = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String selection = DatabaseHelper.KEY_CONTENT_ITEM_TITLE + " LIKE ? OR " +
                    DatabaseHelper.KEY_CONTENT_ITEM_DESCRIPTION + " LIKE ? OR " +
                    DatabaseHelper.KEY_CONTENT_ITEM_TAGS + " LIKE ?";
            String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%", "%" + query + "%"};

            cursor = db.query(DatabaseHelper.TABLE_CONTENT_ITEMS,
                    null,
                    selection,
                    selectionArgs,
                    null, null, DatabaseHelper.KEY_CONTENT_ITEM_LAST_UPDATED + " DESC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ContentItem item = new ContentItem();
                    item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_ID)));
                    item.setTypeId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_TYPE_ID)));
                    item.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_TITLE)));
                    item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_DESCRIPTION)));
                    item.setCreationDate(new java.util.Date(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_CREATION_DATE))));
                    item.setLastUpdated(new java.util.Date(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_LAST_UPDATED))));
                    item.setTags(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_TAGS)));
                    item.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CONTENT_ITEM_STATUS)));
                    contentItems.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DbOperations", "Error searching content items: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return contentItems;
    }

    public int getContentItemsCountByType(int typeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int count = 0;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_CONTENT_ITEMS +
                    " WHERE " + DatabaseHelper.KEY_CONTENT_ITEM_TYPE_ID + " = ?", new String[]{String.valueOf(typeId)});

            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("DbOperations", "Error getting content items count: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return count;
    }
}