package me.flux.fluxme.Business;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by JosueAndroid on 27/3/2018.
 */

public class Streaming {


    private static boolean isPlaying = false;
    private static boolean prepared = false;
    private static String idEmisora = "";
    private static String stream = "http://s41.myradiostream.com:35530/";
    private static String emisora_name = "";
    private static AudioManager audioManager;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    //private static PlayerTask playerTask = new PlayerTask().execute(stream);
    private static final Streaming ourInstance = new Streaming();

    public static Streaming getInstance() {
        return ourInstance;
    }

    private Streaming() {
        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //new PlayerTask().execute(stream);
        /*Esto hay que quitarlo de aqui*/
        /*try{
            mediaPlayer.setDataSource(stream);
            mediaPlayer.prepare();
            prepared = true;
        }catch (IOException e){
            e.printStackTrace();
        }*/
    }

    public static boolean isIsPlaying() {
        return isPlaying;
    }

    public static void setIsPlaying(boolean isPlaying) {
        Streaming.isPlaying = isPlaying;
    }

    public static boolean isPrepared() {
        return prepared;
    }

    public static void setPrepared(boolean prepared) {
        Streaming.prepared = prepared;
    }

    public static AudioManager getAudioManager() {
        return audioManager;
    }

    public static void setAudioManager(AudioManager audioManager) {
        Streaming.audioManager = audioManager;
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer) {
        Streaming.mediaPlayer = mediaPlayer;
    }

    public static void initMediaPlayer(){
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    public static String getStream() {
        return stream;
    }

    public static void setStream(String stream) {
        Streaming.stream = stream;
        pause();
        try{

            //mediaPlayer = new MediaPlayer();
            //initMediaPlayer();
            /*if(isPlaying){
                isPlaying = false;
                mediaPlayer.pause();
                mediaPlayer.stop();
            }*/
            mediaPlayer.setDataSource(stream);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    //play();
                }
            });
            prepared = true;
        }catch (IOException e){
            e.printStackTrace();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    public static String getIdEmisora() {
        return idEmisora;
    }

    public static void setIdEmisora(String idEmisora) {
        Streaming.idEmisora = idEmisora;
    }

    public static String getEmisora_name() {
        return emisora_name;
    }

    public static void setEmisora_name(String emisora_name) {
        Streaming.emisora_name = emisora_name;
    }

    public static void play(){
        if(!isIsPlaying()){
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    public static void pause(){
        if(isIsPlaying()){
            mediaPlayer.pause();
            mediaPlayer.reset();
            isPlaying = false;
        }
    }

    public static void cleanStreaming(){
        setStream("");
        setIdEmisora("");
        setEmisora_name("");
        prepared = false;
        mediaPlayer.release();
    }
}
