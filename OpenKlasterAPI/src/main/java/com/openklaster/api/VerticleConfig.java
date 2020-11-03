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
    private static final String HTTP_PATH = "http";
    private static final String PREFIX_FOR_HTTP_PATH = "prefix";
    private static final String PORT_FOR_HTTP_PATH = "port";
    private static final String ENDPOINT_FOR_HTTP_PATH = "endpoint";
    private static final String ROUTE_FOR_ENDPOINT_HTTP_PATH = "route";
    private static final String CORE_PATH = "core";
    private static final String ROUTE_FOR_CORE_PATH = "route";
    private static final String ENVIRONMENTAL_BENEFITS_PATH = "environmentalbenefits";
    private static final String VERSION_PATH = "version";

    private static final String USER_FOR_HTTP_ENDPOINT_PATH = "user";
    private static final String INSTALLATION_FOR_HTTP_ENDPOINT_PATH = "installation";
    private static final String LOGIN_FOR_HTTP_ENDPOINT_PATH = "login";
    private static final String TOKEN_FOR_HTTP_ENDPOINT_PATH = "token";
    private static final String POWER_CONSUMPTION_FOR_HTTP_ENDPOINT_PATH = "powerconsumption";
    private static final String POWER_PRODUCTION_FOR_HTTP_ENDPOINT_PATH = "powerproduction";
    private static final String ENERGY_CONSUMED_FOR_HTTP_ENDPOINT_PATH = "energyconsumed";
    private static final String ENERGY_PRODUCED_FOR_HTTP_ENDPOINT_PATH = "energyproduced";
    private static final String SUMMARY_FOR_HTTP_ENDPOINT_PATH = "summary";
    private static final String ALL_TOKENS_FOR_HTTP_ENDPOINT_PATH = "alltokens";
    private static final String ALL_INSTALLATION_FOR_HTTP_ENDPOINT_PATH = "allinstallation";

    private static final String USER_FOR_EVENTBUS_PATH = "user";
    private static final String INSTALLATION_FOR_EVENTBUS_PATH = "installation";
    private static final String PRODUCTION_FOR_EVENTBUS_PATH = "production";
    private static final String CONSUMPTION_FOR_EVENTBUS_PATH = "consumption";

    private static final String TREES_SAVED_FOR_ENVIRONMENTAL_PATH = "treessaved";
    private static final String CO2_REDUCED_FOR_ENVIRONMENTAL_PATH = "co2reduced";

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
            this.jsonHttpEndpointRoute = this.jsonConfig.getJsonObject(HTTP_PATH).getJsonObject(ENDPOINT_FOR_HTTP_PATH).getJsonObject(ROUTE_FOR_ENDPOINT_HTTP_PATH);
            this.jsonEventBusRoute = this.jsonConfig.getJsonObject(CORE_PATH).getJsonObject(ROUTE_FOR_CORE_PATH);
            this.jsonEnvironmental = this.jsonConfig.getJsonObject(ENVIRONMENTAL_BENEFITS_PATH);
            this.apiVersion = this.jsonConfig.getInteger(VERSION_PATH);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public String buildEndpoint(int version, String route) {
        return jsonConfig.getJsonObject(HTTP_PATH).getString(PREFIX_FOR_HTTP_PATH) +
                "/" + version + route;
    }

    @Autowired
    @Bean
    public Integer listeningPort() {
        return jsonConfig.getJsonObject(HTTP_PATH).getInteger(PORT_FOR_HTTP_PATH);
    }

    @Autowired
    @Bean(name = "postHandlerLogin")
    public PostHandler postHandlerLogin(DefaultParseStrategy<Login> loginDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(LOGIN_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(USER_FOR_EVENTBUS_PATH), EventbusMethods.LOGIN, loginDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Login> loginDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Login.class);
    }

    @Autowired
    @Bean(name = "postHandlerRegister")
    public PostHandler postHandlerRegister(DefaultParseStrategy<Register> registerDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(USER_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(USER_FOR_EVENTBUS_PATH), EventbusMethods.REGISTER, registerDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Register> registerDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Register.class);
    }

    @Autowired
    @Bean(name = "postHandlerUsername")
    public PostHandler postHandlerUsername(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(TOKEN_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(USER_FOR_EVENTBUS_PATH), EventbusMethods.GENERATE_TOKEN, usernameDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Username> usernameDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Username.class);
    }

    @Autowired
    @Bean(name = "postHandlerPostInstallation")
    public PostHandler postHandlerPostInstallation(DefaultParseStrategy<PostInstallation> postInstallationDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(INSTALLATION_FOR_EVENTBUS_PATH), postInstallationDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<PostInstallation> PostInstallationDefaultParseStrategy() {
        return new DefaultParseStrategy<>(PostInstallation.class);
    }

    @Autowired
    @Bean(name = "postHandlerMeasurementPower")
    public PostHandler postHandlerMeasurementPower(DefaultParseStrategy<MeasurementPower> postMeasurementPowerDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(POWER_CONSUMPTION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(CONSUMPTION_FOR_EVENTBUS_PATH), postMeasurementPowerDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementPower> postMeasurementPowerDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementPower.class);
    }

    @Autowired
    @Bean(name = "postHandlerMeasurementEnergyConsumed")
    public PostHandler postHandlerMeasurementEnergyConsumed(DefaultParseStrategy<MeasurementEnergy> postMeasurementEnergyDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(ENERGY_CONSUMED_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(CONSUMPTION_FOR_EVENTBUS_PATH), postMeasurementEnergyDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "postHandlerMeasurementEnergyProduced")
    public PostHandler postHandlerMeasurementEnergyProduced(DefaultParseStrategy<MeasurementEnergy> postMeasurementEnergyDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(ENERGY_PRODUCED_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(PRODUCTION_FOR_EVENTBUS_PATH), postMeasurementEnergyDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementEnergy> postMeasurementEnergyDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementEnergy.class);
    }

    @Autowired
    @Bean(name = "putHandlerUpdateUser")
    public PutHandler putHandlerUpdateUser(DefaultParseStrategy<UpdateUser> putUpdateUserDefaultParseStrategy) {
        return new PutHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(USER_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(USER_FOR_EVENTBUS_PATH), EventbusMethods.UPDATE_USER, putUpdateUserDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<UpdateUser> putUpdateUserParseStrategy() {
        return new DefaultParseStrategy<>(UpdateUser.class);
    }

    @Autowired
    @Bean(name = "putHandlerInstallation")
    public PutHandler putHandlerInstallation(DefaultParseStrategy<Installation> putInstallationDefaultParseStrategy) {
        return new PutHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(INSTALLATION_FOR_EVENTBUS_PATH), putInstallationDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Installation> putInstallationDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Installation.class);
    }


    @Autowired
    @Bean(name = "getHandlerUser")
    public GetHandler getHandlerUser(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(USER_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(USER_FOR_EVENTBUS_PATH), EventbusMethods.INFO, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "getHandlerInstallationRequest")
    public GetHandler getHandlerInstallationRequest(DefaultParseStrategy<InstallationRequest> installationRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(INSTALLATION_FOR_EVENTBUS_PATH), installationRequestDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<InstallationRequest> installationRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(InstallationRequest.class);
    }

    @Autowired
    @Bean(name = "getHandlerAllInstallationsRequest")
    public GetHandler getHandlerAllInstallations(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(ALL_INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(INSTALLATION_FOR_EVENTBUS_PATH), EventbusMethods.GET_ALL, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "getHandlerMeasurementPowerRequestConsumption")
    public GetHandler getHandlerMeasurementPowerConsumption(DefaultParseStrategy<MeasurementPowerRequest> measurementPowerRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(POWER_CONSUMPTION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(CONSUMPTION_FOR_EVENTBUS_PATH), measurementPowerRequestDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "getHandlerMeasurementPowerRequestProduction")
    public GetHandler getHandlerMeasurementPowerProduction(DefaultParseStrategy<MeasurementPowerRequest> measurementPowerRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(POWER_PRODUCTION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(PRODUCTION_FOR_EVENTBUS_PATH), measurementPowerRequestDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementPowerRequest> measurementPowerRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementPowerRequest.class);
    }

    @Autowired
    @Bean(name = "getHandlerMeasurementEnergyRequestConsumption")
    public GetHandler getHandlerMeasurementEnergyConsumption(DefaultParseStrategy<MeasurementEnergyRequest> measurementEnergyRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(ENERGY_CONSUMED_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(CONSUMPTION_FOR_EVENTBUS_PATH), measurementEnergyRequestDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "getHandlerMeasurementEnergyRequestProduction")
    public GetHandler getHandlerMeasurementEnergyProduction(DefaultParseStrategy<MeasurementEnergyRequest> measurementEnergyRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(ENERGY_PRODUCED_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(PRODUCTION_FOR_EVENTBUS_PATH), measurementEnergyRequestDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementEnergyRequest> measurementEnergyRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementEnergyRequest.class);
    }

    @Autowired
    @Bean(name = "deleteHandlerUsernameToken")
    public DeleteHandler deleteHandlerUsernameToken(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new DeleteHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(TOKEN_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(USER_FOR_EVENTBUS_PATH), EventbusMethods.DELETE_TOKEN, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "deleteHandlerUsernameAllTokens")
    public DeleteHandler deleteHandlerUsernameAllTokens(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new DeleteHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(ALL_TOKENS_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(USER_FOR_EVENTBUS_PATH), EventbusMethods.DELETE_ALL_TOKENS, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "deleteHandlerInstallationRequest")
    public DeleteHandler deleteHandlerInstallationRequest(DefaultParseStrategy<InstallationRequest> installationRequestDefaultParseStrategy) {
        return new DeleteHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(INSTALLATION_FOR_EVENTBUS_PATH), installationRequestDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "summaryHandler")
    public SummaryHandler summaryHandler(DefaultParseStrategy<SummaryRequest> summaryRequestDefaultParseStrategy) {
        return new SummaryHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(SUMMARY_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(PRODUCTION_FOR_EVENTBUS_PATH), summaryRequestDefaultParseStrategy, new SummaryCreator(),
                new EnvironmentalBenefits(jsonEnvironmental.getInteger(CO2_REDUCED_FOR_ENVIRONMENTAL_PATH), jsonEnvironmental.getInteger(TREES_SAVED_FOR_ENVIRONMENTAL_PATH)));
    }

    @Bean
    public DefaultParseStrategy<SummaryRequest> summaryRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(SummaryRequest.class);
    }
}
