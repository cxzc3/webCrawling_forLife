package com.example.infoapp.fragment.weather;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infoapp.R;
import com.example.infoapp.var.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


public class WeatherFragment extends Fragment {
    String TAG = WeatherFragment.class.getSimpleName();
    int cIndex = Constant.dCount;
    boolean coin = true;
    boolean tCoin = true;
    ImageButton btnRefresh;
    TextView tvWeather;
    Handler handler;
    String urlWeather, wUrlWeather;
    Document doc;
    Bundle bundle;

    RecyclerView mRecyclerView = null;
    WeatherAdapter mAdapter = null;
    ArrayList<ItemWeather> mList = new ArrayList<ItemWeather>();

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
        mRecyclerView = view.findViewById(R.id.rv_weater);
        btnRefresh = view.findViewById(R.id.btn_Refresh);

        registerHandler();
        btnSetting();

        mAdapter = new WeatherAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    public void registerHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.d(TAG, "Chk Handle msg : " + msg);
                Bundle bundle = msg.getData();    //new Thread에서 작업한 결과물 받기
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        //tvWeather.setText(bundle.getString(Constant.optPrec));    //받아온 데이터 textView에 출력
                        switch (msg.arg1) {
                            case Constant.statInit:
                                addItem(getResources().getDrawable(R.drawable.ic_launcher_background),
                                        getResources().getString(R.string.field_TempT),
                                        getResources().getString(R.string.field_TempW),
                                        getResources().getString(R.string.field_TempD),
                                        getResources().getString(R.string.field_Prec),
                                        getResources().getString(R.string.field_Humi));
                            case Constant.statSet:
                                addItem(getResources().getDrawable(R.drawable.ic_launcher_background),
                                        bundle.getString(Constant.optTempT),
                                        bundle.getString(Constant.optTempW),
                                        bundle.getString(Constant.optTempD),
                                        bundle.getString(Constant.optPrec),
                                        bundle.getString(Constant.optHumi));
                            case Constant.statFin:
                                mAdapter.notifyDataSetChanged();
                                break;
                        }
                    });
                }
            }
        };
    }

    /**
     * @param icon 미정
     * @param strTempT 시간
     * @param strTempW 날씨
     * @param strTempD 기온
     * @param strPrec 강수량
     * @param strHumi 습도
     */
    public void addItem(Drawable icon,
                        String strTempT, String strTempW, String strTempD,
                        String strPrec, String strHumi) {
        ItemWeather item = new ItemWeather();

        item.setIcon(icon);
        item.setStrTempT(strTempT);
        item.setStrTempW(strTempW);
        item.setStrTempD(strTempD);
        item.setStrPrec(strPrec);
        item.setStrHumi(strHumi);

        mList.add(item);
    }

    public void callWeather() {
        if (!coin) {
            if (tCoin) {
                Toast.makeText(getContext(), "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                tCoin = false;
            }
            return;
        }
        new Thread(() -> {
            try {
                coin = false;
                Thread.sleep(5000);
                coin = true;
                tCoin = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
        mList.clear();
        new Thread(() -> {
            try {
                doc = Jsoup.connect(urlWeather).get();
                msging(Constant.statInit);
                for (int a = 0; a < cIndex; a++) {
                    bundle = new Bundle();
                    tempLoad(a);
                    humidLoad(a);
                    precLoad(a);
                    msging(Constant.statSet);
                }
                coin = false;
                msging(Constant.statFin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void msging(int a) {
        Message msg = handler.obtainMessage();
        msg.arg1 = a;
        if (a == Constant.statSet)
            msg.setData(bundle);
        handler.handleMessage(msg);
    }

    /**
     * 기온을 불러옵니다
     * 기준 - 섭씨
     *
     * @param i index
     */
    public void tempLoad(int i) {
        String sTAG = "_tempLoad()";
        Elements temeles = doc.select(Constant.cRouteTemp);
        Elements temelesT = doc.select(Constant.cRouteTempTime);
        Elements temelesW = doc.select(Constant.cRouteTempWeather);
        Elements temelesD = doc.select(Constant.cRouteTempDegree);
        Element temeleT = temelesT.get(i);
        String strTemeleT;

        if(!temeleT.text().contains(getResources().getString(R.string.clock)))
            strTemeleT = getResources().getString(R.string.clock0);
        else
            strTemeleT = temeleT.text();

        Element temeleW = temelesW.get(i);
        Element temeleD = temelesD.get(i);
        boolean isEmpty = temeles.isEmpty(); //빼온 값 null체크
        Log.d(TAG + sTAG, "isNull? : " + isEmpty); //로그캣 출력
        if (!isEmpty) {
            String tem = strTemeleT + " " + temeleW.text() + " " + temeleD.text();
            Log.d(TAG + sTAG, "TEM is ~~ : " + strTemeleT + " " + temeleW.text() + " " + temeleD.text());
            bundle.putString(Constant.optTemp, tem);
            bundle.putString(Constant.optTempT, strTemeleT);
            bundle.putString(Constant.optTempW, temeleW.text());
            bundle.putString(Constant.optTempD, temeleD.text());
        }
    }

    /**
     * 강수량 및 강수확률을 불러옵니다
     * 기준 - mm / %
     *
     * @param i index
     */
    public void precLoad(int i) {
        String sTAG = "precLoad()";
        Elements preeles = doc.select(Constant.cRoutePrec);
        Elements prepeles = doc.select(Constant.cRoutePrecProb);
        Element preele = preeles.get(i);
        Element prepele = prepeles.get(i);
        boolean isEmpty = preeles.isEmpty(); //빼온 값 null체크
        boolean isEmptyP = prepeles.isEmpty(); //빼온 값 null체크
        Log.d(TAG + sTAG, "isNull? : " + isEmpty); //로그캣 출력
        if (!isEmpty && !isEmptyP) {
            String pre = preele.text() + "mm  " + prepele.text();
            Log.d(TAG + sTAG, "PRE is ~~ : " + pre);
            bundle.putString(Constant.optPrec, pre);
        }
    }

    /**
     * 습도를 불러옵니다.
     * 기준 - %
     *
     * @param i index
     */
    public void humidLoad(int i) {
        String sTAG = "_humidLoad()";
        Elements humeles = doc.select(Constant.cRouteHumi);
        boolean isEmpty = humeles.isEmpty();
        Log.d(TAG + sTAG, "isNull? : " + isEmpty);
        Element humele = humeles.get(i);
        if (!isEmpty) {
            String hum = humele.text() + "%";
            Log.d(TAG + sTAG, "HUM is ~~ : " + hum);
            bundle.putString(Constant.optHumi, hum); //bundle 이라는 자료형에 뽑아낸 결과값 담아서 main Thread로 보내기
        }
    }

    public void btnSetting() {
        btnRefresh.setOnClickListener(view -> {
            callWeather();
        });
    }
}
