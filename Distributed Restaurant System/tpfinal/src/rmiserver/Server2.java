package rmiserver;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import rmiinterface.RMIInterface;

public class Server2 extends UnicastRemoteObject implements RMIInterface {
    private static final long serialVersionUID = 1L;

    protected Server2() throws RemoteException {
        super();
    }

    @Override
    public String getRestaurantDetails() throws RemoteException {
        return "Server2 does not manage restaurant details.";
    }

    @Override
    public String getMenuDetails(String restaurantName) throws RemoteException {
        System.err.println("Client requested menu details for: " + restaurantName);
        switch (restaurantName) {
            case "La Cuisine":
                return "Dish: Couscous, Price: 800 DA, Ingredients: Semolina, Meat, Vegetables, Image: file:///E:/La Couscous.jpg\n" +
                       "Dish: Chorba, Price: 500 DA, Ingredients: Wheat, Tomatoes, Spices, Image: file:///E:/Chorba.jpg";
            case "Le Gourmet":
                return "Dish: Steak, Price: 1500 DA, Ingredients: Meat, Spices, Potatoes, Image: file:///E:/steak.jpg";
            case "Délices":
                return "Dish: Pizza, Price: 1200 DA, Ingredients: Flour, Cheese, Tomatoes, Image: file:///E:/Pizza.jpg";
            default:
                return "Restaurant not found.";
        }
    }

    @Override
    public String processOrder(String restaurantName, String dishName, int quantity, String clientDetails) throws RemoteException {
        return "Server2 does not handle orders.";
    }

    @Override
    public String getDeliveryOptions(String restaurantName) throws RemoteException {
        return "Server2 does not manage delivery options.";
    }

    public static void main(String[] args) {
        try {
            Naming.rebind("//localhost/MyServer2", new Server2());
            System.err.println("Server2 is ready.");
        } catch (Exception e) {
            System.err.println("Server2 exception: " + e.toString());
            e.printStackTrace();
        }
    }
}