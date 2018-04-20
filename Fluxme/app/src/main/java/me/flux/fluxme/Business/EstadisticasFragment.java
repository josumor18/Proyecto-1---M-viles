package me.flux.fluxme.Business;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EstadisticasFragment extends Fragment {

    private PieChart pieChart;
    private ListView votosListView;

    private ArrayList<Voto> votaciones = new ArrayList<>();


    public EstadisticasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadisticas, container, false);

        votosListView = view.findViewById(R.id.votedListView);
        pieChart = view.findViewById(R.id.PieChart);

        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setDrawEntryLabels(true);

        ExecuteGetUbicaciones executeGetUbicaciones = new ExecuteGetUbicaciones();
        executeGetUbicaciones.execute();

        ExecuteGetVotos executeGetVotos = new ExecuteGetVotos();
        executeGetVotos.execute();

        return view;
    }

    private void cargarUbicaciones(JSONObject jsonResult) {
        ArrayList<Integer> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();

        try {
            JSONArray jsonUbicaciones = jsonResult.getJSONArray("localizaciones");
            for(int i = 0; i < jsonUbicaciones.length(); i++) {
                JSONObject jsonUbiacion = (JSONObject) jsonUbicaciones.get(i);

                xEntry.add(jsonUbiacion.getString("pais"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < xEntry.size(); i++) {
            int cantidad = 1;
            for (int j = i + 1; j < xEntry.size(); j++) {
                if (xEntry.get(i).equals(xEntry.get(j))) {
                    xEntry.remove(j);
                    j--;
                    cantidad++;
                }
            }
            yEntry.add(cantidad);
        }

        List<PieEntry> pieEntries = new ArrayList<>();

        for (int i = 0; i < xEntry.size(); i++) {
            pieEntries.add(new PieEntry(yEntry.get(i), xEntry.get(i)));
        }

        if (pieEntries.isEmpty()) {
            pieChart.setCenterText("No hay datos de ubicación");
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries,"Paises");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#b71c1c"));
        colors.add(Color.parseColor("#1a237e"));
        colors.add(Color.parseColor("#00bcd4"));
        colors.add(Color.parseColor("#2e7d32"));
        colors.add(Color.parseColor("#ffd600"));
        colors.add(Color.parseColor("#e65100"));
        colors.add(Color.parseColor("#6d4c41"));

        colors.add(Color.parseColor("#ad1457"));
        colors.add(Color.parseColor("#651fff"));
        colors.add(Color.parseColor("#00b0ff"));
        colors.add(Color.parseColor("#1b5e20"));
        colors.add(Color.parseColor("#827717"));
        colors.add(Color.parseColor("#ff3d00"));


        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void cargarVotos(JSONObject jsonResult) {

        try {
            JSONArray jsonComentarios = jsonResult.getJSONArray("historico");
            for(int i = 0; i < jsonComentarios.length(); i++) {
                JSONObject jsonVoto = (JSONObject) jsonComentarios.get(i);

                votaciones.add(new Voto(jsonVoto.getString("cancion"), jsonVoto.getString("votos")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        votosListView.setAdapter(new VotosAdapter());

    }

    private class Voto {

        String nombre;
        String voto;

        Voto(String nombre, String voto) {
            this.nombre = nombre;
            this.voto = voto;
        }
    }

    private class VotosAdapter extends BaseAdapter {

        public VotosAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return votaciones.size();
        }

        @Override
        public Object getItem(int i) {
            return votaciones.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            if (view == null) {
                view = inflater.inflate(R.layout.custom_votos, null);
            }

            TextView txtNombre = view.findViewById(R.id.cancion_voto);
            TextView txtVoto = view.findViewById(R.id.cantidad_voto);

            Voto voto = votaciones.get(i);
            txtNombre.setText(voto.nombre);
            txtVoto.setText(voto.voto + " Votos");
            return view;
        }
    }

    private class ExecuteGetUbicaciones extends android.os.AsyncTask<String, Void, String> {
        boolean isOk = false;

        public ExecuteGetUbicaciones() {
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            Usuario_Singleton user = Usuario_Singleton.getInstance();

            isOk = api.getUbicacionesByEmisoraID(user.getId(), user.getAuth_token(), Streaming.getIdEmisora());

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

                    cargarUbicaciones(result);

                    Toast.makeText(getActivity(), "Lista obtenida", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ExecuteGetVotos extends android.os.AsyncTask<String, Void, String> {
        boolean isOk = false;

        public ExecuteGetVotos() {
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            Usuario_Singleton user = Usuario_Singleton.getInstance();

            isOk = api.getVotosByEmisoraID(user.getId(), user.getAuth_token(), Streaming.getIdEmisora());

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

                    cargarVotos(result);

                    Toast.makeText(getActivity(), "Lista obtenida", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
