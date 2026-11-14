package core;

import models.Account;
import models.Ticket;

public interface Admin {

    Ticket[] showAllTickets();

    void processNext();

    void changeStatus();

    void undo(); //Undo must be models.Ticket? void? or generic? or Object?

    void sortTicket();

    Account searchUser();

    Account deleteUser();

    Account addAdmin();

    void changeSystemTime();
}
