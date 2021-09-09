package util.config;

import java.io.IOException;

public class ConfigContext {
    private final ReadPropertyFile rpf;

    private String YOUTUBE_API_KEY;
    private String BOT_TOKEN;

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
}
