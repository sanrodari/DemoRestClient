package com.example.demorestclient;

import com.example.demorestclient.utilities.Session;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FirstActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_first);
	}

	public void listSongs(View v) {
		Intent intent = new Intent(this, ListSongs.class);
		startActivity(intent);
	}
	
	public void searchSong(View v) {
		Intent intent = new Intent(this, SearchSong.class);
		startActivity(intent);
	}
	
	public void createSong(View v) {
		Intent intent = new Intent(this, CreateSong.class);
		startActivity(intent);
	}
	
	public void goToProfile(View view) {
		Intent intent = new Intent(this, ProfileActivity.class);
		startActivity(intent);
	}
	
	public void closeSession(View view) {
		Session.sessionData.remove("token");
		Session.sessionData.remove("id");
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
