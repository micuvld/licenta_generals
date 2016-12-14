package ldapConnection;

import org.forgerock.opendj.ldap.*;
import org.forgerock.opendj.ldap.responses.SearchResultEntry;
import org.forgerock.opendj.ldap.responses.SearchResultReference;
import org.forgerock.opendj.ldif.ConnectionEntryReader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by root on 25.11.2016.
 */
public class Connector {
    final String HOST_NAME = "vlad-VirtualBox";
    final int PORT = 1389;
    final String BASE_DN = "dc=spital,dc=moinesti,dc=com";

    private LDAPConnectionFactory factory = new LDAPConnectionFactory(HOST_NAME, PORT);
    private Connection connection;

    private ConnectionEntryReader reader;

    private static Connector instance = null;

    private Connector() {
        try {
            connection = factory.getConnection();

        } catch (LdapException e) {
            System.out.println("Connection attempt failed!");
            e.printStackTrace();
        }
    }

    public static Connector getInstance() {
        if (instance == null) {
            instance = new Connector();
        }

        return instance;
    }


    public Connection getConnection() {
        return connection;
    }

    public ArrayList<Entry> readEntires(String filter) throws LdapException {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        reader = connection.search(BASE_DN, SearchScope.WHOLE_SUBTREE, filter);

        while (reader.hasNext()) {
            if (!reader.isReference()) {
                SearchResultEntry entry = null;
                try {
                    entry = reader.readEntry();
                    entries.add(entry);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                final SearchResultReference ref = reader.readReference();
                System.out.println("Search result reference: " + ref.getURIs());
            }
        }

        return entries;
    }

    public Entry readEntry(String username) throws LdapException {
        return connection.readEntry(username);
    }
}
