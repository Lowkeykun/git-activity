package com.activity5.app;

import com.activity5.model.DynamicTable;
import com.activity5.service.TableManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    final static String FILEPATH = "/Users/jerome/Documents/Deep Dive Java/ECC/Task/project-root-folder/service/src/main/resources/hashMap.txt";
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        DynamicTable dynamicTable = new TableManager();
        File file = new File(FILEPATH);
        checkFile(file);

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(FILEPATH))){
            if(bufferedReader.readLine() == null){
                System.out.println("File is empty!");
                System.out.println("Creating a new table....");
                newTable(dynamicTable);
            } else {
                System.out.println("File is not empty!");
                tableMenuOption();
                int menuOption = inputTableProgressValidation(input, "Choose table progress: ");
                switch(menuOption){
                    case 1:
                        loadTable(dynamicTable);
                        break;
                    case 2:
                        newTable(dynamicTable);
                        break;
                }
            }
        }catch (IOException e){
            System.out.println("Error reading file: " +e.getMessage());
        }
    }

    public static void checkFile (File file) throws IOException{
        if (!file.exists()){
            file.createNewFile();
            System.out.println("File created!");
        } else {
            System.out.println("File already exists!");
        }
    }



    public static void newTable(DynamicTable dynamicNewTable){
        int rows = inputValidation(input, "Enter number of rows: ");
        int columns = inputValidation(input, "Enter number of columns: ");

        input.nextLine();

        dynamicNewTable = new TableManager(rows, columns);
        dynamicNewTable.generateRandomCharacters();

        tableFeaturesHandler(dynamicNewTable);

    }

    public static void loadTable(DynamicTable dynamicTable){
        dynamicTable.readFile();
        tableFeaturesHandler(dynamicTable);
    }

    public static void tableFeaturesHandler(DynamicTable obj){
        boolean flag = false;

        do {
            obj.printTable();
            obj.menuOption();

            int option = inputMenuValidation(input, "Enter your choice: ");

            input.nextLine();
            switch (option) {
                case 1:
                    String searchChar = inputSearchValidation(input, "Enter character/s you want to search: ");
                    obj.searchCharacter(searchChar);
                    break;

                case 2:
                    obj.updateTable();
                    break;

                case 3:
                    int rows = inputValidation(input, "Enter number of rows: ");
                    int columns = inputValidation(input, "Enter number of columns: ");

                    obj = new TableManager(rows,columns);
                    obj.generateRandomCharacters();
                    break;

                case 4:
                    obj.sortTable();
                    break;

                case 5:
                    obj.addColumn();
                    break;

                case 6:
                    obj.saveAndExit();
                    flag = true;
                    break;

                default:
                    System.out.println("Invalid input!");
                    break;

            }
        }while(!flag);
    }

    public static int inputValidation (Scanner input, String instructions){
        return getPositiveInteger(input,instructions);
    }

    public static int inputMenuValidation(Scanner input, String instructions){
        boolean flag = true;
        int menuInput;
        do{
            menuInput = getPositiveInteger(input,instructions);

            switch (menuInput){
                case 1:
                    System.out.println("Searching a character/s on a table....");
                    flag = false;
                    break;
                case 2:
                    System.out.println("Updating a value on a table....");
                    flag = false;
                    break;
                case 3:
                    System.out.println("Resetting....");
                    flag = false;
                    break;
                case 4:
                    System.out.println("Sorting....");
                    flag = false;
                    break;
                case 5:
                    System.out.println("Adding Column....");
                    flag = false;
                    break;
                case 6:
                    System.out.println("Exiting and Saving....");
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid input! Please select on the given choices");
                    break;
            }
        }while (flag);

        return menuInput;
    }

    public static int inputTableProgressValidation(Scanner input, String instructions){
        boolean flag;
        int tableInput;
        do{
            tableInput = getPositiveInteger(input, instructions);

            switch (tableInput){
                case 1:
                    System.out.println("Loading Table....");
                    flag = true;
                    break;
                case 2:
                    System.out.println("Creating Table....");
                    flag = true;
                    break;
                default:
                    System.out.println("Invalid choice!");
                    flag = false;
                    break;
            }
        }while(!flag);
        return tableInput;
    }

    public static String inputSearchValidation(Scanner input, String instructions){
        String inputSearch;
        do{
            inputSearch = getValidString(input, instructions);

            if (inputSearch.length() > 3){
                System.out.println("Invalid Input!, Maximum is 3 characters");
            }

        }while(inputSearch.length() > 3);
        return inputSearch;
    }

    public static int getPositiveInteger(Scanner input, String prompt){
        int numInput;
        do{
            System.out.print(prompt);
            while(!input.hasNextInt()){
                System.out.println("Please enter a positive integer!");
                System.out.print(prompt);
                input.next();
            }
            numInput = input.nextInt();
            if (numInput <= 0){
                System.out.println("Please enter a positive integer!");
            }
        }while(numInput <= 0);
        return numInput;
    }

    public static String getValidString(Scanner input, String prompt){
        String stringInput;
        do {
            System.out.print(prompt);
            stringInput = input.nextLine();
        }while (stringInput.isEmpty());

        return stringInput;
    }

    private static void tableMenuOption(){
        System.out.print("""  
                    +----------------TABLE------------------+
                    | [1] - Load Previous Table             |
                    | [2] - Create New Table                |
                    +---------------------------------------+
                    """);
    }
}