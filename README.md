# IPC EventBus

Faster than `Intents` and easier than `AIDLs`. IPC EventBus is an Android library for sending events between processes or different apps.

<p align="center">
  <img src="https://github.com/NewtronLabs/IpcEventBus/blob/master/Diagram.png" width="56%" height="56%" >
</p>

---

## How to Use 

### Step 1

Include the below dependency in your `build.gradle` project.

```gradle
buildscript {
    repositories {
        google()
        maven { url "https://newtronlabs.jfrog.io/artifactory/libs-release-local"
            metadataSources {
                artifact()
            }
        }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.5.2'
        classpath 'com.newtronlabs.android:plugin:5.0.1-alpha01'
    }
}

allprojects {
    repositories {
        google()
        maven { url "https://newtronlabs.jfrog.io/artifactory/libs-release-local"
            metadataSources {
                artifact()
            }
        }
    }
}

subprojects {
    apply plugin: 'com.newtronlabs.android'
}
```

In the `build.gradle` for your app include:

```gradle
dependencies {
    compileOnly 'com.newtronlabs.ipceventbus:ipceventbus:6.0.1'
}
```


### Step 2

Implement `IIpcEventBusConnectionListener` and `IIpcEventBusObserver`.

```java
public class Listener implements IIpcEventBusConnectionListener, IIpcEventBusObserver {
    public Listener() {
        String targetApp = "com.packagename";
        
        IIpcEventBusConnector connector =
                ConnectorFactory.getInstance().buildConnector(context, this, targetApp);
            
        connector.startConnection();
    }

    @Override
    public void onConnected(IIpcEventBusConnector connector) {
        connector.registerObserver(this);
    }

    @Override
    public void onEvent(IEventIpc event) {
        Log.d("ipceventbus", "Received event: " + event.getClass().getSimpleName());
    }

    @Override
    public void onDisconnected(IIpcEventBusConnector connector) {

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
public class EventExample extends EventIpcSimple {

    public EventExample() {
    }

    EventExample(Parcel in) {
        readFromParcel(in);
    }
}
```

#### Option B - Event containing object(s)

Sometimes it you may which to pass data inside of an event for it to be received on another app. To do this you have to use the `ParcelHelper` class that will do all the heavy lifting for you. The example below shows this. It also shows that interface types are preserved accross IPC.

```java
public class EventExample extends EventIpcSimple {
    private IData mData;

    public EventExample(IData data) {
        mData = data;
    }

    EventExample(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void readFromParcel(Parcel parcel) {
        // Must be done for every object in the event.
        mData = (IData) ParcelHelper.getInstance().createFromParcel(parcel, IData.class);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Must be done for every object in the event.
        ParcelHelper.getInstance().writeToParcel(dest, flags, mData);
    }

    public IData getData() {
        return mData;
    }
}
```

#### Option C - Advance mode

Sometimes you don't want your event to extend `EventIpcSimple`. 

```java
public class EventExample implements IEventIpc {
    public EventExample() {
    }

    EventExample(Parcel in) {
        readFromParcel(in);
    }
    
    public static final Creator<EventExample> CREATOR = new Creator<EventExample>() {
        @Override
        public EventExample createFromParcel(Parcel in) {
            return new EventExample(in);
        }

        @Override
        public EventExample[] newArray(int size) {
            return new EventExample[size];
        }
    };
    
     @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {        
    }

    public void readFromParcel(Parcel in) {
    }
}
```

### Additional Samples

A set of more complex exmaples can be found in this repo's samples folders: **App 1** and **App 2**.

---

## Support Us
Please support the continued development of these libraries. We host and develop these libraries for free. Any support is deeply appriciated. Thank you!

<p align="center">
  <img src="https://drive.google.com/uc?id=1rbY8qjxvWU8GQgaqDrOY4-fYOWobQKk3" width="200" height="200" title="Support us" alt="Support us">
</p>

<p align="center">
  <strong>BTC Address:</strong> 39JmAfnNhaEPKz5wjQjQQj4jcv9BM11NQb
</p>

---

## License

IPC EventBus binaries and source code can only be used in accordance with Freeware license. That is, freeware may be used without payment, but may not be modified. The developer of IPC EventBus retains all rights to change, alter, adapt, and/or distribute the software. IPC EventBus is not liable for any damages and/or losses incurred during the use of IPC EventBus.

You may not decompile, reverse engineer, pull apart, or otherwise attempt to dissect the source code, algorithm, technique or other information from the binary code of IPC EventBus unless it is authorized by existing applicable law and only to the extent authorized by such law. In the event that such a law applies, user may only attempt the foregoing if: (1) user has contacted Newtron Labs to request such information and Newtron Labs has failed to respond in a reasonable time, or (2) reverse engineering is strictly necessary to obtain such information and Newtron Labs has failed to reply. Any information obtained by user from Newtron Labs may be used only in accordance to the terms agreed upon by Newtron Labs and in adherence to Newtron Labs confidentiality policy. Such information supplied by Newtron Labs and received by user shall not be disclosed to a third party or used to create a software substantially similar to the technique or expression of the Newtron Labs IPC EventBus software.

You are solely responsible for determining the appropriateness of using IPC EventBus and assume any risks associated with Your use of IPC EventBus. In no event and under no legal theory, whether in tort (including negligence), contract, or otherwise, unless required by applicable law (such as deliberate and grossly negligent acts) or agreed to in writing, shall Newtron Labs be liable to You for damages, including any direct, indirect, special, incidental, or consequential damages of any character arising as a result of this License or out of the use or inability to use the IPC EventBus (including but not limited to damages for loss of goodwill, work stoppage, computer failure or malfunction, or any and all other commercial damages or losses), even if Newtron Labs has been advised of the possibility of such damages. 

*Patent Pending*

## Contact

solutions@newtronlabs.com
