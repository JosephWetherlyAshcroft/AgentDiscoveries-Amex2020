package org.softwire.training.api.models;

public class TokenResponseApiModel {

    private String token;
    private String expires;
    private int userId;
    private boolean isAdmin;
    private boolean isAgent;

    public TokenResponseApiModel(String token, String expires, int userId, boolean isAdmin, boolean isAgent) {
        this.token = token;
        this.userId = userId;
        this.expires = expires;
        this.isAdmin = isAdmin;
        this.isAgent = isAgent;
    }

    public String getToken() {
        return token;
    }

    public String getExpires() {
        return expires;
    }

    public int getUserId(){return userId;}

    public boolean getIsAdmin(){return isAdmin;}

    public boolean getIsAgent(){return isAgent;}
}
