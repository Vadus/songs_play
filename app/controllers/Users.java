package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import models.Playlist;
import models.TokenAction;
import models.User;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by DTramnitzke on 15.05.2016.
 */
public class Users extends Controller {

    public Result list(){

        List<UserListEntry> users = new ArrayList<>();

        for (User user : User.find.all()) {
            users.add(new UserListEntry(user));
        }
        return ok(Json.toJson(users));
    }

    private class UserListEntry{

        private User user;

        UserListEntry(User user){
            this.user = user;
        }

        public Long getId(){
            return user.id;
        }

        public String getName(){
            return user.name;
        }

        public Date getLastLogin(){
            return user.lastLogin;
        }

        public int getPlaylists(){
            return user.playlists.size();
        }

        public boolean getEmailValidated(){
            return user.emailValidated;
        }
    }

    @Restrict(@Group(Application.ADMIN_ROLE))
    public Result remove(Long id) {
        Result result = null;
        if(id>0){
            User user = User.find.byId(id);
            if(user == null){
                return notFound("User with " + id + " not found!");
            }
            user.permissions.clear();
            user.roles.clear();
            user.linkedAccounts.clear();
            user.update();
            TokenAction.deleteByUser(user, TokenAction.Type.PASSWORD_RESET);
            TokenAction.deleteByUser(user, TokenAction.Type.EMAIL_VERIFICATION);
            user.delete();
            result = ok();
        }
        else{
            result = badRequest("invalid id");
        }
        return result;
    }

    @Restrict(@Group(Application.ADMIN_ROLE))
    public Result activate(Long id) {
        Result result = null;
        if(id>0){
            User user = User.find.byId(id);
            if(user == null){
                return notFound("User with " + id + " not found!");
            }
            User.verify(user);

            Playlist playlist = new Playlist();
            playlist.name = "Songs of " + user.name;
            playlist.user = user;
            playlist.created = new Date();
            playlist.save();

            user.playlists.add(playlist);
            user.update();

            result = ok();
        }
        else{
            result = badRequest("invalid id");
        }
        return result;
    }
}
