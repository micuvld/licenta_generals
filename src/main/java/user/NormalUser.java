package user;

import org.forgerock.opendj.ldap.Entry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vlad on 28.11.2016.
 */
public class NormalUser extends User {
    public NormalUser() {
        this.entry = null;
    }
    public NormalUser(Entry entry) {
        this.entry = entry;
    }

    @Override
    public List getPatients() {
        ArrayList<String> pacientsList = new ArrayList<String>();

        Client client = ClientBuilder.newClient();
        JSONParser parser = new JSONParser();

        String entity = client.target("http://localhost:18080/SecurityProvider")
                .path("patients")
                .queryParam("requesterDn", distinguishedName)
                .request()
                .get(String.class);

        System.out.println(entity);

        try {
            JSONArray entries = (JSONArray)parser.parse(entity);
            for (Object e : entries) {
                pacientsList.add((String)((JSONObject)e).get("member"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return pacientsList;
    }

    @Override
    public String connectToPatient(String patientDn) {
        Client client = ClientBuilder.newClient();
        JSONParser parser = new JSONParser();

        String gatewayHostname = getPatientGateway(patientDn);

        String entity = client.target(gatewayHostname)
                .path("connect")
                .queryParam("requesterDn", distinguishedName)
                .queryParam("targetDn", patientDn)
                .request()
                .get(String.class);

        System.out.println(entity);

        try {
            JSONObject connectionStatus = (JSONObject)parser.parse(entity);
            return (String)connectionStatus.get("status");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "ERROR - connectToPatient";
    }

    @Override
    public String getPatientGateway(String patientDn) {
        Client client = ClientBuilder.newClient();
        JSONParser parser = new JSONParser();

        String entity = client.target("http://localhost:18080/SecurityProvider")
                .path("gateway")
                .queryParam("requesterDn", distinguishedName)
                .queryParam("patientDn", patientDn)
                .request()
                .get(String.class);

        System.out.println(entity);

        try {
            JSONObject gateway = (JSONObject)parser.parse(entity);
            return (String)gateway.get("hostname");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "No gateway found";
    }
}
