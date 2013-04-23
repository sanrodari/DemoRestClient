package com.example.demorestclient;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demorestclient.utilities.HttpRequestUtility;
import com.example.demorestclient.utilities.Session;
import com.github.kevinsawicki.http.HttpRequest;

public class ProfileActivity extends Activity {

	private static final String GET = "get";
	private static final String PUT = "put";
	
	private TextView textViewUsername;
	private EditText editTextNewPassword;
	private EditText editTextPasswordConfirmation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		String id = Session.sessionData.get("id").toString();
		new ProfileTask().execute(GET, id);
		
		textViewUsername = (TextView) findViewById(R.id.textViewUsername);
		editTextNewPassword = (EditText) findViewById(R.id.editTextNewPassword);
		editTextPasswordConfirmation = (EditText) findViewById(R.id.editTextPasswordConfirmation);
	}
	
	public void changePassword(View view) {
		if(validateForm()) {
			String id = Session.sessionData.get("id").toString();
			new ProfileTask().execute(PUT, id, editTextPasswordConfirmation.getText().toString());
		}
		else {
			Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
		}
	}
	
	private class ProfileTask extends AsyncTask<String, Void, String> {

		private String method;

		@Override
		protected String doInBackground(String... params) {
			try {
				method = params[0];
				String id = params[1];
				// Si se debe obtener la información.
				if(method.equals(GET)) {
					String response = HttpRequestUtility.get("/users/" + id).body();
					JSONObject jsonObject = new JSONObject(response);
					
					if(jsonObject.has("username")) {
						return jsonObject.getString("username"); 
					}
				}
				// Si se trata de actualizar la contraseña.
				else if(method.equals(PUT)){
					String newPassword = params[2];
					HttpRequest put = HttpRequestUtility.put("/users/" + id).send("password=" + newPassword);
					String response = put.body();
					
					JSONObject jsonObject = new JSONObject(response);
					
					// Se configura el nuevo token en la sesión.
					if(jsonObject.has("token")) {
						return jsonObject.getString("token");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result != null && method.equals(GET)) {
				textViewUsername.setText(result);
			}
			else if(method.equals(PUT)) {
				Session.sessionData.put("token", result);
				Toast.makeText(ProfileActivity.this,
					"Contraseña modificada correctamente", Toast.LENGTH_LONG).show();
				finish();
			}
		}
		
	}
	
	private boolean validateForm() {
		// TODO Implementar una mejor validación de password.
		String newPassword          = editTextNewPassword.getText().toString();
		String passwordConfirmation = editTextPasswordConfirmation.getText().toString();
		return newPassword.equals(passwordConfirmation);
	}

}
