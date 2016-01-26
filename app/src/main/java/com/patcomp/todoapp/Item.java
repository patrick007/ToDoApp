package com.patcomp.todoapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by patrick on 1/24/16.
 */
public class Item {
    int Id;
    String Name;
    Date dueDate;
    String Notes;
    String level;
    String Status;

    Item() {
        Id = 0;
        Name = "";
        dueDate = new Date();
        Notes = "";
        level = "LOW";
        Status = "TO DO";
    }

    Item(String pName) {
        Id = 0;
        Name = pName;
        dueDate = new Date();
        Notes = "";
        level = "LOW";
        Status = "TO DO";
    }

    Item(int pId, String pName, Date pDueDate, String pNotes, String pLevel, String pStatus) {
        Id = pId;
        Name = pName;
        dueDate = pDueDate;
        Notes = pNotes;
        level = pLevel;
        Status = pStatus;
    }



}
