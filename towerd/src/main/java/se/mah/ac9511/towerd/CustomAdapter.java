package se.mah.ac9511.towerd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by David on 2015-11-03.
 */
public class CustomAdapter extends BaseAdapter {
    ArrayList<Players> players=new ArrayList<Players>();
    LayoutInflater layoutInflater;
    Context context;

    public CustomAdapter(Context context, ArrayList<Players> players) {
        this.players = players;
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Players getItem(int position) {
        return players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        playersHolder pHolder;
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.row,null);
            pHolder= new playersHolder(convertView);
            convertView.setTag(pHolder);
        }
        else
        {
            pHolder=(playersHolder)convertView.getTag();
        }
        pHolder.players1=textViewInfo(convertView, R.id.textView7, players.get(position).getName());
        pHolder.c=checkBox(convertView,R.id.Ready);
        pHolder.c.setChecked(players.get(position).isReady());
        return  convertView;
    }
    private TextView textViewInfo (View v, int id, String Text)
    {
        TextView textView=(TextView)v.findViewById(id);
        textView.setText(Text);
        return textView;
    }
    private CheckBox checkBox(View v, int id)
    {
        CheckBox c=(CheckBox)v.findViewById(id);
        return c;
    }


    private static class playersHolder {
        TextView players1,players2,players3,players4;
        TextView textView;
        CheckBox c;


        public playersHolder (View v)
        {
            players1=(TextView)v.findViewById(R.id.textView7);
            c=(CheckBox)v.findViewById(R.id.Ready);


        }

    }
}

