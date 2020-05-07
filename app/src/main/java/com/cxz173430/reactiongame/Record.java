package com.cxz173430.reactiongame;
/**
 * This is the object class for game record
 * @Author:     Churong Zhang
 * @Date:       3/14/2020
 * @Class:      CS 6326
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Record implements Comparable, Serializable {
    public static final int MAXNAMESIZE = 30;

    public void setName(String name) {
        if(name.length() > MAXNAMESIZE)
            name = name.substring(0, MAXNAMESIZE);
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public long getScore() {
        return score;
    }

    public long getDate() {
        return date;
    }

    private String name;
    private long score;
    private long date;

    /**
     * Construct a record
     * @param name  the name of the player
     * @param score the score that the player get
     * @param date  the date    the date
     */
    public Record(String name, long score, long date)
    {
        if(name.length() > MAXNAMESIZE)
            name = name.substring(0, MAXNAMESIZE);
        this.name = name;
        this.score = score;
        this.date = date;
    }

    /**
     * construct a record with a given string
     * @param str   the data string
     */
    public Record(String str)
    {
        // the data is split by tap\t
        String[] data = str.split("\t");
        if(data.length == 3)
        {   // first index is name
            // second index is the score
            // third index is the date
            this.name = data[0];
            this.score = Long.parseLong(data[1]);
            this.date = Long.parseLong(data[2]);
        }
        else
        {
            this.name = "N/A";
            this.score = 0;
            this.date = 0;
        }

    }


    @Override
    public String toString()
    {   // print the record
        return name + "\t" + score + "\t" + date;
    }

    /**
     * override the compareto function
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        Record r = (Record)o;
        // check if they have the same score
        if(getScore() == r.getScore())
        {
            int different = (int)(getDate() - r.getDate()); // if their score is the same, then check the date
            if(different == 0)
                return name.compareTo(r.getName());
            return different;
        }
        return (int)(getScore() - r.getScore());
    }

    public static int writeToFile(String path, String data) {

        try {
            File dir = new File(path,"Scores");
            PrintWriter writer = new PrintWriter(dir);
            writer.write(data);
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Exception" + "File write failed: " + e.toString());
            return -1;
        }
        return 0;
    }

    public static List<Record> readFromFile(String path)
    {
        List<Record> records = new ArrayList<>();
        try
        {
            File dir = new File(path, "Scores");
            Scanner scanner = new Scanner(dir);
            while(scanner.hasNextLine())
            {
                String data = scanner.nextLine();
                Record r = new Record(data);
                records.add(r);
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return records;
    }
}
