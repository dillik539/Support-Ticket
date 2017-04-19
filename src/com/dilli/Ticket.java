package com.dilli;

import java.util.Date;

/**
 This class defines various variables required to run
 TicketManager class.
 */
public class Ticket {
    private int priority;//stores priority of ticket
    private String reporter; //Stores person or department who reported issue
    private String description;//stores description of the issue.
    private Date dateReported;
    private String ticketResolution;//stores the resolution string
    private Date dateResolved;

    //STATIC Counter - accessible to all Ticket objects.
    //If any Ticket object modifies this counter , all Ticket objects will have the modified value
    //Make it private - only Ticket objedts shuould have access
    private static int staticTicketIDCounter = 1;
    //The ID for each ticket - instance variable. Each Ticket will have its own ticket ID variable
    protected int ticketID;
    //we can autogenerate get and set methods if and when we need

    //Getters and setters for the variables.

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateReported() {
        return dateReported;
    }

    public void setDateReported(Date dateReported) {
        this.dateReported = dateReported;
    }

    public void setTicketResolution(String ticketResolution) {
        this.ticketResolution = ticketResolution;
    }

    public Date getDateResolved() {
        return dateResolved;
    }

    public void setDateResolved(Date dateResolved) {
        this.dateResolved = dateResolved;
    }

    //constructor
    public Ticket (String desc, int p, String rep, Date date,String ticketResolution,Date dateResolved){
        this.description = desc;
        this.priority = p;

        this.reporter = rep;
        this.dateReported = date;
        this.ticketResolution = ticketResolution;
        this.dateResolved = dateResolved;
        this.ticketID = staticTicketIDCounter;
        staticTicketIDCounter++;
    }

  public String getTicketResolution() {
       return ticketResolution;
  }

    protected int getPriority(){
        return priority;

    }

    protected String getDescription() {
        return description;
    }

    protected int getTicketID(){
        return  ticketID;
    }
    //Called automatically if a Ticket object is an argument to System.out.println
    public String toString(){
        String resolvedDateString = (dateResolved == null) ? "Unresolved" : this.dateResolved.toString();
        String resolutionString = (this.ticketResolution == null) ? "Unresolved" : this.ticketResolution;
        return ("ID: " + this.ticketID + " Issue: " + this.description + " Priority: "+ this.priority + " Reported by: "
        + this.reporter + " Reported on: " + this.dateReported + " Resolution: "+ resolutionString + " Resolved on: " + resolvedDateString);
    }
}
