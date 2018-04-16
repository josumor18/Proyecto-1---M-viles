package me.flux.fluxme.Business;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GPSFragment extends Fragment implements OnMapReadyCallback {

    Runnable r;
    final Handler handler  = new Handler();

    GoogleMap mMap;
    SupportMapFragment mapFragment;
    LocationManager locationManager;
    LocationListener locationListener;
    public ArrayList<LatLng> locations = new ArrayList<LatLng>();

    public GPSFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(r);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_gps, container, false);
        View v = inflater.inflate(R.layout.fragment_gps, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fr = getFragmentManager();
            FragmentTransaction ft = fr.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);





        r = new Runnable() {
            public void run() {
                ExecuteGetLocations loc = new ExecuteGetLocations();
                loc.execute();

                handler.postDelayed(this, 30000);
            }
        };

        handler.postDelayed(r, 1000);


        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
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
        };

        getMyLocation(true);
    }

    private void getMyLocation(boolean init){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            mMap.clear();

            LatLng location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

            mMap.addMarker(new MarkerOptions().position(location).title("Tú"));

            if(init){
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            }
        }
    }

    private void cargarLocations(JSONObject jsonResult){
        mMap.clear();
        getMyLocation(false);
        try {
            String token = jsonResult.getString("authentication_token");
            Usuario_Singleton.getInstance().setAuth_token(token);
            LoginActivity.actualizarAuth_Token(token, getActivity().getApplicationContext());
            JSONArray jsonLocations = jsonResult.getJSONArray("ubicaciones");
            for(int i = 0; i < jsonLocations.length(); i++) {
                JSONObject ubicacion = (JSONObject) jsonLocations.get(i);
                LatLng location = new LatLng(ubicacion.getDouble("latitud"), ubicacion.getDouble("longitud"));
                mMap.addMarker(new MarkerOptions().position(location).title("Radio escucha").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class ExecuteGetLocations extends AsyncTask<String, Void, String> {
        boolean isOk = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            Usuario_Singleton user = Usuario_Singleton.getInstance();
            isOk = api.getLocations(user.getId(), user.getAuth_token(), Streaming.getIdEmisora());

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isOk){
                cargarLocations(API_Access.getInstance().getJsonObjectResponse());
            }else{
                String mensaje = "Error getLocations()";
                try {
                    mensaje = API_Access.getInstance().getJsonObjectResponse().getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
