package util.lavaplayer;

public class TrackHelper {
    public static String readableHumanTime(long duration) {
        long seconds = duration / 1000;
        return String.format("%02dh:%02dm:%02ds time left", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }
}
