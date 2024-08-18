package client;
import javax.swing.*;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClientGUI extends JFrame {
    private JTextArea messageArea;
    private JTextField textField;
    private JButton exitButton;
    private ChatClient client;
    private JLabel profilePicLabel;
    private JButton uploadButton;
  
    public ChatClientGUI() {
        super("Chat Application");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Profile Pic Section
        profilePicLabel = new JLabel();
        profilePicLabel.setPreferredSize(new Dimension(100, 100));
        this.add(profilePicLabel, BorderLayout.NORTH); // Use 'this' instead of 'frame'

        uploadButton = new JButton("Upload Profile Picture");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(ChatClientGUI.this); // Use 'this' for the dialog's parent component
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    ImageIcon profilePic = new ImageIcon(selectedFile.getAbsolutePath());
                    profilePicLabel.setIcon(profilePic);
                }
            }
        });

        // Styling variables
        Color backgroundColor = new Color(240, 240, 240);
        Color buttonColor = new Color(75, 75, 75);
        Color textColor = new Color(50, 50, 50);
        Font textFont = new Font("Arial", Font.PLAIN, 14);
        Font buttonFont = new Font("Arial", Font.BOLD, 12);
  
        // Apply styles to the message area
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBackground(backgroundColor);
        messageArea.setForeground(textColor);
        messageArea.setFont(textFont);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        this.add(scrollPane, BorderLayout.CENTER);
  
        // Prompt for user name
        String name = JOptionPane.showInputDialog(this, "Enter your name:", "Name Entry", JOptionPane.PLAIN_MESSAGE);
        this.setTitle("Chat Application - " + name);
  
        // Apply styles to the text field
        textField = new JTextField();
        textField.setFont(textFont);
        textField.setForeground(textColor);
        textField.setBackground(backgroundColor);
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + name + ": "
                        + textField.getText();
                client.sendMessage(message);
                textField.setText("");
            }
        });
  
        // Apply styles to the exit button and initialize it
        exitButton = new JButton("Exit");
        exitButton.setFont(buttonFont);
        exitButton.setBackground(buttonColor);
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> {
            String departureMessage = name + " has left the chat.";
            client.sendMessage(departureMessage);
  
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
  
            System.exit(0);
        });
  
        // Creating a bottom panel to hold the text field and exit button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(exitButton, BorderLayout.EAST);
        this.add(bottomPanel, BorderLayout.SOUTH);
  
        // Initialize and start the ChatClient
        try {
            this.client = new ChatClient("127.0.0.1", 5000, this::onMessageReceived);
            client.startClient();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the server", "Connection error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
  
    private void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }
  
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });
    }
}
