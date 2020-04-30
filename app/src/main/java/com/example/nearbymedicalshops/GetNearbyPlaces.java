package com.example.nearbymedicalshops;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlaces extends AsyncTask<Object,String,String > {
    private String googleplaceData,url;
    private GoogleMap mMap;
    @Override
    protected String doInBackground(Object... objects){
        mMap=( GoogleMap) objects[0];
        url = (String)objects[1];
        Log.d("GetNearbyPlaces",url);
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleplaceData=downloadUrl.ReadTheURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleplaceData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearbyPlaceList = null;
        DataParser dataParser = new DataParser();
        nearbyPlaceList = dataParser.parse(s);
        DisplayNearbyPlaces(nearbyPlaceList);
    }
    private void DisplayNearbyPlaces(List<HashMap<String,String>> nearbyPlaceList){
        for(int i = 0; i<nearbyPlaceList.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String,String> googleNearbyPlace = nearbyPlaceList.get(i);
            String nameofPlaces = googleNearbyPlace.get("place_name");
            String vicinity = googleNearbyPlace.get("vicinity");
            Double lat = Double.parseDouble(googleNearbyPlace.get("lat"));
            Double lng = Double.parseDouble(googleNearbyPlace.get("lng"));
            LatLng latLng= new LatLng(lat,lng);
            markerOptions.position(latLng);
            markerOptions.title(nameofPlaces + " : "+ vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
