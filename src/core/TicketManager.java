package core;

import data_structures.BinaryHeapPriorityQueue;
import data_structures.MergeSort;
import data_structures.MyArrayList;
import data_structures.MyHashTable;
import data_structures.MyStack;
import data_structures.PriorityQueueInterface;
import models.Account;
import models.Ticket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketManager implements TicketManagerInterface {
    private static final int UNDO_LIMIT = 10;
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private MyHashTable<String, Ticket> tickets = new MyHashTable<>();
    private MyHashTable<String, MyArrayList<String>> userIndex = new MyHashTable<>();   // username -> ticket ids
    private PriorityQueueInterface<Ticket> ticketQueue = new BinaryHeapPriorityQueue<>(new TicketComparator(true));
    private PriorityQueueInterface<Ticket> inProgressQueue = new BinaryHeapPriorityQueue<>(new TicketComparator(true));
    private MyStack<Ticket> changesOnTickets = new MyStack<>(UNDO_LIMIT);

    private long offsetMinutes = 0;   // how far the system clock is from the real clock
    private int ticketCounter = 0;


    // moves the system clock forward (positive) or backward (negative) by the given number of hours
    @Override
    public void shiftSystemTime(long hours) {
        offsetMinutes += hours * 60;
    }

    // system time = real time + admin's offset
    private String currentTimeString() {
        return LocalDateTime.now().plusMinutes(offsetMinutes).format(FORMAT);
    }

    @Override
    public long getOffsetMinutes() {
        return offsetMinutes;
    }

    @Override
    public void setOffsetMinutes(long offsetMinutes) {
        this.offsetMinutes = offsetMinutes;
    }

    // pushes a record; if the undo stack is full, the oldest record is dropped first
    private void pushChange(Ticket ticket) {
        if(changesOnTickets.size() == UNDO_LIMIT) {
            MyStack<Ticket> temp = new MyStack<>(UNDO_LIMIT);
            while(!changesOnTickets.isEmpty()) temp.push(changesOnTickets.pop());
            temp.pop();   // the oldest ticket is on top of temp, so it is dropped here
            while(!temp.isEmpty()) changesOnTickets.push(temp.pop());
        }
        changesOnTickets.push(ticket);
    }

    // adds the ticket id to the list of its owner (the list is created if it doesn't exist)
    private void addToIndex(Ticket ticket) {
        MyArrayList<String> ids = userIndex.get(ticket.getUserName());
        if(ids == null){
            ids = new MyArrayList<>();
            userIndex.put(ticket.getUserName(), ids);
        }
        ids.add(ticket.getTicketID());
    }

    @Override
    public Ticket createTicket(String username, String title, int priority) {
        if(priority < 1 || priority > 4) throw new IllegalArgumentException("priority must be between 1 and 4");
        ticketCounter++;
        Ticket ticket = new Ticket("T" + String.valueOf(ticketCounter), username, title, currentTimeString());
        ticket.setPriority(priority);
        tickets.put(ticket.getTicketID(), ticket);
        addToIndex(ticket);
        ticketQueue.enqueue(ticket);
        return ticket;
    }

    // used when tickets are loaded from the CSV file at program start
    @Override
    public void loadTicket(Ticket ticket) {
        tickets.put(ticket.getTicketID(), ticket);
        addToIndex(ticket);
        if(ticket.getStatus().equals("Open")) ticketQueue.enqueue(ticket);
        else if(ticket.getStatus().equals("In Progress")) inProgressQueue.enqueue(ticket);

        int number = Integer.parseInt(ticket.getTicketID().substring(1));
        if(number > ticketCounter) ticketCounter = number;
    }

    @Override
    public Ticket searchTicket(String ticketId) {
        return tickets.get(ticketId);
    }

    @Override
    public Ticket[] getAllTickets() {
        Ticket[] allTickets = new Ticket[tickets.size()];
        tickets.copyValuesTo(allTickets);
        return allTickets;
    }

    @Override
    public Ticket[] getUserTickets(String username) {
        MyArrayList<String> ids = userIndex.get(username);
        if(ids == null) return new Ticket[0];

        Ticket[] userTickets = new Ticket[ids.size()];
        for(int i = 0; i < ids.size(); i++){
            userTickets[i] = tickets.get(ids.get(i));
        }
        return userTickets;
    }

    @Override
    public Ticket deleteTicket(String ticketId) {
        Ticket ticket = searchTicket(ticketId);
        if(ticket == null) return null;

        tickets.remove(ticketId);
        MyArrayList<String> ids = userIndex.get(ticket.getUserName());
        if(ids != null){
            ids.remove(ids.indexOf(ticketId));
            if(ids.isEmpty()) userIndex.remove(ticket.getUserName());
        }
        return ticket;
    }

    // deletes all tickets of a user (used when an account is deleted); returns how many were deleted
    @Override
    public int deleteUserTickets(String username) {
        MyArrayList<String> ids = userIndex.get(username);
        if(ids == null) return 0;

        int count = ids.size();
        for(int i = 0; i < count; i++){
            tickets.remove(ids.get(i));
        }
        userIndex.remove(username);
        return count;
    }

    // removes tickets from the heap until it finds one that is still Open and still exists
    private Ticket pollOpenTicket() {
        while(!ticketQueue.isEmpty()){
            Ticket ticket = ticketQueue.dequeue();
            if(tickets.containsKey(ticket.getTicketID()) && ticket.getStatus().equals("Open")) return ticket;
        }
        return null;
    }

    @Override
    public Ticket processNextTicket() {
        Ticket ticket = pollOpenTicket();
        if(ticket == null) return null;

        ticket.setStatus("In Progress");
        inProgressQueue.enqueue(ticket);
        pushChange(ticket);
        return ticket;
    }

    // moves an In Progress ticket to Closed (Open -> In Progress only happens in processNextTicket)
    @Override
    public boolean changeTicketStatus(String ticketId) {
        Ticket ticket = searchTicket(ticketId);
        if(ticket == null || !ticket.getStatus().equals("In Progress")) return false;

        ticket.setStatus("Closed");
        pushChange(ticket);
        return true;
    }

    // undoes the last change; records of deleted tickets are skipped
    @Override
    public boolean undoLastChange() {
        while(!changesOnTickets.isEmpty()){
            Ticket ticket = changesOnTickets.pop();
            if(!tickets.containsKey(ticket.getTicketID())) continue;   // ticket was deleted, skip it

            if(ticket.getStatus().equals("Closed")){
                ticket.setStatus("In Progress");
            } else if(ticket.getStatus().equals("In Progress")){
                ticket.setStatus("Open");
                ticketQueue.enqueue(ticket);
            }
            return true;
        }
        return false;
    }

    // returns a sorted copy; the hash table itself is not changed
    @Override
    public Ticket[] sortTickets(boolean byPriority) {
        Ticket[] sorted = getAllTickets();
        MergeSort.mergeSort(sorted, new TicketComparator(byPriority));
        return sorted;
    }
}