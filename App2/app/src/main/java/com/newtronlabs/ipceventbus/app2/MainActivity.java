package com.newtronlabs.ipceventbus.app2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.newtronlabs.ipceventbus.connection.IIpcEventBusConnectionListener;
import com.newtronlabs.ipceventbus.connection.connectors.IIpcEventBusConnector;
import com.newtronlabs.ipceventbus.connection.factory.ConnectorFactory;
import com.newtronlabs.ipceventbus.events.Data;
import com.newtronlabs.ipceventbus.events.EventExample;
import com.newtronlabs.ipceventbus.events.IData;
import com.newtronlabs.ipceventbus.events.IEventExample;
import com.newtronlabs.ipceventbus.events.IEventIpc;
import com.newtronlabs.ipceventbus.observer.IIpcEventBusObserver;


public class MainActivity extends AppCompatActivity
        implements IIpcEventBusConnectionListener, IIpcEventBusObserver, View.OnClickListener
{

    private TextView mEventTextView;
    private TextView mDataTextView;
    private IIpcEventBusConnector mConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The app that we want to listen to.
        String targetApp = "com.newtronlabs.ipceventbus.app1";

        // Ideally you would always make this call in the Application class.
        mConnector = ConnectorFactory.getInstance().buildConnector(this, this, targetApp);
        mConnector.startConnection();

        // TextViews to display the received event.
        mEventTextView = (TextView) findViewById(R.id.received_event);
        mDataTextView = (TextView) findViewById(R.id.received_data);

        // Button to post back.
        Button button = (Button) findViewById(R.id.post_event_btn);
        button.setOnClickListener(this);

    }

    @Override
    public void onConnected(IIpcEventBusConnector connector)
    {
        // Register to receive events.
        mConnector = connector;
        connector.registerObserver(this);
    }

    @Override
    public void onDisconnected(IIpcEventBusConnector connector)
    {

    }

    @Override
    public void onEvent(IEventIpc event)
    {
        Log.d("Newtron"," onEvent: " + event.getClass().getSimpleName());
        Runnable runnable = new UiRunnable(event, mEventTextView, mDataTextView);
        runOnUiThread(runnable);
    }

    @Override
    public void onClick(View v)
    {
        // Example of postback to App 1.
        if(v.getId() == R.id.post_event_btn)
        {
            if(mConnector != null)
            {
                IData data = new Data("Posting back.");
                mConnector.postBack(new EventExample(data));
            }
        }
    }


    /**
     * Runnable to post the event on the UI Thread. Different methods may be used. This is for
     * the example.
     */
    private static class UiRunnable implements Runnable
    {
        private IEventIpc mEvent;
        private TextView mEventTextView;
        private TextView mDataTextView;

        private UiRunnable(IEventIpc event, TextView eventTextView, TextView dataTextView)
        {
            mEvent = event;
            mEventTextView = eventTextView;
            mDataTextView = dataTextView;
        }

        @Override
        public void run()
        {
            // Check instanceof the event.
            if(mEvent instanceof IEventExample)
            {
                // Print the class name of the event that arrived.
                mEventTextView.setText(mEvent.getClass().getSimpleName());

                // Get the data out of the event that arrived.
                IData data = ((IEventExample)mEvent).getData();
                mDataTextView.setText(data.getName());
            }
        }
    }
}

