package com.mtg.app;

import com.mtg.http.HttpReq;
import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    static List<Card> data;
    static Swing gui = new Swing();
    static HttpReq request = new HttpReq();
    static Card card;

    private File MTG_DATA = new File("MTG_Cards");
    private File MTG_IDS = new File("MTG_IDs");

    public static void main( String[] args )
    {
        int multverseId = 1;
        card = CardAPI.getCard(multverseId);

        gui.buildFrame();
    }

    public void addCard()
    {
        File fileName = new File("sample");

        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
            bw.write(card.getName() + "\n");
            bw.close();
        }
        catch (IOException e)
        {
            System.out.println("Could not write to file " + fileName);
        }

        JOptionPane.showMessageDialog(gui.frame, card.getName() + " added.");
    }
    public void removeCard()
    {
        File fileName = new File("sample");
        File tempFile = new File("tempSample");

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
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
            tempFile.renameTo(fileName);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Could not open " + fileName);
        }
        catch (IOException f)
        {
            System.out.println("Error reading from " + fileName);
        }

        JOptionPane.showMessageDialog(gui.frame, card.getName() + " removed.");
    }

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

            JOptionPane.showMessageDialog(gui.frame, "Card Not Found!");
        }
    }

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

    public void exitApp()
    {
        System.out.println("Bye, Bye!");
        System.exit(1);
    }

    // USE ONLY TO GENERATE THE DATABASE OF CARDS
    public void getAllCards()
    {
        data = CardAPI.getAllCards();
        File fileName = new File("MTG_IDs");

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
    }


}
