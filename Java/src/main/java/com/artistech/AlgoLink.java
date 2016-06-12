package com.artistech;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

public class AlgoLink {

    private class WebClient {

        private final AlgoLink _parent;
        private final org.json.simple.parser.JSONParser _parser;

        public WebClient(AlgoLink parent) {
            _parent = parent;
            _parser = new org.json.simple.parser.JSONParser();
        }

        private String do_get_raw(String function) {
            HttpClient httpclient = HttpClientBuilder.create().build();
            HttpGet httpget = new HttpGet(this._parent._server + function);
            String ret = "";
            try {
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(instream));
                    try {
                        String line = reader.readLine();
                        while (line != null) {
                            ret += line;
                            line = reader.readLine();
                        }
                        // do something useful
                    } finally {
                        instream.close();
                    }
                }
            } catch (IOException | IllegalStateException ex) {
                logger.log(Level.SEVERE, null, ex);
            } finally {
            }
            return ret;
        }

        private String do_get_string(String function) {
            String ret = this.do_get_raw(function);
            ret = ret.substring(1, ret.length() - 1);
            return ret;
        }

        private JSONObject do_get_object(String function) {
            String ret_str = this.do_get_raw(function);
            JSONObject ret = null;

            try {
                ret = (JSONObject) _parser.parse(ret_str);
            } catch (org.json.simple.parser.ParseException ex) {
                logger.log(Level.SEVERE, null, ex);
            }

            return ret;
        }

        private JSONArray do_get_array(String function) {
            String ret_str = this.do_get_raw(function);
            JSONArray ret = null;

            try {
                ret = (JSONArray) _parser.parse(ret_str);
            } catch (org.json.simple.parser.ParseException ex) {
                logger.log(Level.SEVERE, null, ex);
            }

            return ret;
        }

        private String do_post_raw(String function, JSONObject params) {
            HttpClient httpclient = HttpClientBuilder.create().build();
            HttpPost httpget = new HttpPost(this._parent._server + function);
            String ret = "";

            try {
                List<NameValuePair> formparams = new ArrayList<>();
                java.util.Iterator myIter = params.keySet().iterator();
                while (myIter.hasNext()) {
                    String key = (String) myIter.next();
                    formparams.add(new BasicNameValuePair(key, params.get(key)
                            .toString()));
                }
                formparams.add(new BasicNameValuePair("algolink_id",
                        this._parent._id));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                        formparams, "UTF-8");
                httpget.setEntity(entity);

                try {
                    HttpResponse response = httpclient.execute(httpget);
                    HttpEntity responseEntity = response.getEntity();
                    if (responseEntity != null) {
                        InputStream instream = responseEntity.getContent();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(instream));
                        try {
                            String line = reader.readLine();
                            while (line != null) {
                                ret += line;
                                line = reader.readLine();
                            }
                            // do something useful
                        } finally {
                            instream.close();
                        }
                    }

                } catch (IOException | IllegalStateException ex) {
                    logger.log(Level.SEVERE, null, ex);
                } finally {
                }
            } catch (UnsupportedEncodingException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return ret;
        }

        private String do_post_string(String function) {
            String ret = this.do_post_raw(function, new JSONObject());
            return ret.substring(1, ret.length() - 1);
        }

        private String do_post_string(String function, JSONObject params) {
            String ret = this.do_post_raw(function, params);
            return ret.substring(1, ret.length() - 1);
        }

        private JSONObject do_post_object(String function) {
            String ret_str = this.do_post_raw(function, new JSONObject());
            JSONObject ret = null;

            try {
                ret = (JSONObject) _parser.parse(ret_str);
            } catch (org.json.simple.parser.ParseException ex) {
                logger.log(Level.SEVERE, null, ex);
            }

            return ret;
        }

        private Object do_post_object(String function, JSONObject params) {
            String ret_str = this.do_post_raw(function, params);
            JSONObject ret = null;

            try {
                Object ret2 = _parser.parse(ret_str);
                return ret2;
            } catch (org.json.simple.parser.ParseException ex) {
                logger.log(Level.SEVERE, null, ex);
            }

            return ret;
        }

        private JSONArray do_post_array(String function) {
            String ret_str = this.do_post_raw(function, new JSONObject());
            JSONArray ret = null;

            try {
                ret = (JSONArray) _parser.parse(ret_str);
            } catch (org.json.simple.parser.ParseException ex) {
                logger.log(Level.SEVERE, null, ex);
            }

            return ret;
        }

        private JSONArray do_post_array(String function, JSONObject params) {
            String ret_str = this.do_post_raw(function, params);
            JSONArray ret = null;

            try {
                ret = (JSONArray) _parser.parse(ret_str);
            } catch (org.json.simple.parser.ParseException ex) {
                logger.log(Level.SEVERE, null, ex);
            }

            return ret;
        }
    }

    private final static String DefaultServer = "http://algolink.artistech.com:8080/";
    private String _server;
    private String _id;
    private WebClient _client;
    private static final Logger logger = Logger.getLogger(AlgoLink.class.getName());

    static {
    }

    public AlgoLink(String server) {
        this._server = server;
        this._id = "";
        this._client = new WebClient(this);
    }

    public AlgoLink(String server, String id) {
        this._server = server;
        this._id = id;
        this._client = new WebClient(this);
    }

    public AlgoLink() {
        this(DefaultServer);
    }

    public String getServer() {
        return _server;
    }

    public String getId() {
        return _id;
    }

    public boolean logout() {
        if (this.isLoggedIn()) {
            this._client.do_post_string("logout.json", new JSONObject());
            this._id = "";
            return true;
        }
        return false;
    }

    public boolean isLoggedIn() {
        return !this._id.equals("");
    }

    public String login() {
        if (!this.isLoggedIn()) {
            this._id = this._client.do_get_string("login.json");
            logger.log(Level.FINER, "Login: {0}", this._id);
        }
        return this._id;
    }

    public String getVersion() {
        String obj = this._client.do_get_string("version.json");
        return obj;
    }

    public List<String> getAllConfigTypes() {
        JSONArray obj = this._client.do_get_array("allConfigTypes.json");
        List<String> ret = new ArrayList<>();
        for (Object obj1 : obj) {
            try {
                ret.add((String) obj1);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }

    public List<String> getOrganizationTypes() {
        JSONArray obj = this._client.do_get_array("organizationTypes.json");
        List<String> ret = new ArrayList<>();
        for (Object obj1 : obj) {
            try {
                ret.add((String) obj1);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }

//    public List<String> getDataBuilders() {
//        JSONArray obj = this._client.do_get_array("dataBuilders.json");
//        List<String> ret = new ArrayList<>();
//        for (Object obj1 : obj) {
//            try {
//                ret.add((String) obj1);
//            } catch (Exception ex) {
//            }
//        }
//        return ret;
//    }
    public List<String> getCommunicationFilters() {
        JSONArray obj = this._client.do_get_array("communicationFilters.json");
        List<String> ret = new ArrayList<>();
        for (Object obj1 : obj) {
            try {
                ret.add((String) obj1);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }

    public Object getConfigSchema(String type) {
        Object ret = new JSONObject();
        try {
            JSONObject params = new JSONObject();
            params.put("type", type);
            ret = this._client.do_post_object("configSchema.json", params);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public String sendEntityMessage(String entityId, String message) {
        JSONObject params = new JSONObject();
        params.put("id", entityId);
        params.put("message", message);
        return this._client.do_post_string("send_entity_message.json", params);
    }

    public List<String> getEntities() {
        List<String> ret = new ArrayList<>();

        if (this.isLoggedIn()) {
            JSONArray arr = this._client.do_post_array("entities.json");
            for (Object arr1 : arr) {
                ret.add((String) arr1);
            }
        }

        return ret;
    }

//    public List<String> getUmmfEntities() {
//        List<String> ret = new ArrayList<>();
//
//        if (this.isLoggedIn()) {
//            JSONArray arr = this._client.do_post_array("ummfEntities.json");
//            for (Object arr1 : arr) {
//                ret.add((String) arr1);
//            }
//        }
//
//        return ret;
//    }
//
//    public List<String> getEdgeEntities() {
//        List<String> ret = new ArrayList<>();
//
//        if (this.isLoggedIn()) {
//            JSONArray arr = this._client.do_post_array("edgeEntities.json");
//            for (Object arr1 : arr) {
//                ret.add((String) arr1);
//            }
//        }
//
//        return ret;
//    }
//
//    public List<String> getDiversityCaches() {
//        List<String> ret = new ArrayList<>();
//
//        if (this.isLoggedIn()) {
//            JSONArray arr = this._client
//                    .do_post_array("GetDiversityCaches.json");
//            for (Object arr1 : arr) {
//                ret.add((String) arr1);
//            }
//        }
//
//        return ret;
//    }
//
//    public List<String> getDiversityEntities() {
//        List<String> ret = new ArrayList<>();
//
//        if (this.isLoggedIn()) {
//            JSONArray arr = this._client
//                    .do_post_array("GetDiversityEntities.json");
//            for (Object arr1 : arr) {
//                ret.add((String) arr1);
//            }
//        }
//
//        return ret;
//    }
    public List<JSONObject> getPositions() {
        List<JSONObject> ret = new ArrayList<>();

        if (this.isLoggedIn()) {
            JSONArray arr = this._client
                    .do_post_array("getCommunications.json");
            for (Object arr1 : arr) {
                ret.add((JSONObject) arr1);
            }
        }

        return ret;
    }

    public JSONObject getDbConfig() {
        if (this.isLoggedIn()) {
            return this._client.do_post_object("getDBConfig.json");
        }

        return new JSONObject();
    }

    public JSONObject getPopulation() {
        if (this.isLoggedIn()) {
            return this._client.do_post_object("getPopulation.json");
        }

        return new JSONObject();
    }

    public JSONObject tick() {
        if (this.isLoggedIn()) {
            return this._client.do_post_object("tick.json");
        }

        return new JSONObject();
    }

    public Object createPopulation(JSONArray configs) {
        return this.createPopulation(configs, true, new ArrayList<String>());
    }

    public Object createPopulation(JSONArray configs,
            boolean enable_tick_support) {
        return this.createPopulation(configs, enable_tick_support, new ArrayList<String>());
    }

    public Object createPopulation(JSONArray configs,
            boolean enable_tick_support,
            List<String> outputs) {
        if (this.isLoggedIn()) {
            JSONObject params = new JSONObject();
            params.put("configs", configs);
            params.put("enable_tick_support", enable_tick_support);
            params.put("outputs", outputs);

            return this._client.do_post_object("createPopulation.json", params);
        }

        return new JSONObject();
    }

    public List<JSONObject> getQueuedCommunications() {
        List<JSONObject> ret = new ArrayList<>();
        if (this.isLoggedIn()) {
            JSONArray arr = this._client
                    .do_post_array("GetQueuedWebComms");
            for (Object arr1 : arr) {
                ret.add((JSONObject) arr1);
            }
        }
        return ret;
    }

    public String setQueuedCommunicationCancel(String comm_id, String result) {
        return setQueuedCommunicationCancel(comm_id, result, new HashMap<String, Object>());
    }

    public String setQueuedCommunicationCancel(String comm_id, String result, HashMap<String, Object> payload) {
        if (this.isLoggedIn()) {
            JSONObject params = new JSONObject();
            params.put("commId", comm_id);
            params.put("result", result);
            for(String key : payload.keySet()) {
                params.put(key, payload.get(key));
            }

            return this._client.do_post_string("CancelQueuedCommunication",
                    params);
        }
        return "";
    }

    public String setQueuedCommunicationReceived(String comm_id, String result) {
        return setQueuedCommunicationReceived(comm_id, result, new HashMap<String, Object>());
    }

    public String setQueuedCommunicationReceived(String comm_id, String result, HashMap<String, Object> payload) {
        if (this.isLoggedIn()) {
            JSONObject params = new JSONObject();
            params.put("commId", comm_id);
            params.put("result", result);
            for(String key : payload.keySet()) {
                params.put(key, payload.get(key));
            }

            return this._client.do_post_string("ReceivedQueuedCommunication",
                    params);
        }
        return "";
    }

    public String setQueuedCommunicationStart(String comm_id, String result) {
        return setQueuedCommunicationStart(comm_id, result, new HashMap<String, Object>());
    }

    public String setQueuedCommunicationStart(String comm_id, String result, HashMap<String, Object> payload) {
        if (this.isLoggedIn()) {
            JSONObject params = new JSONObject();
            params.put("commId", comm_id);
            params.put("result", result);
            for(String key : payload.keySet()) {
                params.put(key, payload.get(key));
            }

            return this._client.do_post_string("StartQueuedCommunication",
                    params);
        }
        return "";
    }

    public String setQueuedCommunicationResult(String comm_id, String result) {
        return setQueuedCommunicationResult(comm_id, result, new HashMap<String, Object>());
    }
    public String setQueuedCommunicationResult(String comm_id, String result, HashMap<String, Object> payload) {
        if (this.isLoggedIn()) {
            JSONObject params = new JSONObject();
            params.put("commId", comm_id);
            params.put("result", result);
            for(String key : payload.keySet()) {
                params.put(key, payload.get(key));
            }

            return this._client.do_post_string("SetQueuedWebCommunicationResult",
                    params);
        }
        return "";
    }

    public String setQueuedCommunicationResult2(String results) {
        if (this.isLoggedIn()) {
            JSONObject params = new JSONObject();
            params.put("results", results);

            return this._client.do_post_string("SetQueuedWebCommunicationResult",
                    params);
        }
        return "";
    }

    public static void main(String[] args) {
        com.artistech.utils.logging.Logging.initLogging();
        test("http://localhost:8084/AlgoLink/");
    }

    public static void test(String server) {
        AlgoLink a = new AlgoLink(server);
        logger.log(Level.INFO, "Server:        {0}", a.getServer());
        logger.log(Level.INFO, "Is Logged In:  {0}", a.isLoggedIn());
        logger.log(Level.INFO, "Login:         {0}", a.login());
        logger.log(Level.INFO, "Is Logged I:   {0}", a.isLoggedIn());
        logger.log(Level.INFO, "ID:            {0}", a.getId());

        String __BUILDER_TYPE__ = "com.artistech.algolink.core.DataBuilderConfig";
        String __TEST_TYPE__ = "com.artistech.algolink.orgs.scenario1.Scenario1Config";

        // List<String> types = a.getAllConfigTypes();
        // for(int ii = 0; ii < types.size(); ii++) {
        // System.out.println("All Config Types: " + types.get(ii));
        // System.out.println("All Config Types: " +
        // a.getConfigSchema(types.get(ii)).toString());
        // }
        // types = a.getCommunicationFilters();
        // for(int ii = 0; ii < types.size(); ii++) {
        // System.out.println("Communication Filter: " + types.get(ii));
        // }
        // types = a.getDataBuilders();
        // for(int ii = 0; ii < types.size(); ii++) {
        // System.out.println("DataBuilder: " + types.get(ii));
        // }
        // types = a.getOrganizationTypes();
        // for(int ii = 0; ii < types.size(); ii++) {
        // System.out.println("Organization Type: " + types.get(ii));
        // }
        JSONObject org_cfg = (JSONObject) a.getConfigSchema(__TEST_TYPE__);
        JSONObject db_cfg = (JSONObject) a.getConfigSchema(__BUILDER_TYPE__);
        db_cfg.put("duration", 1440);
        db_cfg.put("realTimeFactor", 0);
        db_cfg.put("neutralPopulation", 0);
        db_cfg.put("moveAllEntities", false);
        db_cfg.put("commFilter", "com.artistech.algolink.web.commfilters.WebCommFilter");
        JSONArray org_list = new JSONArray();
        JSONObject obj1 = new JSONObject();
        obj1.put(__TEST_TYPE__, org_cfg);
        JSONObject obj2 = new JSONObject();
        obj2.put(__BUILDER_TYPE__, db_cfg);

        org_list.add(obj2);
        org_list.add(obj1);

        Object create_population_output = a
                .createPopulation(org_list, true);
        logger.log(Level.FINER, "createPopulation: {0}", create_population_output.toString());//.toJSONString());

        Boolean complete = false;
        while (!complete) {
            JSONObject t = a.tick();
            logger.log(Level.FINER, "tick: {0}", t.get("time").toString());
            complete = (Boolean) t.get("complete");
            if (!complete) {
                List<JSONObject> queuedCommunications = a.getQueuedCommunications();
                for (JSONObject obj : queuedCommunications) {
                    String id = obj.get("id").toString();
                    String result = "SUCCESS from JAVA";
                    a.setQueuedCommunicationResult(id, result);
                }
            }
        }

        logger.log(Level.INFO, "Logout:        {0}", a.logout());
        logger.log(Level.INFO, "Is Logged In:  {0}", a.isLoggedIn());
    }
}
