

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Panel extends JPanel {
    //State Labels
    private JLabel p1StateLabel = new JLabel("Thinking");
    private JLabel p2StateLabel = new JLabel("Thinking");
    private JLabel p3StateLabel = new JLabel("Thinking");
    private JLabel p4StateLabel = new JLabel("Thinking");
    private JLabel p5StateLabel = new JLabel("Thinking");
    // Name Lables
    private JLabel p1NameLabel = new JLabel("P1");
    private JLabel p2NameLabel = new JLabel("P2");
    private JLabel p3NameLabel = new JLabel("P3");
    private JLabel p4NameLabel = new JLabel("P4");
    private JLabel p5NameLabel = new JLabel("P5");

    public Panel() throws IOException {
        // Set Font 
        p1StateLabel.setFont(new Font("Serif", Font.BOLD, 20));
        p2StateLabel.setFont(new Font("Serif", Font.BOLD, 20));
        p3StateLabel.setFont(new Font("Serif", Font.BOLD, 20));
        p4StateLabel.setFont(new Font("Serif", Font.BOLD, 20));
        p5StateLabel.setFont(new Font("Serif", Font.BOLD, 20));
        p1NameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        p2NameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        p3NameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        p4NameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        p5NameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        //Set Postion 
        p1StateLabel.setBounds(240, 80, 100, 25);
        p2StateLabel.setBounds(580, 240, 100, 25);
        p3StateLabel.setBounds(510, 480, 100, 25);
        p4StateLabel.setBounds(100, 480, 100, 25);
        p5StateLabel.setBounds(40, 240, 100, 25);
        p1NameLabel.setBounds(240, 50, 100, 25);
        p2NameLabel.setBounds(580, 210, 100, 25);
        p3NameLabel.setBounds(510, 440, 100, 25);
        p4NameLabel.setBounds(100, 440, 100, 25);
        p5NameLabel.setBounds(40, 210, 100, 25);
        // Add Labels to Panel 
        this.add(p1StateLabel);
        this.add(p2StateLabel);
        this.add(p3StateLabel);
        this.add(p4StateLabel);
        this.add(p5StateLabel);
        this.add(p1NameLabel);
        this.add(p2NameLabel);
        this.add(p3NameLabel);
        this.add(p4NameLabel);
        this.add(p5NameLabel);
        // Draw Philosophers 
        BufferedImage image = null;
        File file = new File("DiningPhilosopher\\images\\philosopher.png");
        // File file = new File("C:\\Users\\danie\\OneDrive\\Documents\\IWU\\CIS-425\\DiningPhilosopherv2\\DiningPhilosopher\\images\\philosopher.png");
        //File file = new File("C:\\Users\\danie\\OneDrive\\Documents\\IWU\\CIS-425\\DiningPhilosopher\\DiningPhilosophers\\images\\philsopher.png");
        image = ImageIO.read(file);
        Image resultingImage = image.getScaledInstance(125, 150, Image.SCALE_DEFAULT);
        JLabel label1 = new JLabel(new ImageIcon(resultingImage));
        JLabel label2 = new JLabel(new ImageIcon(resultingImage));
        JLabel label3 = new JLabel(new ImageIcon(resultingImage));
        JLabel label4 = new JLabel(new ImageIcon(resultingImage));
        JLabel label5 = new JLabel(new ImageIcon(resultingImage));
        this.setLayout(null);
        //P5
        label1.setBounds(90, 210, 125, 150);
        //P1
        label2.setBounds(290, 50, 125, 150);
        //P4
        label3.setBounds(150, 440, 125, 150);
        //P3
        label4.setBounds(420, 440, 125, 150);
        // P2
        label5.setBounds(485, 210, 125, 150);
        this.add(label1);
        this.add(label2);
        this.add(label3);
        this.add(label4);
        this.add(label5);
    }

    public void setStateText(String newState, int id){
        switch (id) {
            case 1: 
                p1StateLabel.setText(newState);
                 break;
            case 2: 
                p2StateLabel.setText(newState);
                break;
            case 3: 
                p3StateLabel.setText(newState);
                break;
            case 4: 
                p4StateLabel.setText(newState);
                break;
            case 5: 
                p5StateLabel.setText(newState);
              break;
          }
    }

    @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.BLACK);
            g2.drawOval(200, 200, 300, 300);
            g2.drawOval(310, 210, 75, 75);
            g2.drawOval(410, 290, 75, 75);
            g2.drawOval(370, 400, 75, 75);
            g2.drawOval(260, 400, 75, 75);
            g2.drawOval(220, 290, 75, 75);
        }
}
