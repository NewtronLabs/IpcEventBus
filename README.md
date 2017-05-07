# IPC EventBus

Faster than `Intents` and easier than `AIDLs`. IPC EventBus is an Android library for sending events between processes or different apps.

<p align="center">
  <img src="https://github.com/NewtronLabs/IpcEventBus/blob/master/IpcEventBus-Diagram.png" width="56%" height="56%" >
</p>


---

## How to Use 

### Step 1

Include the below dependency in your `build.gradle` project.

```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "http://code.newtronlabs.com:8081/artifactory/libs-release-local" }
    }
}
```

In the `build.gradle` for your app include:

```gradle
compile 'com.newtronlabs.ipceventbus:ipceventbus:4.4.0'
```


### Step 2

Implement `IIpcEventBusConnectionListener` and `IIpcEventBusObserver`.

```java
public class Listener implements IIpcEventBusConnectionListener, IIpcEventBusObserver 
{
    public Listener() 
    {
        String targetApp = "com.packagename";
        
        IIpcEventBusConnector connector =
                ConnectorFactory.getInstance().buildConnector(context, this, targetApp);
            
        connector.startConnection();
    }

    @Override
    public void onConnected(IIpcEventBusConnector connector) 
    {
        connector.registerObserver(this);
    }

    @Override
    public void onEvent(IEventIpc event) 
    {
        Log.d("ipceventbus", "Received event: " + event.getClass().getSimpleName());
    }

    @Override
    public void onDisconnected(IIpcEventBusConnector connector) 
    {

    }
}
```

### Step 3

```java
IpcEventBus.getInstance().postEvent(new MyEvent());
```

### Step 4

Create a module with all your events. Then share the module between the apps that will share the events. Ideally you would turn this module into an AAR for easy sharing.

#### Option A - Simple

The easiest way to create an event is to make your event extend `EventIpcSimple` so that all the setup happens in that super class.

```java
public class EventExample extends EventIpcSimple
{
    public EventExample()
    {
    }

    EventExample(Parcel in)
    {
        readFromParcel(in);
    }
}
```

#### Option B - Event containing object(s)

Sometimes it you may which to pass data inside of an event for it to be received on another app. To do this you have to use the `ParcelHelper` class that will do all the heavy lifting for you. The example below shows this. It also shows that interface types are preserved accross IPC.

```java
public class EventExample extends EventIpcSimple 
{
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
```

#### Option C - Advance mode

Sometimes you don't want your event to extend `EventIpcSimple`. 

```java
public class EventExample implements IEventIpc
{
    public EventExample()
    {
    }

    EventExample(Parcel in)
    {
        readFromParcel(in);
    }
    
    public static final Creator<EventExample> CREATOR = new Creator<EventExample>()
    {
        @Override
        public EventExample createFromParcel(Parcel in)
        {
            return new EventExample(in);
        }

        @Override
        public EventExample[] newArray(int size)
        {
            return new EventExample[size];
        }
    };
    
     @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {        
    }

    public void readFromParcel(Parcel in)
    {
    }
}
```

### Additional Samples

A set of more complex exmaples can be found in this repo's samples folders: **App 1** and **App 2**.


---

## License

IPC EventBus binaries and source code can only be used in accordance with Freeware license. That is, freeware may be used without payment, but may not be modified. The developer of IPC EventBus retains all rights to change, alter, adapt, and/or distribute the software. IPC EventBus is not liable for any damages and/or losses incurred during the use of IPC EventBus.

Users may not decompile, reverse engineer, pull apart, or otherwise attempt to dissect the source code, algorithm, technique or other information from the binary code of IPC EventBus unless it is authorized by existing applicable law and only to the extent authorized by such law. In the event that such a law applies, user may only attempt the foregoing if: (1) user has contacted Newtron Labs to request such information and Newtron Labs has failed to respond in a reasonable time, or (2) reverse engineering is strictly necessary to obtain such information and Newtron Labs has failed to reply. Any information obtained by user from Newtron Labs may be used only in accordance to the terms agreed upon by Newtron Labs and in adherence to Newtron Labs confidentiality policy. Such information supplied by Newtron Labs and received by user shall not be disclosed to a third party or used to create a software substantially similar to the technique or expression of the Newtron Labs IPC EventBus software.

*Patent Pending*

## Contact

contact@newtronlabs.com
