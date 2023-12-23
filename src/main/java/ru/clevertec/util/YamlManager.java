package ru.clevertec.util;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlManager {

    /**
     * gets key-values from the specified .yaml file
     *
     * @param filename file name/path
     * @return Map<String, Object> map storing key-values from the specified .yaml file
     */
    public Map<String, Object> getValue(String filename) {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(filename);

        return yaml.load(inputStream);
    }
}