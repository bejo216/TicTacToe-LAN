package com.example.appliakcija3;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appliakcija3.Sockets.ClientSocket;
import com.example.appliakcija3.Sockets.ValidationSockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);




        setContentView(R.layout.activity_page2);



    }
    public void onClickbutton1(View view) throws IOException {



        EditText edittext1= findViewById(R.id.editTextInput1);

        TextView textview1= findViewById(R.id.textView1);
        String Input = edittext1.getText().toString();

        try{

            if (ValidationSockets.IsIPAddressFormat(Input)){
                textview1.setText("Connected!");
                ClientSocket clientSocket = new ClientSocket();
                clientSocket.startClient(Input);
            }
            else{
                textview1.setText("Input is not an IP Address!");
            }



        } catch(Exception e) {

        }




    }
}