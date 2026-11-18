package core;

import models.Account;

public interface AccountManagerInterface {

    void register();
    boolean login();
    void logout();
    String getCurrentUser();
    boolean isLoggedIn();
    boolean isAdmin();
    void addAdmin();
    boolean isUser(String username);
    Account deleteUser(String username);

}
