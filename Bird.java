import java.awt.*;

public class Bird {
    public int x;
    public int y;
    public final int width = GameConstants.BIRD_WIDTH;
    public final int height = GameConstants.BIRD_HEIGHT;
    public Image img;
    public BirdType type;

    public Bird(Image img, int x, int y, BirdType type) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
