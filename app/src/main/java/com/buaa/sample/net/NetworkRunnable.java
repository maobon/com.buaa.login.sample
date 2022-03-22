package com.buaa.sample.net;

/**
 * create by xin on 2022-3-22
 * request server task
 */

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
