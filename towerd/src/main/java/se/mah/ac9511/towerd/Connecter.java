package se.mah.ac9511.towerd;



import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

interface ConnecterEventListener
{
    public void onEvent();
    public void onStatusUpdate();
    public void onLobbyChangedUpdate();
}
enum Status
{
    IDLE,
    OK,
    FAILED,
    BUILD_TOWER_1,
    BUILD_TOWER_2,
    BUILD_TOWER_3,
    BUILD_TOWER_4;

    public static Status fromInteger(int x) {
        switch(x) {
            case 0:
                return IDLE;
            case 1:
                return OK;
            case 2:
                return FAILED;
            case 3:
                return BUILD_TOWER_1;
            case 4:
                return BUILD_TOWER_2;
            case 5:
                return BUILD_TOWER_3;
            case 6:
                return BUILD_TOWER_4;
        }
        return null;
    }
    public static Status fromString(String x) {
        switch(x) {
            case "0":
                return IDLE;
            case "1":
                return OK;
            case "2":
                return FAILED;
            case "3":
                return BUILD_TOWER_1;
            case "4":
                return BUILD_TOWER_2;
            case "5":
                return BUILD_TOWER_3;
            case "6":
                return BUILD_TOWER_4;
        }
        return null;
    }
}

enum ToUpdate
{
    GOLD,
    KILLS,
    LIVES_LEFT,
    SCORE
}

enum StateOfApp
{
    IN_GAME,
    IN_LOBBY,
    OUTSIDE
}

public class Connecter {

    private Firebase mMainConnectionRef;
    private String mShortcut;
    private ConnecterEventListener mConnecterEventListener;
    public void setmConnecterEventListener(ConnecterEventListener connecterEventListener) {
        this.mConnecterEventListener = connecterEventListener;
    }

    private ConnecterEventListener mStatusEventListener;


    public void setStatusEventListener(ConnecterEventListener statusEventListener) {
        this.mStatusEventListener = statusEventListener;
    }
private ConnecterEventListener mLobbyChangedEventListener;

    public void setmLobbyChangedEventListener(ConnecterEventListener mLobbyChangedEventListener) {
        this.mLobbyChangedEventListener = mLobbyChangedEventListener;
    }

    //private Firebase playerRef = new Firebase(a"https://blistering-heat-6102.firebseio.com/");//Lek v?rde
    //private int mGold = 0;
   // private int mKills;
    //private int mScore;
   // private int mLivesLeft;
    private boolean mIsAlive;
    User mUser;
    private boolean mIsLobbyHost;

   // private int mPlayerNr;
    private Status mStatus;
    private String[] mPlayerName;
    private boolean[] mPlayerReady;
    private StateOfApp mAppState;



    private StateOfApp mPrevAppState;

    private String mMyPlayerName;
    private List<ListenFirebase> mListenerMemory;

    //region Getters and Setters
    public void setmShortcut(String s)
    {
        mShortcut = s;
    }

    public StateOfApp getmPrevAppState() {
        return mPrevAppState;
    }

    public String getmMyPlayerName() {
        return mMyPlayerName;
    }

    public void setMyPlayerName(String mMyPlayerName) {
        this.mMyPlayerName = mMyPlayerName;
    }

    public String getPlayerName(int index) {
        return mPlayerName[index];
    }

    public boolean getPlayerReady(int index) { return mPlayerReady[index];}

    public boolean[] getAllPlayersReadiness() {return mPlayerReady;}

    public Status getmStatus() {
        return mStatus;
    }

   // public int getmPlayerNr() {
       // return mPlayerNr;
  //  }

    public boolean IsLobbyHost() {
        return mIsLobbyHost;
    }

    //public int getLivesLeft() {
    //    return mLivesLeft;
    //}

   // public int getScore() {
       // return mScore;
   // }

   // public int getKills() {
   //     return mKills;
    //}

   // public int getGold() {
     //   return mGold;
    //}

    public StateOfApp getmAppState() {
        return mAppState;
    }

