package com.victorsaico.denunciastecsup.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.victorsaico.denunciastecsup.R;
import com.victorsaico.denunciastecsup.fragments.AddDenunciaFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UbicacionActivity extends AppCompatActivity implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, OnMapReadyCallback, LocationListener {
    private static final String TAG = UbicacionActivity.class.getSimpleName();

    private GoogleMap mMap;

    private SharedPreferences sharedPreferences;
    private FloatingActionButton btnregistrar;

    private double latitud = 0;
    private double longitud = 0;
    private String address;

    double lat, lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapubicacion);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onMapClick(LatLng point) {
        mMap.clear();

        Geocoder geocoder = new Geocoder(getApplicationContext());

        mMap.addMarker(new MarkerOptions()
                .position(point)
                .title("Escena del crimen"));

        latitud = point.latitude;
        longitud = point.longitude;

        Log.d(TAG, "LatLng: " + latitud + ", "+ longitud);
        new GetAddress().execute(String.format("%.4f,%.4f",latitud,longitud));
    }

    @Override
    public void onMapLongClick(LatLng point) {
        mMap.clear();

        mMap.addMarker(new MarkerOptions()
                .position(point)
                .title("Aqui te rechazo :'v"));

        latitud = point.latitude;
        longitud = point.longitude;

        Log.d(TAG, "LatLng: " + latitud + ", "+ longitud);
    }

    public void registrarubicacion(View view) {
        Intent intent = new Intent(this, AddDenunciaFragment.class);
        intent.putExtra("Ubicacion", address);
        startActivity(intent);
    }

    private class GetAddress extends AsyncTask<String,Void,String> {

        ProgressDialog dialog = new ProgressDialog(UbicacionActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                double lat = Double.parseDouble(strings[0].split(",")[0]);
                double lng = Double.parseDouble(strings[0].split(",")[1]);
                String response;
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%.4f,%.4f&sensor=false",lat,lng);
                response = http.GetHTTPData(url);
                return response;
            }
            catch (Exception ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);

                address = ((JSONArray)jsonObject.get("results")).getJSONObject(0).get("formatted_address").toString();


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(dialog.isShowing())
                dialog.dismiss();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            setUpMap();
        }

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng tecsup = new LatLng(-12.044169, -76.952898);
        float zoomlevel = 12;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tecsup, zoomlevel));
    }
    private void setUpMap()
    {
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }
}
