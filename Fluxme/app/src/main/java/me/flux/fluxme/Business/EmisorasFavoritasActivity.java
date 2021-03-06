package me.flux.fluxme.Business;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

public class EmisorasFavoritasActivity extends BaseActivity {

    public static ArrayList<Emisora> emisorasFavoritas = new ArrayList<Emisora>();
    ListView lvEmisoras;
    RelativeLayout rlLoaderEmisoras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emisoras_favoritas);

        rlLoaderEmisoras = findViewById(R.id.rlLoaderEmisoras);

        ExecuteGetEmisoras getEmisoras = new ExecuteGetEmisoras();
        getEmisoras.execute();

        initToolbar();

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
        Emisora seleccionada = emisorasFavoritas.get(position);
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

    private void cargarEmisoras(JSONObject jsonResult){

        try {
            emisorasFavoritas.clear();
            String token = jsonResult.getString("authentication_token");
            Usuario_Singleton.getInstance().setAuth_token(token);
            LoginActivity.actualizarAuth_Token(token, getApplicationContext());
            JSONArray jsonEmisoras = jsonResult.getJSONArray("emisoras");
            for(int i = 0; i < jsonEmisoras.length(); i++) {
                JSONObject emisora = (JSONObject) jsonEmisoras.get(i);
                for(Emisora e : ListaEmisorasActivity.emisoras){
                    if(emisora.getInt("idEmisora")==e.getId()){
                        emisorasFavoritas.add(new Emisora(e.getId(), e.getNombre(), e.getLink(), e.getId_admin(),e.getDescripcion()));

                    }
                }
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
            return emisorasFavoritas.size();
        }

        @Override
        public Object getItem(int i) {
            return emisorasFavoritas.get(i);
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
            txtTitulo.setText(emisorasFavoritas.get(i).getNombre());
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
            isOk = api.getEmisorasFavoritas(user.getId(), user.getAuth_token());

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isOk){
                cargarEmisoras(API_Access.getInstance().getJsonObjectResponse());
            }else{
                String mensaje = "Error al obtener las emisoras";

                Toast.makeText(EmisorasFavoritasActivity.this, mensaje, Toast.LENGTH_SHORT).show();
            }
            rlLoaderEmisoras.setVisibility(View.INVISIBLE);
        }
    }
}
