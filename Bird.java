import java.awt.*;

public class Bird {
    public int x;
    public int y;
    public final int width = GameConstants.BIRD_WIDTH;
    public final int height = GameConstants.BIRD_HEIGHT;
    public Image img;

    public Bird(Image img, int x, int y) {
        this.img = img;
        this.x = x;
        this.y = y;
    }
}
