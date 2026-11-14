package models;

public class Ticket {
    final String ticketID; //string or number?
    String userName;
    String title;
    int priority;
    String status;
    final String createdDate;

    public Ticket(String ticketID, String userName, String title, int priority, String status, String createdDate){
        this.ticketID = ticketID;
        this.userName = userName;
        this.title = title;
        this.priority = priority;
        this.status = status;
        this.createdDate = createdDate;
    }

    public String getTicketID() {
        return ticketID;
    }

    public String getUserName() {
        return userName;
    }

    public String getTitle() {
        return title;
    }

    public int getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return "Ticket{" + "Ticket ID:'" + ticketID + '\'' + ", User Name:'" + userName + '\'' + ", Title:'" + title + '\'' + ", Priority:'" + priority + '\'' + ", Status:'" + status + '\'' + ", On Date:'" + createdDate + '\'' + '}';
    }
}
