package com.example.demorestclient.utilities;

import android.util.Log;

import com.example.demorestclient.Constants;
import com.github.kevinsawicki.http.HttpRequest;

public class HttpRequestUtility {
	
	public static final String BASE_URL = "http://10.0.2.2/android-backend/public";
	
	public static HttpRequest delete(String path) {
		return signedRequest(HttpRequest.delete(BASE_URL + path));
	}
	
	public static HttpRequest get(String path) {
		return signedRequest(HttpRequest.get(BASE_URL + path));
	}
	
	public static HttpRequest post(String path) {
		return signedRequest(HttpRequest.post(BASE_URL + path));
	}
	
	public static HttpRequest put(String path) {
		return signedRequest(HttpRequest.put(BASE_URL + path));
	}
	
	public static HttpRequest baseRequest(HttpRequest request) {
		return request
				.accept("application/json")
				.connectTimeout(4000)
				.readTimeout   (4000);
	}
	
	public static HttpRequest signedRequest(HttpRequest request){
		Object token = Session.sessionData.get("token");
		if(token == null) {
			Log.w(Constants.TAG, "No hay una sesión activa.");
			token = "";
		}
		
		return baseRequest(request).authorization(token.toString());
	}
	
	// TODO Utilidad para notificar que no existe una sesión activa cuando
	// se responde un 501.
	
}
