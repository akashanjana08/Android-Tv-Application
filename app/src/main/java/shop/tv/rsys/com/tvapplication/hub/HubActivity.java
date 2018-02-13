package shop.tv.rsys.com.tvapplication.hub;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shop.tv.rsys.com.tvapplication.BrowseErrorActivity;
import shop.tv.rsys.com.tvapplication.DetailsActivity;
import shop.tv.rsys.com.tvapplication.MainActivity;
import shop.tv.rsys.com.tvapplication.Movie;
import shop.tv.rsys.com.tvapplication.R;
import shop.tv.rsys.com.tvapplication.model.BtaResponseModel;
import shop.tv.rsys.com.tvapplication.model.MovieDetailsModel;
import shop.tv.rsys.com.tvapplication.moviesearch.SearchResponsecallback;
import shop.tv.rsys.com.tvapplication.network.ResponseCallback;
import shop.tv.rsys.com.tvapplication.network.RetrofitCallbackResponse;
import shop.tv.rsys.com.tvapplication.network.SearchRetrofitCallbackResponse;
import shop.tv.rsys.com.tvapplication.parser.MovieListParsing;

public class HubActivity extends Activity {


    private Timer mTimer1;
    private TimerTask mTt1;
    private Fragment mGridFragment, mHeaderMenuFragment, mIconMenuFragment;
    private FrameLayout mGridFramelayout, mHeaderMenuFramelayout, mIconMenuFramelayout;
    private ImageView voiceSearchImageView;
    private final static float defaultAlphaValue = 1.0f;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        mHeaderMenuFramelayout = (FrameLayout) findViewById(R.id.header_menu_fragment);
        mGridFramelayout = (FrameLayout) findViewById(R.id.grid_container);
        mIconMenuFramelayout = (FrameLayout) findViewById(R.id.hub_left_menu_icon_fragment);
        voiceSearchImageView = (ImageView) findViewById(R.id.voice_search_imageview);

        setAttachFragments(savedInstanceState);
        setListener();


        System.out.println("Get alpha Value :" + mGridFramelayout.getAlpha());
    }


    private void setListener() {
        voiceSearchImageView.setOnFocusChangeListener(new VoiceSearchFocusListener());
        voiceSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
               // movieDetailsResponse();
            }
        });
    }


    private void setAttachFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mGridFragment = new GridHubFragment();
            FragmentTransaction ft1 = getFragmentManager().beginTransaction();
            ft1.add(R.id.grid_container, mGridFragment, "swim_lane_Fragment").commit();

            mHeaderMenuFragment = new HeaderMenuFragment();
            FragmentTransaction ft2 = getFragmentManager().beginTransaction();
            ft2.add(R.id.header_menu_fragment, mHeaderMenuFragment, "Header_Menu_Fragment").commit();


            mIconMenuFragment = new HubMenuIconFragment();
            FragmentTransaction ft3 = getFragmentManager().beginTransaction();
            ft3.add(R.id.hub_left_menu_icon_fragment, mIconMenuFragment, "icon_Menu_Fragment").commit();
        }
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (getCurrentFocus().getId() == R.id.imagecardview)
            voiceSearchImageView.setFocusable(false);
        else
            voiceSearchImageView.setFocusable(true);


        switch (event.getAction()) {

            case KeyEvent.ACTION_DOWN:
                startTimer(event);
                break;
            case KeyEvent.ACTION_UP:
                stopTimer();
                break;
        }
        return super.dispatchKeyEvent(event);
    }


    private void stopTimer() {
        if (mTimer1 != null) {
            mTimer1.cancel();
            mTimer1.purge();
        }
    }

    private void startTimer(final KeyEvent event) {
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                //TODO
                ((GridHubFragment) mGridFragment).onKeyDownEventKey(event.getKeyCode(), mHeaderMenuFramelayout);
            }
        };
        mTimer1.schedule(mTt1, 10);
    }

    private class VoiceSearchFocusListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                voiceSearchImageView.animate().translationX(-400).withLayer();
                voiceSearchImageView.setImageDrawable(getResources().getDrawable(R.drawable.search_icon));
                mGridFramelayout.setAlpha(0.5f);
                mHeaderMenuFramelayout.setAlpha(0.5f);
                mIconMenuFramelayout.setAlpha(0.5f);
            } else {
                voiceSearchImageView.animate().translationX(-5).withLayer();
                voiceSearchImageView.setImageDrawable(getResources().getDrawable(R.drawable.text_with_voice_search));
                mGridFramelayout.setAlpha(defaultAlphaValue);
                mHeaderMenuFramelayout.setAlpha(defaultAlphaValue);
                mIconMenuFramelayout.setAlpha(defaultAlphaValue);
            }
        }
    }

    private final int REQ_CODE_SPEECH_INPUT = 10000;
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak to search something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
           // startActivity(intent);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Speech not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "Getting Result" , Toast.LENGTH_SHORT).show();
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(this, result.get(0) , Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private static Map<String,String> getQueryMap()
    {
        Map<String,String> queryMap = new HashMap<>();
        queryMap.put("searchScope","title");
        queryMap.put("value","Big");
        queryMap.put("operation","BEGINSWITH");
        queryMap.put("sort","name");
        queryMap.put("subtypesVOD","movie");
        return queryMap;
    }


    ImageCardView getImagecardView(Movie movie)
    {
        ImageCardView imagecardView = new ImageCardView(this);
        imagecardView.setMainImageDimensions(500, 200);
        Glide.with(this)
                .load(movie.getCardImageUrl())
                .centerCrop()
                .dontTransform()
                .into(imagecardView.getMainImageView());
        return imagecardView;
    }


    private void movieDetailsResponse()
    {
        SearchRetrofitCallbackResponse.getNetworkResponse(new SearchResponsecallback() {
            @Override
            public void getResponse(List<MovieDetailsModel.Movie> list) {
                try {

                    Movie movie = MovieListParsing.getMovieSearchDetails(list);
                    Intent intent = new Intent(HubActivity.this, DetailsActivity.class);
                    intent.putExtra(DetailsActivity.MOVIE, movie);
                    //Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(HubActivity.this, getImagecardView(movie), DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, getQueryMap());
    }


}

