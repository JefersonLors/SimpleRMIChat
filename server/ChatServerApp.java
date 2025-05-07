package server;

import java.rmi.RemoteException;
public class ChatServerApp {
    public static void main(String[] args) throws RemoteException {
        if(args.length == 2)
            new ChatServer(args[0], Integer.parseInt(args[1]));
        else
            new ChatServer();
    }
}
