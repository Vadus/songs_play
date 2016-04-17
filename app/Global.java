import be.objectify.deadbolt.java.models.Role;
import com.feth.play.module.pa.providers.password.DefaultUsernamePasswordAuthUser;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import models.SecurityRole;
import models.User;
import models.UserPermission;
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
        }

    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

}
