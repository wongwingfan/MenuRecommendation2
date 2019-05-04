package com.example.menurecommendation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.example.menurecommendation.FirstLaunchActivity.sharedPrefFile;

public class MenuGenerationFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyDBHandler db;
    private SharedPreferences mPreferences;
    private Random rand = new Random();

    public static final String MEAT_COUNTER = "MeatCounter";
    public static final String VEGGIE_COUNTER = "VeggieCounter";
    public static final String SOUP_COUNTER = "SoupCounter";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.appTitle.setText(R.string.today_menu);
        MainActivity.appBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        MainActivity.appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.appBar.setNavigationIcon(null);
                getActivity().onBackPressed();
            }
        });
        View rootView = inflater.inflate(R.layout.fragment_menu_generation, container, false);
        db = MainActivity.dbHandler;
        ArrayList<RecipeDetailsData> data = new ArrayList<>();
        data.addAll(MeatGeneration());
        data.addAll(VeggieGeneration());
        data.addAll(SoupGeneration());
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mAdapter = new CardViewAdapter(data);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private ArrayList<RecipeDetailsData> MeatGeneration(){
        mPreferences = getContext().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        int userPrefNumber = mPreferences.getInt(FirstLaunchActivity.NOM_KEY, 0);
        boolean isSelectBeef = mPreferences.getBoolean(FirstLaunchActivity.BEEF_KEY,false);

        boolean isSelectPork = mPreferences.getBoolean(FirstLaunchActivity.PORK_KEY,false);
        boolean isSelectChicken = mPreferences.getBoolean(FirstLaunchActivity.CHICKEN_KEY,false);

        List<String> foodType = new ArrayList<>();
        if (!isSelectBeef)
            foodType.add("Beef");
        if (!isSelectPork)
            foodType.add("Pork");
        if (!isSelectChicken)
            foodType.add("Chicken");

        return GenerationMechanism(userPrefNumber, MEAT_COUNTER, foodType);
    }

    private ArrayList<RecipeDetailsData> VeggieGeneration(){
        mPreferences = getContext().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        int userPrefNumber = mPreferences.getInt(FirstLaunchActivity.NOV_KEY, 0);
        boolean isSelectBroccoli = mPreferences.getBoolean(FirstLaunchActivity.BROCCOLI_KEY,false);
        boolean isSelectLettuce = mPreferences.getBoolean(FirstLaunchActivity.LETTUCE_KEY,false);
        boolean isSelectSpinach = mPreferences.getBoolean(FirstLaunchActivity.SPINACH_KEY,false);

        List<String> foodType = new ArrayList<>();
        if (!isSelectBroccoli)
            foodType.add("Broccoli");
        if (!isSelectLettuce)
            foodType.add("Lettuce");
        if (!isSelectSpinach)
            foodType.add("Spinach");

        Log.d("SIZE",""+foodType.size());

        return GenerationMechanism(userPrefNumber, VEGGIE_COUNTER, foodType);
    }

    private ArrayList<RecipeDetailsData> SoupGeneration(){
        mPreferences = getContext().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        int userPrefNumber = mPreferences.getInt(FirstLaunchActivity.NOS_KEY, 0);
        boolean isSelectChineseSoup = mPreferences.getBoolean(FirstLaunchActivity.CS_KEY,false);
        boolean isSelectWesternSoup = mPreferences.getBoolean(FirstLaunchActivity.WS_KEY,false);

        List<String> foodType = new ArrayList<>();
        if (!isSelectChineseSoup)
            foodType.add("Chinese soup");
        if (!isSelectWesternSoup)
            foodType.add("Western soup");

        return GenerationMechanism(userPrefNumber, SOUP_COUNTER, foodType);
    }

    private ArrayList<RecipeDetailsData> GenerationMechanism(int userPrefNumber, String counterType, List<String> foodType){
        ArrayList<RecipeDetailsData> result = new ArrayList<>();
        List<Integer> chosenNumber = new ArrayList<>();
        int genNumber;

        Log.d("ALL DATA", ""+userPrefNumber);
        for (String x: foodType)
            Log.d("ALL DATA", x);

        int counter = mPreferences.getInt(counterType,0);

        for (int i = 0; i < userPrefNumber; i++){
            String tag = foodType.get(counter++ % foodType.size());
            List<Integer> IDs = db.findIDsByTag(tag);
            for (int x: IDs)
                Log.d("ALL id", ""+x);
            do {
                genNumber = IDs.get(rand.nextInt(IDs.size()));
            } while (chosenNumber.contains(genNumber));
            chosenNumber.add(genNumber);
            result.add(db.findSingleRecipeByID(genNumber));

        }

        SharedPreferences.Editor prefEditor = mPreferences.edit();
        prefEditor.putInt(counterType, counter);
        prefEditor.apply();
        prefEditor.commit();
        return result;
    }
}