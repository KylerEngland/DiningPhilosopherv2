
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import java.awt.event.*;

public class Frame extends JFrame {

    SemaphorePhilosopher[] semPhilosophers = new SemaphorePhilosopher[5];
    int State[] = new int[5];
    String dinnerMode;
    MonitorPhilosopher[] mPhilosophers = new MonitorPhilosopher[5];
    ReentrantLock[] forks = new ReentrantLock[5];

    public Frame() throws IOException {

        // Create a lock object for each fork
        for (int i = 0; i < 5; i++) {
            forks[i] = new ReentrantLock();
        }

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
        String[] typeOptions = { "Select Mode", "Semaphore Dinner", "Monitor Dinner"};
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

        String[] tickOptions = { "1", "3", "5", "10" };
        JComboBox<String> ticksPerSecondDropdown = new JComboBox<>(tickOptions);
        
        // Set a default value
        String defaultTickOption = "1";
        ticksPerSecondDropdown.setSelectedItem(defaultTickOption);
        
        // Get the selected value, or use the default if no value is selected
        String selectedValue;
        if (ticksPerSecondDropdown.getSelectedItem() != null) {
            selectedValue = (String) ticksPerSecondDropdown.getSelectedItem();
        } else {
            selectedValue = defaultTickOption;
        }
        
        int selectedInt = Integer.parseInt(selectedValue);
        
        
        // Create the output area and add it to the frame
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);

        dinnerTypes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) dinnerTypes.getSelectedItem();
                System.out.println("User selected: " + selectedOption);
                dinnerMode = selectedOption;
                for (int index = 0; index < 5; index++) {
                    if (dinnerMode == "Monitor Dinner") {
                        mPhilosophers[index] = new MonitorPhilosopher(index, panel, selectedInt, outputArea, forks[index]);
                    } else if (dinnerMode == "Semaphore Dinner") {
                        semPhilosophers[index] = new SemaphorePhilosopher(index, panel, selectedInt, outputArea);
                    }
                }
            }
        });

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
        ticksPerSecondDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = (String) ticksPerSecondDropdown.getSelectedItem();
                int selectedInt = Integer.parseInt(selectedValue);
                for (int index = 0; index < 5; index++) {
                    if (dinnerMode == "Monitor Dinner") {
                        mPhilosophers[index].setTicksPerSecond(selectedInt);
                    } else if (dinnerMode == "Semaphore Dinner") {
                        semPhilosophers[index].setTicksPerSecond(selectedInt);
                    }
                }
            }
        });
        
        

        // Create a JScrollPane and add the output area to it
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Set the preferred size of the scroll pane
        scrollPane.setPreferredSize(new Dimension(200, 400));

        // Add the scroll pane to the frame
        this.add(scrollPane, BorderLayout.EAST);

        // Create the 5 Philosophers
        panel.setLayout(null);


        Thread threads[] = new Thread[5];

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int tickSpeed = selectedInt;
                if(dinnerMode == "Monitor Dinner"){
                    for (int index = 0; index < 5; index++) {
                        mPhilosophers[index].setTicksPerSecond(tickSpeed);
                    }
                }
                if(dinnerMode == "Semaphore Dinner"){
                    for (int index = 0; index < 5; index++) {
                        semPhilosophers[index].setTicksPerSecond(tickSpeed);
                    }
                }
                for (int index = 0; index < 5; index++) {
                    if (dinnerMode == "Monitor Dinner") {
                        threads[index] = new Thread(mPhilosophers[index]);                       
                        threads[index].start();
                    } else if (dinnerMode == "Semaphore Dinner") {
                        threads[index] = new Thread(semPhilosophers[index]);
                        threads[index].start();
                    }
                }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Thread threads[] = new Thread[6];
                for (int index = 0; index < 5; index++) {
                    threads[index].stop();
                }
            }
        });

        toolbar.add(ticksPerSecondDropdown);
        toolbar.add(dinnerTypes);
        this.add(panel);
    }

    public SemaphorePhilosopher getSemaphorePhilosopher(int i) {
        return semPhilosophers[i];
    }
}
