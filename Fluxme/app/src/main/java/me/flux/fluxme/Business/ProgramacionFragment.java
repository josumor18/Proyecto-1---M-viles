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
import java.util.concurrent.ExecutionException;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramacionFragment extends Fragment {

    public static ArrayList<Programacion> listaProgramacion = new ArrayList<>();
    ListView lvProgramacion;
    public ProgramacionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_programacion, container, false);
        lvProgramacion = view.findViewById(R.id.lvListaProgramacion);

        ExecuteGetProgramacion executeGetProgramacion = new ExecuteGetProgramacion();
        executeGetProgramacion.execute();

        return view;
    }

    private void cargarProgramaciones(JSONObject jsonResult){
        try {
            listaProgramacion.clear();

            JSONArray jsonProgramacion = jsonResult.getJSONArray("programacion");
            //for(int i = 0; i < jsonTendencias.length(); i++) {
            for(int i = 0; i < jsonProgramacion.length(); i++) {
                if(jsonProgramacion.length() > 0){
                    JSONObject prog = (JSONObject) jsonProgramacion.get(i);
                    listaProgramacion.add(new Programacion(prog.getString("dia"), prog.getString("hora"), prog.getString("titulo")));
                }else{
                    listaProgramacion.add(new Programacion("", "", ""));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ordenarProgramacion();
        lvProgramacion.setAdapter(new ProgramacionAdapter());
    }

    private void ordenarProgramacion(){
        ArrayList<Programacion> progAux = new ArrayList<>();
        for(String d:ProgramacionAdminFragment.listDias){
            for(String h:ProgramacionAdminFragment.listHoras){
                for(Programacion p: listaProgramacion){
                    if(p.getDia().equals(d)&&p.getHora().equals(h))
                        progAux.add(p);
                }
            }
        }
        listaProgramacion.clear();
        listaProgramacion = progAux;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class ProgramacionAdapter extends BaseAdapter {

        public ProgramacionAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return listaProgramacion.size();
        }

        @Override
        public Object getItem(int i) {
            return listaProgramacion.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            if (view == null) {
                view = inflater.inflate(R.layout.custom_programacion_list_item, null);
            }


            TextView txtProgTitulo = view.findViewById(R.id.txtTituloProgramacion);
            TextView txtProgDiaHora = view.findViewById(R.id.txtDiaHora);



            Programacion prog = listaProgramacion.get(i);

            txtProgTitulo.setText(prog.getTitulo());
            String diaHora = prog.getDia() + ": " + prog.getHora();
            txtProgDiaHora.setText(diaHora);
            return view;
        }
    }

    public class ExecuteGetProgramacion extends AsyncTask<String, Void, String> {
        boolean isOk = false;

        public ExecuteGetProgramacion() {

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            Usuario_Singleton user = Usuario_Singleton.getInstance();

            isOk = api.getProgramacion(user.getId(), user.getAuth_token(), Streaming.getIdEmisora());

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

                    cargarProgramaciones(result);

                    //Toast.makeText(getActivity(), "Lista obtenida", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