    public void setmAppState(StateOfApp state) {
        this.mPrevAppState = mAppState;
        this.mAppState = state;
    }

    /** Ska s?ttas i konstruktorn.
     public void setmMainConnectionRef(Firebase mMainConnectionRef) {
     this.mMainConnectionRef = mMainConnectionRef;
     }*/
//endregion

    public Connecter(Firebase mainConnectionRef,User user ,String myPlayerName)
    {
        this.mMainConnectionRef = mainConnectionRef;
      //  mGold = 0;
       // mKills = 0;
        //mScore = 0;
        //mLivesLeft = 0;
        mIsAlive = false;
        mStatus = Status.IDLE;
        mPlayerName = new String[4];
        mPlayerReady = new boolean[4];
        mMyPlayerName = myPlayerName;
        mListenerMemory = new ArrayList<>();
        //mPlayerNr = 9999;
        mAppState = StateOfApp.OUTSIDE;
        mPrevAppState = StateOfApp.OUTSIDE;
        this.mUser=user;

    }

    /** Outward facing call to enter the lobby. Attacting lobby litener separate from an initial
     * read of firebase since initial read is time dependent and needs to call a method and we
     * do not want to have method in the regular listeners.*/
    public void EnterLobby()
    {
        AttachLobbyListeners();
        InitialLobbyRead(mMainConnectionRef.child("Lobby"));
    }


    public void EnterGame(int playerNumber)
    {
         mUser.setPlayerId(playerNumber);
        mShortcut = "Game/" + Integer.toString(playerNumber);
        AttachInGameListeners(mMainConnectionRef.child(mShortcut));
        mPrevAppState = mAppState;
        mAppState = StateOfApp.IN_GAME;
    }

    /** Outward facing call to exit the lobby.*/
    public void ExitLobby()
    {
        RemoveLobbyListeners();
        if(mAppState == StateOfApp.IN_LOBBY|| mAppState == StateOfApp.IN_GAME)
        {
            UpdateNodeStr( "Lobby/"+ mUser.getPlayerId(),"Name","");
            if(mPlayerReady[mUser.getPlayerId()])
            {
                ReadyFlipFlop(mUser.getPlayerId());
            }
        }
        mIsLobbyHost = false;
        if(mIsAlive)
        {
            UpdateNodeBool(mShortcut,"IsAlive",false);
        }
    }

    /** Outward call to change ready status */
    public void ReadyFlipFlop(int playernumber)
    {
        if(mPlayerReady[playernumber])
        {
            mPlayerReady[playernumber] = false;
        }
        else
        {
            mPlayerReady[playernumber] = true;
        }
        UpdateNodeBool("Lobby/" + playernumber, "Ready", mPlayerReady[playernumber]);
    }

    /** Outward facing method for sending positing updates to firebase so the game server
     * can update the pointer position */
    public void UpdatePosition(int x,int y)
    {
        Log.v("Connecter", " Update pos " + mShortcut);
        UpdateNodeXY(mShortcut, x, y);
    }

    /** Outward facing method for changing the Status status. ex If you select a tower for building
     * this should be called with Status.BUILD_TOWER_1, and when you place a tower you will get a
     * response from the game server on success or failure */
    public void UpdateStatus(Status a)
    {
        if(mAppState == StateOfApp.IN_GAME) {
            UpdateNodeInt(mShortcut, "Status", a.ordinal());
            Log.v("Connecter", "Status: "+a.ordinal());
        }
        else
        {
            Log.v("Connecter", "Status: Not in game!!");
        }
    }

