package com.example.infoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName();
    int cIndex = 8;
    Button btnRefresh;
    TextView tvWeather;
    Handler handler;
    String urlWeather, wUrlWeather;
    Document doc;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlWeather = getResources().getString(R.string.urlNaverWeather);
        wUrlWeather = getResources().getString(R.string.wUrlNaverWeather);
        tvWeather = findViewById(R.id.tv_Weather);
        btnRefresh = findViewById(R.id.btn_Refresh);

        registerHandler();
        callWeather();
        btnSetting();
    }


    public void registerHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.d(TAG, "Chk Handle msg : " + msg);
                Bundle bundle = msg.getData();    //new Thread에서 작업한 결과물 받기
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvWeather.setText(bundle.getString("humidity"));    //받아온 데이터 textView에 출력
                    }
                });
            }
        };
    }

    public void callWeather() {
        new Thread(() -> {
            try {
                doc = Jsoup.connect(urlWeather).get();

                for(int a=0;a<cIndex;a++) {
                    bundle = new Bundle();

                    tempLoad(a);
                    humidLoad(a);

                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.handleMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void tempLoad(int i) {
        String sTAG = "_TempLoad()";
        Elements temeles = doc.select("._hourly_weather>ul>li");    //끌어온 html에서 클래스네임이 "temperature_text" 인 값만 선택해서 빼오기
        Element temele = temeles.get(i);
        boolean isEmpty = temeles.isEmpty(); //빼온 값 null체크
        Log.d(TAG + sTAG, "isNull? : " + isEmpty); //로그캣 출력
        if (isEmpty == false) { //null값이 아니면 크롤링 실행
            String tem = temele.text(); //크롤링 하면 "현재온도30'c" 이런식으로 뽑아와지기 때문에, 현재온도를 잘라내고 30'c만 뽑아내는 코드
            Log.d(TAG + sTAG, "TEM is ~~ : " + tem);
            bundle.putString("temperature", tem); //bundle 이라는 자료형에 뽑아낸 결과값 담아서 main Thread로 보내기
        }
    }

    public void humidLoad(int i) {
        String sTAG = "_TempLoad()";
        Elements humelesArg1 = doc.select("._hourly_humidity>div>div>.time_wrap>ul>li");
        Elements humelesArg2 = doc.select("._hourly_humidity>div>div>.graph_wrap>ul>.data");
        boolean isEmpty1 = humelesArg1.isEmpty(); //빼온 값 null체크
        boolean isEmpty2 = humelesArg2.isEmpty(); //빼온 값 null체크
        Log.d(TAG + sTAG, "isNull? : " + isEmpty1 + " " + isEmpty2); //로그캣 출력

        Element humele1 = humelesArg1.get(i);
        Element humele2 = humelesArg2.get(i);
        if (isEmpty1 == false && isEmpty2 == false) { //null값이 아니면 크롤링 실행
            //String hum = humele1.text() + " " + humele2.text() + "%";
            String hum = humele2.text() + "%";
            Log.d(TAG + sTAG, "hum is ~~ : " + hum);
            bundle.putString("humidity", hum); //bundle 이라는 자료형에 뽑아낸 결과값 담아서 main Thread로 보내기
        }
    }

    public void btnSetting() {
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tempLoad();
                callWeather();
            }
        });
    }
}