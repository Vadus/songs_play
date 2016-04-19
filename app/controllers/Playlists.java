package controllers;

import models.Playlist;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.UserProvider;

import javax.inject.Inject;
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

    public Result myList(){
        User user = this.userProvider.getUser(session());

        List<Playlist> lists = new ArrayList<>();
        if(user != null) {
            lists.addAll(user.playlists);
        }
        return ok(Json.toJson(lists));
    }
}
