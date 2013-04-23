package com.example.demorestclient;

import java.net.MalformedURLException;
import java.net.URL;

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
import com.github.kevinsawicki.http.HttpRequest;

public class SearchSong extends Activity {
	
	private Song song;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_song);
	}

	public void searchSong(View v) {
		EditText editTextId = (EditText) findViewById(R.id.editTextId);
		new SearchSongTask().execute(editTextId.getText().toString()); 
	}
	
	private class SearchSongTask extends AsyncTask<String, Void, Song> {

		@Override
		protected Song doInBackground(String... params) {
			String id = params[0];
			
			HttpRequest httpRequest = HttpRequestUtility
				.get("/songs/" + id);
			
			String response;
			try {
				response = httpRequest.body();
				
				JSONObject jsonObject = new JSONObject(response);
				if(jsonObject.has("name")) {
					Song song = new Song();
					song.setName(jsonObject.getString("name"));
					song.setUrl (jsonObject.getString("url"));
					return song;
				}
				else {
					return null;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Song result) {
			if (result != null) {
				song = result;
				TextView textViewName = (TextView) findViewById(R.id.textViewName);
				textViewName.setText(result.getName());
			}
			else {
				Toast.makeText(SearchSong.this, "No se encuentró.", Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	public void navigate(View v) {
		if(song != null) {
			
			try {
				new URL(song.getUrl());
				
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(song.getUrl()));
					
					startActivity(browserIntent);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				
				Toast.makeText(this, "Url mal formada", Toast.LENGTH_LONG).show();
			}
		}
	}
	
}
