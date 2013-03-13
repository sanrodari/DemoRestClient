package com.example.demorestclient;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;

public class CreateSong extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_song);
	}

	public void createSong(View v) {
		EditText editTextUrl = (EditText) findViewById(R.id.editTextUrl);
		EditText editTextName = (EditText) findViewById(R.id.editTextName);
		
		String url = editTextUrl.getText().toString();
		String name = editTextName.getText().toString();
		
		new CreateSongTask().execute(url, name);
	}
	
	private class CreateSongTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			
			String url = params[0];
			String name = params[1];
			
			Map<String, String> form = new HashMap<String, String>();
			form.put("url", url);
			form.put("name", name);
			
			try {
				HttpRequest httpRequest =
					HttpRequest.post("http://android-backend.pagodabox.com/songs/")
					.form(form)
					.connectTimeout(4000)
					.readTimeout   (4000);
				
				String body = httpRequest.body();
			
				JSONObject jsonObject = new JSONObject(body);
				if(jsonObject.has("id")) {
					return jsonObject.getString("id");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result != null && !TextUtils.isEmpty(result)) {
				Toast.makeText(CreateSong.this, "ID de la nueva canción: " + result,
					Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(CreateSong.this, "Error!", Toast.LENGTH_LONG).show();
			}
			finish();
		}
		
	}
}
