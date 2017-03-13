package ldapConnection;

/**
 * Created by vlad on 01.03.2017.
 */
public abstract class SingletonConnector implements IConnector {
    SingletonConnector() {
    }

    public static SingletonConnector getInstance() {
        return null;
    }


}
