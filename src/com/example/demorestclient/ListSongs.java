package com.example.demorestclient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.demorestclient.utilities.HttpRequestUtility;
import com.github.kevinsawicki.http.HttpRequest;

public class ListSongs extends Activity {

	private ListView listView;
	private AsyncTask<Void, Void, List<Song>> execute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_songs);
		
		listView = (ListView) findViewById(R.id.list);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				Object selectedItem = parent.getAdapter().getItem(position);
				if(selectedItem != null) {
					Song song = (Song) selectedItem;
		
					Intent intent = new Intent(ListSongs.this, SongActivity.class);
					intent.putExtra("id", song.getId());
					startActivity(intent);
				}
				
			}
			
		});
		
		execute = new ListSongsTask().execute();
	}
	
	private class ListSongsTask extends AsyncTask<Void, Void, List<Song>> {

		@Override
		protected List<Song> doInBackground(Void... params) {
				
			HttpRequest httpRequest = HttpRequestUtility
				.get("/songs/");
			
			try {
				String response = httpRequest.body();
				
				JSONArray jsonArray = new JSONArray(response);
				
				List<Song> songs = new ArrayList<Song>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					Song song = new Song();
					song.setName(jsonObject.getString("name"));
					song.setUrl (jsonObject.getString("url"));
					song.setId  (jsonObject.getString("id"));
					
					songs.add(song);
				}
				
				
				return songs;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(List<Song> result) {
			if (result != null) {
				ArrayAdapter<Song> adapter = new ArrayAdapter<Song>(ListSongs.this,
				  android.R.layout.simple_list_item_1, android.R.id.text1, result);
				
				listView.setAdapter(adapter);
			}
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Status status = execute.getStatus();
		if(status == Status.FINISHED) {
			new ListSongsTask().execute();
		}
	}
	
}
