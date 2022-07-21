package org.voroshilkin;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.voroshilkin.TranslateResult.Translation;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Translator {

    private static final String KEY = "5850d2e52emshab258172ae27b8fp1a6c7djsnb4b8d192fee4";
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String translateToRussian(String text) throws IOException {

        RequestBody body = new FormBody.Builder()
                .add("q", text)
                .add("target", "ru")
                .add("source", "en")
                .build();

        Request request = new Request.Builder()
                .url("https://google-translate1.p.rapidapi.com/language/translate/v2")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("Accept-Encoding", "application/gzip")
                .addHeader("X-RapidAPI-Key", KEY)
                .addHeader("X-RapidAPI-Host", "google-translate1.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();

        if (response.body() != null) {

            TranslateResult result = mapper.readValue(response.body().byteStream(), TranslateResult.class);

            StringBuilder builder = new StringBuilder();
            for (Translation translation : result.getData().getTranslations()) {
                builder.append(translation.getTranslatedText())
                        .append(" ");
            }

            return builder.toString();
        } else {
            throw new RuntimeException("Response body is empty");
        }
    }
}
