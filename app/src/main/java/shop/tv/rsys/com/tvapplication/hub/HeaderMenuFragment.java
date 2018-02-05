package shop.tv.rsys.com.tvapplication.hub;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shop.tv.rsys.com.tvapplication.MainActivity;
import shop.tv.rsys.com.tvapplication.R;

/**
 * Created by akash.sharma on 1/30/2018.
 */

public class HeaderMenuFragment extends Fragment implements View.OnFocusChangeListener{

    @BindView(R.id.home_textview)
    TextView homeTextview;

    @BindView(R.id.mylib_textview)
    TextView myLibtextview;

    @BindView(R.id.shop_textview)
    TextView shopTextView;


    public static int headerFocus = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_header_menu , container , false);
        ButterKnife.bind(this,view);
        //getWindow().getDecorView().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.gradient_main));
        homeTextview.setOnFocusChangeListener(this);
        myLibtextview.setOnFocusChangeListener(this);
        shopTextView.setOnFocusChangeListener(this);

        return view;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {
            case R.id.home_textview:
                headerFocus = 0;
                homeTextview.setTypeface(hasFocus ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                break;
            case R.id.mylib_textview:
                headerFocus = 1;
                myLibtextview.setTypeface(hasFocus ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                break;
            case R.id.shop_textview:
                headerFocus = 2;
                shopTextView.setTypeface(hasFocus ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                break;
            default:
        }
    }

    @OnClick(R.id.shop_textview)
    public void submit(View view) {
        // TODO submit data to server...
        Intent shopIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(shopIntent);
    }
}