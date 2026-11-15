package core;

import models.Account;

public interface AccountManagerInterface {

    void register();
    boolean login();
    void logout();
    Account getCurrentUser();
    boolean isLoggedIn();
    boolean isAdmin();
    void addAdmin();
    Account searchUser(String username);
    Account deleteUser(String username);
}
