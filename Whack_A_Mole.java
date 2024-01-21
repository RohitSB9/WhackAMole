import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Random;

public class Whack_A_Mole extends JFrame {
    JButton[][] square = new JButton[5][5];
    JButton button1, button2;
    static JLabel label = new JLabel();
    private Thread updateWorker;
    private int counter = 0; // Counter for tracking clicks on the "X" square

    Whack_A_Mole() {
        super("Whack a Mole");
        JPanel p = new JPanel(new GridLayout(5, 5));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                square[i][j] = new JButton();
                square[i][j].addActionListener(new ButtonListener()); // Add ActionListener to each button
                p.add(square[i][j]);
            }
        }
        add(p, BorderLayout.CENTER);

        // Add the count label
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        updateCountLabel();
        add(label, BorderLayout.SOUTH);

        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    void start() {
        updateWorker = new Thread(new Runnable() {
            public void run() {
                Runnable r = new Runnable() {
                    public void run() {
                        buttonUpdate(); // call buttonUpdate every two seconds
                    }
                };
                while (true) {
                    SwingUtilities.invokeLater(r);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {

                        return;
                    }
                }
            }
        });
        updateWorker.start();
    }

    public void buttonUpdate() {
        Random r = new Random();
        int xSquare = r.nextInt(square.length); // Choose a random row for "X"
        int ySquare = r.nextInt(square[xSquare].length); // Choose a random column for "X"

        for (int i = 0; i < square.length; i++) {
            for (int j = 0; j < square[i].length; j++) {
                if (i == xSquare && j == ySquare)
                    square[i][j].setText("X"); // Set the chosen square to "X"
                else
                    square[i][j].setText("O"); // Set other squares to "O"
            }
        }
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            if (clickedButton.getText().equals("X")) {
                counter++; // Increment counter if the "X" square is clicked
                updateCountLabel(); // Update the count label
                buttonUpdate(); // Regenerate all squares randomly again
            }
        }
    }

    private void updateCountLabel() {
        label.setText("Score: " + counter); // Update the count label text
    }

    public void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) { // making sure to stop the thread after the GUI closes
            if (updateWorker.isAlive()) {
                updateWorker.interrupt();
            }
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Whack_A_Mole theTest = new Whack_A_Mole();
                theTest.start();
            }
        });
    }
}