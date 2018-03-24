/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattingprojectserver;

import DAOImp.ChatGroupDAOImp;
import DAOImp.ChatUserDAOImp;
import DAOImp.UserDAOImp;
import DAOImp.FriendDAOImp;
import DAOImp.StatusDAOImp;
import DTO.ChatGroupDTO;
import DTO.ChatUserDTO;
import DTO.FriendshipDTO;
import DTO.StatusDTO;
import DTO.UserDTO;
import chatting.CallBack;
import chatting.Chatting;
import email.MailAPI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
//import DAOImp.UserDAOImp;

/**
 *
 * @author Shimaa
 */
public class ChattingImp extends UnicastRemoteObject implements Chatting,Serializable{
    
    //Vector<CallBack> clients = new Vector<CallBack>();
    Hashtable<Integer, CallBack> clients = new Hashtable<Integer, CallBack>();
    //int i = 1;
    int id = 0;
    
    int sendId = 0;
    
    UserDAOImp userImp = new UserDAOImp();
    FriendDAOImp friendImp = new FriendDAOImp();
    ChatGroupDAOImp groupImp = new ChatGroupDAOImp();
    ChatUserDAOImp groupMem = new ChatUserDAOImp();
    StatusDAOImp statusImp = new StatusDAOImp();
    
    public ChattingImp() throws RemoteException{}
    /**
     *
     * @param client
     * @param user
     * put the remote object of user in clients HashTable with user's id
     */
    public void register(CallBack client, UserDTO user){

        clients.put(user.getId(), client);
        
    }
    
    /**
     *
     * @param userName
     * @return
     * use object from UserDAOImp to call readByUsername method
     */
    public UserDTO logIn(String userName){
    
       return userImp.readByUsername(userName);
        
    }
    
    /**
     *
     * @param user
     * @return
     * use object from UserDAOImp to call create method
     */
    public UserDTO singUp(UserDTO user){
    
        return userImp.create(user);
    }
    /**
     *
     * @param user
     * remove remote object of user from clients HashTabel
     */
    public void unRegister(UserDTO user){
        
        clients.remove(user.getId());
        System.out.println("Client Removed");
    
    }
    
