/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import db.ConnectionFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import rbac.Session;
import rbac.SessionCollection;
import rbac.User;

public class Main {

    public static void main(String[] args) {
//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }
        try {
            ConnectionFactory connFactory =
                    new ConnectionFactory("localhost", 1433, "sa", "1111", "courier");
            
            SessionCollection m = new SessionCollection();
            
            IOrderController orderController = new OrderController(m, connFactory);
            IUserController userController = new UserController(m, connFactory);
            IAuthenticator authenticator = new Authenticator(m);
            
            Registry registry = LocateRegistry.createRegistry(1099);
                        
            registry.bind("orders", orderController);
            registry.bind("users", userController);
            registry.bind("authenticator", authenticator);
            System.out.println("Server listening...");
        } catch (Exception e) {
            System.err.println("OrderManager exception:");
            e.printStackTrace();
        }
    }
}