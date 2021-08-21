package com.example.videoconferencingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class DashboardActivity extends AppCompatActivity {

    EditText secretCodeBox;
    Button joinBtn, shareBtn, createBtn ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        secretCodeBox = findViewById(R.id.codeBox);
        joinBtn = findViewById(R.id.joinBtn);
        shareBtn = findViewById(R.id.shareBtn);
        createBtn = findViewById(R.id.createBtn);

        URL serverURL;

        try{
            serverURL =new URL("https//meet.jit.si");

            JitsiMeetConferenceOptions defaultOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL)
                    .setWelcomePageEnabled(false)
                    .build();

            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        }
        catch (MalformedURLException e ){
            e.printStackTrace();
        }


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(secretCodeBox.getText().toString())
                        .setWelcomePageEnabled(false)
                        .build();

                JitsiMeetActivity.launch(DashboardActivity.this,options);

            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                Random rnd = new Random();
                StringBuilder sb = new StringBuilder(6);
                for (int i = 0; i < 6; i++)
                    sb.append(chars.charAt(rnd.nextInt(chars.length())));
                secretCodeBox.setText(sb);
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = secretCodeBox.getText().toString();
                if (code.length() < 6 || code.length() > 6 ) {
                    Toast.makeText(getApplicationContext(),"Your 6 digit Code is not valid!",Toast.LENGTH_SHORT).show();
                }
                else if (code.length() == 6 ) {
                    Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT," From Connect Kora");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Connect Kora \n Join code: "+ code);
                    startActivity(Intent.createChooser(shareIntent, "Share via "));
                }

            }
        });
    }
}