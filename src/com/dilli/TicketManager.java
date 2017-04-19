package com.dilli;

/*
This program takes user input for the support ticket
program, provides user to add, delete, and resolve tickets,
and writes open and resolved tickets to respective text file.
 */
import java.io.*;
import java.util.*;

public class TicketManager {

    public static void main(String[] args) {
        LinkedList<Ticket> resolvedTickets = new LinkedList<Ticket>();
        LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();
        Scanner scan = new Scanner(System.in);
        System.out.println("Here is a list of open tickets");
        System.out.println();
        //reads all open tickets.
        readOpenTicket();
            //appends open tickets to the file.
            try{
                BufferedWriter openTicket = new BufferedWriter(new FileWriter("Open_Tickets.txt",true));

            //appends resolved tickets to the file.
            BufferedWriter resolvedTicket = new BufferedWriter(new FileWriter("Resolved_Ticket.txt",true));
            while (true) {
                //displays option menu for user input
                System.out.println("1. Enter Ticket\n2. Delete Ticket by ID\n3. Delete Ticket by " +
                        "Issue\n4. Search by Issue\n5. Display All Tickets\n6. Display resolved tickets\n7. Quit");
                int task = Integer.parseInt(scan.nextLine());

                if (task == 1) {
                    //Call addTickets, which will let user enter any number of new tickets
                    addTickets(ticketQueue);
                    for (Ticket ot : ticketQueue) {
                        openTicket.append(ot + "\n");
                    }
                    openTicket.close();

                } else if (task == 2) {
                    //delete a ticket by ID
                    deleteTicket(ticketQueue, resolvedTickets);
                } else if (task == 3) {
                    //delete ticket by issue
                    deleteTicketByIssue(ticketQueue, resolvedTickets);
                    //prints all tickets
                } else if (task == 4) {
                    //search ticket by issue
                    searchByIssue(ticketQueue);
                } else if (task == 5) {
                    //prints all tickets
                    printAllTickets(ticketQueue);

                } else if (task == 6) {
                    //Display all resolved tickets
                    printResolvedTickets(resolvedTickets);
                } else if (task == 7) {
                    //Quit, and saves resolved tickets to the file.
                    System.out.println("Quitting program");
                    for (Ticket rt : resolvedTickets) {
                        resolvedTicket.append(rt + "\n");
                    }
                    resolvedTicket.close();
                    break;
                } else {
                    //prints all tickets
                    printAllTickets(ticketQueue);
                }
            }
            scan.close();
        } catch (IOException ioe) {
            System.out.println("File cannot be found");
        }
    }

    //search tickets by issue
    protected static void searchByIssue(LinkedList<Ticket> ticketQueue){
        Scanner search = new Scanner(System.in);
        System.out.println("Enter a search string");
        String searchString = search.nextLine();
        for(Ticket t: ticketQueue){
            //search condition
            if(t.getDescription().contains(searchString) || t.getDescription().toLowerCase().contains(searchString)){
                System.out.println(t);
            }
        }
    }
    //searches and deletes ticket by issues.
    protected  static void deleteTicketByIssue(LinkedList<Ticket> ticketQueue,LinkedList<Ticket> resolvedTicket) {
        searchByIssue(ticketQueue);
        deleteTicket(ticketQueue,resolvedTicket);
    }
    //Searches and deletes ticket by ticket ID.
    protected static void deleteTicket(LinkedList<Ticket> ticketQueue,LinkedList<Ticket>resolvedTicket) {
        int deleteID;
        //Scanner deleteScanner = new Scanner(System.in);
        if (ticketQueue.size() == 0) { //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }
        deleteID  = Input.getPositiveIntInput("Enter ID of ticket to delete");
        //Loop over all tickets. Delete the one with this ticket ID
        boolean found = false;
        for (Ticket ticket : ticketQueue) {
            if (ticket.getTicketID() == deleteID) {
                found = true;
                 String res = Input.getStringInput("Enter the resolution of the ticket");
                 Date resolvedDate = new Date();
                 int priority = ticket.getPriority();
                 String description = ticket.getDescription();
                 String rep = ticket.getReporter();
                 Ticket tr = new Ticket(description,priority,rep,new Date(),res,resolvedDate);
                 resolvedTicket.add(tr);
                ticketQueue.remove(ticket);
                System.out.println(String.format("Ticket %d deleted", deleteID));
                break; //don't need to loop any more.
            }

        }
        while (!found) { //if ticket not found, ask user to enter the ticket ID again.
            System.out.println("Ticket ID not found, no ticket deleted");
            deleteID = Input.getPositiveIntInput("Please enter the ticket number again");
            for(Ticket T : ticketQueue){
                if(T.getTicketID() == deleteID){
                    found = true;
                    ticketQueue.remove(T);
                    System.out.println(String.format("Ticket %d deleted", deleteID));
                    break;
                }
            }
        }
        printAllTickets(ticketQueue); //print updated list
    }
    //defines add ticket method
    protected static void addTickets(LinkedList<Ticket> ticketQueue) {
        Scanner sc = new Scanner(System.in);
        boolean moreProblems = true;
        String description,reporter,resolution;
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;


        while (moreProblems){
            description = (Input.getStringInput("Enter problem"));
            reporter = (Input.getStringInput("Who reported this issue?"));
            priority = (Input.getPositiveIntInput("Enter priority of " + description));
            resolution = null;
            Date dateResolved = null;
            Ticket t = new Ticket(description, priority, reporter,dateReported,resolution,dateResolved);
            addTicketInPriorityOrder(ticketQueue,t);

            printAllTickets(ticketQueue);

            System.out.println("More tickets to add?");
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("N")) {
                moreProblems = false;
            }
        }
    }
    //defines method that adds tickets in priority order.
    protected static void addTicketInPriorityOrder(LinkedList<Ticket> tickets,Ticket newTicket){
        if(tickets.size() == 0){ //Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5 = server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end
        int newTicketPriority = newTicket.getPriority();
        for(int x = 0; x<tickets.size(); x++){ //use a regular for loop so we know which element we are looking at
            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if(newTicketPriority>= tickets.get(x).getPriority()){
                tickets.add(x,newTicket);
                return;
            }
        }
        //will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other ticket. So, add to the end.
        tickets.addLast(newTicket);

    }
    //defines method that reads all open tickets from the file.
    protected static void readOpenTicket() {
        try {
            //initializes buffered reader that wraps file reader
            BufferedReader bufferedReader = new BufferedReader(new FileReader("Open_Tickets.txt"));
            //reads line from the file
            String line = bufferedReader.readLine();
            while (line != null) {
                System.out.println(line);
                line = bufferedReader.readLine();
            }
            System.out.println();
            bufferedReader.close();
        } catch (IOException ioe) {
            System.out.println("File cannot be found");
        }
    }

    protected static void printAllTickets(LinkedList<Ticket> tickets) {
        System.out.println(" ------- All open tickets ----------");
        for (Ticket t : tickets ) {
            System.out.println(t); //println will call toString on its argument
        }
        System.out.println(" ------- End of ticket list ----------");
    }
    protected static void printResolvedTickets(LinkedList<Ticket> resolvedTicket){
        System.out.println("-------------All resolved tickets --------------");
        for(Ticket tr: resolvedTicket){
            System.out.println(tr);
        }
        System.out.println("------------End of ticket list ------------------");
    }
}

