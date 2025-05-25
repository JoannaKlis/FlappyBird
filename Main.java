import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        MainMenuPanel menuPanel = new MainMenuPanel(e -> {
            BirdType selectedType = (BirdType) e.getSource();
            frame.getContentPane().removeAll();
            GamePanel gamePanel = new GamePanel(selectedType);
            frame.add(gamePanel);
            frame.pack();
            gamePanel.requestFocus();
            frame.revalidate();
        });

        frame.add(menuPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
