package com.bekoal.xpense.service;

import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by begon_000 on 4/27/2015.
 */
public class Http {

    public String read(String httpUrl){

        String httpData = "";
        InputStream inputStream = null;
        HttpsURLConnection connection = null;
        Log.w("HTTP_QUERY", "Http begining");
        try {
            URL url = new URL(httpUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);

            connection.setConnectTimeout(10 * 1000);
//            connection.setReadTimeout(10 * 1000);
//            connection.connect();

            inputStream = connection.getInputStream();


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            httpData = stringBuffer.toString();
            bufferedReader.close();
            inputStream.close();
        } catch (Exception e) {
            Log.e("Ex - reading Http url", e.toString());
        } finally {
            connection.disconnect();
        }
        Log.w("HTTP_QUERY", "Http complete");
        return httpData;
    }
}
