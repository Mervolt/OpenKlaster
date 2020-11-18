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
import com.openklaster.common.SuperVerticleConfig;
import io.vertx.core.json.JsonObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
@ComponentScan
public class APIVerticleConfig extends SuperVerticleConfig {
    private JsonObject jsonConfig;
    private JsonObject jsonHttpEndpointRoute;
    private JsonObject jsonEventBusRoute;
    private JsonObject jsonEnvironmental;
    private Integer apiVersion;

    public APIVerticleConfig() {
        JSONParser parser = new JSONParser();
        try {
            InputStream configStream = new ClassPathResource(configPath).getInputStream();
            Object object = parser.parse(new InputStreamReader(configStream));
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
    @Bean(name = "PostCoreCommunicationHandlerLogin")
    public PostCoreCommunicationHandler PostCoreCommunicationHandlerLogin(DefaultParseStrategy<Login> loginDefaultParseStrategy) {
        return new PostCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.LOGIN_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.LOGIN, loginDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Login> loginDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Login.class);
    }

    @Autowired
    @Bean(name = "PostCoreCommunicationHandlerRegister")
    public PostCoreCommunicationHandler PostCoreCommunicationHandlerRegister(DefaultParseStrategy<Register> registerDefaultParseStrategy) {
        return new PostCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.USER_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.REGISTER, registerDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Register> registerDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Register.class);
    }

