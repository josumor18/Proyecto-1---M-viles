package me.flux.fluxme.Business;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class VotacionesUserFragment extends Fragment {

    ArrayList<Cancion> canciones = new ArrayList<Cancion>();
    ArrayList<Cancion> votadas = new ArrayList<Cancion>();
    ArrayList<Boolean> seleccionadas = new ArrayList<Boolean>();

    ListView lvVotaciones;

    public VotacionesUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_votaciones_user, container, false);

        lvVotaciones = view.findViewById(R.id.lvVotaciones);

        ExecuteGetCanciones executeGetCanciones = new ExecuteGetCanciones();
        executeGetCanciones.execute();

        return view;
    }

    private void cargarArrayCanciones(JSONObject result){
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
        ExecuteGetVotos executeGetVotos = new ExecuteGetVotos();
        executeGetVotos.execute();
    }

    private void cargarListViewCanciones(JSONObject result){
        try {
            String token = result.getString("authentication_token");
            Usuario_Singleton.getInstance().setAuth_token(token);
            LoginActivity.actualizarAuth_Token(token, getActivity().getApplicationContext());
            JSONArray jsonEmisoras = result.getJSONArray("ubicaciones");
            for(int i = 0; i < jsonEmisoras.length(); i++) {
                JSONObject cancion = (JSONObject) jsonEmisoras.get(i);
                Cancion nCan = new Cancion(cancion.getInt("id"),cancion.getString("id_cancion"));
                votadas.add(nCan);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        colocarSeleccionadas();
        lvVotaciones.setAdapter(new CancionesAdapter());
    }

    private void colocarSeleccionadas(){
        for(int i = 0; i < canciones.size(); i++){
            boolean col = false;
            for(Cancion v:votadas){
                if(canciones.get(i).id == v.id){
                    seleccionadas.add(true);
                    col = true;
                }
            }
            if(!col){
                seleccionadas.add(false);
            }
        }
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
                view = inflater.inflate(R.layout.custom_votar_list_item, null);
            }

            TextView txtTitulo = view.findViewById(R.id.txtVotarNomCancion);
            ImageView imgEequis = view.findViewById(R.id.ivOpcionDerecha);
            final int pos = i;
            imgEequis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!seleccionadas.get(pos)){
                        ExecuteAddVoto executeAddVoto = new ExecuteAddVoto(Integer.toString(canciones.get(pos).id), canciones.get(pos).cancion);
                        executeAddVoto.execute();
                    }

                }
            });

            txtTitulo.setText(canciones.get(i).cancion);
            if(seleccionadas.get(i)){
                imgEequis.setImageResource(R.drawable.tab_votar_verde);
            }else{
                imgEequis.setImageResource(R.drawable.tab_votar_des);
            }

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
                cargarArrayCanciones(API_Access.getInstance().getJsonObjectResponse());
            }else{
                String mensaje = "Error al obtener las canciones";

                Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
            }
            //rlLoaderEmisoras.setVisibility(View.INVISIBLE);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class ExecuteGetVotos extends AsyncTask<String, Void, String> {
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
            isOk = api.getMisVotos(user.getId(), user.getAuth_token(), Streaming.getIdEmisora());

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isOk){
                cargarListViewCanciones(API_Access.getInstance().getJsonObjectResponse());
            }else{
                String mensaje = "Error al obtener las canciones";

                Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
            }
            //rlLoaderEmisoras.setVisibility(View.INVISIBLE);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class ExecuteAddVoto extends AsyncTask<String, Void, String> {
        boolean isOk = false;
        String id_cancion;
        String nom_cancion;

        public ExecuteAddVoto(String id_cancion, String nom_cancion) {
            this.id_cancion = id_cancion;
            this.nom_cancion = nom_cancion;
        }

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
            isOk = api.addVoto(user.getId(), user.getAuth_token(), Streaming.getIdEmisora(), id_cancion, nom_cancion);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isOk){
                String token = null;
                try {
                    token = API_Access.getInstance().getJsonObjectResponse().getString("authentication_token");
                    Usuario_Singleton.getInstance().setAuth_token(token);
                    LoginActivity.actualizarAuth_Token(token, getActivity().getApplicationContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ExecuteGetCanciones executeGetCanciones = new ExecuteGetCanciones();
                executeGetCanciones.execute();
            }else{
                String mensaje = "Error al obtener las canciones";

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
