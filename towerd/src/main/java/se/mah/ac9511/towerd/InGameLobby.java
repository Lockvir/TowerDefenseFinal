package se.mah.ac9511.towerd;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InGameLobby extends Fragment implements ConnecterEventListener {
    ListView listView;
    ArrayList<Players> itemsArrayList=new ArrayList<Players>();
    CheckBox playerReady;
    TextView players;
    Players pl;
    User user;
    Connecter c;
    private Firebase myFirebaseRef;
    CustomAdapter ad;
    GameScreenFragment gameScreenFragment;
    Bundle b;
    TextView wait;
    View v;
    public InGameLobby() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_in_game_lobby, container, false);
        myFirebaseRef = new Firebase("https://vivid-heat-894.firebaseio.com/");
        listView = (ListView) v.findViewById(R.id.listView);
        wait=(TextView)v.findViewById(R.id.textView6);
        user=getArguments().getParcelable("user");
        c = new Connecter(myFirebaseRef, user, user.getName());
        //c.SetAllToDefaultValuesOnFirebase(myFirebaseRef);
        c.setmConnecterEventListener(this);
        c.setStatusEventListener(this);
        c.setmLobbyChangedEventListener(this);
        c.EnterLobby();



        playerReady =(CheckBox)v.findViewById(R.id.ready_check);
        playerReady.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   c.ReadyFlipFlop(user.getPlayerId());
            }
        });
        /*playerReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInLobby)
                {
                    c.ReadyFlipFlop(user.getPlayerId());
                }
            }
        });*/

        return v;
    }
    @Override
    public void onEvent() {
        if (c.getmAppState() == StateOfApp.IN_GAME&& c.getmPrevAppState() == StateOfApp.IN_LOBBY) {
            FragmentManager fm = getFragmentManager();
            b = new Bundle();
            b.putParcelable("user", user);
            gameScreenFragment = new GameScreenFragment();
            gameScreenFragment.setArguments(b);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainer, gameScreenFragment);
            //ft.addToBackStack(null);
            ft.commit();
        }
        else if(c.getmAppState() == StateOfApp.OUTSIDE&& c.getmPrevAppState() == StateOfApp.IN_GAME)
        {
            System.exit(0);
        }

    }

    @Override
    public void onStatusUpdate() {

    }

    @Override
    public void onLobbyChangedUpdate() {




        //c.ReadyFlipFlop(user.getPlayerId());
        itemsArrayList = new ArrayList<>();
        for (int i=0; i<4;i++)
        {
            pl=new Players();
            //c.getPlayerName(i);
            pl.setName(c.getPlayerName(i));
            //pl.setPlayer1(user.getName());
            //Log.v("InGameLobby","User: "+c.getPlayerName(i));
            pl.setReady(c.getPlayerReady(i));

            itemsArrayList.add(pl);

        }
        ad=new CustomAdapter(getContext(),itemsArrayList);
        listView.setAdapter(ad);
        //   @Override
        //  public void onLobbyUpdate() {

        // }
    }

}
