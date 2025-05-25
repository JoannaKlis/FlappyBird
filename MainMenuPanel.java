import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MainMenuPanel extends JPanel {
    private final Image background;

    public MainMenuPanel(ActionListener birdSelector) {
        setLayout(null);
        setPreferredSize(new Dimension(GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT));

        background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/flappybirdbg.png"))).getImage();

        JLabel title = new JLabel("Wybierz ptaka:");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBounds(90, 110, 300, 50);
        add(title);

        addBirdButton("Normalny", "/flappybird.png", BirdType.NORMAL, birdSelector, 180);
        addBirdButton("Szybki", "/flappybirdblue.png", BirdType.FAST, birdSelector, 260);
        addBirdButton("Ciężki", "/flappybirdpink.png", BirdType.HEAVY, birdSelector, 340);
    }

    private void addBirdButton(String label, String iconPath, BirdType type, ActionListener listener, int y) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(iconPath)));
        Image scaled = icon.getImage().getScaledInstance(50, 35, Image.SCALE_SMOOTH);
        JButton button = new JButton(label, new ImageIcon(scaled));
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBounds(90, y, 180, 50);
        button.addActionListener(_ -> {
            button.setEnabled(false);
            listener.actionPerformed(new java.awt.event.ActionEvent(type, ActionEvent.ACTION_PERFORMED, null));
        });
        add(button);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT, null);
    }
}
