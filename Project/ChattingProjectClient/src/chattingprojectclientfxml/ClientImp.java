/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattingprojectclientfxml;

import DTO.UserDTO;
import chatting.CallBack;
import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javafx.scene.paint.Color;

/**
 *
 * @author Shimaa
 */
public class ClientImp extends UnicastRemoteObject implements CallBack, Serializable{
    
    SignInController signIn;
    SignUpController signUp;
    MainScreenController mainScreen;

    /**
     *
     * @param signIn
     */
    public void setSignIn(SignInController signIn) {
        this.signIn = signIn;
    }

    /**
     *
     * @param signUp
     */
    public void setSignUp(SignUpController signUp) {
        this.signUp = signUp;
    }

    /**
     *
     * @param mainScreen
     */
    public void setMainScreen(MainScreenController mainScreen) {
        this.mainScreen = mainScreen;
    }
    
    /**
     *
     * @param signIn
     * @throws RemoteException
     */
    public ClientImp(SignInController signIn) throws RemoteException{
    
        this.signIn = signIn;
    }
    
    /**
     *
     * @param signUp
     * @throws RemoteException
     */
    public ClientImp(SignUpController signUp) throws RemoteException{
    
        this.signUp = signUp;
    }
    
    /**
     *
     * @param mainScreen
     * @throws RemoteException
     */
    public ClientImp(MainScreenController mainScreen) throws RemoteException{
    
        this.mainScreen = mainScreen;
    }

    /**
     *
     * @param msg
     * @param receiver
     * @param sender
     * @param color
     * @param size
     * @param family
     * @throws RemoteException
     * use mainScreen object from MianController to call setMessage Method
     */
    @Override
    public void recieve(String msg,int receiver, int sender, String color, int size, String family) throws RemoteException {
      
      mainScreen.setMessage(msg, receiver, sender, color, size, family);

    }

    /**
     *
     * @param msg
     * @param groupId
     * @param sender
     * @param color
     * @param size
     * @param family
     * @throws RemoteException
     * use mainScreen object from MianController to call setMessageGroup Method
     */
    @Override
    public void recieveGroup(String msg,int groupId, int sender, String color, int size, String family) throws RemoteException {
      
      mainScreen.setMessageGroup(msg, groupId, sender, color, size, family);

    }
    
    /**
     *
     * @throws RemoteException
     * use mainScreen object from MianController to call resetFriendsList Method
     */
    @Override
    public void resetFriendList() throws RemoteException{
    
        mainScreen.resetFriendsList();
    }
    
    /**
     *
     * @throws Exception
     * use mainScreen object from MianController to call resetChat Method
     */
    @Override
    public void resetChat() throws Exception{
    
        mainScreen.resetChat();
    }

    /**
     *
     * @param ip
     * @param receiver
     * @param sender
     * @throws Exception
     * use mainScreen object from MianController to call downloadFile Method
     */
    @Override
    public void receiveFile(String ip, int receiver, int sender) throws Exception {
        mainScreen.downloadFile(ip, receiver, sender);
    }
    
    /**
     *
     * @param user1
     * @throws RemoteException
     * use mainScreen object from MianController to call requestRespond Method
     */
    @Override
    public void requestRespond(UserDTO user1) throws RemoteException {
        
        mainScreen.requestRespond(user1);
        System.out.println("requestRespond in chatting client impl");
        System.out.println(user1.getId());
        
    }
    
    /**
     *
     * @param msg
     * use mainScreen object from MianController to call notifyMe Method
     */
    @Override
    public void notifyUsers(String msg){
        
        mainScreen.notifyMe(msg);
    }
    
    
}
