import be.objectify.deadbolt.java.models.Role;
import com.feth.play.module.pa.providers.password.DefaultUsernamePasswordAuthUser;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import models.*;
import play.Logger;
import play.Application;
import play.GlobalSettings;

import javax.inject.Inject;
import java.util.Date;

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

            Tag tagElectronic = new Tag();
            tagElectronic.name = "electronic";
            tagElectronic.save();

            Tag tagRemix = new Tag();
            tagRemix.name = "remix";
            tagRemix.save();

            Tag tagChill = new Tag();
            tagChill.name = "chill";
            tagChill.save();

            Song song1 = new Song();
            song1.pos = 1;
            song1.title = "Keaton Henson - Elevator Song";
            song1.url = "https://www.youtube.com/v/YZ72sspbN3U";
            song1.sourceUrl = "https://www.youtube.com/watch?v=YZ72sspbN3U";
            song1.source = "YT";
            song1.save();

            Song song2 = new Song();
            song2.pos = 2;
            song2.title = "The xx - VCR (Four Tet remix)";
            song2.url = "https://soundcloud.com/four-tet/the-xx-vcr-four-tet-remix";
            song2.sourceUrl = "https://soundcloud.com/four-tet/the-xx-vcr-four-tet-remix";
            song2.source = "SC";
            song2.tags.add(tagElectronic);
            song2.tags.add(tagRemix);
            song2.save();

            Song song3 = new Song();
            song3.pos = 3;
            song3.title = "Rhye - \"The Fall\" (Official Music Video)";
            song3.url = "https://www.youtube.com/v/F6yfFWvoygY";
            song3.sourceUrl = "https://www.youtube.com/watch?v=F6yfFWvoygY";
            song3.source = "YT";
            song3.tags.add(tagChill);
            song3.save();

            Playlist playlist = new Playlist();
            playlist.name = "Dannys Songs";
            playlist.songs.add(song1);
            playlist.songs.add(song2);
            playlist.songs.add(song3);
            playlist.user = u;
            playlist.created = new Date();
            playlist.tags.add(tagElectronic);
            playlist.save();

            u.playlists.add(playlist);

            u.update();

        }

    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

}
