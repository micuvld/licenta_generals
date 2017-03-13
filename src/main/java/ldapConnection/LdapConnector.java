package ldapConnection;

import org.forgerock.opendj.ldap.Connection;
import org.forgerock.opendj.ldap.LDAPConnectionFactory;

/**
 * Created by vlad on 01.03.2017.
 */
public abstract class LdapConnector implements IConnector {
    LDAPConnectionFactory factory;
    Connection connection;

    public Connection getConnection() {
        return connection;
    }
}
