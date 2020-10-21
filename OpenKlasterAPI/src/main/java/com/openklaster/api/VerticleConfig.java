package com.openklaster.api;

import com.openklaster.api.handler.*;
import com.openklaster.api.handler.summary.SummaryCreator;
import com.openklaster.api.model.*;
import com.openklaster.api.model.summary.EnvironmentalBenefits;
import com.openklaster.api.model.summary.SummaryRequest;
import com.openklaster.api.parser.DefaultParseStrategy;
import com.openklaster.api.properties.EventbusMethods;
import io.vertx.core.json.JsonObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;

@Configuration
@ComponentScan
public class VerticleConfig {
    private JsonObject jsonConfig;
    private JsonObject jsonHttpEndpointRoute;
    private JsonObject jsonEventBusRoute;
    private JsonObject jsonEnvironmental;
    private Integer apiVersion;

    public VerticleConfig() {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader("OpenKlasterAPI\\src\\main\\resources\\config-dev.json"));
            JSONObject jsonSimple = (JSONObject) object;
            //noinspection unchecked
            this.jsonConfig = new JsonObject(jsonSimple);
            this.jsonHttpEndpointRoute = this.jsonConfig.getJsonObject("http").getJsonObject("endpoint").getJsonObject("route");
            this.jsonEventBusRoute = this.jsonConfig.getJsonObject("core").getJsonObject("route");
            this.jsonEnvironmental = this.jsonConfig.getJsonObject("environmentalbenefits");
            this.apiVersion = this.jsonConfig.getInteger("version");
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public String buildEndpoint(int version, String route) {
        return jsonConfig.getJsonObject("http").getString("prefix") +
                "/" + version + route;
    }

    @Autowired
    @Bean
    public Integer listeningPort(){
        return jsonConfig.getJsonObject("http").getInteger("port");
    }

