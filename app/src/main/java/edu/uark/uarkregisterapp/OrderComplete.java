package edu.uark.uarkregisterapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.Locale;

public class OrderComplete extends AppCompatActivity {

    private static final String TAG = "OrderComplete";
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int res = textToSpeech.setLanguage(Locale.ENGLISH);
                    if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.i(TAG, "onInit: Language not supported");
                    }
                } else {
                    Log.i(TAG, "onInit: init not successful");
                }
            }
        });

    }

    public void speak(){
        String message = "Thank you for shopping. Please come again";
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }
    public void returnToMain(View view){
        Log.i(TAG, "returnToMain: *********************" + this.getIntent().getParcelableExtra("intent_extra_employee").toString() );

        Intent returnToMain =new Intent(OrderComplete.this, MainActivity.class);
        returnToMain.putExtra(
                "intent_extra_employee", this.getIntent().getParcelableExtra("intent_extra_employee"));

        speak();
        startActivity(returnToMain);

    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

}
