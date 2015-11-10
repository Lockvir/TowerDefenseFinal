package se.mah.ac9511.towerd;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;


/**
 * A simple {@link Fragment} subclass.
 */
public class LobbyFragment extends Fragment{
TextView welcome,tPlayerStatus;
    EditText editName;
    InGameLobby inGameLobby;

    User user;
   Bundle b;
    Bundle connecter;
    Button b1;

    public LobbyFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_lobby, container, false);
        Firebase.setAndroidContext(this.getContext());
        welcome=(TextView)v.findViewById(R.id.welcome);
        editName=(EditText)v.findViewById(R.id.name);
        b1=(Button)v.findViewById(R.id.EnterLobbyButton);
        user = new User("", 100, 100, Status.IDLE, 0,0,0,0);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user.getName().equals("Empty")) {
                    Toast.makeText(getContext(), user.getName(), Toast.LENGTH_LONG).show();
                    user.setName(editName.getText().toString());
                    b = new Bundle();
                    b.putParcelable("user", user);
                    FragmentManager fm = getFragmentManager();
                    inGameLobby = new InGameLobby();
                    inGameLobby.setArguments(b);
                    //c.EnterLobby();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragmentContainer, inGameLobby);
                    //ft.addToBackStack(null);
                    ft.commit();
                } else {
                    Toast.makeText(getContext(), "You need to enter name to play", Toast.LENGTH_LONG).show();
                }


            }
        });

        return v;


    }



}
