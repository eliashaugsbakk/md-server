package no.eliashaugsbakk.uploader.service;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import no.eliashaugsbakk.uploader.model.Image;
import no.eliashaugsbakk.uploader.model.Post;

import java.util.Base64;

/**
 * This class will produce the JSON to send as the HTTP body when uploading a new site.
 *
 * {
 *   "title": "Page Title",
 *   "html": "<h1>Hello World</h1><p>This is a test.</p>",
 *   "images": [
 *     {
 *       "filename": "image1.webp",
 *       "data": "encoded_base64_string_of_image_1"
 *     },
 *     {
 *       "filename": "image2.webp",
 *       "data": "encoded_base64_string_of_image_2"
 *     }
 *   ]
 * }
 * </p>
 * */
public class JsonMaker {

    public String getJson(Post jsonPost) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("title", jsonPost.title());
        jsonObject.add("html", jsonPost.html());

        JsonArray jsonArray = new JsonArray();

        Base64.Encoder encoder = Base64.getEncoder();

        for (Image image : jsonPost.images()) {
            JsonObject jsonImage = new JsonObject();
            jsonImage.add("title", image.title());
            jsonImage.add("data", encoder.encodeToString(image.data()));

            jsonArray.add(jsonImage);
        }

        jsonObject.add("images", jsonArray);

        return jsonObject.toString();
    }
}
