/*
 * Copyright (c) 2016.
 * Ramil Zainullin
 * zainullinramil@gmail.com
 * +7 963 306-16-01
 *
 */

package ru.ramil.JSON_test;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by user on 24.03.2016.
 */
public class DataListLoader extends AsyncTaskLoader<ArrayList<Hotel>> {

    public static String LOG_TAG = "my_log";
    public static String adressJSONmain = "https://dl.dropboxusercontent.com/u/9270000/";
    public static String mainJSONList = "0777.json";

    ArrayList<Hotel> arrHotel;
    JSONTask jsonTask;


    public DataListLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Hotel> loadInBackground() {
        Log.d(LOG_TAG, "DataListLoader.loadInBackground");
        //Парсим json-строку и заполняем массив объектов Hotel
        arrHotel = new ArrayList<Hotel>();
        JSONArray dataJsonArr = null;
        Hotel iHotel;

        String strJson = jsonGet(mainJSONList);
        String strJsonItem;
        String image;
        double lat, lon;

        if (strJson != null) {

            try {
                dataJsonArr = new JSONArray(strJson);

                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject itemHotel = dataJsonArr.getJSONObject(i);


                    strJsonItem = jsonGet(itemHotel.getString("id") + ".json");

                    if (strJsonItem != "") {
                        JSONObject itemJson = new JSONObject(strJsonItem);
                        image = itemJson.getString("image");
                        lat = itemJson.getDouble("lat");
                        lon = itemJson.getDouble("lon");
                    }else {
                        image = "null";
                        lat = 0;
                        lon = 0;
                    }

                    iHotel = new Hotel(i+1, itemHotel.getString("name"), itemHotel.getLong("id"), image, itemHotel.getString("address"), itemHotel.getDouble("stars"), itemHotel.getDouble("distance"), itemHotel.getString("suites_availability"), 0, lat, lon);

                    arrHotel.add(iHotel);

                    Log.d(LOG_TAG, "id: " + iHotel.getID());
                    Log.d(LOG_TAG, "Название отеля: " + iHotel.getName());
                    Log.d(LOG_TAG, "idhotel: " + iHotel.getIDhotel());
                    Log.d(LOG_TAG, "image: " + iHotel.getImage());
                    Log.d(LOG_TAG, "address: " + iHotel.getAddress());
                    Log.d(LOG_TAG, "stars: " + iHotel.getStars());
                    Log.d(LOG_TAG, "distance: " + iHotel.getDistance());
                    Log.d(LOG_TAG, "lat: " + iHotel.getLat());
                    Log.d(LOG_TAG, "lon: " + iHotel.getLon());
                    Log.d(LOG_TAG, "suites_availability: " + iHotel.getSuitesAv());
                    Log.d(LOG_TAG, "------------");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else Log.d(LOG_TAG, "Строка пуста");

        return arrHotel;
    }

    @Override
    public void deliverResult(ArrayList<Hotel> data) {
        if (isReset()) {
            // Запрос пришел когда загрузчик остановлен.  Нам не нужен результат.
            if (data != null) {
                onReleaseResources(data);
            }
        }
        ArrayList<Hotel> oldApps = data;
        arrHotel = data;

        if (isStarted()) {
            // Если загрузчик в данный момент запущен, мы можем сразу же получить его результаты.
            super.deliverResult(data);
        }

        // На данный момент мы можем освободить ресурсы, связанные со "старой" Apps. Теперь,
        // когда новый результат поставляется мы знаем, что он больше не используется.
        if (oldApps != null) {
            onReleaseResources(oldApps);
        }
    }

    @Override
    protected void onStartLoading() {
        if (arrHotel != null) {
            // Если в настоящее время есть результат, можно доставить его немедленно.
            deliverResult(arrHotel);
        }
        if (takeContentChanged() || arrHotel == null) {
            // Если данные изменились с момента последнего запроса, он был загружен или в настоящее время не доступен, начать загрузку.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // Попытка отменить текущую задачу загрузки, если это возможно.
        cancelLoad();
    }

    @Override
    public void onCanceled(ArrayList<Hotel> data) {
        super.onCanceled(data);
        // мы можем освободить ресурсы, связанные с приложением, если это необходимо.
        onReleaseResources(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        // Убедитесь в том, что загрузчик остановлен
        onStopLoading();

        // мы можем освободить ресурсы, связанные с приложением, если это необходимо.
        if (arrHotel != null) {
            onReleaseResources(arrHotel);
            arrHotel = null;
        }
    }
    protected void onReleaseResources(ArrayList<Hotel> apps) {}

    private class JSONTask extends AsyncTask<String, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(String... urls) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultJson;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);


        }
    }
    // получаем строку JSON c заданного адреса adressJSONmain + название листа
    private String jsonGet(String listjson) {
        jsonTask = null;
        jsonTask = new JSONTask();
        jsonTask.execute(adressJSONmain + listjson);

        String strJson = null;
        try {
            strJson = jsonTask.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return strJson;
    }

}