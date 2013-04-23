package com.example.demorestclient;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demorestclient.utilities.HttpRequestUtility;
import com.example.demorestclient.utilities.Session;
import com.github.kevinsawicki.http.HttpRequest;

public class MainActivity extends Activity {

	private static final String LOGIN    = "login";
	private static final String REGISTER = "register";
	private static final String SUCCESS  = "success";
	
	private EditText editTextUsername;
	private EditText editTextPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		editTextUsername = (EditText) findViewById(R.id.editTextUsername);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
	}

	public void login(View view) {
		new SessionsTask().execute(
			LOGIN,
			editTextUsername.getText().toString(),
			editTextPassword.getText().toString());
	}
	
	public void register(View view) {
		new SessionsTask().execute(
			REGISTER,
			editTextUsername.getText().toString(),
			editTextPassword.getText().toString());
	}
	
	private class SessionsTask extends AsyncTask<String, Void, String> {


		@Override
		protected String doInBackground(String... params) {
			
			try {
				String command  = params[0];
				String username = params[1];
				String password = params[2];
				
				Map<String, String> values = new HashMap<String, String>();
				values.put("username", username);
				values.put("password", password);
				
				// Login
				if(command.equals(LOGIN)) {
					HttpRequest httpRequest = HttpRequestUtility
						.post("/sessions")
						.form(values);
					
					String response = httpRequest.body();
					JSONObject jsonObject = new JSONObject(response);
					
					// Inició sesión correctamente.
					if(jsonObject.has("token")){
						Session.sessionData.put("token", jsonObject.getString("token"));
						Session.sessionData.put("id",    jsonObject.getString("id"));
						return SUCCESS;
					}
					else {
						return jsonObject.getString("error");
					}
				}
				// Registro
				else {
					HttpRequest httpRequest = HttpRequestUtility
						.post("/users")
						.form(values);
					
					String response = httpRequest.body();
					JSONObject jsonObject = new JSONObject(response);
					
					// Inició sesión correctamente.
					if(jsonObject.has("token")){
						Session.sessionData.put("token", jsonObject.getString("token"));
						Session.sessionData.put("id",    jsonObject.getString("id"));
						return SUCCESS;
					}
					else {
						return jsonObject.getString("error");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			MainActivity context = MainActivity.this;
			if(result != null && result.equals(SUCCESS)) {
				Intent intent = new Intent(context, FirstActivity.class);
				startActivity(intent);
				finish();
			}
			else {
				result = result == null ? "Error" : result;
				Toast.makeText(context, result, Toast.LENGTH_LONG).show();
			}
		}
	}
	
}
