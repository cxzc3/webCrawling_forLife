package com.example.infoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.infoapp.var.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

    public void btnSetting() {
        btnRefresh.setOnClickListener(view -> {

        });
    }
}