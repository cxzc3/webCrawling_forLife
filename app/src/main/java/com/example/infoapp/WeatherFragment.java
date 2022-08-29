package com.example.infoapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.fragment.app.Fragment;

import com.example.infoapp.var.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeatherFragment extends Fragment {
    String TAG = WeatherFragment.class.getSimpleName();
    int cIndex = Constant.dCount;
    Button btnRefresh;
    TextView tvWeather;
    Handler handler;
    String urlWeather, wUrlWeather;
    Document doc;
    Bundle bundle;

    public WeatherFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        urlWeather = getResources().getString(R.string.urlNaverWeather);
        wUrlWeather = getResources().getString(R.string.wUrlNaverWeather);
    }

    @Override
    public void onResume() {
        super.onResume();
        callWeather();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        tvWeather = view.findViewById(R.id.tv_Weather);
        registerHandler();
        return view;
    }

    public void registerHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.d(TAG, "Chk Handle msg : " + msg);
                Bundle bundle = msg.getData();    //new Thread에서 작업한 결과물 받기
                if(getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        tvWeather.setText(bundle.getString(Constant.optHumi));    //받아온 데이터 textView에 출력
                    });
                }
            }
        };
    }

    public void callWeather() {
        new Thread(() -> {
            try {
                doc = Jsoup.connect(urlWeather).get();

                for (int a = 0; a < cIndex; a++) {
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
        String sTAG = "_tempLoad()";
        Elements temeles = doc.select(Constant.cRouteTemp);
        Element temele = temeles.get(i);
        boolean isEmpty = temeles.isEmpty(); //빼온 값 null체크
        Log.d(TAG + sTAG, "isNull? : " + isEmpty); //로그캣 출력
        if (!isEmpty) {
            String tem = temele.text();
            Log.d(TAG + sTAG, "TEM is ~~ : " + tem);
            bundle.putString(Constant.optTemp, tem);
        }
    }

    public void humidLoad(int i) {
        String sTAG = "_humidLoad()";
        Elements humeles = doc.select(Constant.cRouteHumi);
        boolean isEmpty = humeles.isEmpty();
        Log.d(TAG + sTAG, "isNull? : " + isEmpty);
        Element humele = humeles.get(i);
        if (!isEmpty) {
            String hum = humele.text() + "%";
            Log.d(TAG + sTAG, "hum is ~~ : " + hum);
            bundle.putString(Constant.optHumi, hum); //bundle 이라는 자료형에 뽑아낸 결과값 담아서 main Thread로 보내기
        }
    }
}
