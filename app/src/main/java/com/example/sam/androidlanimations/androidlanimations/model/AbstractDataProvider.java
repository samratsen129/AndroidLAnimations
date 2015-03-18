package com.example.sam.androidlanimations.androidlanimations.model;

public abstract class AbstractDataProvider {

    public static abstract class Data {
        public abstract long getId();

        public abstract boolean isSectionHeader();

        public abstract int getViewType();

        public abstract int getSwipeReactionType();

        public abstract String getText();

        public abstract void setPinnedToSwipeLeft(boolean pinned);

        public abstract boolean isPinnedToSwipeLeft();
    }

    public abstract int getCount();

    public abstract Data getItem(int index);

    public abstract void removeItem(int position);

    public abstract void moveItem(int fromPosition, int toPosition);

    public abstract int undoLastRemoval();
}

