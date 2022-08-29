package com.example.infoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

import com.example.infoapp.fragment.weather.WeatherFragment;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName();
    Button btnRefresh;

    FragmentManager fm;
    FragmentTransaction ft;
    WeatherFragment wFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRefresh = findViewById(R.id.btn_Refresh);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        wFrag = new WeatherFragment();
        ft.replace(R.id.frag_frame, wFrag).commitAllowingStateLoss();

    }
}