    /** Outward facing call to pre-defined pushes to firebase*/
    public void UpdateIntValue(ToUpdate task,int value)
    {
        if(mAppState == StateOfApp.IN_GAME) {
            switch (task) {
                case GOLD:
                    UpdateNodeStr(mShortcut, "Gold", Integer.toString(value));
                    break;
                case KILLS:
                    UpdateNodeStr(mShortcut, "Kills", Integer.toString(value));
                    break;
                case LIVES_LEFT:
                    UpdateNodeStr(mShortcut, "LivesLeft", Integer.toString(value));
                    break;
                case SCORE:
                    UpdateNodeStr(mShortcut, "Score", Integer.toString(value));
                    break;
                default:
                    System.out.println("Error Has occurred in Connector; UpdateIntValue");
                    Log.d("UpdateIntValue", "Error Has occurred in Connector; UpdateIntValue");
                    break;
            }
        }
        else
        {
            Log.v("Connecter", "UpdateIntValue: Not in game!!");
        }
    }

    /** Simple outward facing method for pushing game option changes to firebase */
    public boolean CommitOtionsChanges(int difficulty,int numberOfLives)
    {
        if(mIsLobbyHost) {
            Firebase optionsRef = mMainConnectionRef.child("Options");
            Map<String, Object> opt = new HashMap<>();
            opt.put("Difficulty", Integer.toString(difficulty));
            opt.put("Lives", Integer.toString(numberOfLives));
            optionsRef.setValue(opt);
            return true;
        }
        else{
            System.out.println("You are not currently lobby host. Only the host can change options");
            return false;
        }
    }

    /** A helper method for populating all the fields with default values on firebase
     * CAUTION! this will nuke any content that is there into oblivion. So use with caution */
    public void SetAllToDefaultValuesOnFirebase(Firebase MainRef)
    {
        Firebase lobbyRef = MainRef.child("Lobby");
        Map<String, Object> p = new HashMap<>();
        p.put("Name","Empty");
        p.put("Ready", false);

        Map<String, Map<String, Object>> players = new HashMap<>();
        players.put("0", p);
        players.put("1", p);
        players.put("2", p);
        players.put("3", p);

        lobbyRef.setValue(players);

        Firebase gameRef = MainRef.child("Game");
        Map<String, Object> val = new HashMap<>();
        val.put("Position", "0,0");
        val.put("Kills", "0");
        val.put("LivesLeft", "100");
        val.put("Score", "0");
        val.put("Gold", "0");
        val.put("IsAlive", true);
        val.put("Status", "0");

        Map<String, Map<String, Object>> player = new HashMap<>();
        player.put("0", val);
        player.put("1", val);
        player.put("2", val);
        player.put("3", val);

        gameRef.setValue(player);

        Firebase gameOptionsRef = MainRef.child("Options");
        Map<String, Object> opt = new HashMap<>();
        opt.put("difficulty", "0");
        opt.put("Lives", "100");

        gameOptionsRef.setValue(opt);

        Firebase gameState = MainRef.child("GameState");
        //Map<String, Object> setGameState = new HashMap<>();
        //setGameState.put("GameState", "1");

        gameState.setValue("1");
    }

    //region UpdateNode
    private void UpdateNodeXY(String path, int x, int y)
    {
        if(mAppState == StateOfApp.IN_GAME) {
            Firebase place = mMainConnectionRef.child(path);
            Map<String, Object> putInt = new HashMap<String, Object>();
            String numbers = Integer.toString(x) + "," + Integer.toString(y);
            putInt.put("Position", numbers);
            Log.v("UpdateNodeXY", "Position: " + numbers);
            place.updateChildren(putInt);
        }
        else
        {
            Log.v("Connecter", "Not In game!!");
        }

    }

    private void UpdateNodeStr(String path, String nodeName, String value)
    {
        Firebase place = mMainConnectionRef.child(path);
        Map<String, Object> putInt = new HashMap<String, Object>();
        putInt.put(nodeName, value);
        Log.v("Connecter", "Nodename: " + nodeName + " " + value);
        place.updateChildren(putInt);
    }

    private void UpdateNodeInt(String path, String nodeName, int value)
    {
        Firebase place = mMainConnectionRef.child(path);
        Map<String, Object> putInt = new HashMap<String, Object>();
        putInt.put(nodeName, value);
        Log.v("Connecter", "Nodename: " + nodeName + " " + value);
        place.updateChildren(putInt);
    }

