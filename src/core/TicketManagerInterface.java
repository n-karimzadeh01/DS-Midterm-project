package core;

import models.Ticket;

public interface TicketManagerInterface {
    Ticket createTicket(String username, String title, int priority);
    void loadTicket(Ticket ticket);

    Ticket searchTicket(String ticketId);
    Ticket[] getAllTickets();
    Ticket[] getUserTickets(String username);
    Ticket deleteTicket(String ticketId);
    int deleteUserTickets(String username);

    Ticket processNextTicket();

    // moves an In Progress ticket to Closed (Open -> In Progress only happens in processNextTicket)
    boolean changeTicketStatus(String ticketId);

    boolean undoLastChange();
    Ticket[] sortTickets(boolean byPriority);

    void shiftSystemTime(long hours);
    long getOffsetMinutes();
    void setOffsetMinutes(long offsetMinutes);
}