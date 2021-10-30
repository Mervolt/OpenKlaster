package com.openklaster.app.services.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.integration.mqtt.event.MqttSubscribedEvent;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("openklaster.mqtt.enabled")
public class MqttSubscribeListener implements ApplicationListener<MqttSubscribedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(MqttSubscribeListener.class);

    @Value("${openklaster.mqtt.topic}")
    String topic;
    @Value("${openklaster.mqtt.address}")
    String address;
    @Value("${openklaster.mqtt.port}")
    String port;

    @Autowired
    MqttService mqttService;

    @Override
    public void onApplicationEvent(MqttSubscribedEvent event) {
        logger.info("Subscribed success " + event.getMessage());
    }

    @Bean
    @ConditionalOnBean(MqttSubscribeListener.class)
    public IntegrationFlow mqttInbound() {
        return IntegrationFlows.from(createAdapter())
                .handle(msg -> mqttService.handle(msg))
                .get();

    }

    private MqttPahoMessageDrivenChannelAdapter createAdapter() {
        return new MqttPahoMessageDrivenChannelAdapter(String.format("tcp://%s:%s", address, port),
                "MqttClient", topic);
    }

    @Component
    @ConditionalOnBean(MqttSubscribeListener.class)
    private static class MqttConnectionFailureListener implements ApplicationListener<MqttConnectionFailedEvent> {

        @Override
        public void onApplicationEvent(MqttConnectionFailedEvent event) {
            logger.warn("Error occured during connection" + event.getCause().getMessage());
        }
    }
}