    @Autowired
    @Bean(name = "PostCoreCommunicationHandlerUsername")
    public PostCoreCommunicationHandler PostCoreCommunicationHandlerUsername(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new PostCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.TOKEN_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.GENERATE_TOKEN, usernameDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Username> usernameDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Username.class);
    }

    @Autowired
    @Bean(name = "PostCoreCommunicationHandlerPostInstallation")
    public PostCoreCommunicationHandler PostCoreCommunicationHandlerPostInstallation(DefaultParseStrategy<PostInstallation> postInstallationDefaultParseStrategy) {
        return new PostCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.INSTALLATION_FOR_EVENTBUS_PATH), postInstallationDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<PostInstallation> PostInstallationDefaultParseStrategy() {
        return new DefaultParseStrategy<>(PostInstallation.class);
    }

    @Autowired
    @Bean(name = "PostCoreCommunicationHandlerMeasurementPowerConsumed")
    public PostCoreCommunicationHandler PostCoreCommunicationHandlerMeasurementPowerConsumed(DefaultParseStrategy<MeasurementPower> postMeasurementPowerDefaultParseStrategy) {
        return new PostCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.POWER_CONSUMPTION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.CONSUMPTION_FOR_EVENTBUS_PATH), postMeasurementPowerDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementPower> postMeasurementPowerDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementPower.class);
    }

    @Autowired
    @Bean(name = "PostCoreCommunicationHandlerMeasurementPowerProduced")
    public PostCoreCommunicationHandler PostCoreCommunicationHandlerMeasurementPowerProduced(DefaultParseStrategy<MeasurementPower> postMeasurementPowerDefaultParseStrategy) {
        return new PostCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.POWER_PRODUCTION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.PRODUCTION_FOR_EVENTBUS_PATH), postMeasurementPowerDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "PostCoreCommunicationHandlerMeasurementEnergyConsumed")
    public PostCoreCommunicationHandler PostCoreCommunicationHandlerMeasurementEnergyConsumed(DefaultParseStrategy<MeasurementEnergy> postMeasurementEnergyDefaultParseStrategy) {
        return new PostCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.ENERGY_CONSUMED_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.CONSUMPTION_FOR_EVENTBUS_PATH), postMeasurementEnergyDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "PostCoreCommunicationHandlerMeasurementEnergyProduced")
    public PostCoreCommunicationHandler PostCoreCommunicationHandlerMeasurementEnergyProduced(DefaultParseStrategy<MeasurementEnergy> postMeasurementEnergyDefaultParseStrategy) {
        return new PostCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.ENERGY_PRODUCED_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.PRODUCTION_FOR_EVENTBUS_PATH), postMeasurementEnergyDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementEnergy> postMeasurementEnergyDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementEnergy.class);
    }

    @Autowired
    @Bean(name = "PutCoreCommunicationHandlerUpdateUser")
    public PutCoreCommunicationHandler PutCoreCommunicationHandlerUpdateUser(DefaultParseStrategy<UpdateUser> putUpdateUserDefaultParseStrategy) {
        return new PutCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.USER_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.UPDATE_USER, putUpdateUserDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<UpdateUser> putUpdateUserParseStrategy() {
        return new DefaultParseStrategy<>(UpdateUser.class);
    }

    @Autowired
    @Bean(name = "PutCoreCommunicationHandlerInstallation")
    public PutCoreCommunicationHandler PutCoreCommunicationHandlerInstallation(DefaultParseStrategy<Installation> putInstallationDefaultParseStrategy) {
        return new PutCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.INSTALLATION_FOR_EVENTBUS_PATH), putInstallationDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Installation> putInstallationDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Installation.class);
    }


    @Autowired
    @Bean(name = "GetCoreCommunicationHandlerUser")
    public GetCoreCommunicationHandler GetCoreCommunicationHandlerUser(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new GetCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.USER_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.INFO, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "GetCoreCommunicationHandlerInstallationRequest")
    public GetCoreCommunicationHandler GetCoreCommunicationHandlerInstallationRequest(DefaultParseStrategy<InstallationRequest> installationRequestDefaultParseStrategy) {
        return new GetCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.INSTALLATION_FOR_EVENTBUS_PATH), installationRequestDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<InstallationRequest> installationRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(InstallationRequest.class);
    }

    @Autowired
    @Bean(name = "GetCoreCommunicationHandlerAllInstallationsRequest")
    public GetCoreCommunicationHandler GetCoreCommunicationHandlerAllInstallations(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new GetCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.ALL_INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.INSTALLATION_FOR_EVENTBUS_PATH), EventbusMethods.GET_ALL, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "GetCoreCommunicationHandlerMeasurementPowerRequestConsumption")
    public GetCoreCommunicationHandler GetCoreCommunicationHandlerMeasurementPowerConsumption(DefaultParseStrategy<MeasurementPowerRequest> measurementPowerRequestDefaultParseStrategy) {
        return new GetCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.POWER_CONSUMPTION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.CONSUMPTION_FOR_EVENTBUS_PATH), measurementPowerRequestDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "GetCoreCommunicationHandlerMeasurementPowerRequestProduction")
    public GetCoreCommunicationHandler GetCoreCommunicationHandlerMeasurementPowerProduction(DefaultParseStrategy<MeasurementPowerRequest> measurementPowerRequestDefaultParseStrategy) {
        return new GetCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.POWER_PRODUCTION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.PRODUCTION_FOR_EVENTBUS_PATH), measurementPowerRequestDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementPowerRequest> measurementPowerRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementPowerRequest.class);
    }

    @Autowired
    @Bean(name = "GetCoreCommunicationHandlerMeasurementEnergyRequestConsumption")
    public GetCoreCommunicationHandler GetCoreCommunicationHandlerMeasurementEnergyConsumption(DefaultParseStrategy<MeasurementEnergyRequest> measurementEnergyRequestDefaultParseStrategy) {
        return new GetCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.ENERGY_CONSUMED_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.CONSUMPTION_FOR_EVENTBUS_PATH), measurementEnergyRequestDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "GetCoreCommunicationHandlerMeasurementEnergyRequestProduction")
    public GetCoreCommunicationHandler GetCoreCommunicationHandlerMeasurementEnergyProduction(DefaultParseStrategy<MeasurementEnergyRequest> measurementEnergyRequestDefaultParseStrategy) {
        return new GetCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.ENERGY_PRODUCED_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.PRODUCTION_FOR_EVENTBUS_PATH), measurementEnergyRequestDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<MeasurementEnergyRequest> measurementEnergyRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(MeasurementEnergyRequest.class);
    }

    @Autowired
    @Bean(name = "DeleteCoreCommunicationHandlerUsernameToken")
    public DeleteCoreCommunicationHandler DeleteCoreCommunicationHandlerUsernameToken(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new DeleteCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.TOKEN_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.DELETE_TOKEN, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "DeleteCoreCommunicationHandlerUsernameAllTokens")
    public DeleteCoreCommunicationHandler DeleteCoreCommunicationHandlerUsernameAllTokens(DefaultParseStrategy<Username> usernameDefaultParseStrategy) {
        return new DeleteCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.ALL_TOKENS_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.USER_FOR_EVENTBUS_PATH), EventbusMethods.DELETE_ALL_TOKENS, usernameDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "DeleteCoreCommunicationHandlerInstallationRequest")
    public DeleteCoreCommunicationHandler DeleteCoreCommunicationHandlerInstallationRequest(DefaultParseStrategy<InstallationRequest> installationRequestDefaultParseStrategy) {
        return new DeleteCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.INSTALLATION_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.INSTALLATION_FOR_EVENTBUS_PATH), installationRequestDefaultParseStrategy);
    }

    @Autowired
    @Bean(name = "SummaryCoreCommunicationHandler")
    public SummaryCoreCommunicationHandler SummaryCoreCommunicationHandler(DefaultParseStrategy<SummaryRequest> summaryRequestDefaultParseStrategy) {
        return new SummaryCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.SUMMARY_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.PRODUCTION_FOR_EVENTBUS_PATH), summaryRequestDefaultParseStrategy, new SummaryCreator(),
                new EnvironmentalBenefits(jsonEnvironmental.getInteger(EnvironmentalBenefitsHolder.CO2_REDUCED_FOR_ENVIRONMENTAL_PATH),
                        jsonEnvironmental.getInteger(EnvironmentalBenefitsHolder.TREES_SAVED_FOR_ENVIRONMENTAL_PATH)));
    }

    @Autowired
    @Bean(name = "ChartApiHandler")
    public GetCoreCommunicationHandler chartHandler(DefaultParseStrategy<Temporary> temporaryDefaultParseStrategy) {
        return new GetCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.CHART_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.FILE_REPOSITORY_FOR_EVENTBUS_PATH), "charts", temporaryDefaultParseStrategy);
    }

    @Bean
    public DefaultParseStrategy<Temporary> temporaryDefaultParseStrategy() {
        return new DefaultParseStrategy<>(Temporary.class);
    }

    @Autowired
    @Bean(name = "CredentialsApiHandler")
    public CredentialsApiHandler CredentialsApiHandler() {
        return new CredentialsApiHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute
                .getString(HttpResourceHolder.MANUFACTURER_CREDENTIALS_FOR_HTTP_ENDPOINT_PATH)),
                jsonConfig.getJsonObject(HttpResourceHolder.MANUFACTURER_CREDENTIALS_FOR_ROUTE_HTTP));
    }


    @Bean
    public DefaultParseStrategy<SummaryRequest> summaryRequestDefaultParseStrategy() {
        return new DefaultParseStrategy<>(SummaryRequest.class);
    }

    @Autowired
    @Bean(name = "SelectableDatesApiHandler")
    public GetCoreCommunicationHandler selectableDatesHandler(DefaultParseStrategy<InstallationRequest> installationRequestDefaultParseStrategy) {
        return new GetCoreCommunicationHandler(buildEndpoint(apiVersion, jsonHttpEndpointRoute.getString(HttpResourceHolder.SELECTABLE_DATES_FOR_HTTP_ENDPOINT_PATH)),
                jsonEventBusRoute.getString(EventBusHolder.FILE_REPOSITORY_FOR_EVENTBUS_PATH), "selectableDates", installationRequestDefaultParseStrategy);
    }
}
