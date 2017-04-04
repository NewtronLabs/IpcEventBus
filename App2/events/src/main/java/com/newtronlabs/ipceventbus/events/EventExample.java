package com.newtronlabs.ipceventbus.events;


import android.os.Parcel;

import com.newtronlabs.ipceventbus.parcelhelper.ParcelHelper;

/**
 * Implementing the interface IData is not needed. It has been added to show that type is
 * preserved over IPC while using IPC EventBus.
 */
public class EventExample extends EventIpcSimple implements IEventExample
{
    /**
     * Sometimes you may which to pass data inside of an event to receive it on the other side.
     */
    private IData mData;

    public EventExample(IData data)
    {
        mData = data;
    }

    EventExample(Parcel in)
    {
        readFromParcel(in);
    }

    @Override
    public void readFromParcel(Parcel parcel)
    {
        // Must be done for every object in the event.
        mData = (IData) ParcelHelper.getInstance().createFromParcel(parcel, IData.class);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        // Must be done for every object in the event.
        ParcelHelper.getInstance().writeToParcel(dest, flags, mData);
    }

    @Override
    public IData getData()
    {
        return mData;
    }
}
