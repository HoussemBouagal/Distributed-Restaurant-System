package rmiserver;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import rmiinterface.RMIInterface;

public class Server1 extends UnicastRemoteObject implements RMIInterface {
    private static final long serialVersionUID = 1L;

    protected Server1() throws RemoteException {
        super();
    }

    @Override
    public String getRestaurantDetails() throws RemoteException {
        return "Restaurant 1: La Cuisine, Address: Rue A, Open: 8 AM - 10 PM, Delivery: Yes, Image: file:///E:/La%20Cuisine.jpg\n" +
               "Restaurant 2: Le Gourmet, Address: Rue B, Open: 10 AM - 8 PM, Delivery: No, Image: file:///E:/La%20Restaruant.jpg\n" +
               "Restaurant 3: Délices, Address: Rue C, Open: 9 AM - 9 PM, Delivery: Yes, Image: file:///E:/La%20Restaruant3.jpg\n";
    }


    @Override
    public String getMenuDetails(String restaurantName) throws RemoteException {
        return "Server1 does not manage menus.";
    }

    @Override
    public String processOrder(String restaurantName, String dishName, int quantity, String clientDetails) throws RemoteException {
        return "Server1 does not handle orders.";
    }

    @Override
    public String getDeliveryOptions(String restaurantName) throws RemoteException {
        return "Server1 does not manage delivery options.";
    }

    public static void main(String[] args) {
        try {
            Naming.rebind("//localhost/MyServer1", new Server1());
            System.err.println("Server1 is ready.");
        } catch (Exception e) {
            System.err.println("Server1 exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
