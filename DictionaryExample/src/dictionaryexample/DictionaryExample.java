//Example of dictionary and hash table
//Created by James Vanderhyde, 28 October 2024

package dictionaryexample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class DictionaryExample 
{
    //All the Final Fantasy VII characters are attending SXU.
    //We will read their names and ID numbers from a file.    
    //We will store the names and ID numbers in a hash table.
    private static final String[] table = new String[100];
    
    public static void main(String[] args) 
    {
        int numberOfItems = 0;
        try (Scanner fin = new Scanner(new BufferedReader(new FileReader("src/names.txt"))))
        {
            while (fin.hasNext())
            {
                numberOfItems++;
                
                int idNum = fin.nextInt();
                String name = fin.nextLine().trim();
                System.out.println("0"+idNum+" "+name);
                
                //Add the person to a hash table
                //The hash function is "mod 100"
                table[idNum % 100] = name;
            }
        } 
        catch (IOException e) 
        {
            System.out.println(e);
        }
        
        System.out.println("Read "+numberOfItems+" students.");
        System.out.println("There are "+checkMapSize()+" in the table.");
        
        System.out.println("Look up student #913647 (Expect Aerith)");
        System.out.println(table[913647 % 100]);
        //Hashing 913647 generated a collision.
    }
    
    public static int checkMapSize()
    {
        int size = 0;
        for (String name:table)
        {
            if (name != null)
                size++;
        }
        return size;
    }
}
