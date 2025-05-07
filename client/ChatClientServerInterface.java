package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClientServerInterface extends Remote {
    void chatUpdate(String message)
            throws RemoteException;
}
