package no.eliashaugsbakk.uploader.service;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import no.eliashaugsbakk.uploader.model.JsonPost;

/**
 * This class will produce the JSON to send as the HTTP body when uploading a new site.
 *
 * <p>JSON Is chosen for its simplicity, all information is sent as text, in a human-readable form, making debugging
 * easy. Images are encoded as Base64 to send as text.
 * Example JSON:
 * {
 *   "title": "Page Title",
 *   "html": "<h1>Hello World</h1><p>This is a test.</p>",
 *   "images": [
 *     "encoded_base64_string_of_image_1,
 *     "encoded_base64_string_of_image_2",
 *     "encoded_base64_string_of_image_3"
 *   ]
 * }
 * </p>
 * */
public class JsonMaker {

    public String getJson(JsonPost jsonPost) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("title", jsonPost.title());
        jsonObject.add("html", jsonPost.html());

        JsonArray jsonArray = new JsonArray();

        for (String image : jsonPost.images()) {
            jsonArray.add(image);
        }

        jsonObject.add("images", jsonArray);

        return jsonObject.toString();
    }
}
