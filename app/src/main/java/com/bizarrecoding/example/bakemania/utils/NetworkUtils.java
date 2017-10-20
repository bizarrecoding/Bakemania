package com.bizarrecoding.example.bakemania.utils;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static URL getURL(){
        URL url = null;
        try {
            url = new URL(BASE_URL);
        } catch (Exception e) {
            Log.e("ERROR",e.getMessage());
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }catch (Exception e){
            Log.e("ERROR",e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}
