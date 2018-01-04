package shop.tv.rsys.com.tvapplication.hub;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shop.tv.rsys.com.tvapplication.MainActivity;
import shop.tv.rsys.com.tvapplication.R;

public class HubActivity extends Activity implements View.OnFocusChangeListener{
    /**
     * Called when the activity is first created.
     */
    @BindView(R.id.home_textview)
    TextView homeTextview;

    @BindView(R.id.mylib_textview)
    TextView myLibtextview;

    @BindView(R.id.shop_textview)
    TextView shopTextView;

    public static int headerFocus=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        ButterKnife.bind(this);
        //getWindow().getDecorView().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.gradient_main));
        homeTextview.setOnFocusChangeListener(this);
        myLibtextview.setOnFocusChangeListener(this);
        shopTextView.setOnFocusChangeListener(this);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId())
        {
            case R.id.home_textview:
                headerFocus =0;
                homeTextview.setTypeface(hasFocus ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                break;
            case R.id.mylib_textview:
                headerFocus =1;
                myLibtextview.setTypeface(hasFocus ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                break;
            case R.id.shop_textview:
                headerFocus =2;
                shopTextView.setTypeface(hasFocus ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                break;
            default:
        }
    }

    @OnClick(R.id.shop_textview)
    public void submit(View view) {
        // TODO submit data to server...
        Intent shopIntent = new Intent(this , MainActivity.class);
        startActivity(shopIntent);
    }
}

