package com.treecio.android.hackprague17;

import android.content.Context;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HackyVoice implements RecognitionListener {

    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private static String TAG = "HackyVoice";

    ServiceNotifiesListener listener;

    private Context con;

    List<String> data = new ArrayList<>();

    public HackyVoice(Context parent, ServiceNotifiesListener l) {

        con  = parent;
        speech = SpeechRecognizer.createSpeechRecognizer(con);
        speech.setRecognitionListener(this);

        listener = l;

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
    }

    public void listen() {
        Log.i(TAG, "Listening");
        data.clear();
        proceed();
    }

    public void proceed() {
        Log.i(TAG, "Proceeding");
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                speech.startListening(recognizerIntent);
            }
        });
    }

    public void stop() {
        Log.i(TAG, "Stopping");
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                speech.stopListening();
            }
        });    }

    public List<String> getData() {
        return data;
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(TAG, "FAILED " + errorMessage);
        listener.listeningEnded();
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(TAG, "onResults");
        String entry = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
        Log.i(TAG, "T: " + entry);
        data.add(entry);
        listener.listeningEnded();
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    public String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    void runOnMainThread(Runnable r) {
        Handler mainHandler = new Handler(con.getMainLooper());
        mainHandler.post(r);
    }

}
