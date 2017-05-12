package com.td.truyenbuavd.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	static InputStream is = null;

	static JSONObject jObj = null;

	static String json = "";

	private int timeout = 7;

	HttpResponse httpResponse;

	// constructor
	public JSONParser() {
	}

	public String getJSONFromUrl(String url, String method,
			List<NameValuePair> nameValuePairs) {

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpParams httpParams = httpClient.getParams();
			httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
					timeout * 1000);
			httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT,
					timeout * 1000);
			if (method.equals("post")) {
				HttpPost httpPost = new HttpPost(url);
				if (nameValuePairs != null) {
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				}
				httpResponse = httpClient.execute(httpPost);

			} else {
				HttpGet httpGet = new HttpGet(url);
				httpResponse = httpClient.execute(httpGet);
			}

			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		return json;
	}

}
