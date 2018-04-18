package me.flux.fluxme.Business;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VotacionesAdminFragment extends Fragment {

    ListView lvCancVotarAdmin;
    Button btnAddSongVot;
    EditText edtxtCancionArtista;
    ArrayList<Cancion> canciones = new ArrayList<Cancion>();

    public VotacionesAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_votaciones_admin, container, false);
        lvCancVotarAdmin = view.findViewById(R.id.lvCancVotarAdmin);
        edtxtCancionArtista = view.findViewById(R.id.edtxtCancionArtista);
        btnAddSongVot = view.findViewById(R.id.btnAddSongVot);
        btnAddSongVot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(edtxtCancionArtista.getText().toString().isEmpty())){
                    ExecuteAddCancion executeAddCancion = new ExecuteAddCancion();
                    executeAddCancion.execute(edtxtCancionArtista.getText().toString());
                }else{
                    Toast.makeText(getActivity(), "Falta nombre de canción", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ExecuteGetCanciones executeGetCanciones = new ExecuteGetCanciones();
        executeGetCanciones.execute();

        return view;
    }

    private void cargarCanciones(JSONObject result){
        try {
            canciones.clear();
            String token = result.getString("authentication_token");
            Usuario_Singleton.getInstance().setAuth_token(token);
            LoginActivity.actualizarAuth_Token(token, getActivity().getApplicationContext());
            JSONArray jsonEmisoras = result.getJSONArray("canciones");
            for(int i = 0; i < jsonEmisoras.length(); i++) {
                JSONObject cancion = (JSONObject) jsonEmisoras.get(i);
                Cancion nCan = new Cancion(cancion.getInt("id"),cancion.getString("cancion"));
                canciones.add(nCan);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lvCancVotarAdmin.setAdapter(new CancionesAdapter());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class CancionesAdapter extends BaseAdapter {

        public CancionesAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return canciones.size();
        }

        @Override
        public Object getItem(int i) {
            return canciones.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            if (view == null) {
                view = inflater.inflate(R.layout.custom_simple_list_item, null);
            }

            TextView txtTitulo = view.findViewById(R.id.txtVotarNomCancion);
            /*ImageView imgEequis = view.findViewById(R.id.ivOpcionDerecha);
            final int pos = i;
            imgEequis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExecuteDeleteCancion executeDeleteCancion = new ExecuteDeleteCancion();
                    executeDeleteCancion.execute(Integer.toString(canciones.get(pos).id));
                }
            });*/

            txtTitulo.setText(canciones.get(i).cancion);
            //imgEequis.setImageResource(R.drawable.equis);//BackgroundResource(R.drawable.equis);
            return view;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class ExecuteGetCanciones extends AsyncTask<String, Void, String> {
        boolean isOk = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //rlLoaderEmisoras.setVisibility(View.VISIBLE);
            //rlLogin.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            Usuario_Singleton user = Usuario_Singleton.getInstance();
            isOk = api.getCancionesVotos(user.getId(), user.getAuth_token(), Streaming.getIdEmisora());

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isOk){
                cargarCanciones(API_Access.getInstance().getJsonObjectResponse());
            }else{
                String mensaje = "Error al obtener las canciones";

                Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
            }
            //rlLoaderEmisoras.setVisibility(View.INVISIBLE);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class ExecuteAddCancion extends AsyncTask<String, Void, String> {
        boolean isOk = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //rlLoaderEmisoras.setVisibility(View.VISIBLE);
            //rlLogin.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            Usuario_Singleton user = Usuario_Singleton.getInstance();
            isOk = api.addCancion(user.getId(), user.getAuth_token(), Streaming.getIdEmisora(), strings[0]);

            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isOk){
                edtxtCancionArtista.setText("");
                lvCancVotarAdmin.setAdapter(new CancionesAdapter());

                cargarCanciones(API_Access.getInstance().getJsonObjectResponse());

                String token = null;
                try {
                    token = API_Access.getInstance().getJsonObjectResponse().getString("authentication_token");
                    Usuario_Singleton.getInstance().setAuth_token(token);
                    LoginActivity.actualizarAuth_Token(token, getActivity().getApplicationContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                String mensaje = "Error al agregar canción";

                Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
            }
            //rlLoaderEmisoras.setVisibility(View.INVISIBLE);
        }
    }


    //////////////////////////////////////7
    private class Cancion{
        int id;
        String cancion;

        public Cancion(int id, String cancion) {
            this.id = id;
            this.cancion = cancion;
        }
    }

}
