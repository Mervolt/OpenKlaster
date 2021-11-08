package com.openklaster.app.services.mqtt;

import com.macasaet.fernet.Key;
import com.macasaet.fernet.StringValidator;
import com.macasaet.fernet.Token;
import com.macasaet.fernet.Validator;
import com.openklaster.app.model.entities.measurement.MeasurementUnit;
import com.openklaster.app.model.entities.measurement.SourceMeasurementEntity;
import com.openklaster.app.services.MeasurementsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@ConditionalOnBean(MqttSubscribeListener.class)
public class MqttService implements InitializingBean {

    @Autowired
    private MeasurementsService measurementsService;

    @Value("${openklaster.mqtt.cipher-key}")
    private String cipherKeyString;

    @Value("${openklaster.mqtt.installation-id}")
    private String installationId;

    private Key cipherKey;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");

    private final Validator<String> validator = new StringValidator() {
    };

    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

    private final Map<String, Date> lastMessageTime = new HashMap<>();

    public void handle(Message<?> message) {
        try {
            String decryptedMessage = decryptMessage(message);
            String[] splittedMsg = decryptedMessage.split(";");
            Date date = getDate(splittedMsg[0]);
            if (!checkAndUpdateDate(date, installationId)) return;
            double value = getValue(splittedMsg[1]);
            SourceMeasurementEntity result = measurementsService.addSourceMeasurementEntity(value, installationId,
                    date, MeasurementUnit.kW);
            logger.debug(String.format("Added measurement from mqtt: %s\nOriginal Message: %s", result.toString(), decryptedMessage));
        } catch (Exception e) {
            logger.error("Error during handling mqtt message", e);
            throw e;
        }
    }

    private String decryptMessage(Message<?> message) {
        String payload = (String) message.getPayload();
        Token encryptedToken = Token.fromString(payload);
        return validator.validateAndDecrypt(cipherKey, encryptedToken);
    }

    private double getValue(String value) {
        double parsed = Double.parseDouble(value);
        if (parsed > 0) {
            return 0;
        } else {
            return -parsed / 1000;
        }
    }

    private Date getDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException exc) {
            return new Date();
        }
    }

    private boolean checkAndUpdateDate(Date date, String installationId) {
        Date lastDate = lastMessageTime.get(installationId);
        if (lastDate == null || lastDate.before(Date.from(Instant.now().minusSeconds(60 * 5L)))) {
            updateLastMessageTime(installationId, date);
            return true;
        } else {
            return false;
        }
    }

    private synchronized void updateLastMessageTime(String installationId, Date date) {
        this.lastMessageTime.put(installationId, date);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.cipherKey = new Key(cipherKeyString);
    }


}
