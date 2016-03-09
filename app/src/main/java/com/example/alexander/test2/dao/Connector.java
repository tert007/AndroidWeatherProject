package com.example.alexander.test2.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.String;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Alexander on 28.02.2016.
 */
public class Connector {

    protected static String executeRequest(String request) throws DaoException {

        String response = "";
        URL url = null;
        BufferedReader bufferedReader = null;

        try {
            url = new URL(request);
            /// Мусор
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }

            return response;
        }
        catch (Exception ex){
            throw new DaoException(ex);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                throw new DaoException(ex);
            }
        }

    }
}
