package com.example.bandocounter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView countTextView;
    private Button incrementButton;
    private Button resetButton;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countTextView = findViewById(R.id.count_textview);
        incrementButton = findViewById(R.id.increment_button);
        resetButton = findViewById(R.id.reset_button);

        prefs = getPreferences(Context.MODE_PRIVATE);
        int count = prefs.getInt("count", 0);
        countTextView.setText(String.valueOf(count));

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = prefs.getInt("count", 0) + 1;
                countTextView.setText(String.valueOf(count));
                prefs.edit().putInt("count", count).apply();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;
                countTextView.setText(String.valueOf(count));
                prefs.edit().putInt("count", count).apply();
            }
        });
    }
}
