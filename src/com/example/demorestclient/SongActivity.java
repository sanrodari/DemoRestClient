package com.example.demorestclient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demorestclient.utilities.HttpRequestUtility;

public class SongActivity extends Activity {

	private static final String GET = "get";
	private static final String PUT = "put";
	
	private TextView textViewName;
	
	private EditText editTextName;
	private EditText editTextUrl;
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_song);
		
		textViewName = (TextView) findViewById(R.id.textViewName);
		
		editTextName = (EditText) findViewById(R.id.editTextName);
		editTextUrl  = (EditText) findViewById(R.id.editTextUrl);
		
		Bundle extras = getIntent().getExtras();
		id = extras.getString("id");
		if(extras != null && extras.containsKey("id")) {
			new SongTask().execute(GET, id);
		}
	}
	
	public void changeSong(View view) {
		new SongTask().execute(
			PUT, id,
			editTextName.getText().toString(),
			editTextUrl .getText().toString());
	}
	
	private class SongTask extends AsyncTask<String, Void, Song> {

		private String method;

		@Override
		protected Song doInBackground(String... params) {
			try {
				method = params[0];
				String id = params[1];
				
				String response = "";
				if(method.equals(GET)) {
					response = HttpRequestUtility.get("/songs/" + id).body();
				}
				else if(method.equals(PUT)) {
					String name = params[2];
					String url  = params[3];

					Map<String, String> form = new HashMap<String, String>(2);
					form.put("name", name);
					form.put("url" , url);
					
					response = HttpRequestUtility.put("/songs/" + id).form(form).body();
					
					System.out
							.println("SongActivity.SongTask.doInBackground() " + response);
				}
				
				JSONObject jsonObject = new JSONObject(response);
				
				Song song = new Song();
				song.setName(jsonObject.getString("name"));
				song.setUrl (jsonObject.getString("url"));
				song.setId  (jsonObject.getString("id"));
				return song;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(final Song result) {
			if(result != null) {
				textViewName.setText(result.getName());
				textViewName.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							new URL(result.getUrl());

							Intent browserIntent = new Intent(
									Intent.ACTION_VIEW,
									Uri.parse(result.getUrl()));

							startActivity(browserIntent);
						} catch (MalformedURLException e) {
							Toast.makeText(SongActivity.this, "Url mal formada",
									Toast.LENGTH_LONG).show();
						}						
					}
				});
				
				editTextName.setText(result.getName());
				editTextUrl .setText(result.getUrl());
			}
			
			if(method.equals(PUT) && result != null) {
				Toast.makeText(SongActivity.this, "Canción actuliazada exitosamente.", Toast.LENGTH_LONG).show();
				finish();
			}
		}
		
	}
	


}
