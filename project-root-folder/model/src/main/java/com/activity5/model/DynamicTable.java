package com.activity5.model;

import java.util.HashMap;
import java.util.Map;

public abstract class DynamicTable {
    protected int rows;
    protected int columns;
    protected Map<String, String>[][] table;
    int[] maxKeyLength;
    int[] maxValueLength;


    // Default Constructor
    public DynamicTable(){

    }

    // Constructor that passes the parameters of rows and columns
    public DynamicTable(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        this.table = new HashMap[rows][columns];
    }

    // Abstract methods
    public abstract void generateRandomCharacters();
    public abstract void searchCharacter(String searchChar);
    public abstract void updateTable();
    public abstract void sortTable();
    public abstract void addColumn();
    public abstract void saveAndExit();
    public abstract void readFile();

    // Method for printing the table
    public void printTable(){
        int rows = table.length;
        int columns = table[0].length;

        // counting the length of the key and value
        maxKeyLength = new int[columns];
        maxValueLength = new int[columns];

        // Traversing the values of the table and initializing the maximum length of each value
        for (int j = 0; j < columns; j++){
            for (int i = 0; i < rows; i++){
                for (Map.Entry<String, String> entryLength : table[i][j].entrySet()){
                    String key = entryLength.getKey();
                    String value = entryLength.getValue();
                    maxKeyLength[j] = Math.max(maxKeyLength[j], key.length());
                    maxValueLength[j] = Math.max(maxValueLength[j], value.length());
                }
            }
        }

        // Displaying the dynamic table 
        printRowSeparator(columns); // prints the dynamic row separator based on the length of the table 
        for (int i = 0; i < rows; i++){
            System.out.print("|");
            for (int j = 0; j < columns; j++){
                for (Map.Entry<String, String> entry : table[i][j].entrySet()){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    System.out.print(" ");
                    System.out.print(rightPadding(key, maxKeyLength[j]));
                    System.out.print(",");
                    System.out.print(leftPadding(value, maxValueLength[j]));
                    System.out.print(" |");
                }
            }
            System.out.println();
            printRowSeparator(columns);
        }

    }

    // Helper method to indicate right padding
    public String rightPadding(String cellContent, int length){
        return String.format("%-" + length + "s",cellContent);
    }

    // Helper method to indicate left padding
    public String leftPadding (String cellContent, int length){
        return String.format("%" + length + "s",cellContent);
    }

    // Helper method to display row separator based on the table length
    public void printRowSeparator(int columns) {
        for (int i = 0; i < columns; i++) {
            System.out.print("+");
            // Adjust the width based on the maximum lengths of keys and values
            System.out.print("-".repeat(maxKeyLength[i] + maxValueLength[i] + 3));
        }
        System.out.println("+");

    }

    // Helper method do display the menus
    public void menuOption (){
        System.out.print("""  
                            +-----------------MENU------------------+
                            | [1] - Search a character/s on a table |
                            | [2] - Update a value on the table     |
                            | [3] - Reset                           |
                            | [4] - Sort                            |
                            | [5] - Add column                      |
                            | [6] - Exit                            |
                            +---------------------------------------+
                            """);
    }





}