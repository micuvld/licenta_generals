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
public interface IConnector {
    String HOST_NAME = "vlad-PC";
    int PORT = 2636;
    String BASE_DN = "dc=spital,dc=moinesti,dc=com";

    Boolean login(String username, String password) throws ErrorResultException;

    ArrayList<Entry> readEntires(String filter) throws ErrorResultIOException;

    Entry readEntry(String username) throws ErrorResultException;
}
