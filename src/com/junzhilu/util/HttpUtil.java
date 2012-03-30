/**
 * 
 */
package com.junzhilu.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * @author eureka
 * 
 */
public class HttpUtil {

	public static InputStreamReader urlConn(URL url) {
		InputStreamReader isr = null;
		try {
			// HttpURLConnection
			HttpURLConnection httpconn = (HttpURLConnection) url
					.openConnection();
			if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// InputStreamReader
				isr = new InputStreamReader(httpconn.getInputStream(), "utf-8");
				int i;
				String content = "";
				// read
				while ((i = isr.read()) != -1) {
					content = content + (char) i;
				}
				isr.close();
			}
			// disconnect
			httpconn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isr;
	}

	public static String httpClientConn(String url) {
		String content = null;
		// DefaultHttpClient
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// HttpGet
		HttpGet httpget = new HttpGet(url);
		// ResponseHandler
		BasicResponseHandler responseHandler = new BasicResponseHandler();

		try {
			content = httpclient.execute(httpget, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return content;
	}

	public static Bitmap GetBitMapByUrl(String url) {
		Bitmap returnBitmap = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("microbook-http", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
			}
			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				FlushedInputStream fis = null;
				try {
					inputStream = entity.getContent();
					fis = new FlushedInputStream(inputStream);
					// return BitmapFactory.decodeStream(inputStream);
					// Bug on slow connections, fixed in future release.
					returnBitmap = BitmapFactory.decodeStream(fis);
				} finally {
					if (fis != null) {
						fis.close();
					}
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return returnBitmap;
	}

	/*
	 * An InputStream that skips the exact number of bytes provided, unless it
	 * reaches EOF.
	 */
	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}
}
