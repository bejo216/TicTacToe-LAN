package com.example.appliakcija3;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import com.example.appliakcija3.R;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appliakcija3.Sockets.ClientSocket;
import com.example.appliakcija3.Sockets.ValidationSockets;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.Main_PlayComputer_Button) {
            Log.d("SocketConnection", "0");
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);

        } else if (id == R.id.Main_HostServer_Button) {
            Log.d("SocketConnection", "1");
            Intent intent = new Intent(MainActivity.this, HostServerActivity.class);
            startActivity(intent);

        } else if (id == R.id.Main_ConnectServer_Button) {
            Log.d("SocketConnection", "2");
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