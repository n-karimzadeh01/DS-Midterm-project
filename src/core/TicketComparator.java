package core;

import data_structures.MyComparator;
import models.Ticket;

public class TicketComparator implements MyComparator<Ticket> {
    private final boolean byPriority;

    public TicketComparator(boolean byPriority) {
        this.byPriority = byPriority;
    }

    @Override
    public int compare(Ticket a, Ticket b) {
        // higher priority first
        if(byPriority && a.getPriority() != b.getPriority()) {
            return Integer.compare(b.getPriority(), a.getPriority());
        }
        // older first (the format yyyy-MM-dd HH:mm:ss can be compared as a string)
        int byDate = a.getCreatedDate().compareTo(b.getCreatedDate());
        if(byDate != 0) return byDate;
        // last tie-breaker: the numeric part of the id ("T10" is bigger than "T9")
        return Integer.compare(idNumber(a), idNumber(b));
    }

    private int idNumber(Ticket ticket) {
        return Integer.parseInt(ticket.getTicketID().substring(1));
    }
}