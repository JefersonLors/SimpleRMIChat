package server;

import client.ChatClientServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatServer extends UnicastRemoteObject implements ChatServerClientInterface {
	private String serverName = "Chat-server";
	private int serverPort = 3230;
	private final List<User> userList = new ArrayList<>();

	public ChatServer(String serverName, int serverPort) throws RemoteException {
		super();
		this.serverName = serverName;
		this.serverPort = serverPort;
		initializeServer();
	}
	public ChatServer() throws RemoteException {
		super();
		initializeServer();
	}

	private void initializeServer() throws RemoteException {
		System.out.println(LocalDateTime.now() + " - Initializing chat server...");
		LocateRegistry.createRegistry(this.serverPort).rebind(this.serverName, this);
		System.out.println(LocalDateTime.now() + " - Chat server initialized");
	}
	private void sendInternalMessageToServer(String message) throws RemoteException {
		for(var user : this.userList){
			user.userReference.chatUpdate(LocalDateTime.now() + " - <server> " + message);
		}
	}

	@Override
	public void sendMessageToServer(String message, String userName) throws RemoteException {
		for(var user : this.userList){
			user.userReference.chatUpdate(LocalDateTime.now() + " - <" + userName + "> " + message);
		}
	}

	@Override
	public void registerNewChatUser(String userName, int remoteObjectPort, String remoteObjectName) throws RemoteException, NotBoundException {
		var chatUser = (ChatClientServerInterface) LocateRegistry.getRegistry(remoteObjectPort)
														  .lookup(remoteObjectName);
		this.userList.add(new User(userName, chatUser));
		sendInternalMessageToServer(userName + " joined the chat");
	}

	@Override
	public void leaveChat(String userName) throws RemoteException {
		Optional<User> userToRemove = this.userList.stream()
												   .filter(user -> user.name.equalsIgnoreCase(userName))
										           .findFirst();

		if(userToRemove.isPresent()){
			this.userList.remove(userToRemove.get());
			sendInternalMessageToServer(userName + " left the chat.");
		}
	}

	public class User{
		private final String name;
		private final ChatClientServerInterface userReference;

		public User(String name, ChatClientServerInterface userReference){
			this.name = name ;
			this.userReference = userReference;
		}
	}
}
