package com.example.appliakcija3;

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
        running=true;
        yourTurn =true;
        opponentMove="";
        vsPlayer=false;
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
        if(vsPlayer){
            try {
                SocketHandling.startClient("192.168.0.19");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if(!yourTurn || vsPlayer==true){
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
                SocketHandling.clientSocket.close();
                finish();
                return;
            } else if (set.isEmpty()){
                GameWon('D');
                SocketHandling.clientSocket.close();
                finish();
                return;
            }
            Button button = opponentButtonUI(opponentMove);
            button.setText("Opponent");
            opponentMove="";
            yourTurn=true;
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
            Log.d("GameActivity1", str);
            opponentMove=str;

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

        stringList.add("By mine own hand and cunning, I hath prevailed!");
        stringList.add("Lo! My foes did tremble and flee before mine armies.");
        stringList.add("I hath constructed a Wonder so wondrous, they could not contest it.");
        stringList.add("The villagers sang songs of my glory ere the battle was yet done.");
        stringList.add("Forsooth! Mine scouts didst find the enemy’s sheep… and feasted.");
        stringList.add("My trebuchets sang a mighty song of stone and ruin!");
        stringList.add("Zounds! Even the deer knelt before mine banner!");
        stringList.add("Mine peasants rejoiced, for they shall toil no more beneath enemy yoke.");
        stringList.add("The gods themselves smiled upon my Town Center this day.");
        stringList.add("Aye! No boar, wolf, nor knight could stay my victory!");
        stringList.add("Thy walls were strong, but my rams wert stronger still!");
        stringList.add("I hath claimed every relic, and the monks did cheer my name!");
        stringList.add("My men-at-arms grew tired… of winning!");
        stringList.add("By might and right, I hath brought ruin to mine enemies!");
        stringList.add("The last enemy villager begged to join my kingdom—nay, I made him jester!");
        stringList.add("The sea itself yielded fish enough to feed an army—and so it did!");
        stringList.add("Even the blacksmith wept, for no blade could best mine own!");
        stringList.add("Not a single villager idled; their work wrought a kingdom unmatched.");
        stringList.add("The heralds blew their trumpets so loud, the enemy castles crumbled!");
        stringList.add("Alas for thee; rejoice for me! Victory is mine!");

        return stringList.get(rand.nextInt(stringList.size()));

    }

    public static String getLoseText(){
        Random rand = new Random();
        List<String> stringList= new ArrayList<>();

        stringList.add("'Odsblood! I accidentally resigned by mistake.");
        stringList.add("My townsfolk began too close to mine kingdom's Town Center.");
        stringList.add("Far too many birds flew over mine kingdom.");
        stringList.add("A mere a single scout was present to serve in the beginning.");
        stringList.add("My sheep perished when my folk sought to use it as food.");
        stringList.add("Forsooth, I couldst not tame any wolves.");
        stringList.add("All my beginning citizenry were male.");
        stringList.add("Thy heraldic color was superior to mine own.");
        stringList.add("My ignorant folk couldst not replant the berries!");
        stringList.add("Thou art human, with soul and wit. I am naught but clockwork!");
        stringList.add("My throne was most uncomfortable.");
        stringList.add("Forsooth, the nearby boars grunted too loudly and frequently.");
        stringList.add("'Zounds! I couldst not build a Wonder in the Dark Age!");
        stringList.add("I couldst not comprehend mine folks' speech!");
        stringList.add("My peasants wert puling wretches (a puny 25 hit points)!");
        stringList.add("The shore fish near mine villager appeared all too sleepy.");
        stringList.add("A graceless hillock rose too near my Town Center.");
        stringList.add("My peasants' huts all faced different directions!");
        stringList.add("The deer fled when mine hunters sought to slay them.");
        stringList.add("When I commanded a peasant to hunt fierce boar, it slew him.");
        stringList.add("My peasants foolishly walk on foot instead of astride ponies.");
        stringList.add("Alas, I could find naught but fools gold.");
        stringList.add("No wonder thou wert victorious! I shalt abdicate.");

        return stringList.get(rand.nextInt(stringList.size()));

    }

    public String getDrawText(){
        Random rand = new Random();
        List<String> stringList= new ArrayList<>();

        stringList.add("We fought bravely… and yet, no victor claimeth the field.");
        stringList.add("Twas a battle most even—neither crown nor grave awarded.");
        stringList.add("The armies didst clash, but the earth herself called for peace.");
        stringList.add("Mine sword hath dulled, thine shield hath cracked… and yet, we both yet stand.");
        stringList.add("Alas! The battle hath ended, but no banner flyeth higher.");
        stringList.add("Neither famine nor feast; neither triumph nor defeat. A curious end, indeed.");
        stringList.add("Our scribes declare: the chronicles shall record naught but stalemate.");
        stringList.add("The battlefield lies silent, strewn with valor but lacking victory.");
        stringList.add("The heavens decreed no winner this day; only weary soldiers remain.");
        stringList.add("Though we fought with might, fate hath balanced the scales between us.");

        return stringList.get(rand.nextInt(stringList.size()));

    }







}