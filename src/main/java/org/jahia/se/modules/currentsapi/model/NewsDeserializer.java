package org.jahia.se.modules.currentsapi.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class NewsDeserializer extends StdDeserializer<News> {
    private static Logger logger = LoggerFactory.getLogger(NewsDeserializer.class);

    private static final String NEWS_ID = "/id";
    private static final String NEWS_TITLE = "/title";
    private static final String NEWS_DESCRIPTION = "/description";
    private static final String NEWS_URL = "/url";
    private static final String NEWS_AUTHOR = "/author";
    private static final String NEWS_IMAGE = "/image";
    private static final String NEWS_PUBLISHED = "/published";
    private static final String NEWS_CATEGORY = "/category";


    public NewsDeserializer() {
        this(null);
    }

    public NewsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public News deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode newsNode = oc.readTree(jsonParser);
        News newsAsset = new News();

        JsonNode newsId = newsNode.at(NEWS_ID);
        newsAsset.setId(newsId.textValue());
        logger.info("Deserializing News ID: "+newsId.textValue());

        JsonNode newsTitle = newsNode.at(NEWS_TITLE);
        newsAsset.setTitle(newsTitle.textValue());
        JsonNode newsDescription = newsNode.at(NEWS_DESCRIPTION);
        newsAsset.setDescription(newsDescription.textValue());
        JsonNode newsUrl = newsNode.at(NEWS_URL);
        newsAsset.setUrl(newsUrl.textValue());
        JsonNode newsAuthor = newsNode.at(NEWS_AUTHOR);
        newsAsset.setAuthor(newsAuthor.textValue());
        JsonNode newsImage = newsNode.at(NEWS_IMAGE);
        newsAsset.setImage(newsImage.textValue());
        JsonNode newsPublished = newsNode.at(NEWS_PUBLISHED);
        newsAsset.setPublished(newsPublished.textValue());
        JsonNode newsCategories = newsNode.at(NEWS_CATEGORY);
        newsAsset.setCategories(newsCategories.toString());

        return newsAsset;
    }

    public static List<String> getStringList(JsonNode data, String field) {
        if (data.has(field)) {
            List<String> list = new ArrayList<>();
            data.get(field).forEach(node -> list.add(node.asText()));
            logger.info("Deserializing News Cat: "+list.toString());

            return list;
        } else {
            return null;
        }
    }

}
