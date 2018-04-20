package me.flux.fluxme.Data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
    private JSONArray jsonArrayResponse = new JSONArray();

    private static final API_Access ourInstance = new API_Access();

    public static API_Access getInstance() {
        return ourInstance;
    }

    private API_Access() {
    }

    public boolean change_pass(String id ,String name, String email, String password, String new_password,String authToken){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("id", id);
        Parametros.put("name",name);
        Parametros.put("email",email);
        Parametros.put("password", password);
        Parametros.put("new_password", new_password);
        Parametros.put("authentication_token",authToken);
        return makePOSTRequest("users/change_pass", "PUT", true, true, Parametros, HttpsURLConnection.HTTP_OK);
    }

    public boolean change_user(String id ,String name, String email,String authToken){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("id", id);
        Parametros.put("name",name);
        Parametros.put("email",email);
        Parametros.put("authentication_token",authToken);
        return makePOSTRequest("users/change_user", "PUT", true, true, Parametros, HttpsURLConnection.HTTP_OK);
    }
    public boolean register(String username, String email, String password){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("name", username);
        Parametros.put("email", email);
        Parametros.put("password", password);
        Parametros.put("password_confirmation",password);
        return makePOSTRequest("users/register", "POST", true, true, Parametros, HttpsURLConnection.HTTP_CREATED);

    }
    public boolean setProgramacion(String id_user,String authToken,String idEmisora,String dia,String hora,String titulo){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("idUser", id_user);
        Parametros.put("idEmisora", idEmisora);
        Parametros.put("authentication_token",authToken);
        Parametros.put("dia", dia);
        Parametros.put("hora", hora);
        Parametros.put("titulo", titulo);
        return makePOSTRequest("programacions/setProgramacion", "POST", true, true, Parametros, HttpsURLConnection.HTTP_CREATED);

    }

    public boolean getProgramacion(String idUser, String auth_token, String idEmisora){
        jsonArrayResponse = new JSONArray();
        String urlEsp = "programacions/getProgramacion?idUser=" + idUser + "&authentication_token=" + auth_token + "&idEmisora=" + idEmisora;
        return makeGETRequest(urlEsp, "GET", HttpsURLConnection.HTTP_OK);
    }
    public boolean deleteProgramacion(String idEmisora,String dia,String hora){
        //jsonObjectResponse = new JSONObject();
        String urlEsp = "programacions/deleteProgramacion?idEmisora=" + idEmisora + "&dia" + dia + "&hora" + hora;
        return makeDELETERequest(urlEsp, "DELETE", HttpsURLConnection.HTTP_OK);

    }

    public boolean setSuscription(String idUser, String idEmisora,String authToken){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("idUser", idUser);
        Parametros.put("idEmisora", idEmisora);
        Parametros.put("authentication_token",authToken);
        return makePOSTRequest("user_emisoras/setSuscription", "POST", true, true, Parametros, HttpsURLConnection.HTTP_CREATED);

    }

    public boolean isSuscripted(String idUser, String idEmisora,String authToken){
        jsonObjectResponse = new JSONObject();
        String urlEsp = "user_emisoras/isSuscripted?idUser=" + idUser + "&authentication_token=" + authToken + "&idEmisora=" + idEmisora;
        return makeGETRequest(urlEsp, "GET", HttpsURLConnection.HTTP_OK);

    }

    public boolean deleteSuscription(String idUser, String idEmisora,String authToken){
        jsonObjectResponse = new JSONObject();
        String urlEsp = "user_emisoras/deleteSuscription?idUser=" + idUser + "&authentication_token=" + authToken + "&idEmisora=" + idEmisora;
        return makeDELETERequest(urlEsp, "DELETE", HttpsURLConnection.HTTP_OK);

    }

    //Falta backend
    public boolean change_emisora(String id_user,String auth_token,String id,String nombre, String descripcion){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("id_user",id_user);
        Parametros.put("authentication_token",auth_token);
        Parametros.put("id",id);
        Parametros.put("nombre", nombre);
        Parametros.put("descripcion", descripcion);
        return makePOSTRequest("emisoras/change_emisora", "PUT", true, true, Parametros, HttpsURLConnection.HTTP_OK);


    }
    public boolean login(String email, String password){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("email", email);
        Parametros.put("encrypted_password", password);
        return makePOSTRequest("users/login", "POST", true, true, Parametros, HttpsURLConnection.HTTP_OK);
    }

    public boolean login_token(String email, String auth_token){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("email", email);
        Parametros.put("authentication_token", auth_token);
        return makePOSTRequest("users/login_token", "POST", true, true, Parametros, HttpsURLConnection.HTTP_OK);
    }

    public boolean login_facebook(String name, String email, String auth_token){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("name", name);
        Parametros.put("email", email);
        Parametros.put("authentication_token", auth_token);
        return makePOSTRequest("users/login_facebook", "POST", true, true, Parametros, HttpsURLConnection.HTTP_OK);
    }

    public boolean getEmisoras(String id, String auth_token){
        jsonArrayResponse = new JSONArray();
        String urlEsp = "emisoras/index?id=" + id + "&authentication_token=" + auth_token;
        return makeGETRequest(urlEsp, "GET", HttpsURLConnection.HTTP_OK);
    }

    public boolean getEmisorasFavoritas(String id, String auth_token){
        jsonArrayResponse = new JSONArray();
        String urlEsp = "user_emisoras/getEmisorasFavoritas?id=" + id + "&authentication_token=" + auth_token;
        return makeGETRequest(urlEsp, "GET", HttpsURLConnection.HTTP_OK);
    }

    public boolean addLocation(String id_user, String auth_token, String id_emisora, String longitud, String latitud, String ciudad, String pais){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("id_user", id_user);
        Parametros.put("authentication_token", auth_token);
        Parametros.put("id_emisora", id_emisora);
        Parametros.put("longitud", longitud);
        Parametros.put("latitud", latitud);
        Parametros.put("ciudad", ciudad);
        Parametros.put("pais", pais);
        return makePOSTRequest("ubicaciones/add", "POST", true, true, Parametros, HttpsURLConnection.HTTP_OK);
    }

    public boolean getLocations(String id, String auth_token, String id_emisora){
        jsonArrayResponse = new JSONArray();
        String urlEsp = "ubicaciones/get?id=" + id + "&authentication_token=" + auth_token + "&id_emisora=" + id_emisora;
        return makeGETRequest(urlEsp, "GET", HttpsURLConnection.HTTP_OK);
    }

    public boolean deleteLocations(String id, String auth_token){
        jsonArrayResponse = new JSONArray();
        String urlEsp = "ubicaciones/del_ubicacion?id_user=" + id + "&authentication_token=" + auth_token;
        return makeDELETERequest(urlEsp, "DELETE", HttpsURLConnection.HTTP_OK);
    }

    public boolean addTrending(String id_user, String auth_token, String id_emisora, String tendencias){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("id_user", id_user);
        Parametros.put("authentication_token", auth_token);
        Parametros.put("id_emisora", id_emisora);
        Parametros.put("tendencias", tendencias);
        return makePOSTRequest("trendings/add", "POST", true, true, Parametros, HttpsURLConnection.HTTP_OK);
    }

    public boolean getTrending(String id, String auth_token, String id_emisora){
        jsonArrayResponse = new JSONArray();
        String urlEsp = "trendings/get?id=" + id + "&authentication_token=" + auth_token + "&id_emisora=" + id_emisora;
        return makeGETRequest(urlEsp, "GET", HttpsURLConnection.HTTP_OK);
    }

    public boolean getCancionesVotos(String id, String auth_token, String id_emisora){
        jsonArrayResponse = new JSONArray();
        String urlEsp = "votaciones/get_canciones?id_user=" + id + "&authentication_token=" + auth_token + "&id_emisora=" + id_emisora;
        return makeGETRequest(urlEsp, "GET", HttpsURLConnection.HTTP_OK);
    }

    public boolean getMisVotos(String id, String auth_token, String id_emisora){
        jsonArrayResponse = new JSONArray();
        String urlEsp = "votaciones/get_mis_votos?id=" + id + "&id_user=" + id + "&authentication_token=" + auth_token + "&id_emisora=" + id_emisora;
        return makeGETRequest(urlEsp, "GET", HttpsURLConnection.HTTP_OK);
    }

    public boolean addVoto(String id_user, String auth_token, String id_emisora, String id_cancion, String nom_cancion){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("id_user", id_user);
        Parametros.put("authentication_token", auth_token);
        Parametros.put("id_emisora", id_emisora);
        Parametros.put("id_cancion", id_cancion);
        Parametros.put("nom_cancion", nom_cancion);
        return makePOSTRequest("votaciones/add_voto", "POST", true, true, Parametros, HttpsURLConnection.HTTP_OK);
    }

    public boolean addCancion(String id_user, String auth_token, String id_emisora, String cancion){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("id_user", id_user);
        Parametros.put("authentication_token", auth_token);
        Parametros.put("id_emisora", id_emisora);
        Parametros.put("cancion", cancion);
        return makePOSTRequest("votaciones/add_cancion", "POST", true, true, Parametros, HttpsURLConnection.HTTP_OK);
    }

    public boolean deleteCancion(String id_user, String auth_token, String id_emisora, String id_cancion){
        jsonArrayResponse = new JSONArray();
        String urlEsp = "ubicaciones/del_ubicacion?id_user=" + id_user + "&authentication_token=" + auth_token + "&id_emisora=" + id_emisora + "&id_cancion=" + id_cancion;
        return makeDELETERequest(urlEsp, "DELETE", HttpsURLConnection.HTTP_OK);
    }

    public boolean getCommentsByEmisoraID(String id, String auth_token, String id_emisora){
        jsonArrayResponse = new JSONArray();
        String urlEsp = "comentarios/get_comentarios?idUser=" + id + "&authentication_token=" + auth_token + "&emisora_id=" + id_emisora;
        return makeGETRequest(urlEsp, "GET", HttpsURLConnection.HTTP_OK);
    }

    public boolean getUbicacionesByEmisoraID(String id, String auth_token, String id_emisora){
        jsonArrayResponse = new JSONArray();
        String urlEsp = "estadisticas/get_ubicaciones?idUser=" + id + "&authentication_token=" + auth_token + "&id_emisora=" + id_emisora;
        return makeGETRequest(urlEsp, "GET", HttpsURLConnection.HTTP_OK);
    }

    public boolean getVotosByEmisoraID(String id, String auth_token, String id_emisora){
        jsonArrayResponse = new JSONArray();
        String urlEsp = "estadisticas/get_votaciones?idUser=" + id + "&authentication_token=" + auth_token + "&id_emisora=" + id_emisora;
        return makeGETRequest(urlEsp, "GET", HttpsURLConnection.HTTP_OK);
    }

    public boolean postComment(String id_user, String auth_token, String id_emisora, String user_name, String comment){
        jsonObjectResponse = new JSONObject();
        HashMap<String, String> Parametros = new HashMap<String, String>();
        Parametros.put("idUser", id_user);
        Parametros.put("authentication_token", auth_token);
        Parametros.put("emisora_id", id_emisora);
        Parametros.put("comentarista", user_name);
        Parametros.put("cuerpo", comment);
        return makePOSTRequest("comentarios/add_comentarios", "POST", true, true, Parametros, HttpsURLConnection.HTTP_CREATED);
    }

    /////////////////////// GET Respuesta del servidor: JSONObject ////////////////////////////////
    public JSONObject getJsonObjectResponse(){
        Log.d("estado: ", ""+estadoRequest);
        return jsonObjectResponse;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////



    /////////////////////////////////////////////// Métodos que ejecutan las solicitudes ////////////////////////////////////////////
    // Solicitud para PSOTs
    private boolean makePOSTRequest(String urlEsp, String metodo, boolean doInput, boolean doOutput, HashMap<String, String> Parametros, int responseCode){
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

    // Solicitud para GETs
    private boolean makeGETRequest(String urlEsp, String metodo, int responseCode) {
        String result = "";
        URL url;
        HttpsURLConnection httpsURLConnection;

        try {
            url = new URL(url_base + urlEsp);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();

            //DEFINE PARAMETROS DE CONEXION
            httpsURLConnection.setReadTimeout(15000);
            httpsURLConnection.setConnectTimeout(15000);
            httpsURLConnection.setRequestMethod(metodo);

            //Connect to our url
            httpsURLConnection.connect();

            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(httpsURLConnection.getInputStream());

            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            //Check if the line we are reading is not null
            int rCode = httpsURLConnection.getResponseCode();
            if (responseCode == rCode) {
                String inputLine = "";
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                result = stringBuilder.toString();
            } else {
                result = "Error " + responseCode;
            }


            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();

            jsonObjectResponse = new JSONObject(result);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Solicitud para DELETEs
    private boolean makeDELETERequest(String urlEsp, String metodo, int responseCode){
        URL url;
        HttpsURLConnection httpsURLConnection;

        try {
            url = new URL(url_base + urlEsp);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("DELETE");
            httpsURLConnection.setDoOutput(false);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.connect();
            int r = httpsURLConnection.getResponseCode();
            String rC = Integer.toString(httpsURLConnection.getResponseCode());
            if(r==200){
                return true;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
