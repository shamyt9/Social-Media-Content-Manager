package com.app.socialmediacontentmanager.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import com.app.socialmediacontentmanager.R;
import com.app.socialmediacontentmanager.utils.BackupManager;
import com.google.android.material.button.MaterialButton;
import java.io.IOException;

public class activity_settings extends AppCompatActivity {

    private static final int REQUEST_CODE_EXPORT = 100;
    private static final int REQUEST_CODE_IMPORT = 101;

    private MaterialButton btnExport, btnImport;
    private BackupManager backupManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backupManager = new BackupManager(this);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        btnExport = findViewById(R.id.btnExport);
        btnImport = findViewById(R.id.btnImport);
    }

    private void setupListeners() {
        btnExport.setOnClickListener(v -> createBackupFile());
        btnImport.setOnClickListener(v -> selectBackupFile());
    }

    private void createBackupFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "content_backup_" + System.currentTimeMillis() + ".json");
        startActivityForResult(intent, REQUEST_CODE_EXPORT);
    }

    private void selectBackupFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        startActivityForResult(intent, REQUEST_CODE_IMPORT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    if (requestCode == REQUEST_CODE_EXPORT) {
                        boolean success = backupManager.exportData(uri);
                        if (success) {
                            Toast.makeText(this, "Backup created successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to create backup", Toast.LENGTH_SHORT).show();
                        }
                    } else if (requestCode == REQUEST_CODE_IMPORT) {
                        boolean success = backupManager.importData(uri);
                        if (success) {
                            Toast.makeText(this, "Data imported successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to import data", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}