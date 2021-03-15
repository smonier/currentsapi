package org.jahia.se.modules.currentsapi.taglibs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jahia.se.modules.currentsapi.model.News;
import org.jahia.se.modules.currentsapi.cache.CrunchifyInMemoryCache;
import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Inject Currents API Key into request to be used client side by jsp.
 * Created by smonier@jahia.com on 28/11/2020.
 */
@Component
public class CurrentsApiService {

    static final Logger logger = LoggerFactory.getLogger(CurrentsApiService.class);

    private static String currentsApiKey;
    private static CrunchifyInMemoryCache<String, String> cache = new CrunchifyInMemoryCache<String, String>(1200, 500, 50);

    @Activate
    public void activate(Map<String, ?> props) {
        try {
            setCurrentsApiKey((String) props.get("currentsApiKey"));
            logger.info("API Key:" + getCurrentsApiKey());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getCurrentsApiKey() {
        return currentsApiKey;
    }

    public void setCurrentsApiKey(String currentsApiKey) {
        this.currentsApiKey = currentsApiKey;
    }


    public static List<News> getCurrentsNews(String queryType, String searchKeyword, String language, String category, String region) throws IOException, JSONException {

        List<News> NEWS_ARRAY_LIST = new ArrayList<>();
        String currentsApiToken = currentsApiKey;
        logger.info("********************* CurrentsApiService *********************");
        long l = System.currentTimeMillis();


        StringBuilder address = new StringBuilder();
        address.append("https://api.currentsapi.services/v1/");
        logger.info("QueryType:" + queryType);

        if (queryType != "") {
            address.append(queryType).append("?");
        }
        if (searchKeyword != "") {
            address.append("keywords=").append(searchKeyword);
        }
        if (language != "") {
            address.append("&language=").append(language);
        }
        if (category != "") {
            address.append("&category=").append(category);
        }
        if (region != "") {
            address.append("&country=").append(region);
        }
   /*     if (dateFrom != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.ss");
            GregorianCalendar cal = (GregorianCalendar) dateFrom.getDate();
            sdf.setCalendar(cal);
            String str = sdf.format(cal.getTime());
            address.append("&start_date=").append(str.substring(0, 22));
        }*/
        if (address.length() > 0) {

            logger.info("Currents API URL: " + address.toString());
            try {

                String jsonString = null;
                logger.info("Checking if Response already in cache ...");
                logger.info("Cache Size: " + cache.size());
                if (cache.get(address.toString()) != null) {
                    jsonString = cache.get(address.toString());
                    logger.info("Get Response from Cache ...");

                } else {
                    jsonString = readJsonFromUrl(address.toString() + "&apiKey=" + currentsApiToken);
                    cache.put(address.toString(), jsonString);
                    logger.info("Put Response in Cache ...");
                }
                JSONObject currentsApiJsonObject = new JSONObject(jsonString);
                JSONArray newsArray = new JSONArray(currentsApiJsonObject.getString("news"));

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    logger.info(newsArray.toString());
                    NEWS_ARRAY_LIST = mapper.readValue(newsArray.toString(), new TypeReference<List<News>>() {
                    });

                } catch (Exception e) {
                    logger.error("Error parsing JSONObject in JSONArray");
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                logger.error("Missing information for Currents API News");
                e.printStackTrace();
            } catch (IOException e) {
                logger.error("Missing information for Currents API News");
                e.printStackTrace();
            }

        }
        logger.info("Request {} executed in {} ms", address, (System.currentTimeMillis() - l));

        return NEWS_ARRAY_LIST;
    }


    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            //  JSONObject json = new JSONObject(jsonText);
            return jsonText;
        } finally {
            is.close();
        }
    }

    public static JSONObject urlToJson(URL urlString) throws JSONException {
        StringBuilder sb = null;
        URL url;
        URLConnection urlCon;
        try {
            url = urlString;
            urlCon = url.openConnection();
            BufferedReader in = null;
            if (urlCon.getHeaderField("Content-Encoding") != null
                    && urlCon.getHeaderField("Content-Encoding").equals("gzip")) {
                logger.info("reading data from URL as GZIP Stream");
                in = new BufferedReader(new InputStreamReader(new GZIPInputStream(urlCon.getInputStream())));
            } else {
                logger.info("reading data from URL as InputStream");
                in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            }
            String inputLine;
            sb = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            logger.info("Exception while reading JSON from URL - {}", e);
        }
        if (sb != null) {
            return new JSONObject(sb.toString());
        } else {
            logger.warn("No JSON Found in given URL");
            return new JSONObject("");
        }
    }

}

