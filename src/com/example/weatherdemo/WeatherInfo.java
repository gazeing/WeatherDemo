package com.example.weatherdemo;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;




public class WeatherInfo implements Serializable {
	/*
	 * { current: { city: "朝阳", cityid: "101010300", temp_now: "24", wind:
	 * "东风:2级", humidity: "73%", time: "13:45", date: "2013年6月2日",
	 * index_chuanyi: "舒适", index_uv: "弱", index_xiche: "不宜", index_comfort:
	 * "较舒适", index_chenlian: "较不宜", index_guomin: "不易发" }, forecasts: [ { temp:
	 * "27℃~18℃", weather: "雷阵雨转阴", img: "4", wind: "微风" }, ... ] }
	 */
	private static final long serialVersionUID = -3241230451246355714L;
	public String city;// *
	public String cityid;
	public String temp_now;// *
	public String wind;// *
	public String humidity;// *
	public String time;// *
	public String date;
	public String temp;
	public String weather;
	public String img;
	// 指数
	public String index_chuanyi;
	public String index_uv;
	public String index_xiche;
	public String index_comfort;
	public String index_chenlian;
	public String index_guomin;
	public ArrayList<Forecasts> forcasts = new ArrayList<Forecasts>();

	public static class Forecasts {
		public String temp;
		public String weather;
		public String img;
		public String wind;

		public Forecasts(JSONObject json) {
			temp = json.optString("temp").replaceFirst("℃", "").replace("~", "/");
			weather = json.optString("weather");
			img = json.optString("img");
			wind = json.optString("wind");
		}
		
		public void translate(Context context) {
			weather = weather.replace(context.getString(R.string.sunny), "Sunny")
					.replace(context.getString(R.string.cloudy), "Cloudy")
					.replace(context.getString(R.string.drizzle), "Drizzle")
					.replace(context.getString(R.string.light_rain), "Light Rain")
					.replace(context.getString(R.string.heavy_rain), "Heavy Rain")
					.replace(context.getString(R.string.showers), "Showers")
					.replace(context.getString(R.string.overcast), "Overcast");
			wind = wind.replace(context.getString(R.string.not_available), "N/A");
			img = img.replace(context.getString(R.string.not_available), "N/A");
		}
		
		public Forecasts() {
		}
	}

	public void parse(String content, Context context) throws JSONException {

			JSONObject json = new JSONObject(content);
			JSONObject jsonc = json.getJSONObject("current");
			city = jsonc.optString("city");
			cityid = jsonc.optString("cityid");
			String[] aArray = context.getResources().getStringArray(R.array.p20004);
			final int nSize = (null != aArray ? aArray.length : 0);
			for( int nIdx = 0; nIdx < nSize; nIdx++ ) {
				String strVal = aArray[nIdx];
				if( strVal.startsWith(cityid) ) {
					// Get the city.
					int nPos = strVal.indexOf(":");
					city = strVal.substring(nPos + 1);
					
					break;
				}
			}
			
			temp_now = jsonc.optString("temp").replace(context.getString(R.string.not_available), "N/A");
			wind = (jsonc.optString("WD")+":"+jsonc.optString("WS")).replace(context.getString(R.string.not_available), "N/A");
			humidity = jsonc.optString("SD");
			humidity = humidity.replace(context.getString(R.string.not_available), "N/A");
			time = jsonc.optString("time");
			date = jsonc.optString("date_y");

			index_chuanyi = jsonc.optString("index_chuanyi");
			index_uv = jsonc.optString("index_uv");
			index_xiche = jsonc.optString("index_xiche");
			index_comfort = jsonc.optString("index_comfort");
			index_chenlian = jsonc.optString("index_chenlian");
			index_guomin = jsonc.optString("index_guomin");
			JSONArray fcasts = json.getJSONArray("forecasts");
			int size = fcasts.length();
			for (int i = 0; i < size; i++) {
				Forecasts entity = new Forecasts(fcasts.getJSONObject(i));
				forcasts.add(entity);
				entity.translate(context);
			}
			
			temp = forcasts.get(0).temp;
			weather = forcasts.get(0).weather;
			img = forcasts.get(0).img;
	}

}
