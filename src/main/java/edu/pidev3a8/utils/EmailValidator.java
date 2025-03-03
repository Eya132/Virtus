package edu.pidev3a8.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmailValidator {

    private static final String API_KEY = "30359014f624479696718261c8b9667b"; // Remplacez par votre clé API
    private static final String API_URL = "https://emailvalidation.abstractapi.com/v1/?api_key=" + API_KEY + "&email=";

    public static boolean isEmailValid(String email) {
        try {
            URL url = new URL(API_URL + email);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse la réponse JSON
            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            System.out.println(jsonResponse); // Pour déboguer

            // Extrait les valeurs des champs
            boolean isValidFormat = jsonResponse.getAsJsonObject("is_valid_format").get("value").getAsBoolean();
            boolean isMxFound = jsonResponse.getAsJsonObject("is_mx_found").get("value").getAsBoolean();
            boolean isSmtpValid = jsonResponse.getAsJsonObject("is_smtp_valid").get("value").getAsBoolean();

            // Vérifie si l'email est valide
            return isValidFormat && isMxFound && isSmtpValid;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}