package com.example.demorestclient;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.kevinsawicki.http.HttpRequest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateSong extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_song);
	}

	public void createSong(View v) {
		EditText editTextUrl = (EditText) findViewById(R.id.editTextUrl);
		EditText editTextName = (EditText) findViewById(R.id.editTextName);
		
		Map<String, String> form = new HashMap<String, String>();
		form.put("url", editTextUrl.getText().toString());
		form.put("name", editTextName.getText().toString());
		
		HttpRequest httpRequest =
			HttpRequest.post("http://android-backend.pagodabox.com/songs/")
			.form(form)
			.connectTimeout(4000)
			.readTimeout   (4000);
		
		String body = httpRequest.body();
		
		try {
			JSONObject jsonObject = new JSONObject(body);
			if(jsonObject.has("id")) {
				Toast.makeText(this, "ID de la nueva canción: " + jsonObject.getString("id"),
					Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
