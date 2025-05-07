package server;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServerClientInterface extends Remote {
    void sendMessageToServer(String message, String userName)
            throws RemoteException;
    void registerNewChatUser(String userName, int remoteObjectPort, String remoteObjectName)
            throws RemoteException, NotBoundException, MalformedURLException;
    void leaveChat(String userName)
            throws RemoteException;
}
