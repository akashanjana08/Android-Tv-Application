package shop.tv.rsys.com.tvapplication.custom;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.app.RowsFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.PageRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import shop.tv.rsys.com.tvapplication.CardPresenter;
import shop.tv.rsys.com.tvapplication.Movie;
import shop.tv.rsys.com.tvapplication.R;
import shop.tv.rsys.com.tvapplication.model.BTAResponseModel;
import shop.tv.rsys.com.tvapplication.parser.XmlParser;

/**
 * Created by Akash.Sharma on 12/7/2017.
 */

public class CustomFragment extends BrowseFragment {

    private static final long HEADER_ID_1 = 1;
    private static final String HEADER_NAME_1 = "A to Z";
    private static final long HEADER_ID_2 = 2;
    private static final String HEADER_NAME_2 = "Favorite";
    private static final long HEADER_ID_3 = 3;
    private static final String HEADER_NAME_3 = "Settings";
    private BackgroundManager mBackgroundManager;
    private ArrayObjectAdapter mRowsAdapter;
    static List<Movie> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        loadData();
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());

        try{
           InputStream assestFileInputStream  =   getActivity().getAssets().open("bta_details.xml");
           list =  XmlParser.xmlPullParser(assestFileInputStream);
           getMainFragmentRegistry().registerFragment(PageRow.class,
                    new PageRowFragmentFactory(mBackgroundManager));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupUi() {
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        //setBrandColor(getResources().getColor(R.color.fastlane_background));
        setBrandColor(Color.argb(1,1,2,38));
        setTitle("");
        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object o) {

                return new IconHeaderItemPresenter();
            }
        });
        prepareEntranceTransition();
    }

    private  class PageRowFragmentFactory extends BrowseFragment.FragmentFactory {
        private final BackgroundManager mBackgroundManager;
        PageRowFragmentFactory(BackgroundManager backgroundManager) {
            this.mBackgroundManager = backgroundManager;
        }
        @Override
        public Fragment createFragment(Object rowObj) {
            Row row = (Row)rowObj;
            mBackgroundManager.setDrawable(null);

            if (row.getHeaderItem().getId() == HEADER_ID_1) {
                return new SampleFragmentA();
            } else if (row.getHeaderItem().getId() == HEADER_ID_2) {
               return new SampleFragmentA();
            } else if (row.getHeaderItem().getId() == HEADER_ID_3) {
               return new SampleFragmentA();
            }
            throw new IllegalArgumentException(String.format("Invalid row %s", rowObj));
        }
    }

    private void loadData() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(mRowsAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createRows();
                startEntranceTransition();
            }
        }, 2000);
    }

    private void createRows() {
        IconHeaderItem gridItemPresenterHeader1 = new IconHeaderItem(HEADER_ID_1, HEADER_NAME_1);
        PageRow pageRow1 = new PageRow(gridItemPresenterHeader1);
        mRowsAdapter.add(pageRow1);

        IconHeaderItem gridItemPresenterHeader2 = new IconHeaderItem(HEADER_ID_2, HEADER_NAME_2);
        PageRow pageRow2 = new PageRow(gridItemPresenterHeader2);
        mRowsAdapter.add(pageRow2);

        IconHeaderItem gridItemPresenterHeader3 = new IconHeaderItem(HEADER_ID_3, HEADER_NAME_3);
        PageRow pageRow3 = new PageRow(gridItemPresenterHeader3);
        mRowsAdapter.add(pageRow3);
    }


    /**
     * Page fragment embeds a rows fragment.
     */
    public static class SampleFragmentA extends RowsFragment {
        private  ArrayObjectAdapter mRowsAdapter;
        private static final int NUM_COLS = 10;
        public SampleFragmentA() {

            mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

            setAdapter(mRowsAdapter);
            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(
                        Presenter.ViewHolder itemViewHolder,
                        Object item,
                        RowPresenter.ViewHolder rowViewHolder,
                        Row row) {
                    Toast.makeText(getActivity(), "Implement click handler", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            loadRows();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        private void loadRows() {
            mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
            CardPresenter cardPresenter = new CardPresenter();
            int i,elemets=0;
            for (i = 0; i < list.size()/NUM_COLS; i++) {
                 ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                for (int j = 0; j < NUM_COLS; j++) {
                    elemets++;
                    listRowAdapter.add(list.get(elemets));
                }
                mRowsAdapter.add(new ListRow(null, listRowAdapter));
            }
            if((list.size()%10)!=0)
            {
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                try {
                    for (int j = 0; j < ((list.size() % 10)-1); j++) {
                        elemets++;
                        listRowAdapter.add(list.get(elemets));
                    }
                }
                catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                mRowsAdapter.add(new ListRow(null, listRowAdapter));
            }
            setAdapter(mRowsAdapter);
        }
    }


}
