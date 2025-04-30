package com.example.appliakcija3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {




    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.Game_A1_Button) {
            ButtonLogic(id,"A1");



        } else if (id == R.id.Game_A2_Button) {
            ButtonLogic(id,"A2");


        } else if (id == R.id.Game_A3_Button) {
            ButtonLogic(id,"A3");


        }

    }

    Set<String> set = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        set.add("A1");
        set.add("A2");
        set.add("A3");
        set.add("B1");
        set.add("B2");
        set.add("B3");
        set.add("C1");
        set.add("C2");
        set.add("C3");

        //BUTTONS
        Button Game_A1_Button =findViewById(R.id.Game_A1_Button);
        Button Game_A2_Button =findViewById(R.id.Game_A2_Button);
        Button Game_A3_Button =findViewById(R.id.Game_A3_Button);

        Button Game_B1_Button =findViewById(R.id.Game_B1_Button);
        Button Game_B2_Button =findViewById(R.id.Game_B2_Button);
        Button Game_B3_Button =findViewById(R.id.Game_B3_Button);

        Button Game_C1_Button =findViewById(R.id.Game_C1_Button);
        Button Game_C2_Button =findViewById(R.id.Game_C2_Button);
        Button Game_C3_Button =findViewById(R.id.Game_C3_Button);

        //LISTENERS
        Game_A1_Button.setOnClickListener(this);
        Game_A2_Button.setOnClickListener(this);
        Game_A3_Button.setOnClickListener(this);

        Game_B1_Button.setOnClickListener(this);
        Game_B2_Button.setOnClickListener(this);
        Game_B3_Button.setOnClickListener(this);

        Game_C1_Button.setOnClickListener(this);
        Game_C2_Button.setOnClickListener(this);
        Game_C3_Button.setOnClickListener(this);

        ThreadCheckerForTurnUI();
        ThreadChangeTurn();
    }

    boolean running=true;
    boolean YourTurn=true;

    public void GameRoundTrue(int selectedButtonID) throws InterruptedException {
        Button SelectedButton = findViewById(selectedButtonID);
        SelectedButton.setText("Pressed");
        YourTurn=false;
        TextView textView1= findViewById(R.id.Game_Turn_TextView);
        textView1.setText("Opponents Turn");
        textView1.setTextColor(Color.RED);





        //textView1.setText("Your Turn");
        //textView1.setTextColor(Color.GREEN);

    }

    //Check if its our turn and checks if the button already has text(if it doesnt then it was not clicked-LOGIC)
    public boolean IsItPossibleBoolean(String setElement){

        if (YourTurn && set.contains(setElement)){
            set.remove(setElement);

            Log.d("GameActivity1", "MOZE DUGME+");
            return true;

        }
        Log.d("GameActivity1", "NEMOS DUGME-");
        return false;
    }




    public void ThreadCheckerForTurnUI(){
        TextView textView1= findViewById(R.id.Game_Turn_TextView);
        new Thread(() -> {
            while (running) {
                while (!YourTurn) {
                    try {
                        Thread.sleep(100);
                        Log.d("ThreadDebug1", "Updating UI1");
                        runOnUiThread(() -> {
                            textView1.setText("Opponents Turn");
                            textView1.setTextColor(Color.RED);
                        });
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                Log.d("ThreadDebug1", "Updating UI2");
                runOnUiThread(() -> {

                    textView1.setText("Your Turn2");
                    textView1.setTextColor(Color.GREEN);
                });

            }

        }).start();
    }

    public void ThreadChangeTurn(){
        new Thread(() -> {
            while (running) {
                while (!YourTurn) {
                    try {
                        Log.d("ThreadDebug1", "false");
                        Thread.sleep(10000);
                        YourTurn=true;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                Log.d("ThreadDebug1", "true");


            }

        }).start();
    }



    public void ButtonLogic(int id,String setElement){
        Log.d("GameActivity1", "A1");
        if(IsItPossibleBoolean(setElement)){
            Log.d("GameActivity1", "PROSLO A1");

            try {
                GameRoundTrue(id);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



    }



}