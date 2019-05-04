package com.example.menurecommendation;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



public class RecipeDetailsFragment extends Fragment {
    private TextView NameTextView ;
    private TextView DescriptionTextView ;
    private TextView StepTextView ;
    final static String DATA_RECEIVE = "data_receive";
    private int searchID;
    private RecipeDetailsData RecipeDetailsData;
    private ImageView image;
    private MyDBHandler db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        Bundle args = getArguments();
        NameTextView = rootView.findViewById(R.id.recipename);
        DescriptionTextView = rootView.findViewById(R.id.description);
        StepTextView = rootView.findViewById(R.id.step);
        image = rootView.findViewById(R.id.imageView1);
        MainActivity.appBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        MainActivity.appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.appBar.setNavigationIcon(null);
                getActivity().onBackPressed();
            }
        });


        if (args != null) {
         searchID = args.getInt(DATA_RECEIVE);

        db = MainActivity.dbHandler;
        RecipeDetailsData = db.findHandlerByID(searchID);
        NameTextView.setText(RecipeDetailsData.getRecipeName());
        DescriptionTextView.setText("Cooking Time: "+RecipeDetailsData.getCookingTime()+" Difficulty: "+RecipeDetailsData.getDifficulty());
        StepTextView.setText(RecipeDetailsData.getSteps());
        Resources resource = getContext().getResources();
        Log.d("Photo", "recipe_"+RecipeDetailsData.getID()+".jpg");

        int resourceId = resource.getIdentifier("recipe_"+RecipeDetailsData.getID(), "drawable",
                getContext().getPackageName());
        image.setImageResource(resourceId);
        Log.d("recipe name", ""+RecipeDetailsData.getRecipeName());
         }
        return rootView;
    }



}
