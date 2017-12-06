package com.mtg.app;

import com.mtg.http.HttpReq;
import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/*
// App class handles the logic of the program and is responsible for getting information
// from MagicTheGathering API via its library, and the Scryfall API using an Http request
// handled by the HttpReq class. This information is passed and displayed by the Swing class
 */
public class App 
{
    static List<Card> data;
    static Swing gui = new Swing();
    static HttpReq request = new HttpReq();
    static Card card;

    private File MTG_DATA = new File("MTG_Cards");
    private File MTG_IDS = new File("MTG_IDs");

    public File database = new File("sample");

    // Creates default card object which is a class from the MagicTheGathering API,
    // calls Swing function that builds the GUI
    public static void main( String[] args )
    {
        int multverseId = 1;
        card = CardAPI.getCard(multverseId);

        gui.buildFrame();
    }

    // Adds current card object to database, checks if good file
    public void addCard()
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(database, true));
            bw.write(card.getName() + "\n");
            bw.close();
        }
        catch (IOException e)
        {
            System.out.println("Could not write to file " + database);
        }

        JOptionPane.showMessageDialog(gui.frame, card.getName() + " added.");
    }

    // Creates tempFile and reads from file, if card name is found, it is skipped reading
    // and is added to the tempFile. The TempFIle is then renamed to the original file name.
    public void removeCard()
    {
        File tempFile = new File("temp" + database);

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(database));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
            String line;
            while((line = br.readLine()) != null)
            {
                String trimmedLine = line.trim();
                if (trimmedLine.equals(card.getName()))
                {
                    continue;
                }
                bw.write(line + System.getProperty("line.separator"));
            }

            br.close();
            bw.close();
            tempFile.renameTo(database);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Could not open " + database);
        }
        catch (IOException f)
        {
            System.out.println("Error reading from " + database);
        }

        JOptionPane.showMessageDialog(gui.frame, card.getName() + " removed.");
    }

    // The big one
    // =========================
    // Gets text from GUI text field and searches MTG_Cards file to see if input matches card name database.
    // If it wasn't then the user is notified in the GUI that their input was not found.
    // If found, program gets the line number and looks at the line number in MTG_IDs file to get the multiverse ID
    // multiverse ID is used with Scryfall API to get card information and display to GUI
    public void searchCard()
    {
        String price;
        String artist;
        String rarity;
        String set;

        try
        {
            System.out.println(gui.text.getText());
            int i = 0;
            int id = 0;
            boolean flag = false;
            LineNumberReader lnr = new LineNumberReader(new FileReader(MTG_DATA));

            while (lnr.readLine() != null)
            {
                String temp = lnr.readLine();
                if (temp.equals(gui.text.getText()))
                {
                    System.out.println("Found it!");
                    id = lnr.getLineNumber();
                    System.out.println(id);
                }
            }
            String id_num = Files.readAllLines(Paths.get("MTG_IDS")).get(id - 1);
            System.out.println(id_num);
            card = CardAPI.getCard(Integer.parseInt(id_num));

            price = getCardPrice();
            artist = getCardArtist();
            rarity = getCardRarity();
            set = getCardSet();

            System.out.println(card.getName() + " price = " + price);

            gui.changeImage(card.getImageUrl());
            gui.buildPrice("$" + price);
            gui.buildInfo(artist,rarity,set);
            gui.add.setEnabled(true);
            gui.remove.setEnabled(true);

            JOptionPane.showMessageDialog(gui.frame, "Card Found!");
        }
        catch (IOException e)
        {
            System.out.println("Could not read " + MTG_DATA);
        }
        catch (ArrayIndexOutOfBoundsException f)
        {
            System.out.println("Could not find card.");

            gui.changeImage("Images/card_back.jpg");
            gui.buildPrice("No price available");
            gui.buildInfo("No artist available","No rarity available","No set available");
            gui.add.setEnabled(false);
            gui.remove.setEnabled(false);

            JOptionPane.showMessageDialog(gui.frame, "Card Not Found!","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    // Getters that get string values from HttpReq which handles connection to Scryfall API to be passed
    // to Swing class for the GUI
    public String getCardPrice()
    {
        String price = request.multverseIDInfo(card.getMultiverseid(), "usd");
        return price;
    }
    public String getCardSet()
    {
        String set = request.multverseIDInfo(card.getMultiverseid(),"set_name");
        return set;
    }
    public String getCardArtist()
    {
        String artist = request.multverseIDInfo(card.getMultiverseid(),"artist");
        return artist;
    }
    public String getCardRarity()
    {
        String rarity = request.multverseIDInfo(card.getMultiverseid(),"rarity");
        return rarity;
    }

    // Exits the application
    public void exitApp()
    {
        System.out.println("Bye, Bye!");
        System.exit(1);
    }

    // USE ONLY TO GENERATE THE DATABASE OF CARDS
    // Uses MagicTheGathering API to get the list of all cards
    // writes the name info to a file, and multiverse ID values to another file
    public void getAllCards()
    {
        data = CardAPI.getAllCards();
        File fileName = new File("MTG_IDs");
        File fileName2 = new File("MTG_Cards");

        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
            for (Card card : data)
            {
                bw.write(card.getMultiverseid() + "\n");
            }
            bw.close();
        }
        catch (IOException e)
        {
            System.out.println("Could not write to file " + fileName);
        }

        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName2, true));
            for (Card card : data)
            {
                bw.write(card.getName() + "\n");
            }
            bw.close();
        }
        catch (IOException e)
        {
            System.out.println("Could not write to file " + fileName2);
        }
    }


}
