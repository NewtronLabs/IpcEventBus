package com.newtronlabs.ipceventbus.events;

import android.os.Parcel;

import com.newtronlabs.ipceventbus.ipcobject.ObjectIpcSimple;

/**
 * Implementing the interface IData is not needed. It has been added to show that type is
 * preserved over IPC while using IPC EventBus.
 *
 * If extending ObjectIpcSimple is not optimal for your project you may replace it and
 * implement IObjectIpc directly.
 */
public class Data extends ObjectIpcSimple implements IData
{
    private String mName;

    public Data(String name)
    {
        mName = name;
    }

    Data(Parcel in)
    {
        readFromParcel(in);
    }

    @Override
    public String getName()
    {
        return mName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mName);
    }

    public void readFromParcel(Parcel in)
    {
        mName = in.readString();
    }
}