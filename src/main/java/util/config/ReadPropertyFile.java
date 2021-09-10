package util.config;

import java.io.IOException;
import java.util.Properties;

public class ReadPropertyFile {
    private final Properties prop;

    public ReadPropertyFile(Properties prop) {
        this.prop = prop;
    }

    public ReadPropertyFile() {
        this(new Properties());
    }

    public void loadFile() throws IOException {
        prop.load(ReadPropertyFile.class.getClassLoader().getResourceAsStream("config.properties"));
    }

    public Properties getProp() {
        if (prop == null) {
            try {
                loadFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop;
    }


}
