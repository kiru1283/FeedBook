package androidapp.feedbook.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidapp.feedbook.R;


/** This class is to display the chosen article in a webview control
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleFragment extends Fragment {
    private static final String url = "url";
    private String link;
    private WebView mWebView;

    public ArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *@param link - the link of the atricle to be displayed
     * @return A new instance of fragment ArticleFragment.
     */
     public static ArticleFragment newInstance(String link) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString(url, link);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            link = getArguments().getString(url);

        }
    }

    /**
     * Method to create the view which displays the webview control
     * @param inflater - the layout inflater to create the layouts
     * @param container - the viewgroup container
     * @param savedInstanceState  - instance data
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        mWebView = (WebView) view.findViewById(R.id.art_view);
        mWebView.loadUrl(link);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        return view;
    }

}
