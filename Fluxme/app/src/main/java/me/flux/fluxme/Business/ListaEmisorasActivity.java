package me.flux.fluxme.Business;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

public class ListaEmisorasActivity extends BaseActivity {

    private Double longitud;
    private Double latitud;
    private ArrayList<Emisora> emisoras = new ArrayList<Emisora>();
    ListView lvEmisoras;
    RelativeLayout rlLoaderEmisoras;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_emisoras);

        rlLoaderEmisoras = findViewById(R.id.rlLoaderEmisoras);

        ExecuteGetEmisoras getEmisoras = new ExecuteGetEmisoras();
        getEmisoras.execute();

        initToolbar();

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

        lvEmisoras = findViewById(R.id.listaEmisoras);
        lvEmisoras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setEmisoraToStreaming(position);
            }
        });
    }

    private void setEmisoraToStreaming(int position) {
        rlLoaderEmisoras.setVisibility(View.VISIBLE);
        Emisora seleccionada = emisoras.get(position);
        Streaming.pause();
        main_menu.getItem(1).setIcon(R.drawable.play_button_fluxme);
        Streaming.setIdEmisora(Integer.toString(seleccionada.getId()));
        Streaming.setEmisora_name(seleccionada.getNombre());
        Streaming.setStream(seleccionada.getLink());

        boolean admin = false;
        if(seleccionada.getId_admin() == Integer.parseInt(Usuario_Singleton.getInstance().getId())){
            admin = true;
        }else{
            obtenerUbicacion();

            ExecuteAddLocations loc = new ExecuteAddLocations();
            loc.execute();
        }
        Usuario_Singleton.getInstance().setAdmin(admin);

        main_menu.getItem(0).setTitle(Streaming.getEmisora_name());
        main_menu.getItem(0).setEnabled(true);
        if (Streaming.isPrepared()) {
            Streaming.play();
            if (Streaming.isIsPlaying()) {
                main_menu.getItem(1).setIcon(R.drawable.stop_button_fluxme);
            }
        }
        rlLoaderEmisoras.setVisibility(View.INVISIBLE);
    }

    private void obtenerUbicacion(){
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

    private void cargarEmisoras(JSONObject jsonResult){

        try {
            String token = jsonResult.getString("authentication_token");
            Usuario_Singleton.getInstance().setAuth_token(token);
            LoginActivity.actualizarAuth_Token(token, getApplicationContext());
            JSONArray jsonEmisoras = jsonResult.getJSONArray("emisoras");
            for(int i = 0; i < jsonEmisoras.length(); i++) {
                JSONObject emisora = (JSONObject) jsonEmisoras.get(i);
                emisoras.add(new Emisora(emisora.getInt("id"), emisora.getString("nombre"), emisora.getString("link"), emisora.getInt("id_admin")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lvEmisoras.setAdapter(new EmisorasAdapter());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class EmisorasAdapter extends BaseAdapter {

        public EmisorasAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return emisoras.size();
        }

        @Override
        public Object getItem(int i) {
            return emisoras.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            if (view == null) {
                view = inflater.inflate(R.layout.custom_list_item, null);
            }

            ImageView imgEmisora = view.findViewById(R.id.iV_ListItem);
            TextView txtTitulo = view.findViewById(R.id.textItemTitle);

            /*HttpGetBitmap request = new HttpGetBitmap();
            Bitmap coverImage = null;
            try {
                coverImage = request.execute(i).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            imgMovie.setImageBitmap(coverImage);
            */
            txtTitulo.setText(emisoras.get(i).getNombre());
            return view;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class ExecuteGetEmisoras extends AsyncTask<String, Void, String> {
        boolean isOk = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            rlLoaderEmisoras.setVisibility(View.VISIBLE);
            //rlLogin.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            Usuario_Singleton user = Usuario_Singleton.getInstance();
            isOk = api.getEmisoras(user.getId(), user.getAuth_token());

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isOk){
                cargarEmisoras(API_Access.getInstance().getJsonObjectResponse());
            }else{
                String mensaje = "Error getEmisoras()";

                Toast.makeText(ListaEmisorasActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                //rlLoader.setVisibility(View.INVISIBLE);
                //rlLogin.setVisibility(View.VISIBLE);
            }
            rlLoaderEmisoras.setVisibility(View.INVISIBLE);
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
        }
    }
}
