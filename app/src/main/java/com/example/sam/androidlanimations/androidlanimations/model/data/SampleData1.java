package com.example.sam.androidlanimations.androidlanimations.model.data;

import java.util.ArrayList;
import java.util.List;

import com.example.sam.androidlanimations.R;
import com.example.sam.androidlanimations.androidlanimations.model.Data1;


public final class SampleData1 {
    ArrayList<Data1> arrayList = new ArrayList<Data1>();
    private static SampleData1 _instance = new SampleData1();;

    private SampleData1(){

    }

    public static SampleData1 getInstance(){
        return _instance;
    }

    public List getData1(){

        if (arrayList!=null && !arrayList.isEmpty()) return arrayList;

        arrayList.add(new Data1("Amazon AWs", R.drawable.aws));
        arrayList.add(new Data1("Microsoft Sharepoint", R.drawable.microsoft_sharepoint_logo));
        arrayList.add(new Data1("Android rocks", R.drawable.android));
        arrayList.add(new Data1("Microsoft outlook", R.drawable.outlook));
        arrayList.add(new Data1("Skype for calls", R.drawable.skype));
        arrayList.add(new Data1("SVN for source control", R.drawable.svn));

        return arrayList;
    }

    public List addData(Data1 dataIn){
        if (dataIn!=null) {
            arrayList.add(dataIn);
        }
        return arrayList;
    }

    public List removeData(int pos){
        if (!arrayList.isEmpty() && arrayList.size()>=pos+1) {
            arrayList.remove(pos);
        }
        return arrayList;
    }


}
