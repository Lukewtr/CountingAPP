package com.example.bandocounter;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView countTextView;
    private Button incrementButton;
    private Button resetButton;
    private Button downloadButton;
    private SharedPreferences prefs;
    private SQLiteDatabase db;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                // Permission granted, download the CSV file
                downloadCsv();
            } else {
                // Request permission to manage external storage
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                Toast.makeText(this, "Please grant permission to manage external storage", Toast.LENGTH_SHORT).show();
                downloadCsv();
            }
        } else {
            // For devices running Android 10 or below, request WRITE_EXTERNAL_STORAGE permission
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadCsv();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void downloadCsv() {
        // Export the database table to a CSV file and save it to the external storage directory
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "counter_data.csv");
            FileWriter writer = new FileWriter(file);

            // Query the database table and write each row to the CSV file
            Cursor cursor = db.rawQuery("SELECT * FROM Counter", null);
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                int countIndex = cursor.getColumnIndex("count");
                int timestampIndex = cursor.getColumnIndex("timestamp");

                // Write the CSV header row
                writer.write("ID,Count,Timestamp\n");

                do {
                    int id = cursor.getInt(idIndex);
                    int count = cursor.getInt(countIndex);
                    String timestamp = cursor.getString(timestampIndex);

                    writer.write(String.format("%d,%d,%s\n", id, count, timestamp));
                } while (cursor.moveToNext());

                cursor.close();
            }

            writer.close();


            Toast.makeText(MainActivity.this, "CSV file exported to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countTextView = findViewById(R.id.count_textview);
        incrementButton = findViewById(R.id.increment_button);
        resetButton = findViewById(R.id.reset_button);
        downloadButton = findViewById(R.id.download_button);

        prefs = getPreferences(Context.MODE_PRIVATE);
        int count = prefs.getInt("count", 0);
        countTextView.setText(String.valueOf(count));

        // Initialize the database and create the table if it doesn't exist
        db = openOrCreateDatabase("CounterDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Counter (id INTEGER PRIMARY KEY AUTOINCREMENT, count INTEGER, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = prefs.getInt("count", 0) + 1;
                countTextView.setText(String.valueOf(count));
                prefs.edit().putInt("count", count).apply();

                // Insert a new row into the database with the current count and timestamp
                ContentValues values = new ContentValues();
                values.put("count", count);
                db.insert("Counter", null, values);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;
                countTextView.setText(String.valueOf(count));
                prefs.edit().putInt("count", count).apply();

                // Update the count in the database to 0
                ContentValues values = new ContentValues();
                values.put("count", count);
                db.insert("Counter", null, values);
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
                //              downloadcsv()
        }});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close the database when the activity is destroyed
        db.close();
    }
}

