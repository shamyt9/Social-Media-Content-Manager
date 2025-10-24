package com.app.socialmediacontentmanager.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.JsonReader;
import android.util.JsonWriter;
import com.app.socialmediacontentmanager.database.DbOperations;
import com.app.socialmediacontentmanager.models.ContentItem;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class BackupManager {

    private Context context;
    private DbOperations dbOperations;

    public BackupManager(Context context) {
        this.context = context;
        this.dbOperations = new DbOperations(context);
    }

    public boolean exportData(Uri uri) throws IOException {
        ContentResolver contentResolver = context.getContentResolver();

        try (OutputStream outputStream = contentResolver.openOutputStream(uri);
             OutputStreamWriter writer = new OutputStreamWriter(outputStream);
             JsonWriter jsonWriter = new JsonWriter(writer)) {

            jsonWriter.setIndent("  ");
            jsonWriter.beginArray();

            List<ContentItem> allItems = dbOperations.searchContentItems("");
            for (ContentItem item : allItems) {
                jsonWriter.beginObject();
                jsonWriter.name("title").value(item.getTitle());
                jsonWriter.name("description").value(item.getDescription());
                jsonWriter.name("tags").value(item.getTags());
                jsonWriter.name("status").value(item.getStatus());
                jsonWriter.name("typeId").value(item.getTypeId());
                jsonWriter.name("creationDate").value(item.getCreationDate().getTime());
                jsonWriter.name("lastUpdated").value(item.getLastUpdated().getTime());
                jsonWriter.endObject();
            }

            jsonWriter.endArray();
            return true;
        }
    }

    public boolean importData(Uri uri) throws IOException {
        ContentResolver contentResolver = context.getContentResolver();

        try (InputStream inputStream = contentResolver.openInputStream(uri);
             InputStreamReader reader = new InputStreamReader(inputStream);
             JsonReader jsonReader = new JsonReader(reader)) {

            jsonReader.beginArray();

            while (jsonReader.hasNext()) {
                ContentItem item = new ContentItem();
                jsonReader.beginObject();

                while (jsonReader.hasNext()) {
                    String name = jsonReader.nextName();
                    switch (name) {
                        case "title":
                            item.setTitle(jsonReader.nextString());
                            break;
                        case "description":
                            item.setDescription(jsonReader.nextString());
                            break;
                        case "tags":
                            item.setTags(jsonReader.nextString());
                            break;
                        case "status":
                            item.setStatus(jsonReader.nextInt());
                            break;
                        case "typeId":
                            item.setTypeId(jsonReader.nextInt());
                            break;
                        case "creationDate":
                            item.setCreationDate(new java.util.Date(jsonReader.nextLong()));
                            break;
                        case "lastUpdated":
                            item.setLastUpdated(new java.util.Date(jsonReader.nextLong()));
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }

                jsonReader.endObject();
                dbOperations.addContentItem(item);
            }

            jsonReader.endArray();
            return true;
        }
    }
}