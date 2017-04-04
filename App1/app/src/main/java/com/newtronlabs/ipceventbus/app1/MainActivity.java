package com.newtronlabs.ipceventbus.app1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.newtronlabs.ipceventbus.IpcEventBus;
import com.newtronlabs.ipceventbus.events.Data;
import com.newtronlabs.ipceventbus.events.EventExample;
import com.newtronlabs.ipceventbus.events.IData;
import com.newtronlabs.ipceventbus.events.IEventIpc;
import com.newtronlabs.ipceventbus.observer.IIpcEventBusObserver;

public class MainActivity extends AppCompatActivity implements IIpcEventBusObserver, View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.post_event_btn);
        button.setOnClickListener(this);

        IpcEventBus.getInstance().registerPostBackObserver(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.post_event_btn)
        {
            // Create a Data object with a name.
            IData data = new Data("Hello!");

            // Post the event.
            IpcEventBus.getInstance().postEvent(new EventExample(data));
        }
    }

    @Override
    public void onEvent(IEventIpc event)
    {

    }
}