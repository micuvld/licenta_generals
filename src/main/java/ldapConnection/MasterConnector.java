package ldapConnection;

import org.forgerock.opendj.ldap.*;
import org.forgerock.opendj.ldap.responses.BindResult;
import org.forgerock.opendj.ldap.responses.SearchResultEntry;
import org.forgerock.opendj.ldap.responses.SearchResultReference;
import org.forgerock.opendj.ldif.ConnectionEntryReader;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

/**
 * Created by vlad on 14.02.2017.
 */
public class MasterConnector implements IConnector {
    private static MasterConnector instance = null;
    private LDAPConnectionFactory factory;
    private Connection connection;

    private MasterConnector() {
        try {
            factory = new LDAPConnectionFactory(HOST_NAME, PORT, getTrustOptions(null, null));
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

    public static MasterConnector getInstance() {
        if (instance == null) {
            instance = new MasterConnector();
        }

        return instance;
    }

    public Boolean login(String username, String password) throws ErrorResultException {
        BindResult bindResult = connection.bind("cn=Directory Manager", password.toCharArray());
        return bindResult.isSuccess();
    }

    public ArrayList<Entry> readEntires(String filter) throws ErrorResultIOException {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        ConnectionEntryReader reader = connection.search(BASE_DN, SearchScope.WHOLE_SUBTREE, filter);

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

    public Entry readEntry(String username) throws ErrorResultException {
        return connection.readEntry(username);
    }

    public String trustAllConnect(String username, String password)
    {
        try
        {
            connection.bind(username, password.toCharArray());
            return "Authenticated as " + username + ".";
        }
        catch (final ErrorResultException e)
        {
            System.err.println(e.getMessage());
            System.exit(e.getResult().getResultCode().intValue());
            return "error";
        }
    }

    private LDAPOptions getTrustOptions(String keystorePath, String truststore)
            throws GeneralSecurityException, IOException {
        LDAPOptions lo = new LDAPOptions();
        SSLContextBuilder contextBuilder = new SSLContextBuilder();
        contextBuilder.setKeyManager(KeyManagers.useKeyStoreFile("/home/vlad/Licenta/2client-cert/keystore", "qwerty".toCharArray(), null));
        contextBuilder.setTrustManager(TrustManagers
                        .checkUsingTrustStore("/home/vlad/Licenta/opendj/config/truststore"));

        SSLContext sslContext = contextBuilder.getSSLContext();
        lo.setSSLContext(sslContext);
        lo.setUseStartTLS(false);
        return lo;
    }

}
