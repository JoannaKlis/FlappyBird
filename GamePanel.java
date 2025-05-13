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
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;
    boolean gameOver = false;
    double score = 0;

    private long lastPipeTime;
    private long lastUpdateTime;

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

        lastPipeTime = System.currentTimeMillis();
        lastUpdateTime = System.currentTimeMillis();

        gameLoop = new Timer(20, this);
        gameLoop.start();
    }

    private void placePipes() {
        Random random = new Random();
        int pipeY = (int)((double) -GameConstants.PIPE_HEIGHT / 4 - random.nextDouble() * ((double) GameConstants.PIPE_HEIGHT / 2));
        int gap = GameConstants.BOARD_HEIGHT / 4;

        pipes.add(new Pipe(topPipeImg, GameConstants.BOARD_WIDTH, pipeY));
        pipes.add(new Pipe(bottomPipeImg, GameConstants.BOARD_WIDTH, pipeY + GameConstants.PIPE_HEIGHT + gap));
    }

    @Override
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
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString(gameOver ? "Game Over: " + (int)score : String.valueOf((int)score), 10, 35);
    }

    private void move() {
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000.0f;
        lastUpdateTime = currentTime;

        if (currentTime - lastPipeTime > GameConstants.PIPE_INTERVAL && !gameOver) {
            placePipes();
            lastPipeTime = currentTime;
        }

        velocityY += (int) (gravity * deltaTime * 40);
        bird.y += (int) (velocityY * deltaTime * 40);
        bird.y = Math.max(bird.y, 0);

        for (Pipe pipe : pipes) {
            pipe.x += (int) (velocityX * deltaTime * 40);

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
            }

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
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if (gameOver) {
                bird.y = GameConstants.BOARD_HEIGHT / 2;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                lastPipeTime = System.currentTimeMillis();
                lastUpdateTime = System.currentTimeMillis();
                gameLoop.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
