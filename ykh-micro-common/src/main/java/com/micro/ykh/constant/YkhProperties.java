package com.micro.ykh.constant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @ClassName YkhProperties
 * @Description TODO
 * @Author xiongh
 * @Date 2021/1/7 9:01
 * @Version 1.0
 **/
public class YkhProperties {

    private static final Logger logger = LogManager.getLogger(YkhProperties.class);

    private static YkhProperties config = null;
    private static ResourceBundle rb = null;
    /**
     * 配置文件名
     */
    private static final String CONFIG_FILE = "ykh";

    private YkhProperties() {
        rb = ResourceBundle.getBundle(CONFIG_FILE);
    }

    public synchronized static YkhProperties getInstance() {
        if (null == config) {
            config = new YkhProperties();
        }
        return config;
    }

    public String getValue(String key, String defaultValue) {
        String result = null;
        try {
            result = rb.getString(key);
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            result = defaultValue;
        }
        return result;
    }

    public Integer getIntValue(String key, Integer defaultValue) {

        Integer result = null;
        try {
            result = Integer.valueOf(rb.getString(key));
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            result = defaultValue;
        }
        return result;
    }

    public Boolean getBooleanValue(String key, Boolean defaultValue) {
        Boolean result = false;
        try {
            result = Boolean.valueOf(rb.getString(key));
        } catch (NullPointerException | ClassCastException | MissingResourceException e) {
            result = defaultValue;
        }
        return result;
    }

}
