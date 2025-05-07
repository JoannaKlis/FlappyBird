import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    Image backgroundImg, flappyBirdImg, topPipeImg, bottomPipeImg;

    Bird bird;
    ArrayList<Pipe> pipes;

    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;

    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    Random random = new Random();

    public GamePanel() {
        setPreferredSize(new Dimension(GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        backgroundImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybirdbg.png"))).getImage();
        flappyBirdImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybird.png"))).getImage();
        topPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./toppipe.png"))).getImage();
        bottomPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./bottompipe.png"))).getImage();

        bird = new Bird(flappyBirdImg, GameConstants.BOARD_WIDTH/8, GameConstants.BOARD_HEIGHT/2);
        pipes = new ArrayList<>();

        placePipeTimer = new Timer(1500, e -> placePipes());
        placePipeTimer.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    private void placePipes() {
        int pipeY = (int) ((double) -GameConstants.PIPE_HEIGHT / 4 - random.nextDouble() * ((double) GameConstants.PIPE_HEIGHT / 2));
        int gap = GameConstants.BOARD_HEIGHT / 4;
        int startX = GameConstants.BOARD_WIDTH;

        Pipe topPipe = new Pipe(topPipeImg, startX, pipeY);
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg, startX, pipeY + GameConstants.PIPE_HEIGHT + gap);
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
    }

    private void move() {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.y > GameConstants.BOARD_HEIGHT) {
            gameOver = true;
        }
    }

    private boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if (gameOver) {
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
