
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;
import java.awt.event.*;

public class Frame extends JFrame {
    // Philosopher[] philosophers = new Philosopher[6];
    Vector<MonitorPhilosopher> monitorPhilosophers = new Vector<>(6);
    // Fork[] forks = new Fork[5];
    String dinnerMode;

    public Frame() throws IOException {
        setTitle("My Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 800));
        setResizable(false);

        // Create a toolbar and add it to the frame
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false); // Make the toolbar non-draggable
        toolbar.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add some padding
        toolbar.setBackground(new Color(66, 133, 244)); // Change the background color
        add(toolbar, BorderLayout.PAGE_START);

        // Create Panel below toolbar
        Panel panel = new Panel();
        panel.setBackground(new Color(220, 225, 230));

        // Add the start button to the left side of the toolbar
        JButton startButton = new JButton("Start");

        startButton.setFocusPainted(false); // Remove the focus border
        startButton.setToolTipText("Start the process"); // Add a tooltip
        startButton.setBorderPainted(false); // Remove the border
        startButton.setBackground(new Color(66, 133, 244)); // Change the background color
        startButton.setForeground(new Color(255, 255, 255)); // Change the text color
        startButton.addMouseListener(new ButtonHoverEffect()); // Add a hover effect
        toolbar.add(startButton);

        // Add the stop button to the left side of the toolbar
        JButton stopButton = new JButton("Stop");
        stopButton.setFocusPainted(false); // Remove the focus border
        stopButton.setToolTipText("Stop the process"); // Add a tooltip
        stopButton.setBorderPainted(false); // Remove the border
        stopButton.setBackground(new Color(66, 133, 244)); // Change the background color
        stopButton.setForeground(new Color(255, 255, 255)); // Change the text color
        stopButton.addMouseListener(new ButtonHoverEffect()); // Add a hover effect
        toolbar.add(stopButton);

        // Add a spacer to the right side of the toolbar to move the dropdown to the
        // right
        toolbar.add(Box.createHorizontalGlue());

        // Added a dropdown menu for way of solving the dining philosopher problem.
        String[] typeOptions = { "Semaphore Dinner", "Monitor Dinner" };
        JComboBox<String> dinnerTypes = new JComboBox<>(typeOptions);
        dinnerTypes.setFocusable(false); // Remove the focus border
        dinnerTypes.setMaximumSize(new Dimension(120, 30)); // Limit the size of the dropdown
        dinnerTypes.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 1)); // Add a border
        dinnerTypes.setBackground(new Color(66, 133, 244)); // Change the background color
        dinnerTypes.setForeground(new Color(255, 255, 255)); // Change the text color
        dinnerTypes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                if (isSelected) {
                    renderer.setBackground(new Color(200, 220, 255));
                } else {
                    renderer.setBackground(new Color(66, 133, 244));
                }
                return renderer;
            }
        });
        // Add an ActionListener to the JComboBox
        dinnerTypes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) dinnerTypes.getSelectedItem();
                System.out.println("User selected: " + selectedOption);
                dinnerMode = selectedOption;
            }
        });

        // String[] options = { "Select number of ticks per second", "1", "3", "5", "10"
        // };
        String[] options = { "1", "3", "5", "10" };
        JComboBox<String> ticksPerSecondDropdown = new JComboBox<>(options);

        // ticksPerSecondDropdown.setSelectedItem("Select number of ticks per second");
        // // Set the initial value as selected but not selectable
        ticksPerSecondDropdown.setMaximumSize(new Dimension(120, 30)); // Limit the size of the dropdown
        ticksPerSecondDropdown.setFocusable(false); // Remove the focus border
        ticksPerSecondDropdown.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 1)); // Add a border
        ticksPerSecondDropdown.setBackground(new Color(66, 133, 244)); // Change the background color
        ticksPerSecondDropdown.setForeground(new Color(255, 255, 255)); // Change the text color
        ticksPerSecondDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                if (isSelected) {
                    renderer.setBackground(new Color(200, 220, 255));
                } else {
                    renderer.setBackground(new Color(66, 133, 244));
                }
                return renderer;
            }
        });
        // Create the output area and add it to the frame
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);

        // Create a JScrollPane and add the output area to it
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Set the preferred size of the scroll pane
        scrollPane.setPreferredSize(new Dimension(200, 400));

        // Add the scroll pane to the frame
        this.add(scrollPane, BorderLayout.EAST);
        // Create 5 Forks
        // for (int index = 0; index < 5; index++) {
        //     forks[index] = new Fork();
        // }

        // Create the 5 Philosophers
        panel.setLayout(null);
        String selectedValue = (String) ticksPerSecondDropdown.getSelectedItem();
        int selectedInt = Integer.parseInt(selectedValue);
        // for (int index = 1; index <= 5; index++) {
        //     if (dinnerMode == "Monitor Dinner") {
        //         monitorPhilosophers.add(index, new MonitorPhilosopher(index, panel, outputArea, selectedInt, this));
        //     } else if (dinnerMode == "Semaphore Dinner") {
        //         philosophers[index] = new Philosopher(index, panel, outputArea, selectedInt, this, dinnerMode);
        //     }
        // }

        Thread threads[] = new Thread[6];

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // for (int index = 1; index <= 5; index++) {
                //     if (dinnerMode == "Monitor Dinner") {
                //         threads[index] = new Thread(monitorPhilosophers.get(index));
                //         threads[index].start();
                //     } else if (dinnerMode == "Semaphore Dinner") {
                //         threads[index] = new Thread(philosophers[index]);
                //         threads[index].start();
                //     }
                // }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Thread threads[] = new Thread[6];
                for (int index = 1; index <= 5; index++) {
                    threads[index].stop();
                }
            }
        });

        toolbar.add(ticksPerSecondDropdown);
        toolbar.add(dinnerTypes);
        this.add(panel);
    }

    // public Fork getFork(int index) {
    //     return forks[index];
    // }
}
