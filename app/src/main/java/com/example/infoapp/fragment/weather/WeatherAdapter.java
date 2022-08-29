package com.example.infoapp.fragment.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.infoapp.R;

import java.util.ArrayList;

/**
 * WeatherFragment 리스트 보여주는 Adapter
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private ArrayList<ItemWeather> mData = null;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    WeatherAdapter(ArrayList<ItemWeather> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_weather, parent, false);
        WeatherAdapter.ViewHolder vh = new WeatherAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(WeatherAdapter.ViewHolder holder, int position) {

        ItemWeather item = mData.get(position);

        holder.icon.setImageDrawable(item.getIcon());
        holder.tvTemp.setText(item.getStrTemp());
        holder.tvPrec.setText(item.getStrPrec());
        holder.tvHumi.setText(item.getStrHumi());
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView tvTemp;
        TextView tvPrec;
        TextView tvHumi;

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            icon = itemView.findViewById(R.id.iv_icon);
            tvTemp = itemView.findViewById(R.id.tv_temp);
            tvPrec = itemView.findViewById(R.id.tv_prec);
            tvHumi = itemView.findViewById(R.id.tv_humi);
        }
    }
}