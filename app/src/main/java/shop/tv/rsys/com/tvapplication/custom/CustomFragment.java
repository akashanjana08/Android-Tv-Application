package shop.tv.rsys.com.tvapplication.custom;

import android.app.Activity;
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
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Collections;
import java.util.List;
import shop.tv.rsys.com.tvapplication.CardPresenter;
import shop.tv.rsys.com.tvapplication.Movie;
import shop.tv.rsys.com.tvapplication.MovieList;
import shop.tv.rsys.com.tvapplication.R;

/**
 * Created by Akash.Sharma on 12/7/2017.
 */

public class CustomFragment extends BrowseFragment {

    private static final long HEADER_ID_1 = 1;
    private static final String HEADER_NAME_1 = "Category One";
    private static final long HEADER_ID_2 = 2;
    private static final String HEADER_NAME_2 = "Category Two";
    private static final long HEADER_ID_3 = 3;
    private static final String HEADER_NAME_3 = "Settings";
    private static final long HEADER_ID_4 = 4;
    private static final String HEADER_NAME_4 = "User agreement Fragment";
    private BackgroundManager mBackgroundManager;
    private ArrayObjectAdapter mRowsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        loadData();
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        getMainFragmentRegistry().registerFragment(PageRow.class,
                new PageRowFragmentFactory(mBackgroundManager));
    }

    private void setupUi() {
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        //setBrandColor(getResources().getColor(R.color.fastlane_background));
        setBrandColor(Color.argb(1,1,2,38));
        setTitle(getString(R.string.browse_title));
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
               return new SampleFragmentB();
            } else if (row.getHeaderItem().getId() == HEADER_ID_3) {
               return new SettingsFragment();
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
        //HeaderItem headerItem1 = new HeaderItem(HEADER_ID_1, HEADER_NAME_1);
        IconHeaderItem gridItemPresenterHeader1 = new IconHeaderItem(HEADER_ID_1, HEADER_NAME_1, R.drawable.ic_assistant_black_24dp);
        PageRow pageRow1 = new PageRow(gridItemPresenterHeader1);
        mRowsAdapter.add(pageRow1);

        //HeaderItem headerItem2 = new HeaderItem(HEADER_ID_2, HEADER_NAME_2);
        IconHeaderItem gridItemPresenterHeader2 = new IconHeaderItem(HEADER_ID_2, HEADER_NAME_2, R.drawable.ic_assistant_black_24dp);
        PageRow pageRow2 = new PageRow(gridItemPresenterHeader2);
        mRowsAdapter.add(pageRow2);

        //HeaderItem headerItem2 = new HeaderItem(HEADER_ID_2, HEADER_NAME_2);
        IconHeaderItem gridItemPresenterHeader3 = new IconHeaderItem(HEADER_ID_3, HEADER_NAME_3, R.drawable.ic_assistant_black_24dp);
        PageRow pageRow3 = new PageRow(gridItemPresenterHeader3);
        mRowsAdapter.add(pageRow3);
    }


    /**
     * Page fragment embeds a rows fragment.
     */
    public static class SampleFragmentB extends RowsFragment {
        private  ArrayObjectAdapter mRowsAdapter;
        private static final int NUM_ROWS = 6;
        private static final int NUM_COLS = 10;
        public SampleFragmentB() {
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
            List<Movie> list = MovieList.setupMovies();
            mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
            CardPresenter cardPresenter = new CardPresenter();
            int i;
            for (i = 0; i < NUM_ROWS; i++) {
                if (i != 0) {
                    Collections.shuffle(list);
                }
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                for (int j = 0; j < NUM_COLS; j++) {
                    listRowAdapter.add(list.get(j % 5));
                }
                mRowsAdapter.add(new ListRow(null, listRowAdapter));
            }
            setAdapter(mRowsAdapter);
        }
    }

    /**
     * Page fragment embeds a rows fragment.
     */
    public static class SampleFragmentA extends RowsFragment {
        private  ArrayObjectAdapter mRowsAdapter;
        private static final int NUM_ROWS = 2;
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
            List<Movie> list = MovieList.setupMovies();
            mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
            CardPresenter cardPresenter = new CardPresenter();
            int i;
            for (i = 0; i < NUM_ROWS; i++) {
                  Collections.shuffle(list);

                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                for (int j = 0; j < NUM_COLS; j++) {
                    listRowAdapter.add(list.get(j % 5));
                }
                mRowsAdapter.add(new ListRow(null, listRowAdapter));
            }
            setAdapter(mRowsAdapter);
        }
    }




    public static class SettingsFragment extends RowsFragment {
        private final ArrayObjectAdapter mRowsAdapter;

        public SettingsFragment() {
            ListRowPresenter selector = new ListRowPresenter();
            selector.setNumRows(2);
            mRowsAdapter = new ArrayObjectAdapter(selector);
            setAdapter(mRowsAdapter);

        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            }, 200);
        }

        private void loadData() {

            GridItemPresenter mGridPresenter = new GridItemPresenter();
            ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
            gridRowAdapter.add(getResources().getString(R.string.grid_view));
            gridRowAdapter.add(getString(R.string.error_fragment));
            gridRowAdapter.add(getResources().getString(R.string.personal_settings));
            mRowsAdapter.add(new ListRow(null, gridRowAdapter));
        }

        private  class GridItemPresenter extends Presenter {
            private static final int GRID_ITEM_WIDTH = 200;
            private static final int GRID_ITEM_HEIGHT = 200;
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent) {
                TextView view = new TextView(parent.getContext());
                view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                view.setBackgroundColor(getResources().getColor(R.color.default_background));
                view.setTextColor(Color.WHITE);
                view.setGravity(Gravity.CENTER);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder viewHolder, Object item) {
                ((TextView) viewHolder.view).setText((String) item);
            }

            @Override
            public void onUnbindViewHolder(ViewHolder viewHolder) {
            }
        }
    }
}
