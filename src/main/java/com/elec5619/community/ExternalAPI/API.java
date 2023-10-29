package com.elec5619.community.ExternalAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

public class API {

    public static String filterBadWord(String badWord) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, badWord);

        Request request = new Request.Builder()
                .url("https://api.apilayer.com/bad_words?censor_character=*&content_type=application/json")
                .addHeader("apikey", "vGXuPxkjDO8LH3wrieycQqSuHb3tnD2K")
                .method("POST", body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("censored_content").asText();
    }
}