    @Autowired
    @Bean(name = "postHandlerLogin")
    public PostHandler postHandlerLogin(DefaultParseStrategy<Login> loginDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("login")),
                jsonEventBusRoute.getString("user"), EventbusMethods.LOGIN, loginDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Login> loginDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Login.class);
    }

    @Autowired
    @Bean(name = "postHandlerRegister")
    public PostHandler postHandlerRegister(DefaultParseStrategy<Register> registerDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("user")),
                jsonEventBusRoute.getString("user"), EventbusMethods.REGISTER, registerDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Register> registerDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Register.class);
    }

    @Autowired
    @Bean(name = "postHandlerUsername")
    public PostHandler postHandlerUsername(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("token")),
                jsonEventBusRoute.getString("user"), EventbusMethods.GENERATE_TOKEN, usernameDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Username> usernameDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Username.class);
    }

    @Autowired
    @Bean(name = "postHandlerPostInstallation")
    public PostHandler postHandlerPostInstallation(DefaultParseStrategy<PostInstallation> postInstallationDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("installation")),
                jsonEventBusRoute.getString("installation"), postInstallationDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<PostInstallation> PostInstallationDefaultParseStrategy() {
        return new DefaultParseStrategy<>(PostInstallation.class);
    }

    @Autowired
    @Bean(name = "postHandlerMeasurementPower")
    public PostHandler postHandlerMeasurementPower(DefaultParseStrategy<MeasurementPower> postMeasurementPowerDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("powerconsumption")),
                jsonEventBusRoute.getString("consumption"), postMeasurementPowerDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementPower> postMeasurementPowerDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementPower.class);
    }

    @Autowired
    @Bean(name = "postHandlerMeasurementEnergyConsumed")
    public PostHandler postHandlerMeasurementEnergyConsumed(DefaultParseStrategy<MeasurementEnergy> postMeasurementEnergyDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("energyconsumed")),
                jsonEventBusRoute.getString("consumption"), postMeasurementEnergyDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "postHandlerMeasurementEnergyProduced")
    public PostHandler postHandlerMeasurementEnergyProduced(DefaultParseStrategy<MeasurementEnergy> postMeasurementEnergyDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("energyproduced")),
                jsonEventBusRoute.getString("production"), postMeasurementEnergyDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementEnergy> postMeasurementEnergyDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementEnergy.class);
    }

    @Autowired
    @Bean(name = "putHandlerUpdateUser")
    public PutHandler putHandlerUpdateUser(DefaultParseStrategy<UpdateUser> putUpdateUserDefaultParseStrategy) {
        return new PutHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("user")),
                jsonEventBusRoute.getString("user"), EventbusMethods.UPDATE_USER, putUpdateUserDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<UpdateUser> putUpdateUserParseStrategy() {
        return new DefaultParseStrategy<>(UpdateUser.class);
    }

    @Autowired
    @Bean(name = "putHandlerInstallation")
    public PutHandler putHandlerInstallation(DefaultParseStrategy<Installation> putInstallationDefaultParseStrategy) {
        return new PutHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("installation")),
                jsonEventBusRoute.getString("installation"), putInstallationDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Installation> putInstallationDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Installation.class);
    }


    @Autowired
    @Bean(name = "getHandlerUser")
    public GetHandler getHandlerUser(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("user")),
                jsonEventBusRoute.getString("user"), EventbusMethods.INFO, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "getHandlerInstallationRequest")
    public GetHandler getHandlerInstallationRequest(DefaultParseStrategy<InstallationRequest> installationRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("installation")),
                jsonEventBusRoute.getString("installation"), installationRequestDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<InstallationRequest> installationRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(InstallationRequest.class);
    }

    @Autowired
    @Bean(name = "getHandlerAllInstallationsRequest")
    public GetHandler getHandlerAllInstallations(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("allinstallation")),
                jsonEventBusRoute.getString("installation"), EventbusMethods.GET_ALL, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "getHandlerMeasurementPowerRequestConsumption")
    public GetHandler getHandlerMeasurementPowerConsumption(DefaultParseStrategy<MeasurementPowerRequest> measurementPowerRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("powerconsumption")),
                jsonEventBusRoute.getString("consumption"), measurementPowerRequestDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "getHandlerMeasurementPowerRequestProduction")
    public GetHandler getHandlerMeasurementPowerProduction(DefaultParseStrategy<MeasurementPowerRequest> measurementPowerRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("powerproduction")),
                jsonEventBusRoute.getString("production"), measurementPowerRequestDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementPowerRequest> measurementPowerRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementPowerRequest.class);
    }

    @Autowired
    @Bean(name = "getHandlerMeasurementEnergyRequestConsumption")
    public GetHandler getHandlerMeasurementEnergyConsumption(DefaultParseStrategy<MeasurementEnergyRequest> measurementEnergyRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("energyconsumed")),
                jsonEventBusRoute.getString("consumption"), measurementEnergyRequestDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "getHandlerMeasurementEnergyRequestProduction")
    public GetHandler getHandlerMeasurementEnergyProduction(DefaultParseStrategy<MeasurementEnergyRequest> measurementEnergyRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("energyproduced")),
                jsonEventBusRoute.getString("production"), measurementEnergyRequestDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementEnergyRequest> measurementEnergyRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementEnergyRequest.class);
    }

    @Autowired
    @Bean(name = "deleteHandlerUsernameToken")
    public DeleteHandler deleteHandlerUsernameToken(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new DeleteHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("token")),
                jsonEventBusRoute.getString("user"), EventbusMethods.DELETE_TOKEN, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "deleteHandlerUsernameAllTokens")
    public DeleteHandler deleteHandlerUsernameAllTokens(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new DeleteHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("alltokens")),
                jsonEventBusRoute.getString("user"), EventbusMethods.DELETE_ALL_TOKENS, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "deleteHandlerInstallationRequest")
    public DeleteHandler deleteHandlerInstallationRequest(DefaultParseStrategy<InstallationRequest> installationRequestDefaultParseStrategy) {
        return new DeleteHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("installation")),
                jsonEventBusRoute.getString("installation"), installationRequestDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "summaryHandler")
    public SummaryHandler summaryHandler(DefaultParseStrategy<SummaryRequest> summaryRequestDefaultParseStrategy) {
        return new SummaryHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString("summary")),
                jsonEventBusRoute.getString("production"), summaryRequestDefaultParseStrategy, new SummaryCreator(),
                new EnvironmentalBenefits(jsonEnvironmental.getInteger("co2reduced"), jsonEnvironmental.getInteger("treessaved")));
    }

    @Bean
    public DefaultParseStrategy<SummaryRequest> summaryRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(SummaryRequest.class);
    }
}
