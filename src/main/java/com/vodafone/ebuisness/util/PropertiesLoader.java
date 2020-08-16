package com.vodafone.ebuisness.util;

import com.vodafone.ebuisness.configuration.PropertiesMapping;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class PropertiesLoader {

    private Map<PropertiesMapping, String> propertiesMap;

    PropertiesLoader() {

        propertiesMap = new HashMap<>();
        propertiesMap.put(PropertiesMapping.MARKETING_MAIL
                , "src/main/resources/Mail profiles/Marketing.properties");
        propertiesMap.put(PropertiesMapping.PAYPAL_SANDBOX
                , "src/main/resources/Paypal details/PayPalSandbox.properties");

    }


    public Properties loadProperties(PropertiesMapping prop) {

        Properties properties = new Properties();
        FileReader reader = null;

        try {
            reader = new FileReader((String) propertiesMap.get(prop));
            properties.load(reader);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }


}
