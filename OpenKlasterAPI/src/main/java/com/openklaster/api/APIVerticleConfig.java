package com.openklaster.api;

import com.openklaster.api.configUtil.EnvironmentalBenefitsHolder;
import com.openklaster.api.configUtil.EventBusHolder;
import com.openklaster.api.configUtil.HttpHolder;
import com.openklaster.api.configUtil.HttpResourceHolder;
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
public class APIVerticleConfig {
    private JsonObject jsonConfig;
    private JsonObject jsonHttpEndpointRoute;
    private JsonObject jsonEventBusRoute;
    private JsonObject jsonEnvironmental;
    private Integer apiVersion;

    public APIVerticleConfig() {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader("OpenKlasterAPI\\src\\main\\resources\\config-dev.json"));
            JSONObject jsonSimple = (JSONObject) object;
            //noinspection unchecked
            this.jsonConfig = new JsonObject(jsonSimple);
            this.jsonHttpEndpointRoute = this.jsonConfig.getJsonObject(HttpHolder.HTTP_PATH)
                    .getJsonObject(HttpHolder.ENDPOINT_FOR_HTTP_PATH).getJsonObject(HttpHolder.ROUTE_FOR_ENDPOINT_HTTP_PATH);
            this.jsonEventBusRoute = this.jsonConfig.getJsonObject(EventBusHolder.CORE_PATH).getJsonObject(EventBusHolder.ROUTE_FOR_CORE_PATH);
            this.jsonEnvironmental = this.jsonConfig.getJsonObject(EnvironmentalBenefitsHolder.ENVIRONMENTAL_BENEFITS_PATH);
            this.apiVersion = this.jsonConfig.getInteger(HttpHolder.VERSION_PATH);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public String buildEndpoint(int version, String route) {
        return jsonConfig.getJsonObject(HttpHolder.HTTP_PATH).getString(HttpHolder.PREFIX_FOR_HTTP_PATH) +
                "/" + version + route;
    }

    @Autowired
    @Bean
    public Integer listeningPort() {
        return jsonConfig.getJsonObject(HttpHolder.HTTP_PATH).getInteger(HttpHolder.PORT_FOR_HTTP_PATH);
    }

