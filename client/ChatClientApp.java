package client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ChatClientApp {
    public static void main (String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        ChatClientInterface chatClient;

        if(args.length == 2){
            chatClient = new ChatClient(args[0], Integer.parseInt(args[1]));
        }else if(args.length == 4) {
            chatClient = new ChatClient(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
        } else{
            chatClient = new ChatClient();
        }

        chatClient.getInChat();
    }
}
