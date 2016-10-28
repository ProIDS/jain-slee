package org.mobicents.slee.container.datasource;

public interface SleeDataSourceListener
{
    void shutdown();
    void waitForShutdownCompletion();
}