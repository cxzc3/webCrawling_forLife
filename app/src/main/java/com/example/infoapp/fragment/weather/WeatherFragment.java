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
                        if(msg.arg1 == 1){
                            mAdapter.notifyDataSetChanged() ;
                        }
                        else {
                            addItem(getResources().getDrawable(R.drawable.ic_launcher_background),
                                    bundle.getString(Constant.optTemp), bundle.getString(Constant.optPrec), bundle.getString(Constant.optHumi));
                        }
                    });
                }
            }
        };
    }

    public void addItem(Drawable icon, String strTemp, String strPrec, String strHumi) {
        ItemWeather item = new ItemWeather();

        item.setIcon(icon);
        item.setStrTemp(strTemp);
        item.setStrPrec(strPrec);
        item.setStrHumi(strHumi);

        mList.add(item);
    }

    public void callWeather() {
        mList.clear();
        new Thread(() -> {
            try {
                doc = Jsoup.connect(urlWeather).get();

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

    private void msging(int a){
        Message msg = handler.obtainMessage();
        msg.arg1 = a;
        if(a != 1)
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
        Element temele = temeles.get(i);
        boolean isEmpty = temeles.isEmpty(); //빼온 값 null체크
        Log.d(TAG + sTAG, "isNull? : " + isEmpty); //로그캣 출력
        if (!isEmpty) {
            String tem = temele.text();
            Log.d(TAG + sTAG, "TEM is ~~ : " + tem);
            bundle.putString(Constant.optTemp, tem);
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
