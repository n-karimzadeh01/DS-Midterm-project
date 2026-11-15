package core;

import data_structures.MyHashTable;
import models.Account;

import java.util.Scanner;

public class AccountManager implements AccountManagerInterface {
    private MyHashTable<String, Account> accounts;
    private Account currentUser;
    Scanner sc = new Scanner(System.in);


    @Override
    public void register() {
        String username;
        String firstName;
        String lastName;
        String password;
        String email;
        System.out.println("enter your first name: ");
        firstName = sc.nextLine();
        System.out.println("enter your last name: ");
        lastName = sc.nextLine();
        System.out.println("enter a password: ");
        password = sc.nextLine();
        System.out.println("enter your email address: ");
        email = sc.nextLine();
        System.out.println("choose a username (your username is not changeable): ");
        username = sc.nextLine();
        while(accounts.containsKey(username)){
            System.out.println("this username is already taken, enter another one: ");
            username = sc.nextLine();
        }
        Account newAccount = new Account(username, firstName, lastName, password, email);
        accounts.put(username, newAccount);
    }

    @Override
    public boolean login() {
        Account target;
        String username;
        String password;
        System.out.println("enter username: ");
        username = sc.nextLine();
        target = searchUser(username);
        while(target == null){
            System.out.println("username not found.\n to quit, enter e\n to try again make sure to enter a username: ");
            username = sc.nextLine();
            if(username.equals("e")) return false;
            target = searchUser(username);
        }
        System.out.println("enter your password: ");
        password = sc.nextLine();
        while(password.equals(target.getPassword())){
            System.out.println("wrong password, try entering another password or enter e to exit: ");
            password = sc.nextLine();
            if(username.equals("e")) return false;
        }
        return true;

    }

    @Override
    public void logout() {
        currentUser = null;
    }

    @Override
    public Account getCurrentUser() {
        return currentUser;
    }

    @Override
    public boolean isLoggedIn() {
        return (currentUser != null);
    }

    @Override
    public boolean isAdmin() {
        return (currentUser.getRole().equals("Admin"));
    }

    @Override
    public void addAdmin() {
        String username;
        System.out.println("enter the username:");
        username = sc.nextLine();
        Account newAdmin;
        while(searchUser(username) == null){
            System.out.println("no such username found.");
            System.out.println("enter the username: ");
            username = sc.nextLine();
        }
        newAdmin = searchUser(username);
        newAdmin.setRole("Admin");
    }

    @Override
    public Account searchUser(String username) {
        if(!accounts.containsKey(username)) return null;
        return accounts.get(username);
    }

    @Override
    public Account deleteUser(String username) {
        Account deleted = searchUser(username);
        accounts.remove(username);
        return deleted;
    }
}
