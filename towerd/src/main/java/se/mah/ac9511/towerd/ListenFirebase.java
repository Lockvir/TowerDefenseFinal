package se.mah.ac9511.towerd;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
/**
 * Created by David on 2015-10-28.
 */
public class ListenFirebase {
    public Firebase getLocation() {
        return location;
    }

    public void setLocation(Firebase location) {
        this.location = location;
    }

    public ValueEventListener getListener() {
            return listener;
    }

    public ChildEventListener getChildListener()
    {
        return childListener;
    }

    public void setListener(ValueEventListener listener) {
        this.listener = listener;
    }

    private Firebase location;

    public ListenFirebase(Firebase location, ValueEventListener listener) {
        this.location = location;
        this.listener = listener;
    }

    public ListenFirebase(Firebase location, ChildEventListener listenerC) {
        this.location = location;
        this.childListener = listenerC;
    }

    private ValueEventListener listener;
    private ChildEventListener childListener;
}


