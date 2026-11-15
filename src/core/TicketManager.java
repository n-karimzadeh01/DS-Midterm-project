package core;

import data_structures.MyHashTable;
import data_structures.PriorityQueueInterface;
import models.Ticket;

import java.util.Stack;

public class TicketManager implements TicketManagerInterface {
    private MyHashTable<String, Ticket> tickets;
    private PriorityQueueInterface<Ticket> ticketQueue;
    private Stack<Ticket> changesOnTickets;


    @Override
    public void createTicket() {

    }

    @Override
    public Ticket searchTicket(String ticketId) {
        return tickets.get(ticketId);
    }

    @Override
    public Ticket[] getAllTickets() {
        for(int i = 0; i < tickets.getCapacity(); i++){
            
        }
    }

    @Override
    public Ticket[] getUserTickets(String username) {
        return new Ticket[0];
    }

    @Override
    public Ticket deleteTicket(String ticketId) {
        Ticket ticket = searchTicket(ticketId);
        if(ticket != null) tickets.remove(ticketId);
        return ticket;
    }

    @Override
    public Ticket processNextTicket() {
        return ticketQueue.dequeue();
    }

    @Override
    public void changeTicketStatus(String ticketId, String status) {
        Ticket ticket = searchTicket(ticketId);
        ticket.setStatus(status);
        changesOnTickets.push(ticket);
    }

    @Override
    public void undoLastChange() {
        Ticket ticket = changesOnTickets.pop();
        if(ticket.getStatus().equals("Closed")) ticket.setStatus("In Progress");
        if(ticket.getStatus().equals("In Progress")) ticket.setStatus("Open");
    }

    @Override
    public void sortTickets() {

    }
}
