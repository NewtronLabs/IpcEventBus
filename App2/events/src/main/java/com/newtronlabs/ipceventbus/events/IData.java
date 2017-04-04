package com.newtronlabs.ipceventbus.events;

import com.newtronlabs.ipceventbus.ipcobject.IObjectIpc;

/**
 * An example interface IData to give a type to the Data we will send over IPC.
 */
public interface IData extends IObjectIpc
{
    /**
     * Returns the name of the object.
     * @return The name.
     */
    String getName();
}
