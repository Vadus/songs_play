package controllers;

import models.Playlist;
import models.Song;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DTramnitzke on 08.05.2016.
 */
public class Songs extends Controller {

    public Result list(String username){

        User user = User.findByName(username);

        if(user != null){
            List<Song> songs = new ArrayList<>();
            for(Playlist p : user.playlists){
                songs.addAll(p.songs);
            }

            return ok(Json.toJson(songs));
        }
        return notFound(Json.parse("{\"error\": \"Not Found\"}"));
    }
}
