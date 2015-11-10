package se.mah.ac9511.towerd;


import android.content.Context;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class optionsFragment extends Fragment {
public CheckBox checkBoxOn,checkBoxOff;
  public MediaPlayer mySound;
    SeekBar volControl;
    private AudioManager audioManager = null;
    TextView mControl;
    public optionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_options, container, false);
        checkBoxOn = (CheckBox) v.findViewById(R.id.checkBox);
        checkBoxOff = (CheckBox) v.findViewById(R.id.checkBox2);
        volControl = (SeekBar) v.findViewById(R.id.seekBar);
        mySound=MediaPlayer.create(getContext(),R.raw.l2);
        mControl=(TextView)v.findViewById(R.id.textView4);
        checkBoxOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(checkBoxOn.isChecked()){
                   mySound.setVolume(1,1);
               }
            }
        });
        checkBoxOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxOff.isChecked()) {
                    mySound.setVolume(0, 0);
                }
            }
        });


        volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser)
                {
                    mControl.setText("Music volume: " + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        return v;

    }

}
