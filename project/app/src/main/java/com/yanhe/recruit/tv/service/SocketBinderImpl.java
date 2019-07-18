package com.yanhe.recruit.tv.service;

import android.os.Binder;

import java.lang.ref.WeakReference;

import io.socket.client.Socket;

public class SocketBinderImpl extends Binder implements SocketBinder {
    private Socket mSocket;
    private WeakReference<SocketService> serviceRef;
    public SocketBinderImpl(SocketService service, Socket socket) {
        super();
        serviceRef = new WeakReference<>(service);
        mSocket = socket;
    }

    @Override
    public Socket getSocket() {
        return mSocket;
    }

    @Override
    public void reconnect(String room, String position) {
        if (serviceRef != null && serviceRef.get() != null) {
            serviceRef.get().reconnect(room, position);
        }
    }

}
