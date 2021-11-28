import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RequestHandler {
    final static OkHttpClient okHttpClient = new OkHttpClient();
    final static ObjectMapper objectMapper = new ObjectMapper();
    final static String apiKey = "n4Hnt1jjtt55kFm5gubfGWugokMiS2Ri";

    public static String detectCityId(String cityName) throws IOException {

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("dataservice.accuweather.com")
                .addPathSegment("locations")
                .addPathSegment("v1")
                .addPathSegment("cities")
                .addPathSegment("search")
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("q", cityName)
                .build();

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json")
                .url(httpUrl)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String json = response.body().string();

        String code = objectMapper.readTree(json).get(0).at("/Key").asText();

        return code;
    }

    public static String getForecast(String cityCode, String cityName) throws IOException, ParseException {


        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("dataservice.accuweather.com")
                .addPathSegment("forecasts")
                .addPathSegment("v1")
                .addPathSegment("daily")
                .addPathSegment("5day")
                .addPathSegment(cityCode)
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("metric", "true")
                .build();

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json")
                .url(httpUrl)
                .build();
        Response response = okHttpClient.newCall(request).execute();

        String json = response.body().string();

        String forecastsJson = objectMapper.readTree(json).at("/DailyForecasts").toString();

        DateFormat dfFrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        DateFormat dfTo = new SimpleDateFormat("dd.MM.yyyy");
        for (int i = 0; i < 5; i++) {
            String dayForecast = objectMapper.readTree(forecastsJson).get(i).toString();
            String weatherAtDay = objectMapper.readTree(dayForecast).at("/Day").at("/IconPhrase").asText();
            String weatherAtNight = objectMapper.readTree(dayForecast).at("/Night").at("/IconPhrase").asText();
            String maxTemp = objectMapper.readTree(dayForecast).at("/Temperature").at("/Maximum").at("/Value").asText();
            String minTemp = objectMapper.readTree(dayForecast).at("/Temperature").at("/Minimum").at("/Value").asText();
            String strDate = objectMapper.readTree(dayForecast).at("/Date").asText();
            Date date = dfFrom.parse(strDate);
            System.out.println("В городе " + cityName + " на дату " + dfTo.format(date) + " ожидается днем " + weatherAtDay + ", ночью " + weatherAtNight + ", максимальная температура " + maxTemp + ", минимальная температура " + minTemp);
        }

        return json;
    }
}
