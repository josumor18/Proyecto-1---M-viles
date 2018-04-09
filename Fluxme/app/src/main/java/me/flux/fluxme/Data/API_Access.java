package me.flux.fluxme.Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by JosueAndroid on 7/4/2018.
 */

public class API_Access {
    private final String url_base = "https://fluxme-app.herokuapp.com/api/v1/";
    int estadoRequest = -1;
    private JSONObject jsonObjectResponse = new JSONObject();

    private static final API_Access ourInstance = new API_Access();

    public static API_Access getInstance() {
        return ourInstance;
    }

    private API_Access() {
    }

    public boolean login(String email, String password){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("email", email);
        Parametros.put("encrypted_password", password);
        return makeRequest("users/login", "POST", true, true, Parametros, HttpsURLConnection.HTTP_OK);
    }

    public boolean login_token(String email, String auth_token){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("email", email);
        Parametros.put("authentication_token", auth_token);
        return makeRequest("users/login_token", "POST", true, true, Parametros, HttpsURLConnection.HTTP_OK);
    }

    public boolean login_facebook(String name, String email, String auth_token){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("name", name);
        Parametros.put("email", email);
        Parametros.put("authentication_token", auth_token);
        return makeRequest("users/login_facebook", "POST", true, true, Parametros, HttpsURLConnection.HTTP_OK);
    }

    public JSONObject getResponse(){
        Log.d("estado: ", ""+estadoRequest);
        return jsonObjectResponse;
    }


    private boolean makeRequest(String urlEsp, String metodo, boolean doInput, boolean doOutput, HashMap<String, String> Parametros, int responseCode){
        String result = "";
        URL url;
        HttpsURLConnection httpsURLConnection;

        try {
            url = new URL(url_base + urlEsp);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();

            //crea el objeto JSON para enviar los parámetros
            JSONObject parametros = new JSONObject();
            for(String s: Parametros.keySet()){
                parametros.put(s, Parametros.get(s));
            }

            //DEFINE PARAMETROS DE CONEXION
            httpsURLConnection.setReadTimeout(15000);
            httpsURLConnection.setConnectTimeout(15000);
            httpsURLConnection.setRequestMethod(metodo);
            httpsURLConnection.setDoInput(doInput);
            httpsURLConnection.setDoOutput(doOutput);

            //Obtiene el resultado de la solicitud
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String pars = jsonToString(parametros);
            //String g = httpsURLConnection.g
            bufferedWriter.write(pars);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int rCode = httpsURLConnection.getResponseCode();
            if(responseCode == rCode){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";
                while((linea = bufferedReader.readLine()) != null){
                    stringBuffer.append(linea);
                    break;
                }
                bufferedReader.close();
                result = stringBuffer.toString();
            }else{
                result = "Error " + responseCode;
            }

            jsonObjectResponse = new JSONObject(result);
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private  String jsonToString(JSONObject params) throws JSONException, UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> iterator = params.keys();
        while ((iterator.hasNext())){
            String key = iterator.next();
            Object value = params.get(key);

            if(first){
                //result.append("?");
                first = false;
            }else {
                result.append("&");
            }

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
//////////////////////////////////////
    public class DownloadTask extends AsyncTask<String, Void, String>{

        private String metodo;
        private HashMap<String, String> Parametros = new HashMap<String, String>();
        private boolean doInput, doOutput;
        private int responseCode;
        //private ArrayList<String> Parametros = new ArrayList<String>();

        public DownloadTask(String metodo, boolean doInput, boolean doOutput, HashMap<String, String> Params, int responseCode){//ArrayList<String> Params){
            this.metodo = metodo;
            this.doInput = doInput;
            this.doOutput = doOutput;
            this.Parametros = Params;
            this.responseCode = responseCode;
        }

        @Override
        protected String doInBackground(String... urls) {
            //dataConnection = {url, RequestMethod(GET, POST, etc), doInput(true or false), doOutput(true or false)}
            String result = "";
            URL url;
            HttpsURLConnection httpsURLConnection;

            try {
                url = new URL(url_base + urls[0]);
                httpsURLConnection = (HttpsURLConnection) url.openConnection();

                //crea el objeto JSON para enviar los parámetros
                JSONObject parametros = new JSONObject();
                for(String s: Parametros.keySet()){
                    parametros.put(s, Parametros.get(s));
                }

                //DEFINE PARAMETROS DE CONEXION
                httpsURLConnection.setReadTimeout(15000);
                httpsURLConnection.setConnectTimeout(15000);
                httpsURLConnection.setRequestMethod(metodo);
                httpsURLConnection.setDoInput(doInput);
                httpsURLConnection.setDoOutput(doOutput);

                //Obtiene el resultado de la solicitud
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String pars = jsonToString(parametros);
                //String g = httpsURLConnection.g
                bufferedWriter.write(pars);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                int rCode = httpsURLConnection.getResponseCode();
                if(responseCode == rCode){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";
                    while((linea = bufferedReader.readLine()) != null){
                        stringBuffer.append(linea);
                        break;
                    }
                    bufferedReader.close();
                    result = stringBuffer.toString();
                }else{
                    result = "Error " + responseCode;
                }
                return result;
                /*
                InputStream inputStream = httpsURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data =inputStreamReader.read();

                while (data != -1){
                    char current = (char)data;
                    result += current;
                    data = inputStreamReader.read();
                }
                */

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                jsonObjectResponse = new JSONObject(jsonObject.getString("data"));
                estadoRequest = 1;
            } catch (JSONException e) {
                estadoRequest = 0;
                e.printStackTrace();
            }
        }

        private  String jsonToString(JSONObject params) throws JSONException, UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            Iterator<String> iterator = params.keys();
            while ((iterator.hasNext())){
                String key = iterator.next();
                Object value = params.get(key);

                if(first){
                    //result.append("?");
                    first = false;
                }else {
                    result.append("&");
                }

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }
            return result.toString();
        }
    }
}
