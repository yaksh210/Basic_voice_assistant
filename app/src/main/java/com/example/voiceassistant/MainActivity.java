package com.example.voiceassistant;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.OnSwipe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    OnSwipeTouchListener onSwipeTouchListener;

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    ImageButton btn;
    static TextToSpeech textToSpeech;
     TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv=findViewById(R.id.tvTxt);
        btn=findViewById(R.id.btnSpeak);
        onSwipeTouchListener = new OnSwipeTouchListener(this, findViewById(R.id.relativeLayout));


        textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });



        //For Speech View
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });
    }
    // Start of swipe
    public static class OnSwipeTouchListener implements View.OnTouchListener {
        private final GestureDetector gestureDetector;
        Context context;
        OnSwipeTouchListener(Context ctx, View mainView) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
            mainView.setOnTouchListener(this);
            context = ctx;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
        public class GestureListener extends
                GestureDetector.SimpleOnGestureListener {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }
        void onSwipeRight() {
            Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeRight();
            textToSpeech.speak("speak Open Google to open google",TextToSpeech.QUEUE_FLUSH,null,null);

        }
        void onSwipeLeft() {
            Toast.makeText(context, "Swiped Left", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeLeft();
        }
        void onSwipeTop() {
            Toast.makeText(context, "Swiped Up", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeTop();
        }
        void onSwipeBottom() {

            Toast.makeText(context, "Swiped Down", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeBottom();
        }
        interface onSwipeListener {
            void swipeRight();
            void swipeTop();
            void swipeBottom();
            void swipeLeft();
        }
        onSwipeListener onSwipe;
    }

    // End of Swipe
    private void speak() {

        //Intent Activity
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi Speak something");
        try{
            startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if(resultCode==RESULT_OK && null!=data){
                    ArrayList<String> result= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tv.setText(result.get(0));
                }
                if(tv.getText().toString().equals("open dial"))
                {
                    Intent i=new Intent(Intent.ACTION_DIAL);
                    startActivity(i);
                    textToSpeech.speak("Opening dial pad",TextToSpeech.QUEUE_FLUSH,null,null);
                }
                if (tv.getText().toString().equals("open Google")) {
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                    startActivity(intent);
                    textToSpeech.speak("Opening Google",TextToSpeech.QUEUE_FLUSH,null,null);
                }
                if (tv.getText().toString().equals("what is the weather")) {
                    Intent intent2=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=weather+forecast&rlz=1C1GCEJ_enIN874IN874&oq=weather+forecast&aqs=chrome..69i57j69i59l2j0i271l2.10029j0j1&sourceid=chrome&ie=UTF-8"));
                    startActivity(intent2);
                    textToSpeech.speak("Today's weather is shown",TextToSpeech.QUEUE_FLUSH,null,null);
                }
                if (tv.getText().toString().equals("open Instagram")) {
                    Intent intent3=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com//"));
                    startActivity(intent3);
                    textToSpeech.speak("opening Instagram",TextToSpeech.QUEUE_FLUSH,null,null);
                }
                if (tv.getText().toString().equals("who developed you")) {
                    textToSpeech.speak("Yaksh Rawal Build me",TextToSpeech.QUEUE_FLUSH,null,null);
                    Toast.makeText(this,"Yaksh Rawal",Toast.LENGTH_SHORT).show();
                }

                break;
            }
        }
    }
}