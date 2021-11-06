package com.openklaster.app.measurements.growatt;

import com.openklaster.app.measurements.Inverter;
import com.openklaster.app.measurements.Measurement;
import com.openklaster.app.measurements.growatt.model.GrowattLoginResponse;
import com.openklaster.app.measurements.growatt.model.GrowattLoginResponseWithCookies;
import com.openklaster.app.measurements.growatt.model.GrowattPlantListResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class GrowattInverter implements Inverter<GrowattCredentials> {
    private static final String SERVER_URL = "https://server.growatt.com/";

    private static final String COOKIE = "JSESSIONID";

    @Override
    public Measurement retrieveMeasurement(GrowattCredentials growattCredentials) {
        WebClient webClient = WebClient.create(SERVER_URL);
        GrowattLoginResponseWithCookies loginResponse = login(webClient, growattCredentials);
        String cookie = loginResponse.getCookies().get(COOKIE).get(0).getValue();

        GrowattPlantListResponse growattPlantListResponse = retrievePlantList(webClient, cookie);
        logout(webClient, cookie);

        String currentPower = growattPlantListResponse.getBack().getTotalData().getCurrentPowerSum();
        String energySum = growattPlantListResponse.getBack().getTotalData().getTotalEnergySum();

        return toMeasurement(currentPower, energySum);
    }

    private Measurement toMeasurement(String currentPower, String energySum) {
        String[] powerArray = currentPower.split(" ");
        Double power = toKW(Double.parseDouble(powerArray[0]), powerArray[1]);

        String[] energyArray = energySum.split(" ");
        Double energy = toKWh(Double.parseDouble(energyArray[0]), energyArray[1]);
        return new Measurement(power, energy);
    }

    private GrowattLoginResponseWithCookies login(WebClient webClient, GrowattCredentials growattCredentials) {
        String username = growattCredentials.getUsername();
        String password_md5 = hashPassword(growattCredentials.getPassword());

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("LoginAPI.do")
                        .queryParam("userName", username)
                        .queryParam("password", password_md5)
                        .build())
                .exchangeToMono(response -> response.bodyToMono(GrowattLoginResponse.class)
                        .map(growattLoginResponse -> new GrowattLoginResponseWithCookies(growattLoginResponse, response.cookies())))
                .block();
    }

    private GrowattPlantListResponse retrievePlantList(WebClient webClient, String cookie) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("PlantListAPI.do")
                        .build())
                .cookie(COOKIE, cookie)
                .retrieve()
                .bodyToMono(GrowattPlantListResponse.class)
                .block();
    }

    private void logout(WebClient client, String cookie) {
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("logout.do")
                        .build())
                .cookie(COOKIE, cookie)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    // Normal MD5, except add c if a byte of the digest is less than 10.
    @SneakyThrows
    private static String hashPassword(String password) {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
        byte[] digest = messageDigest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) {
                hexString.append('c');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @SneakyThrows
    private static Double toKWh(Double value, String unit) {
        Double coefficient;

        switch (unit) {
            case "Wh":
                coefficient = 0.001d;
                break;
            case "kWh":
                coefficient = 1d;
                break;
            case "MWh":
                coefficient = 1000d;
                break;

            default:
                throw new Exception("Uknown unit" + unit);
        }
        return value * coefficient;
    }

    @SneakyThrows
    private static Double toKW(Double value, String unit) {
        Double coefficient;

        switch (unit) {
            case "W":
                coefficient = 0.001d;
                break;
            case "kW":
                coefficient = 1d;
                break;
            case "MW":
                coefficient = 1000d;
                break;

            default:
                throw new Exception("Uknown unit" + unit);
        }
        return value * coefficient;
    }
}