    /**
     *
     * @param id
     * @return
     * get friends of user using user's id 
     * use object from FriendDAOImp to call friendList method to get friends id of user then put them in ArrayList
     * use object from UserDAOImp to call MyFriendList method to get friends object of user
     */
    public ArrayList<UserDTO> getFriends(int id){
    
       ArrayList<Integer> arr = new ArrayList<Integer>();
       arr = friendImp.friendlist(id);
       if(arr != null){
       return userImp.MyFriendList(arr);
       }else{
           return null;
       }
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
     * get the remote object of sender and receiver of message then use this remote object to call receive method for each one
     */
    public void sendMessage(String msg, int receiver, int sender, String color, int size, String family) throws RemoteException{
    
        clients.get(receiver).recieve(msg, receiver, sender, color, size, family);
        clients.get(sender).recieve(msg, receiver, sender, color, size, family);
    }
    /**
     *
     * @param id
     * @return
     * use object from ChatGroupDAOImp to call myGroups method to get groups of user from database
     */
    public ArrayList<ChatGroupDTO> getGroups(int id){
    
        return groupImp.myGroups(id);
    }
    /**
     *
     * @param id
     * @return
     * use object from ChatUserDAOImp to call read method to get groups members from database
     */
    public ArrayList<ChatUserDTO> getGroupMembers(int id){
    
        return groupMem.read(id);
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
     * use object from ChatUserDAOImp to call read method to get groups members from database
     * select remote object of each one using their id from clients HashTable 
     * use their remote object to call receive method to send message
     */
    public void sendToGroup(String msg, int groupId, int sender, String color, int size, String family) throws RemoteException{
    
        ChatUserDAOImp chatUser = new ChatUserDAOImp();
        ArrayList<ChatUserDTO> ch = new ArrayList<ChatUserDTO>();
        ch = chatUser.read(groupId);
        for(int i = 0; i<ch.size(); i++){
            //System.out.println(clients.get(ch.get(i).getUserId()));
            if(clients.get(ch.get(i).getUserId()) != null)
        clients.get(ch.get(i).getUserId()).recieveGroup(msg, groupId, sender, color, size, family);
        }
    }
    /**
     *
     * @param user
     * @throws Exception
     * use object from UserDAOImp to call update method to update user
     */
    public void update(UserDTO user) throws Exception{
    
        userImp.update(user);
    }

    
    /**
     *
     * @param user
     * @throws Exception
     * get friends of user 
     * then select their remote object from clients HashTable using their id 
     * use this remote object to call resetFriendList method to refresh the friend list 
     */
    public void tellOnOff(UserDTO user) throws Exception{
    
       ArrayList<UserDTO> arr = new ArrayList<UserDTO>();
       arr = getFriends(user.getId());
       
       for(int i=0; i<arr.size(); i++){
       System.out.println(clients.get(arr.get(i).getId()));
           if(clients.containsKey(arr.get(i).getId())){
               System.out.println("hello");
               clients.get(arr.get(i).getId()).resetFriendList();
               
           }
       }
        
    }
    /**
     *
     * @return
     * @throws Exception
     * use StatusDAOImp object to call readAll method to get all status from database
     */
    @Override
    public ArrayList<StatusDTO> getStatus() throws Exception{
    
        ArrayList<StatusDTO> ob = new ArrayList<StatusDTO>();
        ob.addAll(statusImp.readAll());
        return ob;
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     * use StatusDAOImp object to call read method to get a certain status from database using it's id 
     */
    @Override
    public StatusDTO getStatus(int id) throws Exception{
    
        return statusImp.read(id);
        
    }
    /**
     *
     * @param chat
     * @return
     * @throws Exception
     * use ChatGroupDAOImp object to call create method to create new group in database
     */
    @Override
    public int createGroup(ChatGroupDTO chat) throws Exception{
    
       return groupImp.create(chat);
    }
    /**
     *
     * @param id
     * @param users
     * @return
     * @throws Exception
     * use ChatUserDAOImp object to call chatGroup method to set members for certain group in database using it's id
     */
    @Override
    public int chatGroup(int id,ArrayList<Integer> users) throws Exception{
    
        return groupMem.chatGroup(id, users);
    }
    /**
     *
     * @param user
     * @param members
     * @throws Exception
     * get the remote object of user from clients HashTable using user's id
     * use this remote object to call resetChat method to refresh list of groups 
     * get the remote object of members from clients HashTable using members id
     * use this remote object to call resetChat method to refresh list of groups 
     */
    @Override
    public void newGroup(UserDTO user, ArrayList<Integer> members) throws Exception{
    
       clients.get(user.getId()).resetChat();
       for(int i=0; i<members.size(); i++){
       
           if(clients.get(members.get(i))!= null){
           
               clients.get(members.get(i)).resetChat();
           }
       }
        
    }
    /**
     *
     * @param friendship
     * @throws Exception
     * use FriendDAOImp object to call update method to update the friendShip object in database
     */
    public void updateRelations(FriendshipDTO friendship) throws Exception{
        
        friendImp.update(friendship);
       
    }
    /**
     *
     * @param friendship
     * @throws Exception
     * use FriendDAOImp object to call delete method to delete the friendShip object from database
     */
    public void deleteFriend(FriendshipDTO friendship) throws Exception{
    
        friendImp.delete(friendship);
    }
    
    /**
     *
     * @param id1
     * @param id2
     * @return
     * use FriendDAOImp object to call checkFriends method to get the relationship between two friends from database
     */
    public int checkFriendsStatus(int id1,int id2){
        return friendImp.checkFriends(id1, id2);
    }
    /**
     *
     * @param ip
     * @param receiver
     * @param sender
     * @throws Exception
     * get the remote object of receiver from clients HashTable 
     * use this remote object to call receiveFile method to send file to receiver
     */
    public void acceptFile(String ip, int receiver, int sender) throws Exception{
    
        clients.get(receiver).receiveFile(ip, receiver, sender);
    }
    
    /**
     *
     * @param sender
     * @param receiver
     * @throws RemoteException
     * use FriendDAOImp object to call sendRequest method to send friend request
     */
    @Override
    public void sendRequest(int sender, int receiver) throws RemoteException {
        friendImp.sendRequest(sender, receiver);
    }

    /**
     *
     * @param email
     * @return
     * @throws RemoteException
     * use UserDAOImp object to call findUserByEmail method to find a certain user using user's email from database
     */
    @Override
    public UserDTO findUserByEmail(String email) throws RemoteException {
         UserDTO user = userImp.findUserByEmail(email);
        return user;
    }

    /**
     *
     * @param sender
     * @param receiver
     * @throws RemoteException
     * get the remote object of receiver from clients HashTable
     * use this remote object to call requestRespond method
     */
    @Override
    public void requestRespond(UserDTO sender, UserDTO receiver) throws RemoteException {
        clients.get(receiver.getId()).requestRespond(sender);
    }

    /**
     *
     * @param f
     * @throws RemoteException
     * use FriendDAOImp object to call update method to update the friendShip object in database
     */
    @Override
    public void acceptRequest(FriendshipDTO f) throws RemoteException {
        friendImp.update(f);
    }

    /**
     *
     * @param f
     * @throws RemoteException
     * use FriendDAOImp object to call delete method to delete the friendShip object from database
     */
    @Override
    public void refuseRequest(FriendshipDTO f) throws RemoteException {
        friendImp.delete(f);
    }

    @Override
    public void notifyAllUsers(String msg) throws RemoteException {
        for(Map.Entry m : clients.entrySet()){
            System.out.println(m.getKey());
            System.out.println(m.getValue());
           CallBack d = (CallBack)m.getValue();
            try {
                d.notifyUsers(msg);
                System.out.println(msg + "from d.notify");
            } catch (RemoteException ex) {
                Logger.getLogger(ChattingImp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                
        System.out.println("hello from notify all users");
    }

    @Override
    public void updateUser(UserDTO user) throws RemoteException {
        userImp.update(user);
    }

    @Override
    public void uploadImage(int id, File fis) throws RemoteException {
            try {
            System.out.println(id);
            System.out.println(fis.toString());
            userImp.uploadImage(id, fis);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChattingImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    /**
     *
     * @param id
     * @return
     * @throws RemoteException
     * use UserDAOImp object to call downloadImage method to get image of user from database using user's id
     */
    @Override
    public File downloadImage(int id) throws RemoteException {
         File file = null ;
        try {
            file = userImp.downloadImage(id);
        } catch (IOException ex) {
            Logger.getLogger(ChattingImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return file;
        
    }

    public void welcommingMail(UserDTO user){
        System.out.println(MailAPI.serverMail);
        System.out.println(MailAPI.serverPassword);
        System.out.println(user.getEmail());
        MailAPI mailAPI = new MailAPI();
        mailAPI.setFrom(MailAPI.serverMail);
        mailAPI.setPassword(MailAPI.serverPassword);
        mailAPI.setTo(user.getEmail());
        mailAPI.setSubject("wlelcome to connect chat application");
        mailAPI.setText("welcome to our chat application ,we hope you will have a good time with your friends, here is your user name & password username: " + user.getUserName() + "password: " + user.getPassWord());
        mailAPI.sendMail(mailAPI);
    }
    
    
    public void unRegisterAll(){
    
        Enumeration e = clients.keys();
        while(e.hasMoreElements()){
            clients.remove((Integer) e.nextElement());
            
        }
        
    
    }
    
    
    public void userExit(UserDTO user){
        
        clients.remove(user.getId());
        System.out.println("Client Removed");
    
    }

    
   
}
