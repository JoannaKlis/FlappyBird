import java.awt.*;

public class Pipe {
    public int x;
    public int y;
    public final int width = GameConstants.PIPE_WIDTH;
    public final int height = GameConstants.PIPE_HEIGHT;
    public Image img;
    boolean passed = false;

    public Pipe(Image img, int x, int y) {
        this.img = img;
        this.x = x;
        this.y = y;
    }

}
