package me.flux.fluxme.Business;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

/**
 * Created by alvaroramirez on 4/13/18.
 */

public class RegistrarActivity extends AppCompatActivity{


    EditText edtUsernameRegis;
    EditText edtEmailRegis;
    EditText edtPasswordRegis;
    EditText edtConfirmPasswordRegis;
    TextView txtInvalidRegis;

    ArrayList<EditText> editTextList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        edtEmailRegis = findViewById(R.id.edtEmailRegistrar);
        edtUsernameRegis = findViewById(R.id.edtUsernameRegistrar);
        edtPasswordRegis = findViewById(R.id.edtPasswordRegistrar);
        edtConfirmPasswordRegis = findViewById(R.id.edtConfirmarPassRegistrar);

        editTextList = new ArrayList<>();
        editTextList.add(edtUsernameRegis);
        editTextList.add(edtEmailRegis);
        editTextList.add(edtPasswordRegis);

        txtInvalidRegis = findViewById(R.id.txtInvalidoRegistrar);
    }

    public void registerClicked (View view){

        String pass1 = edtPasswordRegis.getText().toString();
        String pass2 = edtConfirmPasswordRegis.getText().toString();

        for(EditText edit: editTextList){
            if(TextUtils.isEmpty(edit.getText())){
                edit.setError("Campo obligatorio");
            }
        }

        if(pass1.length()<6){
            //Toast.makeText(RegistrarActivity.this, "La contraseña debe contener minimo 6 caracteres", Toast.LENGTH_LONG).show();
            edtPasswordRegis.setError("Min. 6 caracteres");
            edtPasswordRegis.setText("");
            edtConfirmPasswordRegis.setText("");
        }
        else if(!pass1.equals(pass2)){

            //txtInvalidRegis.setVisibility(1);
            Toast.makeText(RegistrarActivity.this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            edtPasswordRegis.setText("");
            edtConfirmPasswordRegis.setText("");

        }
        else{


            ExecuteRegister register = new ExecuteRegister(edtUsernameRegis.getText().toString()
                                                            ,edtEmailRegis.getText().toString()
                                                            ,edtPasswordRegis.getText().toString());
            register.execute();

        }

    }

    public class ExecuteRegister extends AsyncTask<String,Void,String>{
        private String name;
        private String email;
        private String password;

        public ExecuteRegister(String name,String email, String password){
            this.name = name;
            this.email = email;
            this.password = password;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                API_Access api = API_Access.getInstance();
                api.register(name,email,password);
            } catch (Error e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(RegistrarActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            finish();

        }




    }
}
