package shop.tv.rsys.com.tvapplication.hub;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shop.tv.rsys.com.tvapplication.MainActivity;
import shop.tv.rsys.com.tvapplication.R;

public class HubActivity extends Activity  {
    /**
     * Called when the activity is first created.
     */



    Fragment mGridFragment , mHeaderMenuFragment , mIconMenuFragment;
    FrameLayout mGridFramelayout , mHeaderMenuFramelayout , mIconMenuFramelayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        mHeaderMenuFramelayout = (FrameLayout)findViewById(R.id.header_menu_fragment);
        mGridFramelayout       = (FrameLayout)findViewById(R.id.grid_container);
        mIconMenuFramelayout   = (FrameLayout)findViewById(R.id.hub_left_menu_icon_fragment);

        if (savedInstanceState == null) {
            mGridFragment = new GridHubFragment();
            FragmentTransaction ft1 = getFragmentManager().beginTransaction();
            ft1.add(R.id.grid_container, mGridFragment).commit();

            mHeaderMenuFragment = new HeaderMenuFragment();
            FragmentTransaction ft2 = getFragmentManager().beginTransaction();
            ft2.add(R.id.header_menu_fragment, mHeaderMenuFragment , "Header_Menu_Fragment").commit();

            mIconMenuFragment = new HubMenuIconFragment();
            FragmentTransaction ft3 = getFragmentManager().beginTransaction();
            ft3.add(R.id.hub_left_menu_icon_fragment, mIconMenuFragment).commit();
        }
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            try {
                    startTimer(event);
                } catch (Exception e) {
                }

        }
        if (event.getAction() == KeyEvent.ACTION_UP) {

            try {
                stopTimer();
            } catch (Exception e) {
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private Timer mTimer1;
    private TimerTask mTt1;

    private void stopTimer() {
        if (mTimer1 != null) {
            mTimer1.cancel();
            mTimer1.purge();
            // ((GridHubFragment) mFragment).stopScrolling();
        }
    }

    private void startTimer(final KeyEvent event) {
        mTimer1 = new Timer();

        mTt1 = new TimerTask() {
            public void run() {
                //TODO
                ((GridHubFragment) mGridFragment).onKeyDownEventKey(event.getKeyCode() , mHeaderMenuFramelayout);
            }
        };
        mTimer1.schedule(mTt1, 10);
    }
}

