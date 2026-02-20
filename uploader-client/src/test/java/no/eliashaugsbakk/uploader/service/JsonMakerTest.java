package no.eliashaugsbakk.uploader.service;

import no.eliashaugsbakk.uploader.model.JsonPost;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonMakerTest {
    @Test
    void getJson_returns_the_expected_json_string() {
        JsonMaker jsonMaker = new JsonMaker();
        JsonPost jsonPost = new JsonPost("Title", "<p> html string </p>", List.of("image1string", "image2string"));
        String json = jsonMaker.getJson(jsonPost);

        assertTrue(json.contains("Title"));
        assertTrue(json.contains("html string </p>"));
        assertTrue(json.contains("image1string"));
    }
}
