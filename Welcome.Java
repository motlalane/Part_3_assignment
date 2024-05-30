package part_1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Welcome extends JFrame {
    private String[] developers = new String[100];
    private String[] taskNames = new String[100];
    private String[] taskIDs = new String[100];
    private int[] taskDurations = new int[100];
    private String[] taskStatuses = new String[100];
    private int taskCounter = 0; // Counter to track task numbers

    public Welcome() {
        setTitle("EasyKanban");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the frame
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout()); // Set BorderLayout for panel

        JLabel welcomeLabel = new JLabel("Welcome to EasyKanban");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align the text horizontally
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font and size

        panel.add(welcomeLabel, BorderLayout.NORTH); // Add label to top of panel

        // Create a custom menu panel
        JPanel menuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;

        // Create and add numeric menu items
        JButton addTasksButton = new JButton("1) Add tasks");
        addTasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddTaskDialog();
            }
        });
        menuPanel.add(addTasksButton, gbc);

        JButton showReportButton = new JButton("2) Show report");
        showReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showReportMenu();
            }
        });
        menuPanel.add(showReportButton, gbc);

        JButton quitButton = new JButton("3) Quit");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Quit the application
                System.exit(0);
            }
        });
        menuPanel.add(quitButton, gbc);

        panel.add(menuPanel, BorderLayout.CENTER); // Add menu panel to center of the main panel

        add(panel);
    }

    private void showAddTaskDialog() {
        String input = JOptionPane.showInputDialog(null, "Enter the number of tasks:");
        if (input != null) {
            try {
                int numTasks = Integer.parseInt(input);
                if (numTasks > 0) {
                    for (int i = 0; i < numTasks; i++) {
                        JFrame dialog = new JFrame("Add Task");
                        dialog.setSize(400, 300);
                        dialog.setLocationRelativeTo(null);
                        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
                        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                        JTextField taskNameField = new JTextField();
                        JTextField taskDescriptionField = new JTextField();
                        JTextField developerDetailsField = new JTextField();
                        JTextField durationField = new JTextField();

                        JComboBox<String> taskStatusComboBox = new JComboBox<>(new String[]{"To Do", "Done", "Doing"});

                        panel.add(new JLabel("Task Name:"));
                        panel.add(taskNameField);
                        panel.add(new JLabel("Task Description:"));
                        panel.add(taskDescriptionField);
                        panel.add(new JLabel("Developer Details:"));
                        panel.add(developerDetailsField);
                        panel.add(new JLabel("Task Duration (hours):"));
                        panel.add(durationField);
                        panel.add(new JLabel("Task Status:"));
                        panel.add(taskStatusComboBox);

                        JButton submitButton = new JButton("Submit");
                        submitButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String taskName = taskNameField.getText();
                                String taskDescription = taskDescriptionField.getText();
                                String developerDetails = developerDetailsField.getText();
                                String durationInput = durationField.getText();
                                String taskStatus = (String) taskStatusComboBox.getSelectedItem();

                                if (taskName.isEmpty() || taskDescription.isEmpty() || developerDetails.isEmpty() || durationInput.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "All fields are required.");
                                    return;
                                }

                                int taskDuration;
                                try {
                                    taskDuration = Integer.parseInt(durationInput);
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(null, "Please enter a valid number for task duration.");
                                    return;
                                }

                                Task task = new Task(taskName, taskDescription, developerDetails, taskDuration, taskStatus);
                                developers[taskCounter] = developerDetails;
                                taskNames[taskCounter] = taskName;
                                taskIDs[taskCounter] = task.createTaskID();
                                taskDurations[taskCounter] = taskDuration;
                                taskStatuses[taskCounter] = taskStatus;

                                taskCounter++; // Increment the task counter for the next task

                                JOptionPane.showMessageDialog(null, task.printTaskDetails());
                                JOptionPane.showMessageDialog(null, "Task successfully captured");

                                dialog.dispose(); // Close the dialog
                            }
                        });

                        panel.add(submitButton);

                        dialog.add(panel);
                        dialog.setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Number of tasks must be greater than zero.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            }
        }
    }

    private void showReportMenu() {
        JFrame reportFrame = new JFrame("Report Menu");
        reportFrame.setSize(400, 300);
        reportFrame.setLocationRelativeTo(null);
        reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel reportPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton showReportDetailsButton = new JButton("Show Full Task Report");
        showReportDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showReport();
            }
        });
        reportPanel.add(showReportDetailsButton, gbc);

        JButton searchTaskByNameButton = new JButton("Search Task by Name");
        searchTaskByNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String taskName = JOptionPane.showInputDialog(null, "Enter the task name to search:");
                if (taskName != null && !taskName.trim().isEmpty()) {
                    searchTaskByName(taskName.trim());
                } else {
                    JOptionPane.showMessageDialog(null, "Task name cannot be empty.");
                }
            }
        });
        reportPanel.add(searchTaskByNameButton, gbc);

        JButton searchTasksByDeveloperButton = new JButton("Search Tasks by Developer");
        searchTasksByDeveloperButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String developerName = JOptionPane.showInputDialog(null, "Enter the developer name to search:");
                if (developerName != null && !developerName.trim().isEmpty()) {
                    searchTasksByDeveloper(developerName.trim());
                } else {
                    JOptionPane.showMessageDialog(null, "Developer name cannot be empty.");
                }
            }
        });
        reportPanel.add(searchTasksByDeveloperButton, gbc);

        JButton deleteTaskByNameButton = new JButton("Delete Task by Name");
        deleteTaskByNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String taskName = JOptionPane.showInputDialog(null, "Enter the task name to delete:");
                if (taskName != null && !taskName.trim().isEmpty()) {
                    deleteTaskByName(taskName.trim());
                } else {
                    JOptionPane.showMessageDialog(null, "Task name cannot be empty.");
                }
            }
        });
        reportPanel.add(deleteTaskByNameButton, gbc);

        JButton showLongestTaskButton = new JButton("Show Task with Longest Hours");
        showLongestTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLongestTask();
            }
        });
        reportPanel.add(showLongestTaskButton, gbc);

        reportFrame.add(reportPanel);
        reportFrame.setVisible(true);
    }

    private void showReport() {
        StringBuilder report = new StringBuilder();
        report.append("Task Report:\n");
        for (int i = 0; i < taskCounter; i++) {
            report.append("Task Name: ").append(taskNames[i]).append("\n");
            report.append("Task ID: ").append(taskIDs[i]).append("\n");
            report.append("Developer: ").append(developers[i]).append("\n");
            report.append("Task Duration: ").append(taskDurations[i]).append(" hours\n");
            report.append("Task Status: ").append(taskStatuses[i]).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, report.toString());
    }

    private void searchTaskByName(String taskName) {
        StringBuilder result = new StringBuilder();
        result.append("Search Results for Task: ").append(taskName).append("\n");
        boolean found = false;
        for (int i = 0; i < taskCounter; i++) {
            if (taskNames[i].equalsIgnoreCase(taskName)) {
                result.append("Task Name: ").append(taskNames[i]).append("\n");
                result.append("Developer: ").append(developers[i]).append("\n");
                result.append("Task Status: ").append(taskStatuses[i]).append("\n\n");
                found = true;
            }
        }
        if (!found) {
            result.append("No task found with the name: ").append(taskName);
        }
        JOptionPane.showMessageDialog(null, result.toString());
    }

    private void searchTasksByDeveloper(String developerName) {
        StringBuilder result = new StringBuilder();
        result.append("Search Results for Developer: ").append(developerName).append("\n");
        boolean found = false;
        for (int i = 0; i < taskCounter; i++) {
            if (developers[i].equalsIgnoreCase(developerName)) {
                result.append("Task Name: ").append(taskNames[i]).append("\n");
                result.append("Task Status: ").append(taskStatuses[i]).append("\n\n");
                found = true;
            }
        }
        if (!found) {
            result.append("No tasks found assigned to developer: ").append(developerName);
        }
        JOptionPane.showMessageDialog(null, result.toString());
    }

    private void deleteTaskByName(String taskName) {
        for (int i = 0; i < taskCounter; i++) {
            if (taskNames[i].equalsIgnoreCase(taskName)) {
                // Shift elements to the left to remove the task
                System.arraycopy(taskNames, i + 1, taskNames, i, taskCounter - i - 1);
                System.arraycopy(developers, i + 1, developers, i, taskCounter - i - 1);
                System.arraycopy(taskIDs, i + 1, taskIDs, i, taskCounter - i - 1);
                System.arraycopy(taskDurations, i + 1, taskDurations, i, taskCounter - i - 1);
                System.arraycopy(taskStatuses, i + 1, taskStatuses, i, taskCounter - i - 1);
                taskCounter--;
                JOptionPane.showMessageDialog(null, "Task '" + taskName + "' deleted successfully.");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Task '" + taskName + "' not found.");
    }

    private void showLongestTask() {
        if (taskCounter == 0) {
            JOptionPane.showMessageDialog(null, "No tasks available.");
            return;
        }
        int maxDurationIndex = 0;
        for (int i = 1; i < taskCounter; i++) {
            if (taskDurations[i] > taskDurations[maxDurationIndex]) {
                maxDurationIndex = i;
            }
        }
        StringBuilder longestTask = new StringBuilder();
        longestTask.append("Task with Longest Hours:\n");
        longestTask.append("Task Name: ").append(taskNames[maxDurationIndex]).append("\n");
        longestTask.append("Developer: ").append(developers[maxDurationIndex]).append("\n");
        longestTask.append("Task Duration: ").append(taskDurations[maxDurationIndex]).append(" hours\n");
        longestTask.append("Task Status: ").append(taskStatuses[maxDurationIndex]).append("\n");

        JOptionPane.showMessageDialog(null, longestTask.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Welcome().setVisible(true);
            }
        });
    }
}
