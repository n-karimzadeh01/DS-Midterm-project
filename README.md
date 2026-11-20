# DSA-Midterm-project
# Support Ticket System

A console-based support ticket system written in Java for the Data Structures course.
Users can register, log in and create tickets. Admins can process, sort and manage tickets and users.
All data structures (hash table, priority queue, stack, list, merge sort) are implemented from scratch.

## Requirements

- Java 11 or newer (the project uses `Files.readString` / `Files.writeString`)
- Developed and tested with Oracle OpenJDK 24 in IntelliJ IDEA 2026.2.3 on Windows

## How to Run

**In IntelliJ IDEA**
1. Open the project folder.
2. Run the `Main` class.

**From the command line** (from the project root, where the `src` folder is)
```
javac -d out -sourcepath src src/Main.java
java -cp out Main
```

The program creates and reads a `data` folder in the directory you run it from.

## Default Admin Account

| Username: amdin
| Password: admin 

It is created automatically if it does not exist.

## Features

**User**
- Register and log in
- Create a ticket (title and priority from 1 to 4, where 4 is the most important)
- View my tickets and search my tickets by ID

**Admin**
- View all tickets
- Process the next ticket (highest priority first, older first when equal)
- Change a ticket's status (Open, In Progress, Closed)
- Undo the last status change (up to 10 changes)
- Sort tickets by priority or by date
- Search a user and see their tickets
- Delete a user (their tickets are deleted too)
- Add another admin
- Change the system time (move the clock forward or backward in hours)

## Project Structure

```
src/
  Main.java              loads data at start, saves it at exit
  ui/                    ConsoleUI: the only class that reads input and prints
  core/                  AccountManager, TicketManager (the program logic)
  models/                Account, Ticket
  storage/               FileManager (CSV reading and writing)
  data_structures/       MyHashTable, MyArrayList, MyStack, BinaryHeapPriorityQueue, MergeSort, ...
```

## Data Files

Stored in the `data` folder:

- `users.csv`: username, password, firstName, lastName, email, role
- `tickets.csv`: ticketId, username, title, priority, status, createdDate
- `system.txt`: the saved clock offset (`offsetMinutes=...`)

Data is read once when the program starts and saved when it exits.
Use the Exit option from the login screen so everything is saved properly.

## Notes

- The system time is the real time plus an offset set by the admin. The clock keeps running, and the offset is kept between runs.
- Fields must not contain line breaks (CSV limitation).
- Passwords are stored as plain text in `users.csv` (this is a course project, not a production system).
