package com.example.appliakcija3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.appliakcija3.SocketsPackage.SocketHandling;
import com.example.appliakcija3.SocketsPackage.ValidationSockets;

import java.io.IOException;

public class ConnectServerActivity extends AppCompatActivity implements View.OnClickListener {

    public ConnectServerActivity() throws IOException {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.ConnectServer_Join_Button) {


            try {
                onClickButton();

            } catch (IOException e) {


            }
        } else if (id == R.id.ConnectServer_Menu_Button) {
            finish();

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_connectserver);


        //BUTTONS
        Button ConnectServer_Join_Button = findViewById(R.id.ConnectServer_Join_Button);
        Button ConnectServer_Menu_Button = findViewById(R.id.ConnectServer_Menu_Button);


        //LISTENERS
        ConnectServer_Join_Button.setOnClickListener(this);
        ConnectServer_Menu_Button.setOnClickListener(this);
        ConnectServer_Menu_Button.setOnClickListener(this);
    }


    public void onClickButton() throws IOException {



        EditText edittext1= findViewById(R.id.ConnectServer_IPAddressInput_EditText);
        TextView textview1= findViewById(R.id.ConnectServer_StatusOutput_TextView);


        String Input = edittext1.getText().toString();

        try{
            if (ValidationSockets.IsIPAddressFormat(Input)){
                boolean flag = SocketHandling.startClient(Input);

                if (flag){

                    Intent intent = new Intent(ConnectServerActivity.this, GameActivity.class);
                    intent.putExtra("vsPlayer", true);
                    intent.putExtra("yourTurn", true);
                    startActivity(intent);
                    Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
                }
                else {
                    textview1.setText("Unable to connect to host at: " +  Input);
                }

            }
            else{
                textview1.setText("Input is not an IP Address!");
            }



        } catch(Exception ignored) {

        }




    }
}