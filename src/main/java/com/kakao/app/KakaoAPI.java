package com.kakao.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;




public class KakaoAPI {
	
	public String getAccessToken(String code) {
		String accessToken = "";
		String refreshToken = "";
		String reqURL = "https://kauth.kakao.com/oauth/token";
		
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			StringBuilder sb = new StringBuilder();
			sb.append("grant_type=authorization_code");
			sb.append("&client_id=cd1ada7163660ca63c0cbaeff479524f");
			sb.append("&redirect_uri=http://localhost:8080/login");
			sb.append("&code="+code);
			
			bw.write(sb.toString());
			bw.flush();
			
			int responseCode = conn.getResponseCode();
			System.out.println("response code = " + responseCode);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line = "";
			String result = "";
			while((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("response body="+result);
			
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(result);
			
			accessToken = element.getAsJsonObject().get("access_token").getAsString();
			refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
			
			br.close();
			bw.close();
			
			
			
		} catch (Exception e) {
			System.out.println("kakaoAPI클래스의 getAccessToken메소드 에러발생");
			e.printStackTrace();
		}
		return accessToken;
	}
	
	public HashMap<String, Object> getUserInfo(String accessToken) {
		
			HashMap<String, Object> userInfo = new HashMap<String, Object>();
			String reqUrl = "https://kapi.kakao.com/v2/user/me";
			
			try {
				URL url = new URL(reqUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Authorization", "Bearer " + accessToken);
				int responseCode = conn.getResponseCode();
				System.out.println("responseCode = " + responseCode);
				
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				String line = "";
				String result = "";
				while((line = br.readLine()) != null) {
					result += line;
				}
				System.out.println("response body ="+result);
				
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(result);
				
				JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
				JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
				
				String nickname = properties.getAsJsonObject().get("nickname").getAsString();
				String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
				String gender = kakaoAccount.getAsJsonObject().get("gender").getAsString();
				String birthday = kakaoAccount.getAsJsonObject().get("birthday").getAsString();
				String agerange = kakaoAccount.getAsJsonObject().get("age_range").getAsString();
				
				
				userInfo.put("nickname", nickname);
				userInfo.put("email", email);
				userInfo.put("gender", gender);
				userInfo.put("birthday", birthday);
				userInfo.put("agerange", agerange);
				
			} catch (Exception e) {
				System.out.println("kakaoAPI클래스의 getUserInfo메소드 에러발생");
				e.printStackTrace();
			}
			return userInfo;
		}

	public void kakaoLogout(String accessToken) {
		String reqURL= "http://kapi.kakao.com/v1/user/logout";
		
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode = " + responseCode);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line = "";
			String result = "";
			while((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println(result);
			
		} catch (Exception e) {
			System.out.println("kakaoAPI클래스의 kakaoLogout메소드 에러발생");;
			e.printStackTrace();
		}
	}



		

	
}
