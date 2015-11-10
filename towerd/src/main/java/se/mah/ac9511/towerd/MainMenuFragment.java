package se.mah.ac9511.towerd;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainMenuFragment extends Fragment {
    Button btnPlay,btnOptions,btnExit;
    TextView welcome,tPlay;
    GameScreenFragment gameScreenFragment;
    LobbyFragment lobbyFragment;
   // optionsFragment oF;
  boolean play=false;

    public MainMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_main_menu, container, false);
        welcome=(TextView)v.findViewById(R.id.textView);
        tPlay=(TextView)v.findViewById(R.id.textView2);
        btnPlay=(Button)v.findViewById(R.id.playButton);
        //btnOptions=(Button)v.findViewById(R.id.optionsButton);
        btnExit=(Button)v.findViewById(R.id.exitButton);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                lobbyFragment = new LobbyFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainer, lobbyFragment);
                //ft.addToBackStack(null);

//             if(oF.checkBoxOn.isChecked()){
//                  play=true;
//                  oF.mySound.start();
//
//              }
//               if(oF.checkBoxOff.isChecked()){
//                   play=false;
//                   oF.mySound.stop();
//
//              }
//                if(!play){
//                    oF.mySound.stop();
//                }

                ft.commit();
            }
        });

       // btnOptions.setOnClickListener(new View.OnClickListener() {
          //  @Override
          //  public void onClick(View v) {
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                oF = new optionsFragment();
//                ft.replace(R.id.fragmentContainer, oF);
//                ft.addToBackStack(null);
//                ft.commit();

       btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.exit(0);
            }
        });

        return v;
    }


}
