package com.example.appliakcija3;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.Main_PlayComputer_Button) {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("vsPlayer", false);
            intent.putExtra("yourTurn", false);
            startActivity(intent);

        } else if (id == R.id.Main_HostServer_Button) {
            Intent intent = new Intent(MainActivity.this, HostServerActivity.class);
            startActivity(intent);

        } else if (id == R.id.Main_ConnectServer_Button) {
            Intent intent = new Intent(MainActivity.this, ConnectServerActivity.class);
            startActivity(intent);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);





        //BUTTONS
        Button Main_PlayComputer_Button = findViewById(R.id.Main_PlayComputer_Button);
        Button Main_HostServer_Button = findViewById(R.id.Main_HostServer_Button);
        Button Main_ConnectServer_Button = findViewById(R.id.Main_ConnectServer_Button);



        //LISTENERS
        Main_PlayComputer_Button.setOnClickListener(this);
        Main_HostServer_Button.setOnClickListener(this);
        Main_ConnectServer_Button.setOnClickListener(this);








    }









}