package user;

import ldapConnection.LdapConnector;
import org.forgerock.opendj.ldap.ByteString;
import org.forgerock.opendj.ldap.Connection;
import org.forgerock.opendj.ldap.ErrorResultException;
import org.forgerock.opendj.ldap.SearchScope;
import org.forgerock.opendj.ldap.responses.SearchResultEntry;

/**
 * Created by vlad on 01.03.2017.
 */
public class UserOperations {
    public static String[] getPatients(String userUid, Connection connection) {
        ByteString[] patients = new ByteString[1];

        SearchResultEntry entry = null;
        try {
            entry = connection.searchSingleEntry(LdapConnector.BASE_DN,
                    SearchScope.WHOLE_SUBTREE, "uid=" + userUid);

            patients = entry.getAttribute("patientdn").toArray(patients);
        } catch (ErrorResultException e) {
            e.printStackTrace();
        }

        String[] stringPatients = new String[patients.length];
        for (int i = 0; i < patients.length; ++i) {;
            stringPatients[i] = patients[i].toString();
        }

        return stringPatients;
    }
}
