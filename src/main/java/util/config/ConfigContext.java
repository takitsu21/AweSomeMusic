package util.config;

import java.io.IOException;

public class ConfigContext {
    private final ReadPropertyFile rpf;

    private String YOUTUBE_API_KEY;
    private String BOT_TOKEN;
    private String MONGO_USERNAME;
    private String MONGO_PWD;
    private String MONGO_HOST;
    private String MONGO_PORT;


    public ConfigContext(ReadPropertyFile rpf) throws IOException {
        this.rpf = rpf;
        this.rpf.loadFile();
    }

    public ReadPropertyFile getRpf() {
        return rpf;
    }

    public String getYOUTUBE_API_KEY() {
        return rpf.getProp().getProperty("YOUTUBE_API_KEY");
    }

    public String getBOT_TOKEN() {
        return rpf.getProp().getProperty("BOT_TOKEN");
    }

    public String getMONGO_USERNAME() {
        return rpf.getProp().getProperty("MONGO_USERNAME");
    }

    public String getMONGO_PWD() {
        return rpf.getProp().getProperty("MONGO_PWD");
    }

    public String getMONGO_HOST() {
        return rpf.getProp().getProperty("MONGO_HOST");
    }

    public String getMONGO_PORT() {
        return rpf.getProp().getProperty("MONGO_PORT");
    }
}
