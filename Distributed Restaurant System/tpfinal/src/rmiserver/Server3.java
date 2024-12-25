package rmiserver;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import rmiinterface.RMIInterface;

public class Server3 extends UnicastRemoteObject implements RMIInterface {
    private static final long serialVersionUID = 1L;

    protected Server3() throws RemoteException {
        super();
    }

    @Override
    public String getRestaurantDetails() throws RemoteException {
        return "Server3 does not manage restaurant details.";
    }

    @Override
    public String getMenuDetails(String restaurantName) throws RemoteException {
        return "Server3 does not manage menus.";
    }

    @Override
    public String processOrder(String restaurantName, String dishName, int quantity, String clientDetails) throws RemoteException {
        System.err.println("Client placed an order: " + quantity + "x " + dishName + " at " + restaurantName);
        System.err.println("Client details: " + clientDetails);
        
        String orderConfirmation = "Order for " + quantity + "x " + dishName + " at restaurant " + restaurantName + " confirmed.\nClient details: " + clientDetails;
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt", true));
            writer.write(orderConfirmation + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return orderConfirmation;
    }

    @Override
    public String getDeliveryOptions(String restaurantName) throws RemoteException {
        System.err.println("Client requested delivery options for: " + restaurantName);
        switch (restaurantName) {
            case "La Cuisine":
                return "Options: Delivery only.";
            case "Le Gourmet":
                return "Options: Pick-up only.";
            case "Délices":
                return "Options: Delivery and Pick-up available.";
            default:
                return "Restaurant not found.";
        }
    }

    public String collectFeedback(String feedback, String clientDetails) throws RemoteException {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("feedback.txt", true));
            writer.write("Feedback: " + feedback + "\nClient details: " + clientDetails + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Feedback successfully recorded.";
    }

    public static void main(String[] args) {
        try {
            Naming.rebind("//localhost/MyServer3", new Server3());
            System.err.println("Server3 is ready.");
        } catch (Exception e) {
            System.err.println("Server3 exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
