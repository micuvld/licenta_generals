package user;

import org.forgerock.opendj.ldap.Entry;

/**
 * Created by vlad on 27.11.2016.
 *
 *
 * Represents the "cn=Directory Manager" entry
 */
public class Master extends User {
    public Master(Entry entry) {
        this.entry = entry;
    }
}
