package com.newtronlabs.ipceventbus.app1;

import android.app.Application;
import android.util.Log;

import com.newtronlabs.ipceventbus.IpcEventBus;
import com.newtronlabs.ipceventbus.events.Data;
import com.newtronlabs.ipceventbus.events.EventExample;
import com.newtronlabs.ipceventbus.events.IData;
import com.newtronlabs.ipceventbus.events.IEventIpc;
import com.newtronlabs.ipceventbus.observer.IIpcEventBusObserver;

public class App extends Application implements IIpcEventBusObserver
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d("Newtron", "onCreate");
        IpcEventBus.getInstance().registerPostBackObserver(this);

        try
        {
            Thread.sleep(2000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        IData data = new Data("Hello Application");
        IpcEventBus.getInstance().postEvent(new EventExample(data));
    }


    @Override
    public void onEvent(IEventIpc event)
    {
        Log.d("Newtron"," onEvent: " + event.getClass().getSimpleName());
    }
}
