import be.objectify.deadbolt.java.models.Role;
import com.feth.play.module.pa.providers.password.DefaultUsernamePasswordAuthUser;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import models.*;
import play.Logger;
import play.Application;
import play.GlobalSettings;

import javax.inject.Inject;

/**
 * Created by DTramnitzke on 10.04.2016.
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        Logger.info("Application has started");




        UsernamePasswordAuthUser user =
                new UsernamePasswordAuthUser("admin", "admin@klarblick.org") {
        };

        if(User.findByUsernamePasswordIdentity(user) == null) {

            User u = User.create(user, true);
            u.name = "admin";
            User.verify(u);

            Logger.info("user 'admin@klarblick.org' created");

            Song song1 = new Song();
            song1.title = "Moderat - New Error";
            song1.link = "https://www.youtube.com/v/1J9l3O1jmrg";
            //song1.save();

            Song song2 = new Song();
            song2.title = "My Tribute Mixtape Series #13: Christian Löffler";
            song2.link = "https://soundcloud.com/dekunstenaar/presents-christian-loffler";
            //song2.save();

            Playlist playlist = new Playlist();
            playlist.name = "Dannys Songs";
            playlist.songs.add(song1);
            playlist.songs.add(song2);
            //playlist.user = u;
            //playlist.save();

            u.playlists.add(playlist);
            u.update();

        }

    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

}
