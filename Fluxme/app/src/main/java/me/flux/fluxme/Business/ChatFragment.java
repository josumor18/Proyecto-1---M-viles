package me.flux.fluxme.Business;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class ChatFragment extends Fragment {

    private ArrayList<Comentario> comentarios = new ArrayList<>();
    private ListView lvComentarios;


    public ChatFragment() {
        // Required empty public constructor
    }

    private class Comentario {
        String comentarista;
        String texto;
        String fechaHora;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        lvComentarios = view.findViewById(R.id.chatListView);

        ExecuteGetComentarios executeGetComentarios = new ExecuteGetComentarios();
        executeGetComentarios.execute();

        return view;
    }

    private class ComentariosAdapter extends BaseAdapter {

        public ComentariosAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return comentarios.size();
        }

        @Override
        public Object getItem(int i) {
            return comentarios.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            if (view == null) {
                view = inflater.inflate(R.layout.custom_comment, null);
            }

            TextView txtNombre= view.findViewById(R.id.ComentarioNombre);
            TextView txtFechaHora= view.findViewById(R.id.ComentarioHora);
            TextView txtTexto= view.findViewById(R.id.ComentarioTexto);

            Comentario comentario = comentarios.get(i);
            txtNombre.setText(comentario.comentarista);
            txtFechaHora.setText(comentario.fechaHora);
            txtTexto.setText(comentario.texto);
            return view;
        }
    }

    public void enviarComentario(String comentario) {
        ExecutePostComentarios executePostComentarios = new ExecutePostComentarios(comentario);
        executePostComentarios.execute();

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    private class ExecuteGetComentarios extends AsyncTask<String, Void, String> {
        boolean isOk = false;

        public ExecuteGetComentarios() {
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            Usuario_Singleton user = Usuario_Singleton.getInstance();

            isOk = api.getCommentsByEmisoraID(user.getId(), user.getAuth_token(), Streaming.getIdEmisora());

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

                    cargarComentarios(result);

                    Toast.makeText(getActivity(), "Lista obtenida", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class ExecutePostComentarios extends AsyncTask<String, Void, String> {
        boolean isOk = false;
        String comentario;

        public ExecutePostComentarios(String comentario) {
            this.comentario = comentario;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            Usuario_Singleton user = Usuario_Singleton.getInstance();

            isOk = api.postComment(user.getId(), user.getAuth_token(), Streaming.getIdEmisora(), user.getNombre(), comentario);

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

                    ExecuteGetComentarios executeGetComentarios = new ExecuteGetComentarios();
                    executeGetComentarios.execute();

                    Toast.makeText(getActivity(), "Comentario publicado", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void cargarComentarios(JSONObject jsonResult){
        try {
            comentarios.clear();

            JSONArray jsonComentarios = jsonResult.getJSONArray("comentarios");
            for(int i = 0; i < jsonComentarios.length(); i++) {
                JSONObject jsoncomentario = (JSONObject) jsonComentarios.get(i);
                Comentario comentario = new Comentario();
                comentario.comentarista = jsoncomentario.getString("comentarista");
                comentario.fechaHora = jsoncomentario.getString("created_at");
                comentario.texto = jsoncomentario.getString("cuerpo");
                if (comentario.comentarista != null) {
                    comentarios.add(comentario);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lvComentarios.setAdapter(new ComentariosAdapter());
    }

}
