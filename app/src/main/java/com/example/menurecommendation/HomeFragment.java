package com.example.menurecommendation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    public HomeFragment(){}

    generateRecipePageListener mCallback;

    public interface generateRecipePageListener{
        void generateRecipePage();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getActivity() is fully created in onActivityCreated and instanceOf differentiate it between different Activities
        if (getActivity() instanceof generateRecipePageListener)
            mCallback = (generateRecipePageListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.appTitle.setText(R.string.home);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Button genButton = rootView.findViewById(R.id.generate_button);
        Button hisButton = rootView.findViewById(R.id.history_button);

        genButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.generateRecipePage();
            }
        });

        hisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.generateRecipePage();
            }
        });
        return rootView;
    }
}

