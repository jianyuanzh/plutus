package com.yflog.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by vincent on 8/31/16.
 * Base settings
 */
public abstract class BaseSettings {

    private final Properties _props = new Properties();

    protected BaseSettings() {
    }

    protected BaseSettings(String path) throws IOException {
        initProps(path);
    }


    protected void initProps(final String path) throws IOException {
        _props.clear();
        _props.load(new FileInputStream(path));
    }

    public String getSetting(String key) {
        return getSetting(key, "");
    }

    public String getSetting(String key, String defVal) {
        String prop = _props.getProperty(key, defVal);
        if (prop == null) {
            prop = "";
        }
        return prop.trim();
    }

    public int getSettingInt(String key, int defVal) {
        if (!_props.containsKey(key)) {
            return defVal;
        }

        return Integer.parseInt(_props.getProperty(key).trim());
    }

}
