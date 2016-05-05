package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import models.Playlist;
import models.Song;
import models.User;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import service.UserProvider;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DTramnitzke on 19.04.2016.
 */
public class Playlists extends Controller{

    private final UserProvider userProvider;

    @Inject
    public Playlists(final UserProvider userProvider){
        this.userProvider = userProvider;
    }

    public Result list(String username){

        User user = User.findByName(username);

        List<Playlist> lists = new ArrayList<>();
        lists.addAll(user.playlists);
        if(user != null){
            return ok(Json.toJson(lists));
        }
        return notFound(Json.parse("{\"error\": \"Not Found\"}"));
    }

    public Result recent(Integer count){
        List<Playlist> lists = Playlist.find.orderBy("created desc").setMaxRows(count).findList();
        return ok(Json.toJson(lists));
    }

    @Restrict(@Group(Application.USER_ROLE))
    @BodyParser.Of(BodyParser.Json.class)
    public Result add() {
        JsonNode json = request().body().asJson();
        String url = json.findPath("url").textValue();
        if(url == null) {
            return badRequest("Missing parameter [url]");
        }
        String playlistId = json.findPath("playlist").textValue();
        if(playlistId == null){
            return badRequest("Missing parameter [playlist]");
        }

        Logger.info("Adding song " + url + " to playlist " + playlistId);

        String source = null;
        if(url.indexOf("youtube.com") != -1
        || url.indexOf("youtu.be") != -1){
            source = "YT";
        }
        else if (url.indexOf("soundcloud.com") != -1){
            source = "SC";
        }
        if(source == null){
            return badRequest("URL '" + url + "' not supported yet");
        }

        String title = null;
        try {
            Connection conn = Jsoup.connect(url)
                    //.userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(10000);
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22")
                    .timeout(5000).ignoreHttpErrors(true).followRedirects(true);
            Connection.Response response = conn.execute();
            Logger.info("Response from URL '"+url+"': " + response.statusCode() + " - " + response.statusMessage());
            if(response.statusCode() == 200) {
                title = conn.get().title();
            }
        }catch(Exception ex){
            Logger.error("Error reading url " + url, ex);
        }

        if(title == null){
            return badRequest("URL '" + url + "' can not be read");
        }

        String sourceUrl = url;
        if(source.equals("YT")){
            String videoId = null;
            int vPar = url.indexOf("v=");
            if(vPar > -1){
                videoId = url.substring(vPar + 2);
                int nextPar = videoId.indexOf("&");
                if(nextPar > -1){
                    videoId = videoId.substring(0, nextPar);
                }
            }
            else if (url.indexOf("youtu.be") != -1){
                int domainPos = url.indexOf("youtu.be");
                String[] parts = url.substring(domainPos).split("/");
                if(parts.length > 1){
                    videoId = parts[1];
                }
            }
            if(videoId != null){
                url = "https://www.youtube.com/v/" + videoId;
            }
        }

        Playlist playlist = Playlist.find.byId(Long.parseLong(playlistId));

        Song song = new Song();
        song.pos = playlist.songs.size() + 1;
        song.url = url;
        song.sourceUrl = sourceUrl;
        song.source = source;
        song.title = title;
        song.save();

        playlist.songs.add(song);
        playlist.update();

        return ok(Json.toJson(song));
    }

    public Result myList(){
        User user = this.userProvider.getUser(session());

        List<Playlist> lists = new ArrayList<>();
        if(user != null) {
            lists.addAll(user.playlists);
        }
        return ok(Json.toJson(lists));
    }
}
