package com.example.weatherdemo;




import org.json.JSONException;
import org.json.JSONObject;

import com.crazybean.network.NetTask;
import com.example.weatherdemo.api.WeatherApi;
import com.example.weatherdemo.model.WeatherInfo;
import com.example.weatherdemo.views.UiUtils;














import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar.Tab;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeatherFragment extends Fragment implements NetTask.IObserver,OnClickListener{
	


	
	

	WeatherInfo weatherInfo=new WeatherInfo();;
	String cityName,countryName;
	
	

	
public void setWeatherInfo(WeatherInfo weatherInfo) {
		this.weatherInfo = weatherInfo;
		init();
		
	}

private RelativeLayout ll;

private FragmentActivity fa;


View btnForcast;
View btnChart;
View btnIndexs;
LinearLayout indexView;
View chartView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fa = super.getActivity();
		// Intent intent = fa.getIntent();
		ll = (RelativeLayout) inflater.inflate(R.layout.iweather_item,
				container, false);
		
		
		
		cityName = this.getArguments().getString("name");
		countryName = this.getArguments().getString("country");
		


		return ll;
	}
	
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		
		WeatherApi.getWeatherByName(countryName, cityName, this);
	}



	private void init() {
		ll.findViewById(R.id.rlTodayInfo).setOnClickListener(this);
		btnForcast = ll.findViewById(R.id.btnForcast);
		btnChart = ll.findViewById(R.id.btnChart);
		btnIndexs = ll.findViewById(R.id.btnIndexs);
		
		initViews();
		



		
	}
	
	private void initViews() {
		if (weatherInfo != null) {

			initToadyInfo();
			initForcastWeather();

		}
	}
	
	private void initToadyInfo() {
		TextView cityTV = (TextView) ll.findViewById(R.id.cityTV);
		cityTV.setText(weatherInfo.getCity().getName());

//		String time = weather.date + " " + weather.time;
//		time = ConstUtils.checkDate(time, weather.time);
//		TextView tvInfo = (TextView)ll. findViewById(R.id.tvInfo);
//		tvInfo.setText("isoldtime");
//
		TextView todayTV = (TextView) ll.findViewById(R.id.updateTimeTV);
		todayTV.setText("Update: "
				+ getWeatherTime(weatherInfo));
//
		TextView todayTempNow = (TextView) ll.findViewById(R.id.todayTempNow);
		todayTempNow.setText(getNormalTemp(weatherInfo.getTemp().getDay()) + "°C");
//
		TextView todayTemp = (TextView) ll.findViewById(R.id.todayTemp);
		todayTemp.setText(getNormalTemp(weatherInfo.getTemp().getMaximal()) + "°C/"+getNormalTemp(weatherInfo.getTemp().getMinimal())+"°C");
//
		TextView todayDesc = (TextView)ll. findViewById(R.id.todayDesc);
		todayDesc.setText(weatherInfo.getWeather().get(0).getDesc());
//
		TextView todayWet = (TextView) ll.findViewById(R.id.todayWet);
		String strHumidity = fa.getString(R.string.humidity) + weatherInfo.getHumidity();
		todayWet.setText(strHumidity);
//
		TextView todayWind = (TextView) ll.findViewById(R.id.todayWind);
		todayWind.setText(weatherInfo.getWindDeg()+" "+weatherInfo.getWindSpeed());
//
		ImageView img = (ImageView) ll.findViewById(R.id.todayImg);
//
//		img.setImageURI(ConstUtils.getWeatherUri(mContext, weather.img));
		img.setImageResource(getWeatherImage(weatherInfo));
		
	}
	
	private String getWeatherTime(WeatherInfo weatherInfo2) {
//		long timetag = Long.parseLong(weatherInfo2.getTimetag());
//		String time ="";
//		if(timetag!=0){
//			SimpleDateFormat sdf = new SimpleDateFormat("E dd, MMM/yyyy");
//			sdf.setTimeZone(TimeZone.getDefault());
//			return sdf.format(new Date(timetag));
//		}
		return weatherInfo2.getTimetag();
	}

	private int getNormalTemp(String day) {
		
		return (int)(Double.parseDouble(day)-273.15);
	}

	private int getWeatherImage(WeatherInfo weatherInfo2) {
		// TODO Auto-generated method stub
		return R.drawable.day0;
	}

	public void initTabs(View view) {
		(btnForcast).setBackgroundResource(R.drawable.tab_normal);
		(btnChart).setBackgroundResource(R.drawable.tab_normal);
		(btnIndexs).setBackgroundResource(R.drawable.tab_normal);
		
		view.setBackgroundResource(R.drawable.tab_select);
	}

	@Override
	public void onClick(View v) {
		if (weatherInfo == null)
			return;
		switch (v.getId()) {
		case R.id.btnForcast:
			initForcastWeather();
			break;
		case R.id.btnChart:

			initLineChartView();
			break;
		case R.id.btnIndexs:

			initIndexsView();
			break;
//		case R.id.btnTaobao:
//			initTaobaoView();
//			break;
//		case R.id.adview:
//			mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
//					.parse(url)));
//			break;
		case R.id.rlTodayInfo:
			UiUtils.makeToast(fa,fa.getString(R.string.updating));
//			getWeatherFromNet();
			break;
		default:
			break;
		}

	}

	private void initIndexsView() {
		initTabs(btnIndexs);

		LinearLayout forcastLLayout = (LinearLayout) ll.findViewById(R.id.forcastLLayout);
		if (indexView != null) {
			forcastLLayout.removeAllViews();
			forcastLLayout.addView(indexView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			return;
		}
		LayoutInflater inflater = (LayoutInflater) fa
				.getSystemService(fa.LAYOUT_INFLATER_SERVICE);
		indexView = (LinearLayout) inflater.inflate(R.layout.weather_indexs,
				null);

		((TextView) indexView.findViewById(R.id.tvChuanyi))
				.setText("23");
		((TextView) indexView.findViewById(R.id.tvUV))
				.setText("44");
		((TextView) indexView.findViewById(R.id.tvXiche))
				.setText("72");
		((TextView) indexView.findViewById(R.id.tvComfort))
				.setText("51");
		((TextView) indexView.findViewById(R.id.tvChenlian))
				.setText("37");
		((TextView) indexView.findViewById(R.id.tvGuomin))
				.setText("81");

		forcastLLayout.removeAllViews();
		forcastLLayout.addView(indexView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	private void initLineChartView() {
		// TODO Auto-generated method stub
		
	}

	private void initForcastWeather() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleResult(byte[] aData, int aType, int aErrCode) {
	    JSONObject aObject = null;
        try {
            String strInput = new String(aData);
            aObject = new JSONObject(strInput);
        } catch (JSONException aException) {
            aException.printStackTrace();
        } finally {
            onResponse(aObject, aType, aErrCode);
        }
		
	}
	
	protected void onResponse(JSONObject aObject, int aType, int aErrCode) {
		
		weatherInfo.fromJson(aObject);
		this.setWeatherInfo(weatherInfo);

	}

	@Override
	public void notifyData(byte[] arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	
    public static WeatherFragment newInstance(Tab tab) {

    	WeatherFragment f = new WeatherFragment();
        Bundle b = new Bundle();
        b.putString("name", tab.getText().toString());
        b.putString("country", tab.getContentDescription().toString());

        f.setArguments(b);

        return f;
    }

}