    private void UpdateNodeDouble(String path, String nodeName, double value)
    {
        Firebase place = mMainConnectionRef.child(path);
        Map<String, Object> putInt = new HashMap<String, Object>();
        putInt.put(nodeName, value);
        Log.v("Connecter", "Nodename: " + nodeName + " " + value);
        place.updateChildren(putInt);

    }

    private void UpdateNodeBool(String path, String nodeName, boolean value)
    {
        Firebase place = mMainConnectionRef.child(path);
        Map<String, Object> putBool = new HashMap<String, Object>();
        putBool.put(nodeName, value);
        Log.v("Connecter", "Nodename: " + nodeName + " " + value);
        place.updateChildren((putBool));

    }
    //endregion

    private void AttachInGameListeners(Firebase node)
    {
        mListenerMemory.add(new ListenFirebase(node.child("Gold"), node.child("Gold").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUser.setMoney(Integer.valueOf(snapshot.getValue().toString()));
                Log.v("Connecter", "Gold" + snapshot.getValue().toString());
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed" + firebaseError.getMessage());
            }
        })));

        mListenerMemory.add(new ListenFirebase(node.child("Kills"), node.child("Kills").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUser.setKills(Integer.valueOf(snapshot.getValue().toString()));
                Log.v("Connecter", "Kills" + snapshot.getValue().toString());
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed" + firebaseError.getMessage());
            }
        })));

        mListenerMemory.add(new ListenFirebase(node.child("LivesLeft"), node.child("LivesLeft").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUser.setLivesLeft(Integer.valueOf(snapshot.getValue().toString()));
                Log.v("Connecter", "LivesLeft" + snapshot.getValue().toString());
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed" + firebaseError.getMessage());
            }
        })));

        mListenerMemory.add(new ListenFirebase(node.child("Score"), node.child("Score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUser.setScore(Integer.valueOf(snapshot.getValue().toString()));
                Log.v("Connecter", "Score" + snapshot.getValue().toString());
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed" + firebaseError.getMessage());
            }
        })));

        mListenerMemory.add(new ListenFirebase(node.child("Status"), node.child("Status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUser.setPriority(Status.fromString(snapshot.getValue().toString()));
                mStatusEventListener.onStatusUpdate();
                Log.v("Connecter", "Status" + snapshot.getValue().toString());
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed" + firebaseError.getMessage());
            }
        })));

        mListenerMemory.add(new ListenFirebase(node.child("IsAlive"), node.child("IsAlive").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mIsAlive = (boolean) snapshot.getValue();
                Log.v("Connecter", "IsAlive" + snapshot.getValue().toString());
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed" + firebaseError.getMessage());
            }
        })));
    }

    /** Reads the lobby node on firebase and calls ThereIsRoom to see if there is an available spot,
     * and if not removes the Other listeners from firebase. Listeners are un-expensive
     * and therefore this extra one time listener make a clean synchronous lobby entry.*/
    private void InitialLobbyRead(Firebase ref)
    {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Forloop
                int i = 0;
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    mPlayerName[i] = postSnapshot.child("Name").getValue().toString();
                    mPlayerReady[i] = (boolean) postSnapshot.child("Ready").getValue();
                    Log.v("Connecter", "InitialLobbyRead: onDataChange " + mPlayerName[0]);
                    i++;
                }
                if (ThereIsRoom(mPlayerName)) {
                    //AttachInGameListeners(mMainConnectionRef.child(mShortcut));
                    mPrevAppState = mAppState;
                    mAppState = StateOfApp.IN_LOBBY;
                    //mAppState = StateOfApp.IN_GAME; //For testing.
                    Log.v("Connecter", "In lobby!! :)");
                } else {
                    System.out.println("No room in lobby");
                    Log.v("Connecter", "No room in lobby");
                    ExitLobby();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed" + firebaseError.getMessage());
                Log.v("Connecter", "onCancelled");
            }
        });
    }

    private boolean ThereIsRoom(String[] players)
    {
        for (int i = 0; i < players.length; i++) {
            Log.v("Connecter", "Player: " + Integer.toString(i)+ " " + players[i]);
            if (players[i].equals("Empty")) {
                mUser.setPlayerId(i);
                //mConnecterEventListener.onEvent();
                UpdateNodeStr("Lobby/" + i, "Name", mMyPlayerName);
                mShortcut = "Game/" + Integer.toString(i);
                Log.v("Connecter", "UpdateNodeStr: " + mMyPlayerName + " " + mShortcut);
                UpdateNodeInt("", "GameState", 1);
                if (i == 0) {
                    mIsLobbyHost = true;
                }
                return true;
            }
        }
        return false;
    }

    private void RemoveLobbyListeners()
    {
        int i = 0;
        for (ListenFirebase listener: mListenerMemory
                ) {
            listener.getLocation().removeEventListener(listener.getListener());
            Log.v("Connecter", "Remove lobby listeners: " + Integer.toString(i));
            i++;
        }
    }

    private void AttachLobbyListeners() {
        Log.v("Connecter", "AttachLobbyListeners");
        Firebase ref = mMainConnectionRef.child("Lobby"); //new Firebase("https://vivid-heat-894.firebaseio.com/Lobby/");//mMainConnectionRef.child("/Lobby/0/Name"); //.child("lobby").child(Integer.toString(i)).child("Name");
        mListenerMemory.add(new ListenFirebase(ref, ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //mPlayerName[0] = snapshot.child("0/Name/").getValue().toString();

                int i = 0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    mPlayerName[i] = postSnapshot.child("Name").getValue().toString();
                    mPlayerReady[i] = (boolean) postSnapshot.child("Ready").getValue();
                    Log.v("Connecter", "onDataChange " + mPlayerName[i]);
                    Log.v("Connecter", "onDataChange " + mPlayerReady[i]);
                    Log.v("Connecter", "onDataChange ---------------------");
                    i++;
                }
                mLobbyChangedEventListener.onLobbyChangedUpdate();
                //connecterUserEventListener.onLobbyUpdate();
               // mLobbyChangedEventListener.onLobbyChangedUpdate();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed" + firebaseError.getMessage());
                Log.v("Connecter", "onCancelled");
            }
        })));

        ref = mMainConnectionRef.child("GameState");
        mListenerMemory.add(new ListenFirebase(ref, ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPrevAppState = mAppState;

                switch (Integer.valueOf(dataSnapshot.getValue().toString()))
                {
                    case 0:
                        mAppState = StateOfApp.OUTSIDE;
                        break;
                    case 1:
                        mAppState = StateOfApp.IN_LOBBY;
                        break;
                    case 2:
                        if(mAppState != StateOfApp.IN_GAME)
                        {
                            EnterGame(mUser.getPlayerId());
                        }
                        mAppState = StateOfApp.IN_GAME;
                        break;
                    default:
                        break;
                }
                mConnecterEventListener.onEvent();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.v("Connecter", "onCancelled");
            }
        })));
    }



    /** lobby
     *          player1
     *              Name
     *              Ready
     *          player2
     *              Name
     *              Ready
     *          player3
     *              Name
     *              Ready
     *          player4
     *              Name
     *              Ready
     *      Options
     *          numberOfLives
     *          difficulty
     *          handicap?
     *
     *  Game
     *      player1  <--Actual name on firebase :0
     *          Position
     *              X
     *              Y
     *          Kills
     *          LivesLeft
     *          Gold
     *          Score
     *          IsAlive
     *          Status
     *             enum value range {
     *             IDLE
     *             OK
     *             FAILED
     *             BUILD_TOWER_1
     *             BUILD_TOWER_2
     *             BUILD_TOWER_3
     *             BUILD_TOWER_4} <-- Actually just numbers from 0-6
     *     player2  <--Actual name on firebase :1
     *          ...
     *
     *
     *
     *
     *
     *          Options          Testa
     *          lobby host.      Testa
     *          populate fields. Testa
     *          */

}