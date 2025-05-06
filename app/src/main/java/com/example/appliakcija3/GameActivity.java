package com.example.appliakcija3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appliakcija3.SocketsPackage.SocketHandling;

import java.io.IOException;
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


    //a collection of moves that are available

    //Users set -for logic when determining winning condition
    Set<String> playerSet;
    public static Set<String> opponentSet;
    Set<String> set;
    public static boolean running;
    public static String opponentMove;
    public static boolean yourTurn;
    public static boolean vsPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();

        running=true;
        yourTurn =intent.getBooleanExtra("yourTurn", true);
        opponentMove="";
        vsPlayer=intent.getBooleanExtra("vsPlayer", false);
        Set<String> MetaDataSet = new HashSet<>();

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
        playerSet = new HashSet<>();
        opponentSet = new HashSet<>();
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

        if(!vsPlayer && !yourTurn){
            try {
                ComputerMove();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
        if(!yourTurn && vsPlayer){
            OpponentMoveUpdateThread();
        }




    }


    //Happens when a move by the player is legal
    public void GameRoundTrue(int selectedButtonID,String setElement) throws InterruptedException, IOException {
        yourTurn =false;
        Button SelectedButton = findViewById(selectedButtonID);
        SelectedButton.setText("Pressed");

        //Function looks if the player has WON
        playerSet.add(setElement);
        if(WinningConditionChecker(playerSet)){
            GameWon('W');
            if (vsPlayer){
                SocketHandling.SendString("W");
                SocketHandling.clientSocket.close();
            }
            running=false;
            finish();
            return;

        }
        //Function looks if there are no more available positions
        if(set.isEmpty()){
            GameWon('D');
            if (vsPlayer){
                SocketHandling.SendString("D");
                SocketHandling.clientSocket.close();



            }
            running=false;
            finish();
            return;

        }

        TextView textView1= findViewById(R.id.Game_Turn_TextView);
        textView1.setText("Opponents Turn");
        textView1.setTextColor(Color.RED);

        //Computer plays and sees if its L or D
        if(!vsPlayer){
            ComputerMove();


            if (WinningConditionChecker(opponentSet)){
                GameWon('L');
                finish();
                return;

            } else if (set.isEmpty()){
                GameWon('D');
                finish();
                return;

            }

        }
        else {
            SocketHandling.SendString(setElement);
            SocketHandling.WaitMove();
            OpponentMoveUpdateThread();

        }





    }

    //Logic that checks if the oplayer has won the game
    public boolean WinningConditionChecker(Set playedMoves){

        //HORIZONTAL
        if (playedMoves.contains("A1") && playedMoves.contains("A2") && playedMoves.contains("A3")) return true;
        else if (playedMoves.contains("B1") && playedMoves.contains("B2") && playedMoves.contains("B3")) return true;
        else if (playedMoves.contains("C1") && playedMoves.contains("C2") && playedMoves.contains("C3")) return true;

        //VERTICAL
        else if (playedMoves.contains("A1") && playedMoves.contains("B1") && playedMoves.contains("C1")) return true;
        else if (playedMoves.contains("A2") && playedMoves.contains("B2") && playedMoves.contains("C2")) return true;
        else if (playedMoves.contains("A3") && playedMoves.contains("B3") && playedMoves.contains("C3")) return true;

        //DIAGONAL
        else if (playedMoves.contains("A1") && playedMoves.contains("B2") && playedMoves.contains("C3")) return true;
        else if (playedMoves.contains("C1") && playedMoves.contains("B2") && playedMoves.contains("A3")) return true;

        return false;


    }

    //Check if its our turn and checks if the button already has text(if it doesnt then it was not clicked-LOGIC)
    public boolean IsItPossibleBoolean(String setElement){

        if (yourTurn && set.contains(setElement)){
            set.remove(setElement);

            Log.d("GameActivity1", "MOZE DUGME+");
            return true;

        }
        Toast.makeText(getApplicationContext(), "Impossible move", Toast.LENGTH_SHORT).show();
        Log.d("GameActivity1", "NEMOS DUGME-");
        return false;
    }

    public Button opponentButtonUI(String setElement){

        if (setElement.equals("A1")&& set.contains("A1")) {
            set.remove("A1");
            return getButtonView(R.id.Game_A1_Button);

        }
        else if (setElement.equals("A2") && set.contains("A2")) {
            set.remove("A2");
            return getButtonView(R.id.Game_A2_Button);


        } else if (setElement.equals("A3") && set.contains("A3")) {
            set.remove("A3");
            return getButtonView(R.id.Game_A3_Button);

        } else if (setElement.equals("B1") && set.contains("B1")) {
            set.remove("B1");
            return getButtonView(R.id.Game_B1_Button);

        } else if (setElement.equals("B2") && set.contains("B2")) {
            set.remove("B2");
            Log.d("ThreadDebug2", "jel poslao b2 mentalc");
            return getButtonView(R.id.Game_B2_Button);

        } else if (setElement.equals("B3") && set.contains("B3")) {
            set.remove("B3");
            return getButtonView(R.id.Game_B3_Button);

        } else if (setElement.equals("C1") && set.contains("C1")) {
            set.remove("C1");
            return getButtonView(R.id.Game_C1_Button);

        } else if (setElement.equals("C2") && set.contains("C2")) {
            set.remove("C2");
            return getButtonView(R.id.Game_C2_Button);

        } else if (setElement.equals("C3") && set.contains("C3")) {
            set.remove("C3");
            return getButtonView(R.id.Game_C3_Button);

        }
        return null;
    }
    //TextView topText= findViewById(R.id.Game_Turn_TextView);
    public void GameWon(Character status) throws IOException {
        switch (status){
            case 'W':
                Log.d("Winning", "WON");
                Toast.makeText(getApplicationContext(), getWinText(), Toast.LENGTH_LONG).show();
                break;

            case 'L':
                Log.d("Winning", "LOST");
                Toast.makeText(getApplicationContext(), getLoseText(), Toast.LENGTH_LONG).show();
                break;

            case 'D':
                Log.d("Winning", "DRAW");
                Toast.makeText(getApplicationContext(), getDrawText(), Toast.LENGTH_LONG).show();
                break;

        }
        running=false;


    }



    public void ComputerMove() throws IOException {
        Random rand = new Random();
        List<String> list = new ArrayList<>(set);
        if(!list.isEmpty()){
            String str = list.get(rand.nextInt(list.size()));
            opponentSet.add(str);
            opponentMove=str;

            Button button = opponentButtonUI(opponentMove);
            button.setText("Opponent");
            opponentMove="";
            yourTurn=true;
            if(list.isEmpty()){
                GameWon('D');
                running=false;
                finish();
                return;
            }
        } else {
                GameWon('D');
                running=false;
                finish();
                return;

        }


    }



    //Updates the UI depending on if its your turn or not, updates opponents move when it changes and resets it, if running ends
    //finishes the intent
    public void OpponentMoveUpdateThread(){

        TextView textView1= findViewById(R.id.Game_Turn_TextView);
        new Thread(() -> {
            boolean actionDone=false;
            while (!actionDone) {
                //so it wouldnt be so performance heavy
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if(!opponentMove.equals("")){
                    if(opponentMove.equals("W")){

                        runOnUiThread(() -> {
                            try {
                                GameWon('L');
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            //DODAJ I D AKO SE DOBIJE - mozda
                        });
                        try {
                            SocketHandling.clientSocket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        finish();
                        return;
                    }

                    if(opponentMove.equals("D")){

                        runOnUiThread(() -> {
                            try {
                                GameWon('D');
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            //DODAJ I D AKO SE DOBIJE - mozda
                        });
                        try {
                            SocketHandling.clientSocket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        finish();
                        return;
                    }

                    Button button = opponentButtonUI(opponentMove);

                        runOnUiThread(() -> {
                            if (button!=null){

                            }
                            textView1.setText("Your Turn2");
                            textView1.setTextColor(Color.GREEN);
                            button.setText("Opponent");

                        });
                        actionDone=true;
                        opponentMove="";
                }
            }
            yourTurn=true;
            Log.d("zavrseno", "zavrseno1");

        }).start();
    }

    public void ButtonLogic(int id,String setElement){
        Log.d("GameActivity1", "A1");
        if (running){


        if(IsItPossibleBoolean(setElement)){
            Log.d("GameActivity1", "PROSLO A1");

            try {
                GameRoundTrue(id,setElement);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }


        }
    }

    public Button getButtonView(int id){
        Button button= findViewById(id);
        return button;
    }



    public String getWinText(){
        Random rand = new Random();
        List<String> stringList= new ArrayList<>();

        stringList.add("WIN! Victory is mine! Sound the trumpets!");
        stringList.add("WIN! By sword and wit, I hath prevailed!");
        stringList.add("WIN! The foes did flee before my banner!");
        stringList.add("WIN! Lo! I stand triumphant this day!");
        stringList.add("WIN! All hail! The battle is won!");

        return stringList.get(rand.nextInt(stringList.size()));

    }

    public static String getLoseText(){
        Random rand = new Random();
        List<String> stringList= new ArrayList<>();

        stringList.add("LOSE! Alas! My kingdom lies in ruin.");
        stringList.add("LOSE! I am defeated; fate was unkind.");
        stringList.add("LOSE! Woe! Mine armies hath fallen.");
        stringList.add("LOSE! My banner is trampled in the dust.");
        stringList.add("LOSE! Zounds! I hath lost this day.");

        return stringList.get(rand.nextInt(stringList.size()));

    }

    public String getDrawText(){
        Random rand = new Random();
        List<String> stringList= new ArrayList<>();

        stringList.add("DRAW! The battle ends, yet no victor stands.");
        stringList.add("DRAW! No crown, no grave; a stalemate remains.");
        stringList.add("DRAW! Neither side claimeth the field today.");
        stringList.add("DRAW! Peace holds sway o'er this weary land.");
        stringList.add("DRAW! A draw! The bards will puzzle at this.");

        return stringList.get(rand.nextInt(stringList.size()));

    }







}