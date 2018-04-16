package me.flux.fluxme.Business;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.flux.fluxme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramacionAdminFragment extends Fragment {

   // private ArrayList<Tendencia> tendencias = new ArrayList<Tendencia>();
    private  ArrayList<EditText> nombresCanciones = new ArrayList<EditText>();
    private  ArrayList<EditText> linksImgsCanciones = new ArrayList<EditText>();

    private RelativeLayout rlProgSemAdmin;
    private RelativeLayout rlTendAdmin;
    ListView lvTendAdmin;
    RadioButton rbProgramacion;
    RadioButton rbTendencias;
    RadioGroup rgrp_Opcion;
    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(rbProgramacion.isChecked()){
                rlProgSemAdmin.setVisibility(View.VISIBLE);
                rlTendAdmin.setVisibility(View.INVISIBLE);
            }else if(rbTendencias.isChecked()){
                rlTendAdmin.setVisibility(View.VISIBLE);
                rlProgSemAdmin.setVisibility(View.INVISIBLE);
            }
        }
    };

    public ProgramacionAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_programacion_admin, container, false);
        rlProgSemAdmin = view.findViewById(R.id.rlProgSemAdmin);
        rlTendAdmin = view.findViewById(R.id.rlTendAdmin);

        rbProgramacion = view.findViewById(R.id.rbProgramacion);
        rbTendencias = view.findViewById(R.id.rbTendencias);
        rgrp_Opcion = view.findViewById(R.id.rgrp_Opcion);
        rgrp_Opcion.setOnCheckedChangeListener(checkedChangeListener);

        initTextBoxes(view);

        return view;
    }

   /* public class Tendencia{
        public String cancion;
        public String imgLink;

        public Tendencia(String cancion, String imgLink) {
            this.cancion = cancion;
            this.imgLink = imgLink;
        }
    }*/
   private void initTextBoxes(View view){
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion1));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion1));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion2));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion2));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion3));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion3));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion4));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion4));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion5));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion5));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion6));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion6));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion7));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion7));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion8));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion8));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion9));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion9));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion10));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion10));
   }
}
