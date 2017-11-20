package androidapp.feedbook.view;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidapp.feedbook.R;
import androidapp.feedbook.controller.MarkArticle;
import androidapp.feedbook.exceptions.JSONFileException;


/**Class to display the articles in a feed
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewArticlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewArticlesFragment extends Fragment {
    private String category;
   // private String url;
    private String inputUser;
   // private JSONArray userarrFeed;
    private List<String> allArticles;
    private static final String user = "user";
    private static final String articles = "articles";
    private static final String categ = "category";
    private MarkArticle articleObj;
    private JSONArray userArticle = null;

   public ViewArticlesFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *@param category -the category of the feed of which the articles are displayed
     * @param username - the username of the logged in user
     * @param listArt - the list of articles to be displayed
     * @return A new instance of fragment ViewArticlesFragment.
     */

    public static ViewArticlesFragment newInstance(String username, ArrayList<String> listArt, String category) {
        ViewArticlesFragment fragment = new ViewArticlesFragment();
        Bundle args = new Bundle();
        args.putString(user, username);
        args.putStringArrayList(articles, listArt);
        args.putString(categ,category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inputUser = getArguments().getString(user);
            allArticles = getArguments().getStringArrayList(articles);
            category = getArguments().getString(categ);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_view_articles, container, false);

        articleObj = new MarkArticle(this.getContext());

        //check teh articles marked as favourite
        try {
            userArticle = articleObj.viewFavourites(inputUser);
        } catch (JSONFileException e) {
            Toast.makeText(this.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

        LinearLayout content = (LinearLayout) view.findViewById(R.id.linear_top);
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

        for(String article : allArticles){

           createArticle(article,ll);

        }
        //add the scroll, linearlayout and textview to the mainlayout
        content.addView(sv);

        return view;
    }

    /**
     * Method to create the layout to display the article details
     * @param article - the string with all details of a single article in the feeed
     * @param ll - Linear Layout in which the article will be displayed
     */
    private void createArticle(String article,LinearLayout ll ){

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

        boolean fav = false;
        String[] array = article.split("\\|", -1);
        String   link = "";

        for(int i=0;i<array.length;i++) {
            String element = array[i].substring(array[i].indexOf("=")+1);
            String type = array[i].substring(0,array[i].indexOf("="));
            if(!element.trim().isEmpty() ){

                if(type.equals("link")){

                    link = element.trim();

                    //check if its a favourite article
                    for (Object Obj : userArticle) {
                        JSONObject listObj = (JSONObject) Obj;
                        if(listObj.get("category").toString().equals(category)&&listObj.get("url").toString().equals(link)) {
                            fav = true;
                        }
                    }
                }
                else {
                    // create a new textview
                    TextView rowTextView = new TextView(this.getContext());
                    if (type.trim().equals("title")) {

                        //rowTextView.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);

                        rowTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        rowTextView.setId(i + 800);
                        rowTextView.setText(element);
                        rowTextView.setTextSize(18);
                        rowTextView.setTextColor(Color.BLUE);
                        rowTextView.setClickable(true);
                        rowTextView.setPadding(0, 10, 0, 5);
                        //tv.setGravity(Gravity.CENTER);
                        rowTextView.setTag(link);
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

                        larticle.addView(rowTextView);

                    }
                    else if(type.trim().equals("pubdate")) {

                        rowTextView.setText(element);

                        rowTextView
                                .setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,0.8f));

                        //layout to include fav and share button
                        LinearLayout rtext = new LinearLayout(getActivity());
                        rtext.setLayoutParams(new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT));

                        //button for Star
                        ToggleButton tglPreference = new ToggleButton(this.getContext());
                        tglPreference.setId(i);
                        tglPreference
                                .setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,0.1f));
                        tglPreference.setTextOn("");
                        tglPreference.setTextOff("");
                        tglPreference.setChecked(fav);
                        tglPreference.setTag(link);
                        if(fav){
                            tglPreference.setBackgroundDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_img_star_yellow));
                        }else{
                            tglPreference.setBackgroundDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_img_star_grey));
                        }

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

                        //Imagebutton for sharing
                        ImageButton imgShare = new ImageButton (this.getContext());
                        imgShare.setId(i);
                        imgShare.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,0.1f));
                        imgShare.setTag(link);
                        imgShare.setBackgroundDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_share));
                        imgShare.setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View view) {

                                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    String shareBody = "FeedBook User "+inputUser+" would like to share an interesting article "+view.getTag().toString();
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Interesting Article");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                                }
                            }
                        );


                        // add the textview to the linearlayout
                        rtext.addView(rowTextView);

                        rtext.addView(tglPreference);

                        rtext.addView(imgShare);

                        larticle.addView(rtext);


                    }else
                    {
                        // set some properties of rowTextView
                        rowTextView.setText(element);
                        // add the textview to the linearlayout
                        larticle.addView(rowTextView);
                    }

                }
            }
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
