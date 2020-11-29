package com.news.goodlife.Fragments.SlideInFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

public class BudgetManagementFragment extends Fragment {

    View root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.slidein_budget_manage_layout, container, false);

        SingletonClass test = SingletonClass.getInstance();

        listeners();
        return root;
    }

    boolean clicked = false;
    boolean directionset = false;
    boolean directionHorizontal = false;
    float TDX,TDY, movedX, movedY;
    float barrier = 10;
    private void listeners() {
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //some code....
                        TDX = event.getX();
                        TDY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        directionset = false;
                        directionHorizontal = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(!directionset){
                            movedX = Math.abs(TDX - event.getX());
                            movedY = Math.abs(TDY - event.getY());
                            if(movedX > barrier){
                                directionHorizontal = true;
                                directionset = true;
                                TDX = event.getX();
                            }
                            if(movedY > barrier){
                                directionHorizontal = false;
                                directionset = true;
                                TDY = event.getY();
                            }
                        }
                        else{
                            if(directionHorizontal){
                                movedX = TDX - event.getRawX();
                                //moving Horizontal
                                Log.i("Horizontal", ""+movedX);
                                if(movedX < 0){

                                }
                            }
                            else{
                                movedY = TDY - event.getRawY();
                                //Move Vertical
                                Log.i("Vertical", ""+movedY);
                            }
                        }

                        break;
                    default:
                        break;
                }
                return true;

            }
        });
    }
}
