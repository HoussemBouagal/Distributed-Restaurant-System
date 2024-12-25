package rmiclient;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.*;
import java.awt.*;
import java.io.File;

import rmiinterface.RMIInterface;

public class ClientOperation {
    private static RMIInterface look_up1;
    private static RMIInterface look_up2;
    private static RMIInterface look_up3;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Distributed Restaurant System");

            // Set window icon
            ImageIcon appIcon = new ImageIcon("restaurant-building.png");
            if (appIcon.getImage() != null) {
                frame.setIconImage(appIcon.getImage());
            } else {
                System.err.println("Window icon could not be loaded.");
            }

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.getContentPane().setBackground(new Color(245, 245, 245));

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(Color.WHITE);

            JLabel welcomeLabel = new JLabel("Welcome to our restaurant menu of Guelma", JLabel.CENTER);
            welcomeLabel.setFont(new Font("Roboto", Font.BOLD, 28));
            welcomeLabel.setForeground(new Color(70, 130, 180));
            mainPanel.add(welcomeLabel, BorderLayout.NORTH);

            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setFont(new Font("Roboto", Font.PLAIN, 16));
            outputArea.setBackground(new Color(245, 245, 245));
            JScrollPane scrollPane = new JScrollPane(outputArea);
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 15, 15));
            buttonPanel.setBackground(Color.WHITE);

            JButton searchButton = createStyledButton("", "Search", new Color(30, 144, 255));
            JButton selectDishButton = createStyledButton("", "Select Dish", new Color(40, 180, 120));
            JButton deliveryButton = createStyledButton("", "Delivery Options", new Color(255, 69, 0));

            buttonPanel.add(searchButton);
            buttonPanel.add(selectDishButton);
            buttonPanel.add(deliveryButton);

            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            frame.add(mainPanel);

            try {
                // Connect to RMI servers
                look_up1 = (RMIInterface) Naming.lookup("//localhost/MyServer1");
                look_up2 = (RMIInterface) Naming.lookup("//localhost/MyServer2");
                look_up3 = (RMIInterface) Naming.lookup("//localhost/MyServer3");
            } catch (MalformedURLException | NotBoundException | RemoteException e) {
                outputArea.setText("Error: " + e.getMessage());
                return;
            }

            // Action for the search button
            searchButton.addActionListener(e -> {
                try {
                    String restaurantDetails = look_up1.getRestaurantDetails();
                    String[] restaurants = restaurantDetails.split("\n");

                    JPanel restaurantPanel = new JPanel(new GridLayout(0, 1, 10, 10));
                    restaurantPanel.setBackground(Color.WHITE);

                    for (String restaurant : restaurants) {
                        String[] parts = restaurant.split(", Image: ");
                        String details = parts[0];
                        String imageUrl = parts.length > 1 ? parts[1].trim() : null;

                        JPanel entryPanel = new JPanel(new BorderLayout());
                        entryPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
                        entryPanel.setBackground(Color.WHITE);

                        JLabel detailsLabel = new JLabel("<html>" + details.replace(", ", "<br>") + "</html>");
                        detailsLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
                        entryPanel.add(detailsLabel, BorderLayout.CENTER);

                        if (imageUrl != null) {
                            try {
                                URL url = new URL(imageUrl);
                                ImageIcon icon = new ImageIcon(url);
                                Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                                JLabel imageLabel = new JLabel(new ImageIcon(image));
                                entryPanel.add(imageLabel, BorderLayout.WEST);
                            } catch (Exception ex) {
                                System.err.println("Error loading image: " + ex.getMessage());
                            }
                        }

                        restaurantPanel.add(entryPanel);
                    }

                    JFrame resultFrame = new JFrame("Restaurant Details");
                    resultFrame.setSize(800, 600);
                    resultFrame.add(new JScrollPane(restaurantPanel));
                    resultFrame.setVisible(true);
                } catch (RemoteException ex) {
                    outputArea.setText("Error while fetching restaurant details: " + ex.getMessage());
                }
            });

            // Action for the select dish button
            selectDishButton.addActionListener(e -> {
                try {
                    String restaurantName = JOptionPane.showInputDialog(frame, "Enter the restaurant name:", "Select a dish", JOptionPane.QUESTION_MESSAGE);

                    if (restaurantName != null && !restaurantName.isEmpty()) {
                        String menuResponse = look_up2.getMenuDetails(restaurantName);
                        outputArea.setText(menuResponse);

                        String[] dishes = menuResponse.split("\n");

                        // Create a new frame to display dishes for selection
                        JPanel dishPanel = new JPanel();
                        dishPanel.setLayout(new GridLayout(0, 3, 20, 20)); // Increase space between items for better comfort
                        dishPanel.setBackground(Color.WHITE);

                        for (String dish : dishes) {
                            String[] dishParts = dish.split(", Image: ");
                            String dishName = dishParts[0].trim();
                            String imagePath = dishParts.length > 1 ? dishParts[1].trim() : null;

                            // Create a panel for each dish with a border
                            JPanel dishEntryPanel = new JPanel();
                            dishEntryPanel.setLayout(new BorderLayout());
                            dishEntryPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
                            dishEntryPanel.setBackground(Color.WHITE);

                            // Create a panel to hold the image and button vertically
                            JPanel contentPanel = new JPanel();
                            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Stack image and button vertically
                            contentPanel.setBackground(Color.WHITE);

                            // Load and display the image if available
                            if (imagePath != null) {
                                try {
                                    // Remove 'file:///' and load the image from the local path
                                    File imageFile = new File(imagePath.replace("file:///", ""));
                                    if (imageFile.exists()) {
                                        ImageIcon dishImageIcon = new ImageIcon(imageFile.getAbsolutePath()); // Load image
                                        Image image = dishImageIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH); // Resize image to a larger size
                                        JLabel imageLabel = new JLabel(new ImageIcon(image)); // Create a label for the image
                                        contentPanel.add(imageLabel); // Add image to the panel
                                    } else {
                                        System.err.println("Image not found: " + imagePath);
                                    }
                                } catch (Exception ex) {
                                    System.err.println("Error loading image: " + ex.getMessage());
                                }
                            }

                            // Create a button with the dish name
                            JButton dishButton = new JButton(dishName);
                            dishButton.setFont(new Font("Roboto", Font.PLAIN, 16)); // Larger font for better readability
                            dishButton.setPreferredSize(new Dimension(200, 60)); // Increase button size for clarity
                            dishButton.setBackground(new Color(70, 130, 180)); // Button color
                            dishButton.setForeground(Color.WHITE); // Text color
                            dishButton.setFocusPainted(false); // Remove focus highlight
                            contentPanel.add(dishButton); // Add button below the image

                            // Set action on clicking the dish button
                            dishButton.addActionListener(dishEvent -> {
                                try {
                                    String selectedDish = dishButton.getText();
                                    String quantityStr = JOptionPane.showInputDialog(frame, "Enter the quantity:", "Dish quantity", JOptionPane.QUESTION_MESSAGE);
                                    int quantity = Integer.parseInt(quantityStr);

                                    String clientName = JOptionPane.showInputDialog(frame, "Enter your full name:", "Client information", JOptionPane.QUESTION_MESSAGE);
                                    String phoneNumber = JOptionPane.showInputDialog(frame, "Enter your phone number:", "Client information", JOptionPane.QUESTION_MESSAGE);

                                    // Prompt for delivery option
                                    String eatingOption = (String) JOptionPane.showInputDialog(
                                            frame,
                                            "Select the eating option:",
                                            "Eating option",
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            new String[]{"Delivery", "At Site"},
                                            "Delivery"
                                    );

                                    String addressdelivery = "";
                                    if ("Delivery".equals(eatingOption)) {
                                        addressdelivery = JOptionPane.showInputDialog(frame, "Enter your delivery address:", "Client information", JOptionPane.QUESTION_MESSAGE);
                                    }

                                    String orderResponse = look_up3.processOrder(
                                            restaurantName,
                                            selectedDish,
                                            quantity,
                                            "Full name: " + clientName + ", Phone number: " + phoneNumber + ", Delivery address: " + addressdelivery
                                    );

                                    outputArea.setText(orderResponse);
                                    outputArea.append("\nSelected Restaurant: " + restaurantName);
                                    outputArea.append("\nSelected Dishes: " + selectedDish + ", Quantity: " + quantity);
                                    outputArea.append("\nEating option: " + eatingOption);
                                } catch (RemoteException ex) {
                                    outputArea.setText("Error processing the order: " + ex.getMessage());
                                } catch (NumberFormatException ex) {
                                    outputArea.setText("Invalid quantity. Please enter a valid number.");
                                }
                            });

                            dishEntryPanel.add(contentPanel, BorderLayout.CENTER); // Add the panel with image and button to the main panel
                            dishPanel.add(dishEntryPanel);
                        }

                        // Create and display a frame to show dishes
                        JFrame dishFrame = new JFrame("Select a Dish");
                        dishFrame.setSize(1000, 700); // Adjust size for better visibility
                        dishFrame.add(new JScrollPane(dishPanel)); // Add scrollable panel
                        dishFrame.setVisible(true);
                    }
                } catch (RemoteException | NumberFormatException ex) {
                    outputArea.setText("Error: " + ex.getMessage());
                }
            });


            // Action for the delivery options button
            deliveryButton.addActionListener(e -> {
                try {
                    String restaurantName = JOptionPane.showInputDialog(
                            frame,
                            "Enter the restaurant name:",
                            "Delivery Options",
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (restaurantName != null && !restaurantName.isEmpty()) {
                        // Fetching delivery options from Server3
                        String deliveryOptions = look_up3.getDeliveryOptions(restaurantName);
                        outputArea.setText("Delivery Options for " + restaurantName + ":\n" + deliveryOptions);
                    } else {
                        outputArea.setText("Restaurant name cannot be empty.");
                    }
                } catch (RemoteException ex) {
                    outputArea.setText("Error while fetching delivery options: " + ex.getMessage());
                }
            });

            frame.setVisible(true);
        });
    }

    private static JButton createStyledButton(String iconPath, String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setIcon(new ImageIcon(iconPath));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        return button;
    }
}
