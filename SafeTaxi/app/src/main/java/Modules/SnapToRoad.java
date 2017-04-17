package Modules;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZIPPER on 4/11/2017.
 */

public class SnapToRoad {
    private static final String DIRECTION_URL_API = "https://roads.googleapis.com/v1/snapToRoads?path=";
    private static final String GOOGLE_API_KEY = "&interpolate=true&key=AIzaSyCaQQKFYXOWltKhlQyA-C_DRmniF1BdRig\t";
    private SnapToRoadListener listener;
    private String txt;
    public int len;

    public SnapToRoad(SnapToRoadListener listener, String txt) {
        this.listener = listener;
        this.txt = txt;

    }



    public void execute() throws UnsupportedEncodingException {
        listener.onSnapToRoadStart();
        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(txt, "utf-8");
        return DIRECTION_URL_API + txt + GOOGLE_API_KEY;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String res) {


            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSon(String data) throws JSONException {
        if (data == null)
            return;

        List<Location> Snap = new ArrayList<>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonSnapToRoad = jsonData.getJSONArray("snappedPoints");
        len = jsonSnapToRoad.length();
        for(int i=0;i<jsonSnapToRoad.length();i++)
        {
            Location location = new Location();
           // JSONObject JsonLongtitide = jsonSnapToRoad.getJSONObject("location").getDouble("latitude");
            Double JsonLongtitude  = jsonSnapToRoad.getJSONObject(i).getJSONObject("location").getDouble("longitude");
            Double JsonLattitude  = jsonSnapToRoad.getJSONObject(i).getJSONObject("location").getDouble("latitude");

            location.location = addList(JsonLattitude,JsonLongtitude);
//            for(int j=0;i<JsonLocation.length();j++)
//            {
//
//                location.location = addList(JsonLocation.getDouble("latitude"), JsonLocation.getDouble("longitude"));
//
//            }


            //Log.d("latlong" ,JsonLattitude+""+JsonLongtitude);
            //location.location = addList(JsonLattitude,JsonLongtitude);
            Snap.add( location);



        }
        listener.onSnapToRoadSuccess(Snap);



    }

    private List<LatLng> addList(Double JsonLattitude, Double JsonLongtitude) {

        List<LatLng> decoded = new ArrayList<>();

           LatLng x =  new LatLng(JsonLattitude , JsonLongtitude);
            decoded.add(x);


       // Log.d("array", String.valueOf(decoded));


        return decoded;
    }


}
