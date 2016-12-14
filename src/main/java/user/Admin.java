package user;

import org.forgerock.opendj.ldap.*;

/**
 * Created by root on 25.11.2016.
 */
public class Admin extends User {
    public Admin(Entry entry) {
        this.entry = entry;
    }
}
