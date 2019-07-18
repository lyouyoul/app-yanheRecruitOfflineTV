package com.yanhe.recruit.tv.service;

import io.socket.client.Socket;

public interface SocketBinder {
    Socket getSocket();
    void reconnect(String room, String position);
}
