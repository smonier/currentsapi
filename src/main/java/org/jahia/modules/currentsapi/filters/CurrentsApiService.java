package org.jahia.modules.currentsapi.filters;

import org.apache.commons.lang.StringUtils;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;
import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.zip.GZIPInputStream;

/**
 * Inject Currents API Key into request to be used client side by jsp.
 * Created by smonier@jahia.com on 28/11/2020.
 */
public class CurrentsApiService extends AbstractFilter {
    private static final String CURRENTS_API_KEY_ATTR = "currentsApiKey";
    private String currentsApiKey;
    static final Logger logger = LoggerFactory.getLogger(CurrentsApiService.class);
    private Calendar publish_date;

    @Override
    public String prepare(RenderContext renderContext, Resource resource, RenderChain chain) throws Exception, JSONException {
        if (StringUtils.isNotEmpty(currentsApiKey) && renderContext.getRequest().getAttribute(CURRENTS_API_KEY_ATTR) == null) {
            renderContext.getRequest().setAttribute(CURRENTS_API_KEY_ATTR, currentsApiKey);
        }

        logger.info("********************* CurrentsApiService *********************");

        JCRNodeWrapper nodeWrapper = resource.getNode();

        StringBuilder address = new StringBuilder();
        address.append("https://api.currentsapi.services/v1/");
        logger.info("QueryType:" + nodeWrapper.getProperty("queryType").getString());

        if (nodeWrapper.hasProperty("queryType")) {
            address.append(nodeWrapper.getProperty("queryType").getString()).append("?");
        }
        if (nodeWrapper.hasProperty("searchKeyword")) {
            address.append("keywords=").append(nodeWrapper.getProperty("searchKeyword").getString());
        }
        if (nodeWrapper.hasProperty("language")) {
            address.append("&language=").append(nodeWrapper.getProperty("language").getString());
        }
        if (nodeWrapper.hasProperty("category")) {
            address.append("&category=").append(nodeWrapper.getProperty("category").getString());
        }
        if (nodeWrapper.hasProperty("region")) {
            address.append("&country=").append(nodeWrapper.getProperty("region").getString());
        }
        if (nodeWrapper.hasProperty("dateFrom")) {
            logger.info(String.valueOf(nodeWrapper.getProperty("dateFrom").getDate().getTime()));
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.ss");
            GregorianCalendar cal = (GregorianCalendar) nodeWrapper.getProperty("dateFrom").getDate();
            sdf.setCalendar(cal);
            String str = sdf.format(cal.getTime());
            address.append("&start_date=").append(str.substring(0, 22));
        }
        if (address.length() > 0) {

            logger.info("Currents API URL: " + address.toString());
            try {
                //URL url = new URL(address.toString() + "&apiKey=" + currentsApiKey);
                // JSONObject currentsApiJsonObject = new JSONObject(urlToJson(url));
                String jsonString = readJsonFromUrl(address.toString() + "&apiKey=" + currentsApiKey);
                JSONObject currentsApiJsonObject = new JSONObject(jsonString);
                JSONArray newsArray = new JSONArray(currentsApiJsonObject.getString("news"));
                ArrayList<Object> NEWS_ARRAY_LIST = new ArrayList<>();
                logger.info(newsArray.toString());

                try {
                    //    JSONArray jsonArray = new JSONArray(newsArray);
                    for (int i = 0; i < newsArray.length(); i++) {
                        JSONObject array1 = newsArray.getJSONObject(i);
                        NEWS_ARRAY_LIST.add(new News(array1.getString("id"), array1.getString("title"), array1.getString("description"), array1.getString("url"), array1.getString("author"), array1.getString("image"), array1.getString("published")));
                    }

                } catch (JSONException e) {
                    logger.error("Error parsing JSONObject in JSONArray");
                    e.printStackTrace();
                }
                HttpServletRequest request = renderContext.getRequest();
                request.setAttribute("newsList", NEWS_ARRAY_LIST);

            } catch (JSONException e) {
                logger.error("Missing information for Currents API News");
                e.printStackTrace();
            } catch (IOException e) {
                logger.error("Missing information for Currents API News");
                e.printStackTrace();
            }

        }
        return null;
    }

    public void setCurrentsApiKey(String currentsApiKey) {
        this.currentsApiKey = currentsApiKey;
    }

    public String getCurrentsApiKey() {
        return currentsApiKey;
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

