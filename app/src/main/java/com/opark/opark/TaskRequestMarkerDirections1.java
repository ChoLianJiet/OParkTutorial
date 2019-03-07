package com.opark.opark;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;


public class TaskRequestMarkerDirections1 implements Callable<LatLng> {

    private static final String TAG = "TaskRequestMarkerDirect";
    private LatLng mLatLng;

    @Override
    public LatLng call() throws Exception {
        return mLatLng;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    public class TaskRequestMarkerDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "doInBackground: Success! JSON is " + responseString);
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParserMarker taskParser = new TaskParserMarker();
            taskParser.execute(s);

        }
    }

    public class TaskParserMarker extends AsyncTask<String, Void, LatLng> {
        @Override
        protected LatLng doInBackground(String... strings) {
            JSONObject jsonObject = null;
            LatLng routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                RoadsParser roadsParser = RoadsParser.fromJson(jsonObject);
                routes = roadsParser.getLatLng();
//                Log.d(TAG, "doInBackground: JSON do in background is " + jsonObject);
//                String gsonLatLng = (new Gson().fromJson(jsonObject, LatLng.class));
//                RoadsParser roadsParser = new RoadsParser();
//                routes.add((List<HashMap<Double, Double>>) roadsParser.parse(jsonObject));
                Log.d(TAG, "doInBackground: routes are " + routes);
                return routes;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            Log.d(TAG, "onPostExecute: latlong to plot own marker is " + latLng);
//            executePlotOwnMarker(latLng);
            setLatLng(latLng);
        }
    }

    public void setLatLng(LatLng latLng) {
        Log.d(TAG, "setLatLng: latlng is " + latLng);
        mLatLng = latLng;
        Log.d(TAG, "setLatLng: mLatLng is now " + mLatLng);
        getLatLng();
    }

    public LatLng getLatLng() {
        Log.d(TAG, "getLatLng: latlng is " + mLatLng);
        return mLatLng;
    }
}
