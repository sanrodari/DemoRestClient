package com.example.demorestclient;

public class Song {

	private String name;
	
	private String url;
	
	private String id;
	
	@Override
	public String toString() {
		return String.format("Nombre: %s, URL: %s", name, url);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
