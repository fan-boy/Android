package com.example.address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView addr;
	Button get;
	static double latitude;
	static double longitude;
	//private ProgressDialog pDialog;
	private static String url = "";
	String jsonStr;
	String finalAddress;
	JSONArray address = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addr = (TextView) findViewById(R.id.addr);
		get = (Button) findViewById(R.id.get);

		get.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GPSTracker gps = new GPSTracker(MainActivity.this);
				if (gps.canGetLocation()) {

					latitude = gps.getLatitude();
					longitude = gps.getLongitude();
				}

				if (!(latitude == 0 && longitude == 0)) {
					url = "http://maps.google.com/maps/api/geocode/json?latlng="
							+ latitude + "," + longitude + "&sensor=true";
					new GetAddress().execute();

				}
				addr.setText("Mumbai");
			}
		});
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetAddress extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					address = jsonObj.getJSONArray("results");

					JSONObject c = address.getJSONObject(0);

					finalAddress = c.getString("formatted_address");

					// Getting JSON Array node

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			/**
			 * Updating parsed JSON data into ListView
			 * */
			addr.setText(finalAddress);
		}

	}

}
