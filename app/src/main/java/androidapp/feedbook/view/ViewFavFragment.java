package androidapp.feedbook.view;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Vector;

import androidapp.feedbook.R;
import androidapp.feedbook.controller.MarkArticle;
import androidapp.feedbook.exceptions.JSONFileException;


/** Class to display the list of articles marked as favourite
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewFavFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewFavFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private String inputUser;
    private static final String user = "user";
    private Vector choices ;
    private String category;
    private MarkArticle articleObj;
    private JSONArray userArticle = null;

    public ViewFavFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *@param username - username of the logged in user
     * @return A new instance of fragment ViewFavFragment.
     */

    public static ViewFavFragment newInstance(String username) {
        ViewFavFragment fragment = new ViewFavFragment();
        Bundle args = new Bundle();
        args.putString(user, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inputUser = getArguments().getString(user);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_fav, container, false);

        //get list of favourite articles
        articleObj = new MarkArticle(this.getContext());

        try {

            userArticle = articleObj.viewFavourites(inputUser);

        } catch (JSONFileException e) {

            Toast.makeText(this.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        // Inflate the layout for this fragment
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_fav);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        choices = new Vector<CharSequence>();
        choices.add("News");
        choices.add("Sports");
        choices.add("Food");
        choices.add("Science");
        choices.add("Travel");

        // Create an ArrayAdapter using the string list and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, this.choices);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = adapterView.getItemAtPosition(i).toString();

        getStarred(category);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * Method to dispaly the list of articles marked as starred from the favourites.json
     * @param category - the category of the feed in which the article was viewed
     */
    private void getStarred(String category){

        LinearLayout content = (LinearLayout) getView().findViewById(R.id.linear_fav);
        content.removeAllViewsInLayout();

        ScrollView sv = new ScrollView(getActivity());
        sv .setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        LinearLayout ll = new LinearLayout(getActivity());
        ll.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        if (userArticle != null) {

            if (userArticle.size() > 0) {
                int i = 1;
                for (Object Obj : userArticle) {
                    JSONObject listObj = (JSONObject) Obj;
                     if(listObj.get("category").toString().equals(category)){

                        LinearLayout larticle = new LinearLayout(getActivity());
                        larticle.setLayoutParams(new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT));
                        larticle.setOrientation(LinearLayout.VERTICAL);

                        GradientDrawable drawable = new GradientDrawable();
                        drawable.setShape(GradientDrawable.RECTANGLE);
                        drawable.setStroke(3, Color.GRAY);
                        drawable.setCornerRadius(8);
                        //drawable.setColor(Color.BLUE);
                        larticle.setBackgroundDrawable(drawable);

                        ll.addView(larticle);

                     //layout to include fav and share button
                     LinearLayout rtext = new LinearLayout(getActivity());
                     rtext.setLayoutParams(new FrameLayout.LayoutParams(
                             FrameLayout.LayoutParams.MATCH_PARENT,
                             FrameLayout.LayoutParams.WRAP_CONTENT));


                         TextView rowTextView = new TextView(this.getContext());
                        rowTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        rowTextView.setId(i + 800);
                        rowTextView.setText(listObj.get("url").toString());
                        rowTextView.setTextSize(18);
                        rowTextView.setTextColor(Color.BLUE);
                        rowTextView.setClickable(true);
                        rowTextView.setPadding(0, 10, 0, 5);
                        //tv.setGravity(Gravity.CENTER);
                        rowTextView.setTag(listObj.get("url").toString());
                        rowTextView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ArticleFragment nextFrag = ArticleFragment.newInstance(v.getTag().toString());
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_frame, nextFrag, "findThisFragment")
                                        .addToBackStack(null)
                                        .commit();

                            }
                        });

                        rowTextView
                                .setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,0.9f));

                         // add the textview to the linearlayout
                         rtext.addView(rowTextView);


                         //button for Star
                        ToggleButton tglPreference = new ToggleButton(this.getContext());
                        tglPreference.setId(i);
                        tglPreference
                                .setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,0.1f));
                        tglPreference.setTextOn("");
                        tglPreference.setTextOff("");
                        tglPreference.setChecked(true);
                        tglPreference.setTag(listObj.get("url").toString());
                        tglPreference.setBackgroundDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_img_star_yellow));
                        tglPreference.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    buttonView.setBackgroundDrawable(ContextCompat.getDrawable(buttonView.getContext(), R.drawable.ic_img_star_yellow));
                                    Toast.makeText(buttonView.getContext(),"Marked As Starred Item",Toast.LENGTH_LONG).show();
                                    markStarred(buttonView.getTag().toString(),false);
                                }
                                else {
                                    buttonView.setBackgroundDrawable(ContextCompat.getDrawable(buttonView.getContext(), R.drawable.ic_img_star_grey));
                                    Toast.makeText(buttonView.getContext(),"Removed From Starred Items",Toast.LENGTH_LONG).show();
                                    markStarred(buttonView.getTag().toString(),true);
                                }
                            }
                        });

                        //add button to layout
                        rtext.addView(tglPreference);

                        //add innner layout to outter layout
                        larticle.addView(rtext);
                    }
                    //articles.add(urltext);
                    i++;
                }

                content.addView(sv);


            } else {

                Toast.makeText(this.getContext(),"No Articles Starred In this Category.",Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(this.getContext(),"No Articles Marked as Starred.",Toast.LENGTH_LONG).show();

        }


    }

    //Method to save or remove the article url from the favourites.json
    private void markStarred(String url,boolean remove){

        try {
            articleObj.saveArticle(category, url, inputUser, remove);
        } catch (JSONFileException e) {
            Toast.makeText(this.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
}
