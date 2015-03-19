package com.example.sam.androidlanimations.androidlanimations.adapters;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.sam.androidlanimations.R;
import com.example.sam.androidlanimations.androidlanimations.activities.MainActivity;
import com.example.sam.androidlanimations.androidlanimations.model.Data1;


public class ListView1Adapter extends RecyclerView.Adapter<ListView1Adapter.ViewHolder>{
    private List<Data1> data1;
    private Context ctx;
    private int lastPosition = -1;
    private static View.OnTouchListener mTouchListener;
    private static ValueAnimator mAnimator;
    private static int finalHeight;

    public ListView1Adapter(List<Data1> data1, Context context, View.OnTouchListener mTouchListener){
        this.data1 = data1;
        this.ctx = context;
        this.mTouchListener = mTouchListener;
    }

    public void loadData(Data1 data){
        if (this.data1==null){
            this.data1 = new ArrayList<Data1>();
        }
        this.data1.add(data);
        notifyDataSetChanged();
    }

    @Override
    public ListView1Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_listview1, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ListView1Adapter.ViewHolder viewHolder, final int i) {
        Data1 data = data1.get(i);
        viewHolder.textView.setText(data.getName());
        viewHolder.imageView.setImageBitmap(null);
        viewHolder.imageView.setImageResource(data.getImagePath());
        viewHolder.textViewDesc.setText(data.getNameDesc());
        viewHolder.removeButton.setEnabled(true);
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.detailContainer.isShown()){
                    collapseRow(viewHolder.detailContainer);
                } else {
                    expandRow(viewHolder.detailContainer);
                }
            }
        });

        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((i+1)>data1.size()) return;
                Context ctx = v.getContext();
                MainActivity activity = (MainActivity)ctx;
                viewHolder.removeButton.setEnabled(false);
                removeItemFromList(i);
                activity.removeItem(i);
            }});

        setAnimation(viewHolder.container, i);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(ctx, android.R.anim.slide_in_left);
            animation.setDuration(500);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return data1 == null ? 0 : data1.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;
        public CardView container;
        public Button removeButton;
        public TextView textViewDesc;
        public LinearLayout detailContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            container = (CardView) itemView.findViewById(R.id.card_view);
            textView = (TextView) itemView.findViewById(R.id.text_view);
            imageView = (ImageView)itemView.findViewById(R.id.image_view);
            removeButton = (Button) itemView.findViewById(R.id.button_remove);
            textViewDesc = (TextView) itemView.findViewById(R.id.text_view_desc);
            detailContainer = (LinearLayout) itemView.findViewById(R.id.expandable_ll);
            container.setOnTouchListener(mTouchListener);
            detailContainer.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {

                        @Override
                        public boolean onPreDraw() {
                            detailContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                            detailContainer.setVisibility(View.GONE);

                            final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                            final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                            detailContainer.measure(widthSpec, heightSpec);
                            finalHeight = detailContainer.getHeight();
                            mAnimator = slideAnimator(0, detailContainer.getHeight(), detailContainer);
                            return true;
                        }
                    });

        }

    }

    public void addToList(String name) {
        int position = 0;
        if (data1.size() > 1) {
            position = new Random().nextInt(data1.size() - 1);
        }
        data1.add(position, new Data1("Android iOS friends again!", R.drawable.android_ios));
        notifyItemInserted(position);
    }

    public void removeItemFromList(int position) {
        if (position != -1) {
            data1.remove(position);
            notifyItemRemoved(position);
        }
    }

    private static ValueAnimator slideAnimator(int start, int end, final View linearLayout) {
        Log.d("ListViewAdapter", "LIST VIEW " + start + " - " + end);
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
                layoutParams.height = value;
                linearLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private void expandRow(View linearLayout){
        linearLayout.setVisibility(View.VISIBLE);
        mAnimator = slideAnimator(0, finalHeight, linearLayout);
        mAnimator.start();
    }

    private void collapseRow(final View linearLayout){
        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, linearLayout);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }
}
