package com.adeoluwa.android.bakingapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import com.adeoluwa.android.bakingapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Merlyne on 6/20/2017.
 */

public class NetworkUtils {
    static String RECIPE_BASE_URL;
    static String PARAM_API;

    public static URL buildUrl(Context context) {
        RECIPE_BASE_URL = context.getString(R.string.base_url);
        PARAM_API = context.getString(R.string.network_resource_param);

        Uri builtUri = Uri.parse(RECIPE_BASE_URL).buildUpon()
                .appendEncodedPath(PARAM_API)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    @SuppressLint("NewApi")
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        StringBuilder builder;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            int response = urlConnection.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK)
            {
                builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream())))
                {
                    String line;
                    while ((line = reader.readLine()) != null){
                        builder.append(line);
                    }
                }
                return builder.toString();
            }
        }catch (Exception e)
        {
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }
}
