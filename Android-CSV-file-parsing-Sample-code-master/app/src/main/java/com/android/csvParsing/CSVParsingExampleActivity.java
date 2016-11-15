package com.android.csvParsing;

import java.io.BufferedInputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.util.ByteArrayBuffer;

import au.com.bytecode.opencsv.CSVReader;

// class that parse the category.csv stored in assets file and display all category in spinner
public class CSVParsingExampleActivity extends Activity implements
		OnItemSelectedListener {
	/** Called when the activity is first created. */
	private Spinner spMainSelectCategory;
	private TextView tvMainSelectedCate;
	private ArrayList<String> categoryList = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		spMainSelectCategory = (Spinner) findViewById(R.id.spMainSelectCategory);
		tvMainSelectedCate = (TextView) findViewById(R.id.tvMainSelectedCate);


		List<String[]> list = new ArrayList<String[]>();
		String next[] = {};

		int responseCode;
		URL address = null;
		try {
			address = new URL("http://aqi.indiaspend.org/aq/api/aqfeed/868004022710761/27-07-2016/?format=csv");

			HttpURLConnection conn = (HttpURLConnection) address.openConnection();
			responseCode=conn.getResponseCode();
			Log.d("Data CSV", String.valueOf(responseCode));
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.connect();

			Log.d("Data CSV", "connected");
			InputStream is = conn.getInputStream();
			Log.d("Data CSV", "reading");

			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer bab = new ByteArrayBuffer(64);
			int current = 0;

			while((current = bis.read()) != -1) {
				Log.d("Data CSV", "reading");
				bab.append((byte)current);
			}
			Log.d("Data CSV", "reading");

		} catch (Exception e) {
			Log.d("not_running", "not_running");

			e.printStackTrace();
		}

		try {
			InputStreamReader csvStreamReader = new InputStreamReader(
					CSVParsingExampleActivity.this.getAssets().open(
							"Category.csv"));

			CSVReader reader = new CSVReader(csvStreamReader);
			for (;;) {
				next = reader.readNext();
				if (next != null) {
					list.add(next);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {
			categoryList.add(list.get(i)[1]);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				categoryList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spMainSelectCategory.setAdapter(adapter);
		spMainSelectCategory.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		tvMainSelectedCate.setText("You have selected:"
				+ categoryList.get(arg2)+" Category");

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}