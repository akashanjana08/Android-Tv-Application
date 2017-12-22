package android.support.v17.leanback.app;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v17.leanback.transition.TransitionHelper;
import android.support.v17.leanback.transition.TransitionListener;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.BrowseFrameLayout;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.PageRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridView;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import shop.tv.rsys.com.tvapplication.CardPresenter;
import shop.tv.rsys.com.tvapplication.Movie;
import shop.tv.rsys.com.tvapplication.R;
import shop.tv.rsys.com.tvapplication.custom.IconHeaderItem;
import shop.tv.rsys.com.tvapplication.custom.IconHeaderItemPresenter;
import shop.tv.rsys.com.tvapplication.model.BtaResponseModel;
import shop.tv.rsys.com.tvapplication.network.ResponseCallback;
import shop.tv.rsys.com.tvapplication.network.RetrofitCallbackResponse;
import shop.tv.rsys.com.tvapplication.parser.MovieListParsing;

/**
 * Created by Akash.Sharma on 12/7/2017.
 */

public class CustomFragment extends BrowseFragment {

    private static final long HEADER_ID_1 = 1;
    private static final String HEADER_NAME_1 = "populair";
    private static final long HEADER_ID_2 = 2;
    private static final String HEADER_NAME_2 = "nieuw";
    private static final long HEADER_ID_3 = 3;
    private static final String HEADER_NAME_3 = "recent";
    private static final long HEADER_ID_4 = 4;
    private static final String HEADER_NAME_4 = "a tot z";
    private BackgroundManager mBackgroundManager;
    private ArrayObjectAdapter mRowsAdapter;
    static List<Movie> list;
    static ProgressBar progressBar;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = (ProgressBar)getActivity().findViewById(R.id.progressBar);
        mBrowseFrame.setOnChildFocusListener(mOnChildFocusListener);
       // mBrowseFrame.setBackgroundColor(getResources().getColor(R.color.hpg_color));
        mBrowseFrame.setOnFocusSearchListener(mOnFocusSearchListener);
    }

    private void setupUi() {
        // setBadgeDrawable(getActivity().getResources().getDrawable(
        // R.drawable.videos_by_google_banner));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(Color.argb(1,1,2,38));
        //setBrandColor(getResources().getColor(R.color.fastlane_background));
        setTitle("");
        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object o) {
                return new IconHeaderItemPresenter();
            }
        });
        //prepareEntranceTransition();

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
                return new SampleFragmentA("categoryPrimary");
            } else if (row.getHeaderItem().getId() == HEADER_ID_2) {
                return new SampleFragmentA("pureSortName");
            } else if (row.getHeaderItem().getId() == HEADER_ID_3) {
                return new SampleFragmentA("pureReleaseDateRev");
            }else if (row.getHeaderItem().getId() == HEADER_ID_4) {
                return new SampleFragmentA("pureReleaseDateRev");
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
        IconHeaderItem gridItemPresenterHeader1 = new IconHeaderItem(HEADER_ID_1, HEADER_NAME_1,R.id.header_radio_button);
        PageRow pageRow1 = new PageRow(gridItemPresenterHeader1);
        mRowsAdapter.add(pageRow1);

        IconHeaderItem gridItemPresenterHeader2 = new IconHeaderItem(HEADER_ID_2, HEADER_NAME_2,R.id.header_radio_button);
        PageRow pageRow2 = new PageRow(gridItemPresenterHeader2);
        mRowsAdapter.add(pageRow2);

        IconHeaderItem gridItemPresenterHeader3 = new IconHeaderItem(HEADER_ID_3, HEADER_NAME_3,R.id.header_radio_button);
        PageRow pageRow3 = new PageRow(gridItemPresenterHeader3);
        mRowsAdapter.add(pageRow3);

        IconHeaderItem gridItemPresenterHeader4 = new IconHeaderItem(HEADER_ID_4, HEADER_NAME_4,R.id.header_radio_button);
        PageRow pageRow4 = new PageRow(gridItemPresenterHeader4);
        mRowsAdapter.add(pageRow4);

    }


    /**
     * Page fragment embeds a rows fragment.
     */
    public static class SampleFragmentA extends RowsFragment {
        private  ArrayObjectAdapter mRowsAdapter;
        private static final int NUM_COLS = 10;
        String sortValue=null;
        public SampleFragmentA(){
            enableRowScaling(false);
        }
        public SampleFragmentA(String sortValue) {
            this.sortValue=sortValue;
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
            progressBar.setVisibility(View.VISIBLE);
            RetrofitCallbackResponse.getNetworkResponse(new ResponseCallback() {
                @Override
                public void getResponse(List<BtaResponseModel.Movie> listdata) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        list = MovieListParsing.getMovieList(listdata);
                        loadRows();
                        //setSelectedPosition(0, true, new ListRowPresenter.SelectItemViewHolderTask(5));
                        getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, getQueryMap(sortValue));
        }

        private void loadRows() {

            mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_LARGE));
            CardPresenter cardPresenter = new CardPresenter(getActivity());
            int i,elemets=0;
            for (i = 0; i < list.size()/NUM_COLS; i++) {
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                for (int j = 0; j < NUM_COLS; j++) {

                    listRowAdapter.add(list.get(elemets));
                    elemets++;
                }
                mRowsAdapter.add(new ListRow(null, listRowAdapter));
            }
            if((list.size()%10)!=0)
            {
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                try {
                    for (int j = 0; j < ((list.size() % 10)); j++) {
                        listRowAdapter.add(list.get(elemets));
                        elemets++;
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

    private static Map<String,String> getQueryMap(String sortValue)
    {
        Map<String,String> queryMap = new HashMap<>();
        queryMap.put("fc_Language","nl");
        queryMap.put("fc_CategoryID","83802703-d3d1-42a7-b7c6-f25f1a593d84");
        queryMap.put("fc_HDCapable","true");
        queryMap.put("c_ContentTypes","CoD");
        queryMap.put("fc_StartIndex","0");
        queryMap.put("fc_HowMany","60");
        queryMap.put("fc_UnlockedPackagesOnly","true");
        queryMap.put("fc_Sort",sortValue);
        queryMap.put("ac_InvoicePrices","true");
        queryMap.put("MAC","0022CEC5725B");
        return queryMap;
    }

    private final BrowseFrameLayout.OnChildFocusListener mOnChildFocusListener =
            new BrowseFrameLayout.OnChildFocusListener() {

                @Override
                public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
                    if (getChildFragmentManager().isDestroyed()) {
                        return true;
                    }
                    // Make sure not changing focus when requestFocus() is called.
                    if (mCanShowHeaders && mShowingHeaders) {
                        if (mHeadersFragment != null && mHeadersFragment.getView() != null
                                && mHeadersFragment.getView().requestFocus(
                                direction, previouslyFocusedRect)) {
                            return true;
                        }
                    }
                    if (mMainFragment != null && mMainFragment.getView() != null
                            && mMainFragment.getView().requestFocus(direction, previouslyFocusedRect)) {
                        return true;
                    }
                    return getTitleView() != null
                            && getTitleView().requestFocus(direction, previouslyFocusedRect);
                }

                @Override
                public void onRequestChildFocus(View child, View focused) {
                    if (getChildFragmentManager().isDestroyed()) {
                        return;
                    }
                    if (!mCanShowHeaders || isInHeadersTransition()) return;
                    int childId = child.getId();
                    if (childId == android.support.v17.leanback.R.id.browse_container_dock && mShowingHeaders) {
                        startHeadersTransitionInternal(false);
                    } else if (childId == android.support.v17.leanback.R.id.browse_headers_dock && !mShowingHeaders) {
                        startHeadersTransitionInternal(true);
                    }
                }
            };

    void createHeadersTransition() {
        mHeadersTransition = TransitionHelper.loadTransition(FragmentUtil.getContext(this),
                mShowingHeaders
                        ? android.support.v17.leanback.R.transition.lb_browse_headers_in : android.support.v17.leanback.R.transition.lb_browse_headers_out);

        TransitionHelper.addTransitionListener(mHeadersTransition, new TransitionListener() {
            @Override
            public void onTransitionStart(Object transition) {
            }
            @Override
            public void onTransitionEnd(Object transition) {
                mHeadersTransition = null;
                if (mMainFragmentAdapter != null) {
                    mMainFragmentAdapter.onTransitionEnd();
                    if (!mShowingHeaders && mMainFragment != null) {
                        View mainFragmentView = mMainFragment.getView();
                        if (mainFragmentView != null && !mainFragmentView.hasFocus()) {
                            mainFragmentView.requestFocus();
                        }
                    }
                }
                if (mHeadersFragment != null) {
                    mHeadersFragment.onTransitionEnd();
                    if (mShowingHeaders) {
                        VerticalGridView headerGridView = mHeadersFragment.getVerticalGridView();
                        if (headerGridView != null && !headerGridView.hasFocus()) {
                            headerGridView.requestFocus();
                        }
                    }
                }

                // Animate TitleView once header animation is complete.
                updateTitleViewVisibility();

                if (mBrowseTransitionListener != null) {
                    mBrowseTransitionListener.onHeadersTransitionStop(mShowingHeaders);
                }
            }
        });
    }

    private final BrowseFrameLayout.OnFocusSearchListener mOnFocusSearchListener =
            new BrowseFrameLayout.OnFocusSearchListener() {
                @Override
                public View onFocusSearch(View focused, int direction) {
                    // if headers is running transition,  focus stays
                    if (mCanShowHeaders && isInHeadersTransition()) {
                        return focused;
                    }
                    //mShowingHeaders=false;
                    if (DEBUG) Log.v(TAG, "onFocusSearch focused " + focused + " + direction " + direction);

                    if (getTitleView() != null && focused != getTitleView()
                            && direction == View.FOCUS_UP) {
                        return getTitleView();
                    }
                    if (getTitleView() != null && getTitleView().hasFocus()
                            && direction == View.FOCUS_DOWN) {
                        return mCanShowHeaders && mShowingHeaders
                                ? mHeadersFragment.getVerticalGridView() : mMainFragment.getView();
                    }

                    boolean isRtl = ViewCompat.getLayoutDirection(focused) == View.LAYOUT_DIRECTION_RTL;
                    int towardStart = isRtl ? View.FOCUS_RIGHT : View.FOCUS_LEFT;
                    int towardEnd = isRtl ? View.FOCUS_LEFT : View.FOCUS_RIGHT;
                    if (mCanShowHeaders && direction == towardStart) {
                        if (isVerticalScrolling() || mShowingHeaders || !isHeadersDataReady()) {
                            return focused;
                        }
                        return mHeadersFragment.getVerticalGridView();
                    } else if (direction == towardEnd) {
                        if (isVerticalScrolling()) {
                            return focused;
                        } else if (mMainFragment != null && mMainFragment.getView() != null) {
                            return mMainFragment.getView();
                        }
                        return focused;
                    } else if (direction == View.FOCUS_DOWN && mShowingHeaders) {
                        // disable focus_down moving into PageFragment.
                        return focused;
                    } else {
                        return null;
                    }
                }
            };

}
