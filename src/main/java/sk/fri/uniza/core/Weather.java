package sk.fri.uniza.core;

import org.primefaces.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Weather {
    private String obec;
    private String apiKey;
    private String firstParam;
    private String secondParam;
    private static String openWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=";
    private static String keyForm = "&APPID=";
    private static String defaultApiKey = "a5201e652ac2ccfc2d0766c1c8e6e310";

    public Weather() {
    }

    public Weather(String obec, String apiKey, String firstParam, String secondParam) {
        this.obec = obec;
        this.apiKey = apiKey;
        this.firstParam = firstParam;
        this.secondParam = secondParam;
    }

    public String getObec() {
        return obec;
    }

    public void setObec(String obec) {
        this.obec = obec;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(String firstParam) {
        this.firstParam = firstParam;
    }

    public String getSecondParam() {
        return secondParam;
    }

    public void setSecondParam(String secondParam) {
        this.secondParam = secondParam;
    }

    public static String getOpenWeatherUrl() {
        return openWeatherUrl;
    }

    public static void setOpenWeatherUrl(String openWeatherUrl) {
        Weather.openWeatherUrl = openWeatherUrl;
    }

    public static String getKeyForm() {
        return keyForm;
    }

    public static void setKeyForm(String keyForm) {
        Weather.keyForm = keyForm;
    }

    public static String getDefaultApiKey() {
        return defaultApiKey;
    }

    public static void setDefaultApiKey(String defaultApiKey) {
        Weather.defaultApiKey = defaultApiKey;
    }

    public static float callWeatherApi(Weather weather) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Weather.openWeatherUrl);
        stringBuilder.append(weather.getObec());
        stringBuilder.append(Weather.keyForm);
        stringBuilder.append(weather.getApiKey());

        String url = stringBuilder.toString();

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // optional default is GET
        try {
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        int responseCode = 0;
        try {
            responseCode = con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputLine = null;
        StringBuffer response = new StringBuffer();
        while (true) {
            try {
                if (!((inputLine = in.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.append(inputLine);
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject myResponse = new JSONObject(response.toString());

        float f = Float.parseFloat(myResponse.getJSONObject(weather.getFirstParam()).get(weather.getSecondParam()).toString());

        return f;
    }
}
