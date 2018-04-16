package me.flux.fluxme.Business;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.util.stream.Stream;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

/**
 * Created by JosueAndroid on 27/3/2018.
 */

public class BaseActivity extends AppCompatActivity {

    private final MyActivityLifecycleCallbacks mCallbacks = new MyActivityLifecycleCallbacks();

    protected Double longitud;
    protected Double latitud;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    private static boolean isChanging = false;
    Menu main_menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if(this instanceof ListaEmisorasActivity){
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
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

        //}
        SharedPreferences preferences = getSharedPreferences("user.preferences.fluxme", MODE_PRIVATE);
        preferences.edit().putString("idAux", Usuario_Singleton.getInstance().getId()).apply();

        getApplication().registerActivityLifecycleCallbacks(mCallbacks);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isChanging = false;
        Usuario_Singleton user = Usuario_Singleton.getInstance();
        if(!(Streaming.getStream().isEmpty()) && !(Usuario_Singleton.getInstance().isAdmin())){
            if(!Streaming.isIsPlaying()){
                obtenerUbicacion();

                ExecuteAddLocations loc = new ExecuteAddLocations();
                loc.execute();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            if(!Streaming.isIsPlaying() && !isChanging){
                Usuario_Singleton user = Usuario_Singleton.getInstance();
                ExecuteDeleteLocations loc = new ExecuteDeleteLocations(user.getId(), user.getAuth_token());
                loc.execute();
            }
            isChanging = false;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getApplication().unregisterActivityLifecycleCallbacks(mCallbacks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_appbar, menu);
        if(!Streaming.getIdEmisora().isEmpty()){
            menu.getItem(0).setTitle(Streaming.getEmisora_name());
            menu.getItem(0).setEnabled(true);
        }
        if(Streaming.isIsPlaying()){
            menu.getItem(1).setIcon(R.drawable.stop_button_fluxme);
        }else{
            menu.getItem(1).setIcon(R.drawable.play_button_fluxme);
        }
        if(this instanceof EmisoraActivity){
            menu.getItem(0).setEnabled(false);
        }else{
            menu.getItem(0).setEnabled(true);
        }
        main_menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_emisora:
                isChanging = true;
                Intent intentEmisora = new Intent(getApplicationContext(), EmisoraActivity.class);
                startActivity(intentEmisora);
                //item.setEnabled(false);
                return true;
            case R.id.item_play:
                if(Streaming.isIsPlaying()){
                    item.setIcon(R.drawable.play_button_fluxme);
                    Streaming.pause();
                }else{
                    if(Streaming.isPrepared()){
                        //Streaming.setStream(Streaming.getStream());
                        Streaming.play();
                        if(Streaming.isIsPlaying()){
                            item.setIcon(R.drawable.stop_button_fluxme);
                        }
                    }else{
                        if(!(this instanceof ListaEmisorasActivity) && Streaming.getStream().isEmpty()) {
                            isChanging = true;
                            Intent intentEmisoras = new Intent(getApplicationContext(), ListaEmisorasActivity.class);
                            startActivity(intentEmisoras);
                        }
                    }
                }
                return true;
            case R.id.item_inicio:
                return true;
            case R.id.item_perfil:
                isChanging = true;
                Intent intentPerfil = new Intent(getApplicationContext(), PerfilActivity.class);
                startActivity(intentPerfil);

                main_menu.getItem(0).setEnabled(true);
                return true;
            case R.id.item_emisoras:
                isChanging = true;
                Intent intentEmisoras = new Intent(getApplicationContext(), ListaEmisorasActivity.class);
                startActivity(intentEmisoras);

                main_menu.getItem(0).setEnabled(true);
                return true;
            case R.id.item_cerrar_sesion:
                Streaming.pause();
                Streaming.cleanStreaming();

                Usuario_Singleton user = Usuario_Singleton.getInstance();
                ExecuteDeleteLocations loc = new ExecuteDeleteLocations(user.getId(), user.getAuth_token());
                loc.execute();

                LoginActivity.cerrarSesion(getApplicationContext());
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
                main_menu.getItem(0).setEnabled(true);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initToolbar(){
        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    protected void obtenerUbicacion(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            LatLng location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            longitud = lastLocation.getLongitude();
            latitud = lastLocation.getLatitude();

        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class ExecuteAddLocations extends AsyncTask<String, Void, String> {
        boolean isOk = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            Usuario_Singleton user = Usuario_Singleton.getInstance();

            isOk = api.addLocation(user.getId(), user.getAuth_token(), Streaming.getIdEmisora(), Double.toString(longitud), Double.toString(latitud));

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isOk){
                String token = null;
                try {
                    token = (API_Access.getInstance().getJsonObjectResponse()).getString("authentication_token");
                    Usuario_Singleton.getInstance().setAuth_token(token);
                    LoginActivity.actualizarAuth_Token(token, getApplicationContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    public static class ExecuteDeleteLocations extends AsyncTask<String, Void, String> {
        String id;
        String auth_token;

        public ExecuteDeleteLocations(String id, String auth_token) {
            this.id = id;
            this. auth_token = auth_token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();

            api.deleteLocations(id, auth_token);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }




    // Clase para capturar el ciclo de vida de la aplicacion
    public class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.i(activity.getClass().getSimpleName(), "onCreate(Bundle)");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.i(activity.getClass().getSimpleName(), "onStart()");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.i(activity.getClass().getSimpleName(), "onResume()");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.i(activity.getClass().getSimpleName(), "onPause()");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.i(activity.getClass().getSimpleName(), "onSaveInstanceState(Bundle)");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.i(activity.getClass().getSimpleName(), "onStop()");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.i(activity.getClass().getSimpleName(), "onActivityDestroy()");
            //Usuario_Singleton user = Usuario_Singleton.getInstance();
            //ExecuteDeleteLocations loc = new ExecuteDeleteLocations(user.getId(), user.getAuth_token());
            SharedPreferences preferences = activity.getSharedPreferences("user.preferences.fluxme", MODE_PRIVATE);
            ExecuteDeleteLocations loc = new ExecuteDeleteLocations(preferences.getString("idAux", ""), preferences.getString("string.token.sesion", ""));
            loc.execute();
        }
    }
}
