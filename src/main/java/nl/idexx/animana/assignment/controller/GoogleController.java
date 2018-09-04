package nl.idexx.animana.assignment.controller;

import nl.idexx.animana.assignment.model.Artwork;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Configuration
@PropertySource(ignoreResourceNotFound = true, value = "classpath:application.properties")
@RestController
public class GoogleController {
    private static final Logger logger = LoggerFactory.getLogger(GoogleController.class);
    private final String TYPE = "Book";

    @Value("${googleMaxResults}")
    private String googleMaxResults;

    @Value("${googleUrl}")
    private String googleUrl;

    @RequestMapping("/getGoogleBooks")
    public List<Artwork> getGoogleBooks(@RequestParam(name="searchTerm", required=true) String searchTerm) {

        logger.debug("received searchTerm: " + searchTerm);

        final String uri = googleUrl+"?q=\""+searchTerm+"\"&maxResults="+googleMaxResults;
        logger.debug("Calling: " + uri);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        logger.debug("Result: "+ result);
        List<Artwork> returnList = new ArrayList<>();

        if(result != null){
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray items = jsonObj.getJSONArray("items");

                for(int i = 0; i < items.length(); i++){
                    JSONObject itemInfo = new JSONObject(items.get(i).toString());
                    JSONObject volumeInfo = itemInfo.getJSONObject("volumeInfo");
                    String authors = volumeInfo.getString("authors").replaceAll("[\\[\\](){}]","")       // Removes brackets
                            .replaceAll("\\\\","")               // Removes backslashes
                            .replaceAll("\"","");                // Removes quotes
                    Artwork artwork2add = new Artwork(  authors,
                                                        volumeInfo.getString("title"),
                                                        TYPE);
                    returnList.add(artwork2add);
                    logger.debug("artwork added: "+ artwork2add.getType()+" a: "+artwork2add.getAuthor()+" t: "+artwork2add.getTitle());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return returnList;
    }

}
