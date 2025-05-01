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

import com.example.appliakcija3.Sockets.ClientSocket;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

        } else if (id == R.id.Game_B1_Button) {
            ButtonLogic(id,"B1");

        } else if (id == R.id.Game_B2_Button) {
            ButtonLogic(id,"B2");

        } else if (id == R.id.Game_B3_Button) {
            ButtonLogic(id,"B3");

        } else if (id == R.id.Game_C1_Button) {
            ButtonLogic(id,"C1");

        } else if (id == R.id.Game_C2_Button) {
            ButtonLogic(id,"C2");

        } else if (id == R.id.Game_C3_Button) {
            ButtonLogic(id,"C3");

        }


    }
    Set<String> MetaDataSet = new HashSet<>();

    //a collection of moves that are available
    Set<String> set;
    //Users set -for logic when determining winning condition
    Set<String> Playerset = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);


        MetaDataSet.add("A1");
        MetaDataSet.add("A2");
        MetaDataSet.add("A3");
        MetaDataSet.add("B1");
        MetaDataSet.add("B2");
        MetaDataSet.add("B3");
        MetaDataSet.add("C1");
        MetaDataSet.add("C2");
        MetaDataSet.add("C3");
        set = new HashSet<>(MetaDataSet);

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
        try {
            ClientSocket.startClient("192.168.0.19");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ThreadCheckerForTurnUI();
        //ThreadChangeTurn();


    }

    boolean running=true;
    public static String opponentMove;
    public static boolean YourTurn=true;
    //Happens when a move by the player is legal
    public void GameRoundTrue(int selectedButtonID,String setElement) throws InterruptedException, IOException {
        YourTurn=false;
        Button SelectedButton = findViewById(selectedButtonID);
        SelectedButton.setText("Pressed");

        //FUNKCIJA KOJA GLEDA JEL JE IGRAC POBEDIO
        Playerset.add(setElement);
        if(WinningConditionChecker(Playerset)){
        ClientSocket.SendString("I WON");

        }
        TextView textView1= findViewById(R.id.Game_Turn_TextView);
        textView1.setText("Opponents Turn");
        textView1.setTextColor(Color.RED);



        //ceka red
        ClientSocket.SendString(setElement);


        ClientSocket.WaitMove();




    }

    //Logic that checks if the oplayer has won the game
    public boolean WinningConditionChecker(Set PlayerPlayedMoves){

        //HORIZONTAL
        if (PlayerPlayedMoves.contains("A1") && PlayerPlayedMoves.contains("A2") && PlayerPlayedMoves.contains("A3")) return true;
        else if (PlayerPlayedMoves.contains("B1") && PlayerPlayedMoves.contains("B2") && PlayerPlayedMoves.contains("B3")) return true;
        else if (PlayerPlayedMoves.contains("C1") && PlayerPlayedMoves.contains("C2") && PlayerPlayedMoves.contains("C3")) return true;

        //VERTICAL
        else if (PlayerPlayedMoves.contains("A1") && PlayerPlayedMoves.contains("B1") && PlayerPlayedMoves.contains("C1")) return true;
        else if (PlayerPlayedMoves.contains("A2") && PlayerPlayedMoves.contains("B2") && PlayerPlayedMoves.contains("C2")) return true;
        else if (PlayerPlayedMoves.contains("A3") && PlayerPlayedMoves.contains("B3") && PlayerPlayedMoves.contains("C3")) return true;

        //DIAGONAL
        else if (PlayerPlayedMoves.contains("A1") && PlayerPlayedMoves.contains("B2") && PlayerPlayedMoves.contains("C3")) return true;
        else if (PlayerPlayedMoves.contains("C1") && PlayerPlayedMoves.contains("B2") && PlayerPlayedMoves.contains("A3")) return true;

        return false;


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

    public Button opponentButtonUI(String setElement){

        if (setElement.equals("A1")&& set.contains("A1")) {
            return getButtonView(R.id.Game_A1_Button);


        }
        else if (setElement == "A2"&& set.contains("A2")) {
            set.remove("A2");
            return getButtonView(R.id.Game_A2_Button);


        } else if (setElement == "A3"&& set.contains("A3")) {
            getButtonView(R.id.Game_A3_Button).setText("Opponent");
            set.remove("A3");

        } else if (setElement == "B1"&& set.contains("B1")) {
            getButtonView(R.id.Game_B1_Button).setText("Opponent");
            set.remove("B1");

        } else if (setElement == "B2"&& set.contains("B2")) {
            getButtonView(R.id.Game_B2_Button).setText("Opponent");
            set.remove("B2");

        } else if (setElement == "B3"&& set.contains("B3")) {
            getButtonView(R.id.Game_B3_Button).setText("Opponent");
            set.remove("B3");

        } else if (setElement == "C1"&& set.contains("C1")) {
            getButtonView(R.id.Game_C1_Button).setText("Opponent");
            set.remove("C1");

        } else if (setElement == "C2"&& set.contains("C2")) {
            getButtonView(R.id.Game_C2_Button).setText("Opponent");
            set.remove("C2");

        } else if (setElement == "C3"&& set.contains("C3")) {
            getButtonView(R.id.Game_C3_Button).setText("Opponent");
            set.remove("C3");

        }
        return null;
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
                //updateOpponentMoveUI(opponentMove);
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
                        Thread.sleep(1000);
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
                GameRoundTrue(id,setElement);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }



    }


    public Button getButtonView(int id){
        Button button= findViewById(id);
        return button;
    }




}