import core.AccountManager;
import core.TicketManager;
import models.Account;
import models.Ticket;
import storage.FileManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path USERS_FILE = DATA_DIR.resolve("users.csv");
    private static final Path TICKETS_FILE = DATA_DIR.resolve("tickets.csv");
    private static final Path SYSTEM_FILE = DATA_DIR.resolve("system.txt");

    private static final String[] TICKETS_HEADER = {"ticketId", "username", "title", "priority", "status", "createdDate"};

    private static final Scanner sc = new Scanner(System.in);
    private static final FileManager fileManager = new FileManager();
    private static final TicketManager ticketManager = new TicketManager();
    private static final AccountManager accountManager = new AccountManager();

    public static void main(String[] args) {
        loadAll();
        openingPanel();
        saveAll();
    }

    private static void loadAll() {
        try {
            if(fileManager.exists(USERS_FILE)){
                fileManager.readCsv(USERS_FILE, fields -> {
                    Account account = new Account(fields[0], fields[1], fields[2], fields[3], fields[4]);
                    account.setRole(fields[5]);
                    accountManager.loadAccount(account);
                });
            }
            accountManager.ensureDefaultAdmin();   // after loading, so a saved admin is not overwritten

            if(fileManager.exists(TICKETS_FILE)){
                fileManager.readCsv(TICKETS_FILE, fields -> {
                    Ticket ticket = new Ticket(fields[0], fields[1], fields[2], fields[5]);
                    ticket.setPriority(Integer.parseInt(fields[3]));
                    ticket.setStatus(fields[4]);   // must be before loadTicket
                    ticketManager.loadTicket(ticket);
                });
            }

            // system.txt has one line: offsetMinutes=120
            if(Files.exists(SYSTEM_FILE)){
                String line = Files.readAllLines(SYSTEM_FILE, StandardCharsets.UTF_8).get(0);
                String[] parts = line.split("=");
                if(parts.length == 2 && parts[0].equals("offsetMinutes")){
                    ticketManager.setOffsetMinutes(Long.parseLong(parts[1].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read data files: " + e.getMessage());
        }
    }

    private static void saveAll() {
        try {
            // TODO: save users.csv (needs getAllAccounts in AccountManager)

            Ticket[] all = ticketManager.getAllTickets();
            String[][] rows = new String[all.length][];
            for(int i = 0; i < all.length; i++){
                Ticket t = all[i];
                rows[i] = new String[]{t.getTicketID(), t.getUserName(), t.getTitle(),
                        String.valueOf(t.getPriority()), t.getStatus(), t.getCreatedDate()};
            }
            fileManager.writeCsv(TICKETS_FILE, TICKETS_HEADER, Arrays.asList(rows));

            Files.writeString(SYSTEM_FILE, "offsetMinutes=" + ticketManager.getOffsetMinutes() + "\n", StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Could not save data files: " + e.getMessage());
        }
    }

    // ---------- PANELS ----------

    static void openingPanel(){
        while(true){
            System.out.println("1) Login\n2) Register\n3) Exit");
            String choice = sc.nextLine().trim();
            switch(choice){
                case "1":
                    if(accountManager.login()){
                        if(accountManager.isAdmin()) adminPanel();
                        else userPanel();
                    }
                    break;
                case "2":
                    accountManager.register();
                    break;
                case "3":
                    return;   // leaves the method, so it also leaves the while loop
                default:
                    System.out.println("invalid choice");
            }
        }
    }

    static void adminPanel(){
        while(true){
            System.out.println("1) Support Tickets\n2) Mange System\n3)Log out");
            String choice = sc.nextLine().trim();
            switch(choice){
                case "1":
                    ticketSupportPanel();
                    break;
                case "2":
                    systemManagementPanel();
                    break;
                case "3":
                    accountManager.logout();
                    return;
                default:
                    System.out.println("invalid choice");
            }
        }
    }

    static void ticketSupportPanel(){
        while(true){
            System.out.println("1) View All Tickets\n2) Process Next\n3) Change Status\n4) Undo\n0) Back");
            String choice = sc.nextLine().trim();
            switch(choice){
                case "1":
                    for(Ticket t : ticketManager.getAllTickets()) System.out.println(t);
                    break;
                case "2": {
                    Ticket next = ticketManager.processNextTicket();
                    if(next == null) System.out.println("no open ticket");
                    else System.out.println("now in progress: " + next);
                    break;
                }
                case "3": {
                    System.out.println("enter ticket id: ");
                    String id = sc.nextLine().trim();
                    if(ticketManager.changeTicketStatus(id)) System.out.println("ticket closed");
                    else System.out.println("ticket not found or not In Progress");
                    break;
                }
                case "4":
                    if(ticketManager.undoLastChange()) System.out.println("last change undone");
                    else System.out.println("nothing to undo");
                    break;
                case "0":
                    return;
                default:
                    System.out.println("invalid choice");
            }
        }
    }

    static void systemManagementPanel(){
        while(true){
            System.out.println("1) Sort Tickets\n2) Search core.User\n3) Delete core.User\n4) Add core.Admin\n5) Change System Time\n0) Back");
            String choice = sc.nextLine().trim();
            switch(choice){
                case "1": {
                    System.out.println("sort by: 1) priority  2) date");
                    String by = sc.nextLine().trim();
                    if(by.equals("1") || by.equals("2")){
                        for(Ticket t : ticketManager.sortTickets(by.equals("1"))) System.out.println(t);
                    } else {
                        System.out.println("invalid choice");
                    }
                    break;
                }
                case "2": {
                    System.out.println("enter username: ");
                    String username = sc.nextLine().trim();
                    System.out.println("[" + username + "]");
                    boolean found = accountManager.isUser(username);
                    if(! found){
                        System.out.println("user not found");
                    } else {
                        System.out.println(found);
                        for(Ticket t : ticketManager.getUserTickets(username)) System.out.println(t);
                    }
                    break;
                }
                case "3": {
                    System.out.println("enter username: ");
                    String username = sc.nextLine().trim();
                    Account deleted = accountManager.deleteUser(username);
                    if(deleted == null){
                        System.out.println("user not found");
                    } else {
                        int count = ticketManager.deleteUserTickets(username);
                        System.out.println("user deleted with " + count + " tickets");
                    }
                    break;
                }
                case "4":
                    accountManager.addAdmin();
                    break;
                case "5":
                    ChangeSystemTime();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("invalid choice");
            }
        }
    }

    static void userPanel(){
        String username = accountManager.getCurrentUser();
        while(true){
            System.out.println("1) Register\n2) Create models.Ticket\n3) My Tickets\n4) Search models.Ticket\n5) Log out");
            String choice = sc.nextLine().trim();
            switch(choice){
                case "1":
                    System.out.println("you are already registered");
                    break;
                case "2": {
                    System.out.println("enter title: ");
                    String title = sc.nextLine();
                    System.out.println("enter priority (1 to 4): ");
                    try {
                        int priority = Integer.parseInt(sc.nextLine().trim());
                        if(priority < 1 || priority > 4) System.out.println("priority must be between 1 and 4");
                        else System.out.println("created: " + ticketManager.createTicket(username, title, priority));
                    } catch (NumberFormatException e) {
                        System.out.println("invalid number");
                    }
                    break;
                }
                case "3":
                    for(Ticket t : ticketManager.getUserTickets(username)) System.out.println(t);
                    break;
                case "4": {
                    System.out.println("enter ticket id: ");
                    Ticket t = ticketManager.searchTicket(sc.nextLine().trim());
                    // a user can only see his own tickets
                    if(t == null || !t.getUserName().equals(username)) System.out.println("ticket not found");
                    else System.out.println(t);
                    break;
                }
                case "5":
                    accountManager.logout();
                    return;
                default:
                    System.out.println("invalid choice");
            }
        }
    }

    static void ChangeSystemTime(){
        System.out.println("enter hours to move the clock (positive: forward, negative: backward): ");
        try {
            long hours = Long.parseLong(sc.nextLine().trim());
            ticketManager.shiftSystemTime(hours);
            System.out.println("system time moved by " + hours + " hours");
        } catch (NumberFormatException e) {
            System.out.println("invalid number");
        }
    }
}