package com.example.sam.androidlanimations.androidlanimations.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.example.sam.androidlanimations.R;
import com.example.sam.androidlanimations.androidlanimations.adapters.ListView1Adapter;
import com.example.sam.androidlanimations.androidlanimations.model.Data1;
import com.example.sam.androidlanimations.androidlanimations.model.data.SampleData1;
import com.example.sam.androidlanimations.androidlanimations.utils.BackgroundContainer;
import com.melnykov.fab.FloatingActionButton;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.Date;
import java.util.HashMap;

public class BasicFragment extends Fragment implements View.OnClickListener{

    private RecyclerView mListView1;
    private FloatingActionButton fab;
    private ListView1Adapter mAdapterListView1;
    private Button addButton;
    private Context context;

    private BackgroundContainer mBackgroundContainer;
    private boolean mSwiping = false;
    private boolean mItemPressed = false;
    private HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();

    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;
    private Handler handler;

    public BasicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basic, container, false);
        context = getActivity();
        mListView1 = (RecyclerView)view.findViewById(R.id.list_view1);
        mBackgroundContainer = (BackgroundContainer)view.findViewById(R.id.listViewBackground);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mListView1.setLayoutManager(llm);
        mListView1.setItemAnimator(new DefaultItemAnimator());
        addButton = (Button)view.findViewById(R.id.button_add);
        addButton.setOnClickListener(this);
        //mListView1.setItemAnimator(new MyItemAnimator());
        mAdapterListView1 = new ListView1Adapter(SampleData1.getInstance().getData1(), context, mTouchListener);

        mListView1.setAdapter(mAdapterListView1);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToRecyclerView(mListView1);
        fab.setOnClickListener(this);

        handler = new Handler();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void enableFloatingButton(){
        fab.setEnabled(true);
    }

    private void enableAddButton(){
        addButton.setEnabled(true);
    }

    public void onAddView(View v0){
        mAdapterListView1.loadData(new Data1("Android iOS friends again! " + (new Date()).toString(), R.drawable.android_ios));
        addButton.setEnabled(false);
        //TODO: DISABLE AND WAIT FOR ANIMATION TO END BEFORE ENABLING
        handler.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                enableAddButton();

            }
        }, 2000);
    }


/*    public class MyItemAnimator extends DefaultItemAnimator{

        @Override
        public boolean animateRemove(final RecyclerView.ViewHolder holder){
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
            animation.setDuration(1000);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {


                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            holder.itemView.startAnimation(animation);

            dispatchRemoveFinished(holder);

            return false;
        }

    }*/

    /**
     * Handle touch events to fade/move dragged items as they are swiped out
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        float mDownX;
        private int mSwipeSlop = -1;

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(context).
                        getScaledTouchSlop();
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mItemPressed) {
                        // Multi-item swipes not handled
                        return false;
                    }
                    mItemPressed = true;
                    mDownX = event.getX();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1);
                    v.setTranslationX(0);
                    mItemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                {
                    float x = event.getX() + v.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    if (!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) {
                            mSwiping = true;
                            mListView1.requestDisallowInterceptTouchEvent(true);
                            mBackgroundContainer.showBackground(v.getTop(), v.getHeight());
                        }
                    }
                    if (mSwiping) {
                        v.setTranslationX((x - mDownX));
                        v.setAlpha(1 - deltaXAbs / v.getWidth());
                    }
                }
                break;
                case MotionEvent.ACTION_UP:
                {
                    // User let go - figure out whether to animate the view out, or back into place
                    if (mSwiping) {
                        float x = event.getX() + v.getTranslationX();
                        float deltaX = x - mDownX;
                        float deltaXAbs = Math.abs(deltaX);
                        float fractionCovered;
                        float endX;
                        float endAlpha;
                        final boolean remove;
                        if (deltaXAbs > v.getWidth() / 4) {
                            // Greater than a quarter of the width - animate it out
                            fractionCovered = deltaXAbs / v.getWidth();
                            endX = deltaX < 0 ? -v.getWidth() : v.getWidth();
                            endAlpha = 0;
                            remove = true;
                        } else {
                            // Not far enough - animate it back
                            fractionCovered = 1 - (deltaXAbs / v.getWidth());
                            endX = 0;
                            endAlpha = 1;
                            remove = false;
                        }
                        // Animate position and alpha of swiped item
                        // NOTE: This is a simplified version of swipe behavior, for the
                        // purposes of this demo about animation. A real version should use
                        // velocity (via the VelocityTracker class) to send the item off or
                        // back at an appropriate speed.
                        long duration = (int) ((1 - fractionCovered) * SWIPE_DURATION);
                        mListView1.setEnabled(false);
                        v.animate().setDuration(duration).
                                alpha(endAlpha).translationX(endX).
                                withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Restore animated values
                                        v.setAlpha(1);
                                        v.setTranslationX(0);
                                        if (remove) {
                                            animateRemoval(mListView1, v);
                                        } else {
                                            mBackgroundContainer.hideBackground();
                                            mSwiping = false;
                                            mListView1.setEnabled(true);
                                        }
                                    }
                                });
                    }
                }
                mItemPressed = false;
                break;
                default:
                    return false;
            }
            return true;
        }
    };

    /**
     * This method animates all other views in the ListView container (not including ignoreView)
     * into their final positions. It is called after ignoreView has been removed from the
     * adapter, but before layout has been run. The approach here is to figure out where
     * everything is now, then allow layout to run, then figure out where everything is after
     * layout, and then to run animations between all of those start/end positions.
     */
    private void animateRemoval(final RecyclerView listview, View viewToRemove) {
        int firstVisiblePosition = ((LinearLayoutManager)listview.getLayoutManager()).findFirstVisibleItemPosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = mAdapterListView1.getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }

        // Delete the item from the adapter
        int position = listview.getChildPosition(viewToRemove);
        mAdapterListView1.removeItemFromList(position);
        mAdapterListView1.notifyDataSetChanged();
        SnackbarManager.show(
                Snackbar.with(getActivity())
                        .text("Item Deleted")
                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .actionLabel("Undo")
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                Toast.makeText(context, "TBD - Not Implemented", Toast.LENGTH_LONG);
                            }
                        }));

        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = ((LinearLayoutManager)listview.getLayoutManager()).findFirstVisibleItemPosition();
                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = mAdapterListView1.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
                                        mBackgroundContainer.hideBackground();
                                        mSwiping = false;
                                        listview.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        //int childHeight = child.getHeight() + listview.getDividerHeight();
                        int childHeight = child.getHeight() + 5;
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {
                                    mBackgroundContainer.hideBackground();
                                    mSwiping = false;
                                    listview.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_add){
            onAddView(v);
        }
        if (v.getId() == R.id.fab){
            mAdapterListView1.loadData(new Data1("TeamViewer for screen sharing! " + (new Date()).toString(), R.drawable.teamviewer_ikon));
            fab.setEnabled(false);
            //TODO: DISABLE AND WAIT FOR ANIMATION TO END BEFORE ENABLING
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enableFloatingButton();

                }
            }, 2000);
        }
    }
}
