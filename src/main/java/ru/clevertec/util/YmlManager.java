package ru.clevertec.util;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class YmlManager {

    /**
     * gets key-values from the specified .yaml file
     * @param filename file name/path
     * @return Map<String, Object> map storing key-values from the specified .yaml file
     */
    public static Map<String, Object> getValue(String filename) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        try (FileInputStream input = new FileInputStream(filename)) {
           return yaml.load(input);
        } catch (IOException e) {
            throw new FileNotFoundException("file "+ filename + " not found");
        }
    }
}