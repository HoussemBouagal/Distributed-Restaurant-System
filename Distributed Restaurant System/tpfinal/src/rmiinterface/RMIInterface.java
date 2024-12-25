package rmiinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIInterface extends Remote {
    // Method to fetch restaurant details
	public String getRestaurantDetails() throws RemoteException;
    
    // Method to fetch menu details based on the restaurant name
    String getMenuDetails(String restaurantName) throws RemoteException;
    
    // Method to process orders based on restaurant, dish, quantity, and client details
    String processOrder(String restaurantName, String dishName, int quantity, String clientDetails) throws RemoteException;
    
    // Method to fetch delivery options for a restaurant
    String getDeliveryOptions(String restaurantName) throws RemoteException;


	
}
