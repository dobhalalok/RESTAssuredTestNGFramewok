package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.api.applicationAPI.PlaylistApi;
import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;

import io.qameta.allure.*;
import io.restassured.response.Response;

import org.testng.annotations.Test;

import static com.spotify.oauth2.utils.FakerUtils.generateDescription;
import static com.spotify.oauth2.utils.FakerUtils.generateName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
@Epic("Spotify Oauth 2.0")
@Feature("Playlist API")

public class PlaylistTests extends BaseTest{
    @Link("https://example.org/mylink/")
    @Link(name="azzure",type="myLink")
    @TmsLink("12345")
    @Issue("The New ISSUE Number : 15")
    @Test(description ="User should be able to create a playlist in Spotify")
    @Story("Create a playlist story")
    public void shouldBeAbleToCreatePlaylist(){
      //  Playlist requestPlaylist=playlistBuilder("New Playlist","New playlist description",false);
        Playlist requestPlaylist=playlistBuilder(generateName(),generateDescription(),false);
        Response response=PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_201);
        assertPlaylistEqual(response.as(Playlist.class),requestPlaylist);

    }

    @Test
    public void shouldBeAbleToGetAPlaylist(){
        Playlist requestPlaylist=playlistBuilder("New Playlist","New playlist description",false);
        Response response=PlaylistApi.get(DataLoader.getInstance().getGetPlaylistId());
       // assertStatusCode(response.statusCode(), StatusCode.CODE_200.getCode());
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);
        assertPlaylistEqual(response.as(Playlist.class),requestPlaylist);
   }

    @Test
    public void shouldBeAbleToUpdateAPlaylist(){
        Playlist requestPlaylist=playlistBuilder(generateName(),generateDescription(),false);
        Response response =PlaylistApi.update(DataLoader.getInstance().getUpdatePlaylistId(), requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);
    }
    @Story("Create a playlist story")
    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithoutName(){

        Playlist requestPlaylist=playlistBuilder("",generateDescription(),false);
        Response response=PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_400);
        Error error=response.as(Error.class);
        assertError(response.as(Error.class), StatusCode.CODE_400);
    }
    @Story("Create a playlist story")
    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithExpiredToken(){
        String invalidToken="12345";
        Playlist requestPlaylist=playlistBuilder(generateName(),generateDescription(),false);
        Response response=PlaylistApi.post(invalidToken,requestPlaylist);
        assertStatusCode(response.statusCode(),StatusCode.CODE_401);
        assertError(response.as(Error.class), StatusCode.CODE_401);

    }
    @Step
    public Playlist playlistBuilder(String name, String description, boolean _public){
        return Playlist.builder().
                name(name).
                description(description).
                _public(_public).build();
    }
    @Step
    public void assertPlaylistEqual(Playlist requestPlaylist,Playlist responsePlaylist){

        assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(),equalTo(requestPlaylist.get_public()));
    }
    @Step
    public void assertStatusCode(int actualStatusCode,StatusCode statusCode){
        assertThat(actualStatusCode,equalTo(statusCode.code));
    }

    @Step
    public void assertError(Error responseErr,StatusCode statusCode){
        assertThat(responseErr.getError().getStatus(),equalTo(statusCode.code));
        assertThat(responseErr.getError().getMessage(),equalTo(statusCode.msg));
    }


}
