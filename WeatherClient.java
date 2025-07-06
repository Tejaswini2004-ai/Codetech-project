import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class WeatherClient {

    // Weather code to description mapping
    static String getWeatherDescription(int code) {
        return switch (code) {
            case 0 -> "Clear sky";
            case 1, 2 -> "Mainly clear";
            case 3 -> "Overcast";
            case 45, 48 -> "Fog";
            case 51, 53, 55 -> "Drizzle";
            case 61, 63, 65 -> "Rain";
            case 71, 73, 75 -> "Snowfall";
            case 95 -> "Thunderstorm";
            default -> "Unknown";
        };
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter a city name: ");
            String city = scanner.nextLine();

            try {
                // Use Open-Meteo API with geocoding
                String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + city;
                HttpURLConnection geoConn = (HttpURLConnection) new URL(geoUrl).openConnection();
                geoConn.setRequestMethod("GET");

                BufferedReader geoReader = new BufferedReader(new InputStreamReader(geoConn.getInputStream()));
                StringBuilder geoResponse = new StringBuilder();
                String geoLine;
                while ((geoLine = geoReader.readLine()) != null) {
                    geoResponse.append(geoLine);
                }
                geoReader.close();

                JSONObject geoObj = new JSONObject(geoResponse.toString());
                if (!geoObj.has("results")) {
                    System.out.println("❌ City not found. Try again.\n");
                    continue;
                }

                JSONObject location = geoObj.getJSONArray("results").getJSONObject(0);
                double lat = location.getDouble("latitude");
                double lon = location.getDouble("longitude");

                // Get weather data
                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + lat +
                        "&longitude=" + lon + "&current_weather=true";
                HttpURLConnection weatherConn = (HttpURLConnection) new URL(weatherUrl).openConnection();
                weatherConn.setRequestMethod("GET");

                BufferedReader weatherReader = new BufferedReader(new InputStreamReader(weatherConn.getInputStream()));
                StringBuilder weatherResponse = new StringBuilder();
                String weatherLine;
                while ((weatherLine = weatherReader.readLine()) != null) {
                    weatherResponse.append(weatherLine);
                }
                weatherReader.close();

                JSONObject weatherObj = new JSONObject(weatherResponse.toString());
                JSONObject current = weatherObj.getJSONObject("current_weather");

                System.out.println("\n------ Weather Data for " + city + " ------");
                System.out.println("Temperature: " + current.getDouble("temperature") + "°C");
                System.out.println("Wind Speed: " + current.getDouble("windspeed") + " km/h");
                int code = current.getInt("weathercode");
                System.out.println("Weather: " + getWeatherDescription(code) + " (code " + code + ")");
                System.out.println("Time: " + current.getString("time") + "\n");

            } catch (Exception e) {
                System.out.println("❌ Error fetching weather data. Try again.");
                e.printStackTrace();
            }

            System.out.print("Do you want to check another city? (yes/no): ");
            String again = scanner.nextLine();
            if (!again.equalsIgnoreCase("yes")) {
                System.out.println("Goodbye!");
                break;
            }
        }

        scanner.close();
    }
}
