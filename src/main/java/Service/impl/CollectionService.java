package Service.impl;

import Config.URLApi;
import Models.Collection.Collection;
import Models.Collection.CollectionItem;
import Models.User.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CollectionService {

    public int getCollectionInfo(long id){
        try{
            //get collectionID
            Path userDir = Paths.get("Data", "User");
            File userData = new File(userDir.toAbsolutePath().toString() + "\\UserData.json");
            ObjectMapper objectMapper = new ObjectMapper();
            User userInfo = objectMapper.readValue(userData, User.class);
            System.out.println(userInfo.getCollectionID());
            Response response = Request.Get("http://103.9.86.61:8080/admin_srs/api/v1/public/collection-item/find-by-collection?collection-id=" + id + "&enable=true").execute();
            byte[] responseByte = response.returnContent().asBytes();
            String responseString = new String(responseByte, StandardCharsets.UTF_8);
            JSONParser jsonParser = new JSONParser();
            JSONObject responseJs = (JSONObject) jsonParser.parse(responseString);
            JSONArray data = (JSONArray) responseJs.get("data");

            ArrayList<CollectionItem> listCollectionItem = new ArrayList<>();

            data.forEach(dataE -> {
                long idCollectionItem = (long) ((JSONObject) dataE).get("id");
                long stt = (long) ((JSONObject) dataE).get("stt");
                boolean video = (boolean)((JSONObject) dataE).get("video") ;
                String url = (String) ((JSONObject) dataE).get("url");

                CollectionItem collectionItem = new CollectionItem();
                collectionItem.setCollectionItemID(idCollectionItem);
                collectionItem.setVideo(video);
                collectionItem.setUrl(url);
                collectionItem.setStt(stt);

                listCollectionItem.add(collectionItem);

            });



            Collection collection = new Collection();
            collection.setCollectionID(id);
            collection.setListCollection(listCollectionItem);

            Path collectionDir = Paths.get("Data","Collection\\Info");
            Files.createDirectories(collectionDir);
            File collectionDataFile = new File(collectionDir.toAbsolutePath().toString() + "\\CollectionData.json");

            if(!collectionDataFile.exists()){
                collectionDataFile.createNewFile();
            }

            ObjectMapper collectionObM = new ObjectMapper();
            collectionObM.writerWithDefaultPrettyPrinter().writeValue(collectionDataFile, collection);




            return 200;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            return 400;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 500;
    }


}
