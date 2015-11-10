package se.mah.ac9511.towerd;


import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameScreenFragment extends Fragment implements ConnecterEventListener{
    TextView tUser,tMoney,tState;
   int Gold;
    ImageView image1,image2,image3;
    int x,y;
    Connecter c;
    SurfaceView surfaceView;
    private SurfaceHolder holder;
    private Firebase myFirebaseRef;
    Paint paint;
    Canvas canvas;
    User user;
    View v;
    ViewGroup viewGroup;
    private int mTower1Cost = 100;




    public GameScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v=inflater.inflate(R.layout.fragment_game_screen, container, false);
        user=getArguments().getParcelable("user");
        Firebase.setAndroidContext(this.getContext());
        myFirebaseRef = new Firebase("https://vivid-heat-894.firebaseio.com/");
        c = new Connecter(myFirebaseRef,user,user.getName());
        c.setmLobbyChangedEventListener(this);
        c.EnterGame(user.getPlayerId());
        c.setStatusEventListener(this);
        Log.v("LobbyFragment", "ID: " + user.getPlayerId());
        surfarceH(v);
        TextViewInfo(v);

       return v;

    }
    private void surfarceH(View v) {
        surfaceView = (SurfaceView) v.findViewById(R.id.surfaceView);
        surfaceView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                DragAndDrop(event);
                return true;
            }
        });
//        surfaceView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                //HandleTouch(event);
//                return true;
//            }
//        });

    }
    private void TextViewInfo(View v)
    {
        image1=(ImageView)v. findViewById(R.id.imageView3);
        //image2=(ImageView)v. findViewById(R.id.imageView2);
       // image3=(ImageView)v. findViewById(R.id.imageView3);
        tUser = (TextView) v.findViewById(R.id.textView);
        tMoney = (TextView) v.findViewById(R.id.textView3);
        tState = (TextView) v.findViewById(R.id.textView2);
        tUser.setText(user.getName());
       image1.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               ClipData clipData = ClipData.newPlainText("", "");
               View.DragShadowBuilder shadow = new View.DragShadowBuilder(image1);
               image1.startDrag(clipData, shadow, image1, 0);
               image1.setVisibility(View.VISIBLE);
               image1.setOnDragListener(new View.OnDragListener() {
                   @Override
                   public boolean onDrag(View v, DragEvent event) {
                       DragAndDrop(event);
                       return true;
                   }
               });
               return true;
           }
       });

    }
    private void UserPos(MotionEvent event)
    {
        x = (int) event.getX();
        y = (int) event.getY();
        user.setxPos(x);
        user.setyPos(y);
        c.UpdatePosition(user.getxPos(), user.getyPos());
    }
