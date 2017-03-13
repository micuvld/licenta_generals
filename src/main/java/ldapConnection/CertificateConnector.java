package ldapConnection;

import org.forgerock.opendj.ldap.*;
import org.forgerock.opendj.ldap.responses.SearchResultEntry;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

/**
 * Created by vlad on 01.03.2017.
 */
public class CertificateConnector extends LdapConnector {
    public CertificateConnector(String keystorePath, String storepass, String truststorePath) {
        try {
            factory = new LDAPConnectionFactory(HOST_NAME, PORT,
                    getTrustOptions(keystorePath, storepass, truststorePath));
            connection = factory.getConnection();
            System.out.println("Connection handler creating succeeded!");
        } catch (ErrorResultException e) {
            System.out.println("Connection handler creating failed!");
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public Boolean login(String username, String password) throws ErrorResultException {
        try
        {
            String dn = getDnForUid(username);
            System.out.println(dn);
            connection.bind(dn, password.toCharArray());
            return true;
        }
        catch (final ErrorResultException e)
        {
            System.err.println(e.getMessage());
            System.exit(e.getResult().getResultCode().intValue());
            return false;
        }
    }

    public String getDnForUid(String uid) throws ErrorResultException {
        SearchResultEntry entry = connection.searchSingleEntry(BASE_DN,
                SearchScope.WHOLE_SUBTREE, "(uid=" + uid + ")");

        return entry.getName().toString();
    }

    public ArrayList<Entry> readEntires(String filter) throws ErrorResultIOException {
        return null;
    }

    public Entry readEntry(String username) throws ErrorResultException {
        return null;
    }

    LDAPOptions getTrustOptions(String keystorePath, String storepass, String truststorePath)
            throws GeneralSecurityException, IOException {
        LDAPOptions lo = new LDAPOptions();
        SSLContextBuilder contextBuilder = new SSLContextBuilder();
        contextBuilder.setKeyManager(KeyManagers.useKeyStoreFile(keystorePath, storepass.toCharArray(), null));
        contextBuilder.setTrustManager(TrustManagers
                .checkUsingTrustStore(truststorePath));

        SSLContext sslContext = contextBuilder.getSSLContext();
        lo.setSSLContext(sslContext);
        lo.setUseStartTLS(false);
        return lo;
    }
}
