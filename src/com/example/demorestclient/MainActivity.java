package com.example.demorestclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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

}