// private void HandleTouch (MotionEvent event) {
//
//     final int actionPeformed = event.getAction();
//     switch (actionPeformed) {
//         case MotionEvent.ACTION_DOWN: {
//
//
////             viewGroup = (ViewGroup) image1.getParent();
////             viewGroup.removeView(image1);
////             viewGroup.addView(image1);
////             image1.setVisibility(View.VISIBLE);
////             user.setxPos(x);
////             user.setyPos(y);
////             c.UpdatePosition(user.getxPos(), user.getyPos());
////             PlaceTower();
////             v.setVisibility(View.VISIBLE);
////             Log.i("ACTION_DOWN", "Resultat: " + x + "," + y);
//             UserPos(event);
//             break;
//         }
//         case MotionEvent.ACTION_MOVE: {
////             viewGroup=(ViewGroup)image1.getParent();
////             viewGroup.removeView(image1);
////             viewGroup.addView(image1);
//             //image1.setVisibility(View.VISIBLE);
//             //user.setxPos(x);
//            // user.setyPos(y);
//            // c.UpdatePosition(user.getxPos(), user.getyPos());
//             UserPos(event);
//            // v.setVisibility(View.VISIBLE);
//             Log.i("ACTION_DOWN", "Resultat: " + x + "," + y);
//             break;
//         }
//         case MotionEvent.ACTION_UP:
//             viewGroup=(ViewGroup)image1.getParent();
//             viewGroup.removeView(image1);
//             viewGroup.addView(image1);
//             image1.setVisibility(View.VISIBLE);
//             user.setxPos(x);
//             user.setyPos(y);
//             c.UpdatePosition(user.getxPos(), user.getyPos());
//             UserPos(event);
//             PlaceTower();
//             v.setVisibility(View.VISIBLE);
//             Log.i("ACTION_DOWN", "Resultat: " + x + "," + y);
//             break;
//
//
//     }
// }
    private void DragAndDrop(DragEvent event)
    {
        x = (int) event.getX();
        y = (int) event.getY();
        user.setxPos(x);
        user.setyPos(y);
      c.UpdatePosition(user.getxPos(), user.getyPos());
        final int actionPeformed = event.getAction();
        switch (actionPeformed) {
            case DragEvent.ACTION_DRAG_STARTED: {
                //UserPos(event);
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                    Log.v("GameScreenFragment", "Can accept this data");
                    v.setVisibility(View.INVISIBLE);

                    user.setxPos(x);
                    user.setyPos(y);
                    c.UpdatePosition(user.getxPos(), user.getyPos());
                    //Toast.makeText(getContext(), "DragEvent.ACTION_DRAG_STARTED: " + x + "," + y,Toast.LENGTH_LONG).show();
                    Log.i("ACTION_DOWN", "Resultat: " + x + "," + y);
                }


                break;
            }

            case DragEvent.ACTION_DROP: {
               //UserPos(event);
                switch (image1.getId()){
                    case R.id.imageView3:
                        viewGroup=(ViewGroup)image1.getParent();
                        viewGroup.removeView(image1);
                        viewGroup.addView(image1);
                        image1.setVisibility(View.VISIBLE);
                        user.setxPos(x);
                        user.setyPos(y);
                        c.UpdatePosition(user.getxPos(), user.getyPos());
                        PlaceTower();
                        v.setVisibility(View.VISIBLE);
                        Log.v("GameScreenFragment", "Test: " + event.getResult());
                        Log.i("ACTION_DOWN", "Resultat: " + x + "," + y);
                        //Toast.makeText(getContext(), "ragEvent.ACTION_DROP: " + x + "," + y,Toast.LENGTH_LONG).show();


                }
                break;
            }

            case DragEvent.ACTION_DRAG_ENDED: {
                viewGroup = (ViewGroup) image1.getParent();
                viewGroup.removeView(image1);
                viewGroup.addView(image1);
                image1.setVisibility(View.VISIBLE);
                v.setVisibility(View.VISIBLE);
                //user.setxPos(x);
                //user.setyPos(y);
                //c.UpdatePosition(user.getxPos(), user.getyPos());

                //Toast.makeText(getContext(), "DragEvent.ACTION_DRAG_ENDED",Toast.LENGTH_LONG).show();
                break;}
                case DragEvent.ACTION_DRAG_EXITED: {
                    viewGroup = (ViewGroup) image1.getParent();
                    viewGroup.removeView(image1);
                    viewGroup.addView(image1);
                    image1.setVisibility(View.VISIBLE);
                    v.setVisibility(View.VISIBLE);
                    break;
                }
//            case DragEvent.ACTION_DRAG_ENDED:
//                switch (image1.getId()) {
//                    case R.id.imageView3:
//                        viewGroup = (ViewGroup) image1.getParent();
//                        viewGroup.removeView(image1);
//                        viewGroup.addView(image1);
//                        image1.setVisibility(View.VISIBLE);
//                        v.setVisibility(View.VISIBLE);
//                        Log.v("GameScreenFragment", "Test: " + event.getResult());
//                        Log.i("ACTION_DOWN", "Resultat: " + x + "," + y);
//                        break;
//                }
        }

    }
    private void PlaceTower()
    {
        //tMoney.setText(user.getMoney());
        Toast.makeText(getContext(), "Resultat: " + x + "," + y,Toast.LENGTH_LONG).show();
        if(user.getMoney()>= mTower1Cost) {
            c.UpdateStatus(Status.BUILD_TOWER_1);
        }
        else
        {
            Toast.makeText(getContext(),"Not enough money!!",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onEvent() {

    }

    @Override
    public void onStatusUpdate() {
        Status s = user.getPriority();
        switch (s)
            {
                case OK:
                    user.setMoney(user.getMoney() - mTower1Cost);
                    c.UpdateIntValue(ToUpdate.GOLD, user.getMoney());
                    user.setPriority(Status.IDLE);
                    tMoney.setText(Integer.toString(user.getMoney()));
                    Log.v("GameScreen","Money: "+user.getMoney());
                    c.UpdateStatus(Status.IDLE);
                    tState.setText(user.getPriority().toString());
                    break;
                case FAILED:
                    c.UpdateStatus(Status.IDLE);
                    Toast.makeText(getContext(),"Cant place tower here!! :(", Toast.LENGTH_LONG).show();
                    break;
                case IDLE:

                    break;
                case BUILD_TOWER_1:
                    break;
                case BUILD_TOWER_2:
                    break;
                case BUILD_TOWER_3:
                    break;
                case BUILD_TOWER_4:
                    break;
                default:
                    break;
            }
    }

    @Override
    public void onLobbyChangedUpdate() {

    }
}