    @Autowired
    @Bean(name = "postHandlerLogin")
    public PostHandler postHandlerLogin(DefaultParseStrategy<Login> loginDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.LOGIN_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.LOGIN, loginDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Login> loginDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Login.class);
    }

    @Autowired
    @Bean(name = "postHandlerRegister")
    public PostHandler postHandlerRegister(DefaultParseStrategy<Register> registerDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.USER_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.REGISTER, registerDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Register> registerDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Register.class);
    }

    @Autowired
    @Bean(name = "postHandlerUsername")
    public PostHandler postHandlerUsername(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.TOKEN_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.GENERATE_TOKEN, usernameDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Username> usernameDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Username.class);
    }

    @Autowired
    @Bean(name = "postHandlerPostInstallation")
    public PostHandler postHandlerPostInstallation(DefaultParseStrategy<PostInstallation> postInstallationDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.INSTALLATION_FOR_EVENTBUS_PATH), postInstallationDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<PostInstallation> PostInstallationDefaultParseStrategy() {
        return new DefaultParseStrategy<>(PostInstallation.class);
    }

    @Autowired
    @Bean(name = "postHandlerMeasurementPower")
    public PostHandler postHandlerMeasurementPower(DefaultParseStrategy<MeasurementPower> postMeasurementPowerDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.POWER_CONSUMPTION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.CONSUMPTION_FOR_EVENTBUS_PATH), postMeasurementPowerDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementPower> postMeasurementPowerDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementPower.class);
    }

    @Autowired
    @Bean(name = "postHandlerMeasurementEnergyConsumed")
    public PostHandler postHandlerMeasurementEnergyConsumed(DefaultParseStrategy<MeasurementEnergy> postMeasurementEnergyDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.ENERGY_CONSUMED_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.CONSUMPTION_FOR_EVENTBUS_PATH), postMeasurementEnergyDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "postHandlerMeasurementEnergyProduced")
    public PostHandler postHandlerMeasurementEnergyProduced(DefaultParseStrategy<MeasurementEnergy> postMeasurementEnergyDefaultParseStrategy) {
        return new PostHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.ENERGY_PRODUCED_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.PRODUCTION_FOR_EVENTBUS_PATH), postMeasurementEnergyDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementEnergy> postMeasurementEnergyDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementEnergy.class);
    }

    @Autowired
    @Bean(name = "putHandlerUpdateUser")
    public PutHandler putHandlerUpdateUser(DefaultParseStrategy<UpdateUser> putUpdateUserDefaultParseStrategy) {
        return new PutHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.USER_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.UPDATE_USER, putUpdateUserDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<UpdateUser> putUpdateUserParseStrategy() {
        return new DefaultParseStrategy<>(UpdateUser.class);
    }

    @Autowired
    @Bean(name = "putHandlerInstallation")
    public PutHandler putHandlerInstallation(DefaultParseStrategy<Installation> putInstallationDefaultParseStrategy) {
        return new PutHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.INSTALLATION_FOR_EVENTBUS_PATH), putInstallationDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Installation> putInstallationDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Installation.class);
    }


    @Autowired
    @Bean(name = "getHandlerUser")
    public GetHandler getHandlerUser(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.USER_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.INFO, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "getHandlerInstallationRequest")
    public GetHandler getHandlerInstallationRequest(DefaultParseStrategy<InstallationRequest> installationRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.INSTALLATION_FOR_EVENTBUS_PATH), installationRequestDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<InstallationRequest> installationRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(InstallationRequest.class);
    }

    @Autowired
    @Bean(name = "getHandlerAllInstallationsRequest")
    public GetHandler getHandlerAllInstallations(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.ALL_INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.INSTALLATION_FOR_EVENTBUS_PATH), EventbusMethods.GET_ALL, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "getHandlerMeasurementPowerRequestConsumption")
    public GetHandler getHandlerMeasurementPowerConsumption(DefaultParseStrategy<MeasurementPowerRequest> measurementPowerRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.POWER_CONSUMPTION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.CONSUMPTION_FOR_EVENTBUS_PATH), measurementPowerRequestDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "getHandlerMeasurementPowerRequestProduction")
    public GetHandler getHandlerMeasurementPowerProduction(DefaultParseStrategy<MeasurementPowerRequest> measurementPowerRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.POWER_PRODUCTION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.PRODUCTION_FOR_EVENTBUS_PATH), measurementPowerRequestDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementPowerRequest> measurementPowerRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementPowerRequest.class);
    }

    @Autowired
    @Bean(name = "getHandlerMeasurementEnergyRequestConsumption")
    public GetHandler getHandlerMeasurementEnergyConsumption(DefaultParseStrategy<MeasurementEnergyRequest> measurementEnergyRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.ENERGY_CONSUMED_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.CONSUMPTION_FOR_EVENTBUS_PATH), measurementEnergyRequestDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "getHandlerMeasurementEnergyRequestProduction")
    public GetHandler getHandlerMeasurementEnergyProduction(DefaultParseStrategy<MeasurementEnergyRequest> measurementEnergyRequestDefaultParseStrategy) {
        return new GetHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.ENERGY_PRODUCED_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.PRODUCTION_FOR_EVENTBUS_PATH), measurementEnergyRequestDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementEnergyRequest> measurementEnergyRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementEnergyRequest.class);
    }

    @Autowired
    @Bean(name = "deleteHandlerUsernameToken")
    public DeleteHandler deleteHandlerUsernameToken(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new DeleteHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.TOKEN_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.DELETE_TOKEN, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "deleteHandlerUsernameAllTokens")
    public DeleteHandler deleteHandlerUsernameAllTokens(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new DeleteHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.ALL_TOKENS_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.DELETE_ALL_TOKENS, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "deleteHandlerInstallationRequest")
    public DeleteHandler deleteHandlerInstallationRequest(DefaultParseStrategy<InstallationRequest> installationRequestDefaultParseStrategy) {
        return new DeleteHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.INSTALLATION_FOR_EVENTBUS_PATH), installationRequestDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "summaryHandler")
    public SummaryHandler summaryHandler(DefaultParseStrategy<SummaryRequest> summaryRequestDefaultParseStrategy) {
        return new SummaryHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.SUMMARY_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.PRODUCTION_FOR_EVENTBUS_PATH), summaryRequestDefaultParseStrategy, new SummaryCreator(),
                new EnvironmentalBenefits(jsonEnvironmental.getInteger(EnvironmentalBenefitsHolder.CO2_REDUCED_FOR_ENVIRONMENTAL_PATH),
                        jsonEnvironmental.getInteger(EnvironmentalBenefitsHolder.TREES_SAVED_FOR_ENVIRONMENTAL_PATH)));
    }

    @Bean
    public DefaultParseStrategy<SummaryRequest> summaryRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(SummaryRequest.class);
    }
}
