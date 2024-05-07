package com.activity5.service;

import java.io.*;
import java.util.Scanner;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import com.activity5.model.DynamicTable;
import org.apache.commons.io.FileUtils;



public class TableManager extends DynamicTable {
    private Random random;

    // Initializing the location of text file
    private final String FILEPATH = "/Users/jerome/Documents/Deep Dive Java/ECC/Task/project-root-folder/service/src/main/resources/hashMap.txt";

    // Default constructor
    public TableManager(){

    }

    // Constructor calling the superclass (to call the members of the parent class)
    public TableManager(int rows, int columns) {
        super(rows, columns);
        this.random = new Random();
    }

    // Overriding the abstract methods from the parent class
    @Override
    public void generateRandomCharacters() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Map<String, String> cell = new HashMap<>();
                String randomCharsKey = generateRandomCharacter();
                String randomCharsValue = generateRandomCharacter();
                cell.put(randomCharsKey, randomCharsValue);
                table[i][j] = cell;
            }
        }
    }

    @Override
    public void searchCharacter(String searchChar) {
        int maxOccurrences = 0;

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                Map<String, String> maps = table[i][j];
                int occurrences = countOccurrences(maps, searchChar);
                if (occurrences > maxOccurrences) {
                    maxOccurrences = occurrences;
                }
                if (occurrences > 0) {
                    System.out.printf("[%d][%d] - %d Occurrences%n", i, j, occurrences);
                }
            }
        }
        if (maxOccurrences == 0) {
            System.out.printf("Character '%s' sequence not found.\n", searchChar);
        }
    }

    @Override
    public void updateTable() {
        updatingValue();
    }

    @Override
    public void sortTable() {
        sortingValue(table);
    }

    @Override
    public void addColumn() {
        addingColumn(table);
    }

    @Override
    public void saveAndExit() {
        writeToFile();
    }

    @Override
    public void readFile() {
         readToFile();
    }

    private File getFileExtension(){
        return FileUtils.getFile(FILEPATH);
    }

    // Method to traverse the value of table and write it to the file
    private void writeToFile(){
        try(BufferedWriter out = new BufferedWriter(new FileWriter(getFileExtension()))){
            for (int i = 0; i < table.length; i++){
                for (int j = 0; j < table[0].length; j++){
                    for (Map.Entry<String, String> entry : table[i][j].entrySet()){
                        out.write(" ");
                        out.write(entry.getKey()+ '\u00B8' + entry.getValue());
                        out.write(" ");
//                        out.newLine();
                    }
                }
                out.newLine();
            }
        }catch (IOException e){
            System.err.println("Error writing the file: " +e.getMessage());
        }
    }

    // method to traverse the value of table from the file 
    private void readToFile(){
        try (BufferedReader reader = new BufferedReader(new FileReader(getFileExtension()))) {
            String line;
            int rowCount = 0;
            int columnCount = 0;
            // Determine the row and column counts inside the file
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    rowCount++;
                    String[] cells = line.trim().split("\\s+");
                    columnCount = Math.max(columnCount, cells.length);
                }
            }
            // Initialize the table
            table = new HashMap[rowCount][columnCount];
            // Reset the reader to read from the beginning
            reader.close();
            BufferedReader reader1 = new BufferedReader(new FileReader(getFileExtension()));
            int rowIndex = 0;
            // Read the file again to populate the table
            while ((line = reader1.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] cells = line.trim().split("\\s+");
                    for (int columnIndex = 0; columnIndex < cells.length; columnIndex++) {
                        String cellContent = cells[columnIndex].trim();
                        String[] keyValue = cellContent.split("\u00B8");
//                        System.out.println(keyValue.length+ " " +cellContent);
                        if (keyValue.length == 2) {
                            String key = keyValue[0].trim();
                            String value = keyValue[1].trim();
                            Map<String, String> cellMap = new HashMap<>();
                            cellMap.put(key, value);
                            table[rowIndex][columnIndex] = cellMap;
                        }
                    }
                    rowIndex++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    // Method for dynamic column
    private void addingColumn(Map<String, String>[][] map) {
        Scanner input = new Scanner(System.in);
        String newKey = " ";
        String newVal = " ";

        int rows = map.length;
        int columns = map[0].length + 1; // adds additional row to the existing column length of table


        int insertRow = getValidRowLength(input, "Enter which row number in the new column you want to insert: ");

        // prints the add column options
        addColumnOption();

        int chosenOption = columnOptionValidation(input, "Please select on the given choices: ");

        input.nextLine();

        // Condition statement based on the user choice
        if(chosenOption == 1) {
            newKey = generateRandomCharacter();
            newVal = generateRandomCharacter();
        } else if(chosenOption == 2){
            newKey = inputNewValueValidation(input, "Enter new key: ");
            newVal = inputNewValueValidation(input, "Enter new value: ");
        }

        // Overwrite the length of tha table
        table = new HashMap[rows][columns];

        // Declaring new table passing the values of rows and column
        Map<String, String>[][] newTable = new HashMap[rows][columns];


        // Traversing the values of the passed parameter
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < map[i].length; j++) {
                newTable[i][j] = map[i][j]; // copying the contents of the passed parameter to newTable
            }

            newTable[i][columns - 1] = new HashMap<>();
            newTable[i][columns - 1].put("xxx", "xxx"); // Declaring temporary variables for the added column
        }

        // Inserting the new key and value for the specified row on the last column
        for (int i = 0; i < rows; i++){
            Map<String, String> newCellContent = new HashMap<>();
            newCellContent.put(newKey,newVal);
            newTable[insertRow][columns - 1] = newCellContent;
        }

        // Initializing the new table to the existing table
        table = newTable;
    }

    // Method for sorting values
    private void sortingValue(Map<String, String>[][] map){
        int choseOrder = sortingMenu();
        for (Map<String, String>[] row : map) {
            List<String> keyAndValue = new ArrayList<>();
            for (Map<String, String> cell : row) {
                StringBuilder concatenatedCell = new StringBuilder();
                if (cell != null) {
                    for (Map.Entry<String, String> entry : cell.entrySet()) {
                        concatenatedCell.append(entry.getKey()).append(entry.getValue());
                    }
                }
                keyAndValue.add(concatenatedCell.toString());
            }
            if (choseOrder == 1) {
                Collections.sort(keyAndValue);
            } else {
                keyAndValue.sort(Collections.reverseOrder());
            }
            for (int i = 0; i < row.length; i++) {
                if (row[i] != null) {
                    String sortedConcatenatedCell = keyAndValue.get(i);
                    String key = sortedConcatenatedCell.substring(0, 3);
                    String value = sortedConcatenatedCell.substring(3,6);
                    row[i].clear();
                    row[i].put(key, value);
                }
            }
        }
    }
    private int sortingMenu(){
        Scanner input = new Scanner(System.in);
        boolean flag = true;
        int sortOption;
            sortMenuOption();
        do {
            sortOption = validInteger(input);

            switch (sortOption){
                case 1:
                    flag = false;
                    break;
                case 2:
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid input! Please select given choices");
                    break;
            }
        }while(flag);
        return sortOption;
    }
    private void sortMenuOption(){
        System.out.print("""  
                    +----------------ORDER------------------+
                    | [1] - Ascending Order                 |
                    | [2] - Descending Order                |
                    +---------------------------------------+
                    """);
    }

    private void addColumnOption(){
        System.out.print("""  
                    +---------------OPTIONS-----------------+
                    | [1] - Generate random keys and values |
                    | [2] - Insert keys and values          |
                    +---------------------------------------+
                    """);
    }

    private int columnOptionValidation(Scanner input, String instructions){
        int validOption;
        boolean flag = true;
        do{
            System.out.print(instructions);
            while(!input.hasNextInt()){
                System.out.println("Invalid Input! Please enter an integer!");
                System.out.print(instructions);
                input.next();
            }
            validOption = input.nextInt();

            switch (validOption){
                case 1:
                    System.out.println("Generating random key and value....");
                    flag = false;
                    break;
                case 2:
                    System.out.println("Insert key and value....");
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid Input!");
                    break;
            }
        }while(flag);

        return validOption;
    }

    private int validInteger(Scanner input){
        int validInt;
        do {
            System.out.print("Choose sorting order: ");
            while(!input.hasNextInt()){
                System.out.println("Invalid Input! Enter an integer!");
                System.out.print("Choose sorting order: ");
                input.next();
            }

            validInt = input.nextInt();
            if (validInt < 0){
                System.out.println("Please enter a positive integer!");
            }

        }while(validInt <= 0);
        return validInt;
    }

    private void updatingValue(){
        Scanner input = new Scanner(System.in);
        int updateRow = userInputUpdateTableValidation(table, input, "Enter row number: ");
        // int updateColumn = userInputUpdateTableValidation(table, input, "Enter column number: ");
        System.out.print("Enter column number: ");
        int updateColumn = input.nextInt();
        input.nextLine();

        // retrieving the data at the specified row and column
        Map<String, String> mapToUpdate = table[updateRow][updateColumn];
        boolean flag;
        do {
            System.out.print("Do you want to update key/value? [key/value]: ");
            String updateChoice = input.nextLine();

            if (updateChoice.equalsIgnoreCase("key") || updateChoice.equalsIgnoreCase("k")) {
                String newKey = inputNewValueValidation(input, "Enter new key value: ");

                // created a new object
                Map<String, String> newMap = new HashMap<>();
                for (Map.Entry<String, String> entry : mapToUpdate.entrySet()) {
                    newMap.put(newKey, entry.getValue()); // replacing the key without modifying the value
                }
                table[updateRow][updateColumn] = newMap; // passing the object to the indices
                flag = false;

            } else if (updateChoice.equalsIgnoreCase("value") || updateChoice.equalsIgnoreCase("v")) {
                String newVal = inputNewValueValidation(input, "Enter new value: ");

                for (Map.Entry<String, String> entry : mapToUpdate.entrySet()) {
                    entry.setValue(newVal); // setting the new value
                }
                flag = false;
            } else {
                System.out.println("Invalid input!");
                flag = true;
            }
        } while (flag);
    }

    private int countOccurrences(Map<String, String>map ,String searchChar){
        int occurrences = 0;

        for (String key : map.keySet()){
            occurrences += countStringOccurrences(key, searchChar);
        }
        for (String value : map.values()){
            occurrences += countStringOccurrences(value, searchChar);
        }
        return occurrences;
    }

    private int countStringOccurrences(String tableData, String searchChar){
        int counter = 0;
        int index = tableData.indexOf(searchChar);

        while(index != -1){
            counter++;
            index = tableData.indexOf(searchChar, index + 1);
        }
        return counter;
    }

    private String generateRandomCharacter(){
        Random stringRandom = new Random();
        StringBuilder sb = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+{}:\"<>?|[];,'./`~";

        for (int i = 0; i < 3; i++){
            int index = stringRandom.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    private int userInputUpdateTableValidation (Map<String, String>[][] map, Scanner input, String instructions){
        int numInput;
        do{
            System.out.print(instructions);
            while (!input.hasNextInt()) {
                System.out.println("Please enter an integer");
                System.out.print(instructions);
                input.next();
            }
            numInput = input.nextInt();

            if (numInput >= 0 && numInput < map.length || numInput >= 0 && numInput < map[0].length){
                // accepts user input
            }else{
                System.out.println("Please enter a valid length of table!");
            }
        }while(numInput < 0 || numInput >= map.length || numInput >= map[0].length);

        return numInput;
    }

    private String inputNewValueValidation(Scanner input, String instructions){
        String maxValue;
        do{
            System.out.print(instructions);
            maxValue = input.nextLine();

            if (maxValue.length() != 3){
                System.out.println("Please enter three characters!");
            }
        }while(maxValue.length() != 3);
        return maxValue;
    }

    private int getValidRowLength(Scanner input, String prompt){
        int numInput;
        do{
            System.out.print(prompt);
            while(!input.hasNextInt()){
                System.out.println("Please enter a positive integer!");
                System.out.print(prompt);
                input.next();
            }
            numInput = input.nextInt();
            if (numInput < 0){
                System.out.println("Please enter a positive integer!");
            } else if (numInput >= table.length){
                System.out.println("Invalid Input!, You exceeded the length of the row.");

            }
        }while(numInput < 0 || numInput >= table.length);
        return numInput;
    }
}