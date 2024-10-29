//Generates text based on existing text
//Created by James Vanderhyde, 28 October 2024

package dictionaryexample;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class TextPrediction 
{
    public static class Counter
    {
        private int count = 0;
        
        public int get()
        {
            return this.count;
        }
        
        public void increment()
        {
            this.count++;
        }
        
        @Override
        public String toString()
        {
            return ""+this.count;
        }
    }
    
    private static final HashMap<String,HashMap<String,Counter>> table = new HashMap<>();
    private static final Random rng = new Random();
    
    public static void main(String[] args)
    {
        //Read the document into a string
        String url = "https://www.gutenberg.org/cache/epub/43936/pg43936.txt";
        String content = "";
        try (Scanner scanner = new Scanner(new java.net.URI(url).toURL().openStream(),
            java.nio.charset.StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            if (scanner.hasNext())
                content = scanner.next();
            else
                System.out.println("Document contains no data.");
        } 
        catch (URISyntaxException | IOException e) 
        {
            System.out.println("Could not read document:");
            System.out.println(e);
        }
        
        //Split into words, leaving out all punctuation and spaces
        String[] words = content.split("\\W");
        
        //Remove blank strings and front and end matter
        int numInputWords = 0;
        boolean foundStart = false;
        for (String word : words) 
        {
            //Stop before the license and other end matter
            if (word.equals("END"))
                break;
            
            //Start after the front matter
            if (word.equals("START"))
            {
                foundStart = true;
                continue;
            }
            
            if (foundStart)
            {
                //Skip over blank strings
                if (word.trim().length() > 0)
                {
                    //Add word to array
                    words[numInputWords] = word.trim().toLowerCase();
                    numInputWords++;
                }
            }
        }
        
        //Put each word into the frequency table
        for (int i=0; i<numInputWords; i++)
        {
            String word = words[i];
            String following = words[i+1];
            if (!table.containsKey(word))
                table.put(word, new HashMap<>());
            var subtable = table.get(word);
            if (!subtable.containsKey(following))
                subtable.put(following, new Counter());
            subtable.get(following).increment();
        }
        
        //Generate
        int numWords = 100;
        String currentWord = "the";
        while (numWords > 0)
        {
            //Print the current word
            System.out.print(currentWord+" ");
            
            //Get the table of words following the current word
            String nextWord = null;
            var subtable = table.get(currentWord);
            int total = 0;
            for (Map.Entry<String,Counter> entry : subtable.entrySet())
                total += entry.getValue().get();
            int random = rng.nextInt(total);
            
            //Visualize for debugging
            //System.out.println();
            //System.out.println("Total: "+total+", random: "+random);
            //System.out.println(subtable);
            
            //Choose a random following word
            for (Map.Entry<String,Counter> entry : subtable.entrySet())
            {
                if (random < entry.getValue().get())
                {
                    nextWord = entry.getKey();
                    break;
                }
                random -= entry.getValue().get();
            }
            
            //Set up for next iteration
            numWords--;
            currentWord = nextWord;
        }
        System.out.println();
    }
}
