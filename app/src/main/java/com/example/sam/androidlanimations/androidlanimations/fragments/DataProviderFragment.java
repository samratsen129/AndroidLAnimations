package com.example.sam.androidlanimations.androidlanimations.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.sam.androidlanimations.androidlanimations.model.AbstractDataProvider;
import com.example.sam.androidlanimations.androidlanimations.model.data.ExampleDataProvider;


public class DataProviderFragment extends Fragment {
    private ExampleDataProvider mDataProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);  // keep the mDataProvider instance
        mDataProvider = new ExampleDataProvider(true); // true: example test data
    }

    public AbstractDataProvider getDataProvider() {
        return mDataProvider;
    }
}

