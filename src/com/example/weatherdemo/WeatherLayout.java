package com.example.weatherdemo;


import org.json.JSONObject;

import com.example.weatherdemo.model.WeatherInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class WeatherLayout extends RelativeLayout implements OnClickListener {
	WeatherInfo weather;
//	AsyncHttpResponseHandler callback = new AsyncHttpResponseHandler() {
//
//		@Override
//		public void onSuccess(String content, String url) {
//			ConstUtils.log(url, "onSuccess " + content);
//
//			isLoading = false;
//			try {
//				weather = new WeatherInfo();
//				weather.parse(content, getContext());
//				// 保存到数据库
//				ConstUtils.setWeatherInfo(mContext, cityCode, content);
//				initViews();
//				return;
//			} catch (Exception e) {
//
//			}
//			showToast(getContext().getString(R.string.update_failed));
//		}
//
//		@Override
//		public void onFailure(Throwable error, String url) {
//			ConstUtils.log("http", "onFailure:" + url);
//			// error.printStackTrace();
//			MobclickAgent.onEvent(mContext, "http", "WeatherLayout:" + url);
//				setError();
//		}
//
//	};

	
	
	String cityCode;
	Context mContext;

	LinearLayout indexView;
	View chartView;
	LinearLayout taobaoView;

	View btnForcast;
	View btnChart;
	View btnIndexs;
	View btnTaobao;

	private boolean isLoading;

	String url = "http://shop103271332.m.taobao.com/";

	public WeatherLayout(Context context) {
		super(context);

		mContext = context;
		// 加载布局文件
		LayoutInflater layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.iweather_item, this);
		// 初始化点击时间
		findViewById(R.id.rlTodayInfo).setOnClickListener(this);
		btnForcast = findViewById(R.id.btnForcast);
		btnChart = findViewById(R.id.btnChart);
		btnIndexs = findViewById(R.id.btnIndexs);
		btnTaobao = findViewById(R.id.btnTaobao);

		btnForcast.setOnClickListener(this);
		btnChart.setOnClickListener(this);
		btnIndexs.setOnClickListener(this);

	}

	public void initToadyInfo() {
//		TextView cityTV = (TextView) findViewById(R.id.cityTV);
//		cityTV.setText(weather.city);
//
//		String time = weather.date + " " + weather.time;
//		time = ConstUtils.checkDate(time, weather.time);
//		TextView tvInfo = (TextView) findViewById(R.id.tvInfo);
//		tvInfo.setText(ConstUtils.isOldData(time));
//
//		TextView todayTV = (TextView) findViewById(R.id.updateTimeTV);
//		todayTV.setText("Update: "
//				+ ConstUtils.getTomorrowDateFormat("dd/MM  HH:mm", time, 0));
//
//		TextView todayTempNow = (TextView) findViewById(R.id.todayTempNow);
//		todayTempNow.setText(weather.temp_now + "°");
//
//		TextView todayTemp = (TextView) findViewById(R.id.todayTemp);
//		todayTemp.setText(weather.temp);
//
//		TextView todayDesc = (TextView) findViewById(R.id.todayDesc);
//		todayDesc.setText(weather.weather);
//
//		TextView todayWet = (TextView) findViewById(R.id.todayWet);
//		String strHumidity = getContext().getString(R.string.humidity) + weather.humidity;
//		todayWet.setText(strHumidity);
//
//		TextView todayWind = (TextView) findViewById(R.id.todayWind);
//		todayWind.setText(weather.wind);
//
//		ImageView img = (ImageView) findViewById(R.id.todayImg);
//
//		img.setImageURI(ConstUtils.getWeatherUri(mContext, weather.img));

	}

