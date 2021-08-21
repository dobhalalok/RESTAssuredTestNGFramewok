package com.spotify.oauth2.api.applicationAPI;

import com.spotify.oauth2.api.RestResource;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.ConfigLoader;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.spotify.oauth2.api.Route.PLAYLISTS;
import static com.spotify.oauth2.api.Route.USERS;
import static com.spotify.oauth2.api.applicationAPI.TokenManager.getToken;
import static com.spotify.oauth2.api.applicationAPI.TokenManager.renewToken;


public class PlaylistApi {

    @Step
    public static Response post(Playlist requestPlayist){
        return RestResource.post(USERS + "/" +ConfigLoader.getInstance().getUser() + PLAYLISTS, getToken(), requestPlayist);
    }

    public static Response post(String token,Playlist requestPlayist){
        return RestResource.post(USERS + "/" + ConfigLoader.getInstance().getUser() + PLAYLISTS ,token,requestPlayist);
    }

    public static Response get(String playlistID){
        return RestResource.get(PLAYLISTS + "/" + playlistID, getToken());
    }
    public static Response update(String playlistID , Playlist requestPlaylist){
        return RestResource.update(PLAYLISTS + "/" + playlistID, getToken(), requestPlaylist);
    }

}
