package com.example.grammarize;

import static com.example.grammarize.R.layout.activity_main;
import static com.example.grammarize.R.string.gpt3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
	String gpt3APIKey = getString(gpt3);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(activity_main);
    }
}
