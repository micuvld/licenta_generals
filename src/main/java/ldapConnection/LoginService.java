package ldapConnection;

import org.forgerock.opendj.ldap.Entry;
import org.forgerock.opendj.ldap.LdapException;
import user.Admin;
import user.Master;
import user.User;

/**
 * Created by root on 25.11.2016.
 */
public class LoginService {
//    Dictionary<String, Class> entryDictionary;
//
//    private void populateDictionary() {
//        entryDictionary.put("Admin", Admin.class);
//    }

    public static User login(Connector connector, String username, String password) throws LdapException {
        connector.getConnection().bind(username, password.toCharArray());

        Entry entry = connector.readEntry(username);

        if (getEntryType(entry).equals("Admin") ) {
            return new Admin(entry);
        } else {
            return new Master(entry);
        }

    }

    /*
     * Used only for authenticating as "cn=Directory Manager"
     */
    public static void masterLogin(Connector connector, String password) throws LdapException {
        connector.getConnection().bind("cn=Directory Manager", password.toCharArray());
    }

    private static String getEntryType(Entry entry) {
        String entryName = entry.getName().toString();
        String[] nameComponents = entryName.split(",");

        for (String nameComponent : nameComponents) {
            String[] componentAndValue = nameComponent.split("-");
            if (componentAndValue[0].equals("ou")) {
                return componentAndValue[1];
            }
        }

        return "GenericUser";
    }

}