//	public String getWeatherMsg() {
//		if (weather == null)
//			return "";
//		StringBuilder msg = new StringBuilder();
//		msg.append("Today:").append(weather.city).append(",")
//				.append(weather.weather).append(",").append(weather.temp)
//				.append(",Humidity:").append(weather.humidity).append(",")
//				.append(weather.wind).append("|Tomorrow：")
//				.append(weather.forcasts.get(1).weather).append(",")
//				.append(weather.forcasts.get(1).temp).append("|2 Days：")
//				.append(weather.forcasts.get(2).weather).append(",")
//				.append(weather.forcasts.get(2).temp).append("-ColorWeather");
//
//		return msg.toString();
//	}

	private void initViews() {
		if (weather != null) {

			initToadyInfo();
			initForcastWeather();

		}
	}

	public void initAQI(String content) {

		try {
			TextView cityTV = (TextView) findViewById(R.id.aqiTV);
			JSONObject json = new JSONObject(content);
			int index = Integer.parseInt(json.getString("aqi"));
			if (index < 100)
				;
			else if (index < 250)
				cityTV.setTextColor(Color.YELLOW);
			else
				cityTV.setTextColor(Color.RED);
			cityTV.setText(json.getString("aqi"));
			TextView msgTV = (TextView) findViewById(R.id.aqiMsgTV);
			msgTV.setText("(" + json.getString("desc") + ")");
		} catch (Exception e) {
			// ConstUtils.saveBugs(mContext, e);
		}
	}

	private void initForcastWeather() {
//		initTabs(btnForcast);
//
//		LinearLayout forcastLLayout = (LinearLayout) findViewById(R.id.forcastLLayout);
//
//		LayoutInflater inflater = (LayoutInflater) mContext
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		String time = weather.date + " " + weather.time;
//		time = ConstUtils.checkDate(time, weather.time);
//		forcastLLayout.removeAllViews();
//		boolean isWeek = Boolean.parseBoolean(ConstUtils.getValueByKey(
//				mContext, SettingsActivity.KEY_SHOW_WEEK, "true"));
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 4);
//		for (int i = 1; i < 6; i++) {
//			WeatherInfo.Forecasts w = weather.forcasts.get(i);
//			LinearLayout item = (LinearLayout) inflater.inflate(
//					R.layout.day_item, null);
//
//			TextView tvDay = (TextView) item.findViewById(R.id.tvDay);
//			if (isWeek)
//				tvDay.setText(ConstUtils.getTomorrowDateFormat("EE", time, i));
//			else
//				tvDay.setText(ConstUtils.getTomorrowDateFormat("dd/MM", time, i));
//			ImageView img = (ImageView) item.findViewById(R.id.imgIcon);
//
//			img.setImageURI(ConstUtils.getWeatherUri(mContext, w.img));
//
//			TextView tvTemp = (TextView) item.findViewById(R.id.tvTemp);
//			tvTemp.setText(w.temp);
//
//			TextView tvWeather = (TextView) item.findViewById(R.id.tvWeather);
//			
//			int nPos = w.weather.indexOf("转");
//			if (nPos > 0)
//				tvWeather.setText(w.weather.substring(0, nPos));
//			else
//				tvWeather.setText(w.weather);
//
//			TextView tvWind = (TextView) item.findViewById(R.id.tvWind);
//			String wind = w.wind.replace("-", "/");
//			nPos = wind.indexOf("转");
//			if (nPos > 0) {
//				wind = (wind.substring(nPos + 1));
//				if (!wind.contains("风")) {
//					wind = w.wind.replace("-", "/");
//					wind = (wind.substring(0, nPos));
//				}
//			}
//			if (wind.length() > 3) {
//				wind = wind.replace("风", ":").replace("小于", "<")
//						.replace("大于", ">");
//			}
//			tvWind.setText(wind);
//
//			forcastLLayout.addView(item, lp);
//		}

	}

	private void initLineChartView() {
//		initTabs(btnChart);
//
//		LinearLayout forcastLLayout = (LinearLayout) findViewById(R.id.forcastLLayout);
//		if (chartView != null) {
//			forcastLLayout.removeAllViews();
//			forcastLLayout.addView(chartView);
//			return;
//		}
//
//		TemperatureChart tChart = new TemperatureChart();
//
//		int[] yH = new int[6];
//		int[] yL = new int[6];
//		for (int i = 0; i < 6; i++) {
//			WeatherInfo.Forecasts w = weather.forcasts.get(i);
//			String[] temps = w.temp.replace("℃", "").split("/");
//			int t1 = Integer.parseInt(temps[0]);
//			int t2 = Integer.parseInt(temps[1]);
//			if (t1 > t2) {
//				yH[i] = t1;
//				yL[i] = t2;
//			} else {
//				yH[i] = t2;
//				yL[i] = t1;
//			}
//		}
//
//		String time = weather.date + " " + weather.time;
//		time = ConstUtils.checkDate(time, weather.time);
//		chartView = tChart.getGraphicalChart(mContext,
//				forcastLLayout.getWidth(), forcastLLayout.getHeight(), yH, yL,
//				time);
//		forcastLLayout.removeAllViews();
//		forcastLLayout.addView(chartView);

	}

	private void initIndexsView() {
//		initTabs(btnIndexs);
//		LinearLayout forcastLLayout = (LinearLayout) findViewById(R.id.forcastLLayout);
//		if (indexView != null) {
//			forcastLLayout.removeAllViews();
//			forcastLLayout.addView(indexView, new LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//			return;
//		}
//		LayoutInflater inflater = (LayoutInflater) mContext
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		indexView = (LinearLayout) inflater.inflate(R.layout.weather_indexs,
//				null);
//
//		((TextView) indexView.findViewById(R.id.tvChuanyi))
//				.setText(weather.index_chuanyi);
//		((TextView) indexView.findViewById(R.id.tvUV))
//				.setText(weather.index_uv);
//		((TextView) indexView.findViewById(R.id.tvXiche))
//				.setText(weather.index_xiche);
//		((TextView) indexView.findViewById(R.id.tvComfort))
//				.setText(weather.index_comfort);
//		((TextView) indexView.findViewById(R.id.tvChenlian))
//				.setText(weather.index_chenlian);
//		((TextView) indexView.findViewById(R.id.tvGuomin))
//				.setText(weather.index_guomin);
//
//		forcastLLayout.removeAllViews();
//		forcastLLayout.addView(indexView, new LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	private void initTaobaoView() {
//		initTabs(btnTaobao);
//
//		LinearLayout forcastLLayout = (LinearLayout) findViewById(R.id.forcastLLayout);
//		if (taobaoView != null) {
//			forcastLLayout.removeAllViews();
//			forcastLLayout.addView(taobaoView, new LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//			return;
//		}
//		LayoutInflater inflater = (LayoutInflater) mContext
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		taobaoView = (LinearLayout) inflater.inflate(R.layout.taobao, null);
//		indexView.setOnClickListener(this);
//
//		forcastLLayout.removeAllViews();
//		forcastLLayout.addView(taobaoView, new LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	@Override
	public void onClick(View v) {
		if (weather == null)
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
		case R.id.btnTaobao:
			initTaobaoView();
			break;
//		case R.id.adview:
//			mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
//					.parse(url)));
//			break;
		case R.id.rlTodayInfo:
			showToast(mContext.getString(R.string.updating));
			getWeatherFromNet();
			break;
		default:
			break;
		}

	}

	public void initTabs(View view) {
		(btnForcast).setBackgroundResource(R.drawable.tab_normal);
		(btnChart).setBackgroundResource(R.drawable.tab_normal);
		(btnIndexs).setBackgroundResource(R.drawable.tab_normal);
		(btnTaobao).setBackgroundResource(R.drawable.tab_normal);

		view.setBackgroundResource(R.drawable.tab_select);
	}

	private void getWeatherFromNet() {
		if (isLoading)
			return;

		isLoading = true;
//		AsyncHttpClient.getInstance().get(ConstUtils.weather_url + cityCode,
//				callback);
	}

	public void setCityCode(String code) {

//		cityCode = code;
//		String data = ConstUtils.isDataOutTime(mContext, cityCode);
//		if (data != null && !"".equals(data)) {
//
//			try {
//				weather = new WeatherInfo();
//				weather.parse(data, getContext());
//				ConstUtils.log("cache weather", weather.city);
//			} catch (Exception e) {
//				// ConstUtils.saveBugs(mContext, e);
//				getWeatherFromNet();
//				return;
//			}
//
//			initViews();
//
//			// 尝试更新天气
//			int update = Integer.parseInt(ConstUtils.getValueByKey(mContext,
//					SettingsActivity.KEY_UPDATE, "1"));
//			// 手动刷新，不更新天气
//			if (update != 5)
//				getWeatherFromNet();
//		} else {
//			weather = ConstUtils.getWeatherInfo(mContext, cityCode);
//			initViews();
//			getWeatherFromNet();
//		}

	}

	public void setError() {
		// showToast("天气更新失败");
		isLoading = false;
		// TextView todayDesc = (TextView) findViewById(R.id.todayDesc);
		// todayDesc.setText("网络超时，请点击屏幕，尝试刷新");
		findViewById(R.id.rlTodayInfo).setOnClickListener(this);
	}

	/*
	 * @Override public void onWeatherSuccess(String code, int widget_ID) {
	 * isLoading = false; WeatherInfo[] weathers =
	 * HTTPUtils.getWeathersFromDB(mContext, code); if (weathers6 != null) {
	 * String oldTime = weathers6[0].date_y + " " + weathers6[0].Time; String
	 * newTime = weathers[0].date_y + " " + weathers[0].Time;
	 * 
	 * SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	 * sf.setTimeZone(TimeZone.getDefault()); Date d1 = new Date(); Date d2 =
	 * new Date(); try { d1 = sf.parse(oldTime.trim()); d2 =
	 * sf.parse(newTime.trim()); } catch (Exception e) { } if (d1.before(d2)) {
	 * weathers6 = weathers; indexView = null; chartView = null; initViews(); }
	 * } else { weathers6 = weathers; initViews(); } }
	 * 
	 * @Override public void onWeatherError(String code, int widget_ID) {
	 * isLoading = false; showToast("天气更新失败，显示为旧数据"); }
	 */

	private void showToast(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}
}
