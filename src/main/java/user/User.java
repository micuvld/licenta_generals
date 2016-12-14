package user;

import org.forgerock.opendj.ldap.Entry;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.client.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by root on 25.11.2016.
 */
public abstract class User{
    protected Entry entry;
    protected String distinguishedName;
    private AuthenticationStatus authenticationStatus = AuthenticationStatus.UNAUTHORIZED;

    public Entry getEntry() {
        return entry;
    }

    public AuthenticationStatus getAuthenticationStatus() {
        return authenticationStatus;
    }

    public User() {
        entry = null;
    }

    public User(Entry entry) {
        this.entry = entry;
    }

    public void printEntry() {
        System.out.println(entry.getName());
    }

    public void authenticate(String dn, String password) throws URISyntaxException, IOException {
        Client client = ClientBuilder.newClient();
        JSONParser parser = new JSONParser();

        String entity = client.target("http://localhost:18080/SecurityProvider")
                .path("auth")
                .queryParam("requesterDn", dn)
                .queryParam("password", password)
                .request()
                .get(String.class);

        System.out.println(entity);

        try {
            JSONObject responseObject = (JSONObject)parser.parse(entity);
            String authenticationStatusNumber = (String)responseObject.get("status");
            authenticationStatus = AuthenticationStatus.values()[parseInt(authenticationStatusNumber)];
            distinguishedName = dn;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public List getPatients() {
        return null;
    }

    public String getPatientGateway(String pacientDn) {
        return null;
    }

    public String connectToPatient(String pacientDn) { return null; }
}
