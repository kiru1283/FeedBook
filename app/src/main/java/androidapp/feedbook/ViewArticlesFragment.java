package androidapp.feedbook;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewArticlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewArticlesFragment extends Fragment {
    private String category;
    private String url;
    private String inputUser;
    private JSONArray userarrFeed;
    private List<String> allArticles;
    private static final String user = "user";
    private static final String articles = "articles";

    public ViewArticlesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewArticlesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewArticlesFragment newInstance(String username, ArrayList<String> listArt) {
        ViewArticlesFragment fragment = new ViewArticlesFragment();
        Bundle args = new Bundle();
        args.putString(user, username);
        args.putStringArrayList(articles, listArt);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inputUser = getArguments().getString(user);
            allArticles = getArguments().getStringArrayList(articles);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_view_articles, container, false);

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


            String[] array = article.split("\\|", -1);
            String   link = "";
            for(int i=0;i<array.length;i++) {
                String element = array[i].substring(array[i].indexOf("=")+1);
                String type = array[i].substring(0,array[i].indexOf("="));
                if(!element.trim().isEmpty() ){

                    if(type.equals("link")){

                          link = element;
                    }
                    else {
                        // create a new textview
                        TextView rowTextView = new TextView(this.getContext());
                        if (type.trim().equals("title")) {

                            //rowTextView.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
                            rowTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
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
                                    ArticleFragment nextFrag= ArticleFragment.newInstance(v.getTag().toString());
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.content_frame, nextFrag,"findThisFragment")
                                            .addToBackStack(null)
                                            .commit();

                                }
                            });

                        }else
                        {
                        // set some properties of rowTextView or something
                        rowTextView.setText(element);
                        }

                        // add the textview to the linearlayout
                        larticle.addView(rowTextView);
                    }
                }
            }

        }
        //add the scroll, linearlayout and textview to the mainlayout
        content.addView(sv);

        return view;
    }

}
