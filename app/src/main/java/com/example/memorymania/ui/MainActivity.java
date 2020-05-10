package com.example.memorymania.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.memorymania.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button button = findViewById(R.id.starter_button);
        button.setEnabled(true);
    }

    public void startGame(View view) {
        ((Button) view).setEnabled(false);

        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);
    }

    public void setSettings(View view) {
        Toast.makeText(this, "Coming soon mate!", Toast.LENGTH_SHORT).show();

        // TODO: implement settings activity
    }
}
