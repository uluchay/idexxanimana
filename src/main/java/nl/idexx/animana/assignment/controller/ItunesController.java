package nl.idexx.animana.assignment.controller;

import nl.idexx.animana.assignment.model.Artwork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ItunesController{
    private static final Logger logger = LoggerFactory.getLogger(ItunesController.class);
    private final String TYPE = "Album";

    @Value("${iTunesMaxResults}")
    private String iTunesMaxResults;

    @Value("${iTunesUrl}")
    private String iTunesUrl;

    @RequestMapping("/getItunesAlbums")
    public List<Artwork> getItunesAlbums(@RequestParam(name="searchTerm", required=true) String searchTerm) {
        searchTerm = searchTerm.replaceAll(" ", "+"); // format search term
        logger.debug("received searchTerm: " + searchTerm);
        final String uri = iTunesUrl+"&term="+searchTerm+"&limit="+iTunesMaxResults;
        logger.debug("Calling: " + uri);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        logger.debug("Result: "+ result);
        List<Artwork> returnList = new ArrayList<>();

        if(result != null){
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray results = jsonObj.getJSONArray("results");

                for(int i = 0; i < results.length(); i++){
                    JSONObject albumDetails = results.getJSONObject(i);
                    Artwork artwork2add = new Artwork(  albumDetails.get("artistName").toString(),
                                                        albumDetails.get("collectionName").toString(),
                                                        TYPE);
                    returnList.add(artwork2add);
                    logger.debug("artwork added: "+ artwork2add.getType()+" a: "+artwork2add.getAuthor()+" t: "+artwork2add.getTitle());
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return returnList;
    }


}