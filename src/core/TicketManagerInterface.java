package core;

import models.Ticket;

public interface TicketManagerInterface {
    void createTicket();
    Ticket searchTicket(String ticketId);
    Ticket[] getAllTickets();
    Ticket[] getUserTickets(String username);
    Ticket deleteTicket(String ticketId);

    Ticket processNextTicket();
    void changeTicketStatus(String ticketId, String status);
    void undoLastChange();
    void sortTickets();
}
