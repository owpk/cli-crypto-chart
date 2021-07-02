package org.owpk;

public interface View<T> {
    EventSocket getEventHandler();
    void setEventHandler(EventSocket socket);
    void handleData(T data);
    void handleError(Throwable data);
}
