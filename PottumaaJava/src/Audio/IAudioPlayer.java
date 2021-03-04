package Audio;

public interface IAudioPlayer {
    void play();
    void stop();
    void close();
    float getVolume();
    void setVolume(float volume);
}
