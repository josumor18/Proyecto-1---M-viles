package me.flux.fluxme.Business;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TendenciasFragment extends Fragment {

    private ArrayList<CancionTendencia> tendencias = new ArrayList<CancionTendencia>();
    ListView lvListaTendencias;

    public TendenciasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tendencias, container, false);
        lvListaTendencias = view.findViewById(R.id.lvListaTendencias);

        ExecuteGetTrending executeGetTrending = new ExecuteGetTrending();
        executeGetTrending.execute();

        return view;
    }


    private void cargarTendencias(JSONObject jsonResult){
        try {
            tendencias.clear();

            JSONArray jsonTendencias = jsonResult.getJSONArray("trending");
            for(int i = 0; i < jsonTendencias.length(); i++) {
                JSONObject tendencia = (JSONObject) jsonTendencias.get(i);
                tendencias.add(new CancionTendencia(tendencia.getInt("posicion"), tendencia.getString("cancion"), tendencia.getString("artista"), tendencia.getString("imagen")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ordenarTendencias();
        lvListaTendencias.setAdapter(new TendenciasAdapter());
    }

    private void ordenarTendencias(){
        ArrayList<CancionTendencia> tendAux = new ArrayList<CancionTendencia>();
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10;j++) {
                if (tendencias.get(j).getPosicion() == i + 1) {
                    tendAux.add(tendencias.get(j));
                    continue;
                }
            }
        }
        tendencias.clear();
        tendencias = tendAux;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class TendenciasAdapter extends BaseAdapter {

        public TendenciasAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return tendencias.size();
        }

        @Override
        public Object getItem(int i) {
            return tendencias.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            if (view == null) {
                view = inflater.inflate(R.layout.custom_tendencias_list_item, null);
            }

            TextView txtNumTendAdmin= view.findViewById(R.id.txtNumTendAdmin);
            ImageView ivImagenCancion = view.findViewById(R.id.ivImagenCancion);
            TextView txtTendNombreCancion = view.findViewById(R.id.txtTendtNombreCancion);
            TextView txtTendNombreArtista = view.findViewById(R.id.txtTendNombreArtista);

            HttpGetBitmap request = new HttpGetBitmap();
            Bitmap coverImage = null;
            try {
                coverImage = request.execute(i).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            CancionTendencia tend = tendencias.get(i);
            txtNumTendAdmin.setText(Integer.toString(tend.getPosicion()));
            ivImagenCancion.setImageBitmap(coverImage);
            txtTendNombreCancion.setText(tend.getCancion());
            txtTendNombreArtista.setText(tend.getArtista());
            return view;
        }
    }

    public class HttpGetBitmap extends AsyncTask<Integer, Void, Bitmap> {
        private static final String REQUEST_METHOD = "GET";
        private static final int READ_TIMEOUT = 15000;
        private static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected Bitmap doInBackground(Integer... index){

            int i = index[0];
            Bitmap cover;

            CancionTendencia tend = tendencias.get(i);
            String address = tend.getLink();

            try {

                //Create a URL object holding our url
                URL myUrl = new URL(address);

                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();

                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.setDoInput(true);
                connection.connect();

                //Create a new InputStream
                InputStream input = connection.getInputStream();
                cover = BitmapFactory.decodeStream(input);

            }
            catch(IOException e){
                e.printStackTrace();
                cover = null;
            }
            return cover;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class ExecuteGetTrending extends AsyncTask<String, Void, String> {
        boolean isOk = false;

        public ExecuteGetTrending() {

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            Usuario_Singleton user = Usuario_Singleton.getInstance();

            isOk = api.getTrending(user.getId(), user.getAuth_token(), Streaming.getIdEmisora());

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isOk){
                String token = null;
                try {
                    JSONObject result = API_Access.getInstance().getJsonObjectResponse();
                    token = (result).getString("authentication_token");
                    Usuario_Singleton.getInstance().setAuth_token(token);
                    LoginActivity.actualizarAuth_Token(token, getActivity().getApplicationContext());

                    cargarTendencias(result);

                    Toast.makeText(getActivity(), "Lista obtenida", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
