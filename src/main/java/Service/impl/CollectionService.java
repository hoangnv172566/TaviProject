package Service.impl;

import Config.URLApi;
import Models.Collection.Collection;
import Models.Collection.CollectionItem;
import Models.User.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CollectionService {
    public long getIDCollection(User user){

        long id = 0;

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URLApi.LOGIN);

        JSONObject userJs = new JSONObject();
        userJs.put("username", user.getUsername());
        userJs.put("password", user.getPassword());

        try{
            StringEntity params = new StringEntity(userJs.toString());
            httpPost.setHeader("content-type", "application/json");
            httpPost.setEntity(params);
            HttpResponse response = httpClient.execute(httpPost);
            JSONParser jsonParser = new JSONParser();
            JSONObject responseJson = (JSONObject) jsonParser.parse(EntityUtils.toString(response.getEntity()));
            JSONObject dataJs = (JSONObject) responseJson.get("data");
            JSONObject appCollection = (JSONObject) dataJs.get("appCollection");
            id = (long) appCollection.get("id");

        } catch (UnsupportedEncodingException | ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return id;
    }

    public Collection getCollectionInfor(User user) throws IOException {
        Collection collection = new Collection();
        ArrayList<CollectionItem> listCollectionItem = new ArrayList<>();
        long idCollection = getIDCollection(user);
        collection.setCollectionID(idCollection);

        String response = Request.Get("http://103.9.86.61:8080/admin_srs/api/v1/public/collection-item/find-by-collection?collection-id="+idCollection+"&enable=true").execute().returnContent().asString();
        JSONParser jsonParser = new JSONParser();
        try{
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response);

            ArrayList<JSONObject> listJson = (ArrayList<JSONObject>) jsonObject.get("data");

            listJson.forEach(collectionItemJson->{
                CollectionItem collectionItem = new CollectionItem();

                //getting Info
                long stt = (long) collectionItemJson.get("stt");
                long id = (long) collectionItemJson.get("id");
                String url = (String) collectionItemJson.get("url");
                boolean isVideo = (boolean) collectionItemJson.get("video");

                //mapping data
                collectionItem.setCollectionItemID(id);
                collectionItem.setStt(stt);
                collectionItem.setUrl(url);
                collectionItem.setVideo(isVideo);

                listCollectionItem.add(collectionItem);
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }

        collection.setListCollection(listCollectionItem);
        return collection;

    }


}
