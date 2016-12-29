package com.andro.naveen.famousapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    ViewFlipper viewFlipper;
    Animation Fade_in, Fade_out;
    TextView select;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        select = (TextView) view.findViewById(R.id.select_items);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.ViewFlipper1);
        Fade_in = AnimationUtils.makeInAnimation(getContext(),false);
        Fade_out = AnimationUtils.makeOutAnimation(getContext(), false);
        viewFlipper.setInAnimation(Fade_in);
        viewFlipper.setOutAnimation(Fade_out);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping();
        select.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectItem.class);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}
