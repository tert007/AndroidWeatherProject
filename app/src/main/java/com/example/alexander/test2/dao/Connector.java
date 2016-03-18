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

    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 150000;

    protected static String executeRequest(String request) throws DaoException {

        String response = "";
        URL url;
        BufferedReader bufferedReader = null;

        try {
            url = new URL(request);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }

            return response;
        }
        catch (IOException ex){
            throw new DaoException("Connection exception: " + ex);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                throw new DaoException("Buffered Reader can't be closed: " + ex);
            }
        }

    }
}
