package client;

import server.ChatServerClientInterface;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ChatClient extends UnicastRemoteObject implements ChatClientServerInterface, ChatClientInterface {
    private final String chatClientPrefixName = "Chat_user_";
    private String chatClientName = "unknown user";
    private int chatClientPort = 9032;
    private String chatServerName = "Chat-server";
    private int chatServerPort = 3230;
    private ChatServerClientInterface chatServer;

    protected ChatClient() throws RemoteException, NotBoundException, MalformedURLException {
        super();
        initializeChat();
        connectToChatServer();
    }
    protected ChatClient(String chatClientName, int chatClientPort)
            throws RemoteException, NotBoundException, MalformedURLException {
        super();
        this.chatClientName = chatClientName;
        this.chatClientPort = chatClientPort;
        initializeChat();
        connectToChatServer();
    }
    protected ChatClient(String chatClientName, int chatClientPort, String chatServerName, int chatServerPort)
            throws RemoteException, NotBoundException, MalformedURLException {
        super();
        this.chatClientName = chatClientName;
        this.chatClientPort = chatClientPort;
        this.chatServerName = chatServerName;
        this.chatServerPort = chatServerPort;
        initializeChat();
        connectToChatServer();
    }
    private void initializeChat() throws RemoteException {
        System.out.println(LocalDateTime.now() + " - Initializing chat...");
        LocateRegistry.createRegistry(this.chatClientPort)
                      .rebind( this.chatClientPrefixName + this.chatClientName, this);
        System.out.println(LocalDateTime.now() + " - Chat initialized");
    }

    private void connectToChatServer()
            throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println(LocalDateTime.now() + " - Connecting to [" + this.chatServerName + "]...");
        this.chatServer = (ChatServerClientInterface) LocateRegistry.getRegistry(this.chatServerPort)
                                                              .lookup(this.chatServerName);

        this.chatServer.registerNewChatUser(this.chatClientName, this.chatClientPort, this.chatClientPrefixName + this.chatClientName);

        System.out.println(LocalDateTime.now() + " - Server connected");
    }

    @Override
    public void chatUpdate(String message) {
        System.out.println(message);
    }

    @Override
    public void getInChat() throws RemoteException {
        System.out.println("Type /quit to leave the chat");

        var input = new Scanner(System.in);

        var content = "";

        while(true){
            content = input.nextLine();

            if(content.equals("/quit"))
                break;

            chatServer.sendMessageToServer(content, this.chatClientName);
        }
        chatServer.leaveChat(this.chatClientName);
    }
}
