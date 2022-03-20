package com.buaa.sample.net;

public class NetworkRunnable implements Runnable {

    private final RequestServer requestServer;

    public NetworkRunnable(RequestServer requestServer) {
        this.requestServer = requestServer;
    }

    @Override
    public void run() {
        requestServer.onResponse();
    }
}
