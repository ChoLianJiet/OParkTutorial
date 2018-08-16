package com.opark.opark.share_parking;

/**
 * Created by User on 12/12/2017.
 */

public class Matchmaking {

    private String adatem;
    private String sessionKey;

    public Matchmaking(String adatem, String sessionKey) {
        this.adatem = adatem;
        this.sessionKey = sessionKey;
    }

    public Matchmaking() {
    }

    public String getAdatem() {
        return adatem;
    }

    public String getSessionKey() {
        return sessionKey;
    }
}
