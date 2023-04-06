package com.example.bandocounter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bandocounter.R;


public class MainActivity extends AppCompatActivity {

    private int count = 0;
    private TextView countTextView;
    private Button incrementButton;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countTextView = (TextView) findViewById(R.id.count_textview);
        incrementButton = (Button) findViewById(R.id.increment_button);
        resetButton = (Button) findViewById(R.id.reset_button);

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                countTextView.setText(String.valueOf(count));
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                countTextView.setText(String.valueOf(count));
            }
        });
    }
}
