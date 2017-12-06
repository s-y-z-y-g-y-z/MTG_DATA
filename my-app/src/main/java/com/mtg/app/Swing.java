package com.mtg.app;

import com.mtg.http.HttpReq;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class Swing implements ActionListener {

    //Class calls
    App app = new App();

    //Swing stuff
    JFrame frame = new JFrame("Magical Tutor!");
    JTextField text = new JTextField(20);
    JLabel cardImage = new JLabel(new ImageIcon());
    JButton add = new JButton("Add");
    JButton remove = new JButton("Remove");
    JPanel left = new JPanel(new FlowLayout());
    JPanel right = new JPanel(new BorderLayout());
    JPanel center = new JPanel(new BorderLayout());
    JPanel top = new JPanel(new GridLayout(0,2));
    JPanel bot = new JPanel(new GridLayout(0,2));

    //Text areas
    TextArea left_text = new TextArea(5,15);
    TextArea right_text = new TextArea(5,15);

    public void buildFrame() {

        frame.setBackground(Color.black);
        frame.setLayout(new BorderLayout());

        buildTextField();
        buildImage(app.card.getImageUrl());
        buildPrice(app.getCardPrice());
        buildButtons();
        buildInfo(app.getCardArtist(),app.getCardRarity(),app.getCardSet());
        buildMenuBar();


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void buildImage(String image_url)
    {
        URL url;
        Image image = null;

        try
        {
            url = new URL(image_url);
            image = ImageIO.read(url);
        }
        catch (IOException io)
        {
            System.out.println("could not load file");
        }
        ImageIcon icon = new ImageIcon(image);
        cardImage.setIcon(icon);
        center.add(cardImage);

        frame.getContentPane().add(center, BorderLayout.CENTER);
    }

    public void changeImage(String image_url)
    {
        Image image = null;
        if (image_url.contains("http")) {
            try {
                URL url = new URL(image_url);
                image = ImageIO.read(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                image = ImageIO.read(new File(image_url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ImageIcon icon = new ImageIcon(image);
        cardImage.setIcon(icon);
    }

    public void buildTextField()
    {

        JButton search = new JButton("Search");
        search.addActionListener(this);
        search.setActionCommand("text");

        top.add(text);
        top.add(search);

        frame.getContentPane().add(top, BorderLayout.NORTH);
    }

    public void buildButtons()
    {
        add.addActionListener(this);
        add.setActionCommand("add");

        remove.addActionListener(this);
        remove.setActionCommand("remove");

        bot.add(add);
        bot.add(remove);

        frame.getContentPane().add(bot, BorderLayout.SOUTH);
    }

    public void buildPrice(String price)
    {
        left_text.setText(null);
        left_text.setEnabled(false);
        left_text.append("Price\n");
        left_text.append(price);

        left.setName("Price:");
        left.add(left_text);

        frame.getContentPane().add(left, BorderLayout.WEST);
    }

    public void buildInfo(String artist, String rarity, String set)
    {
        right_text.setText(null);
        right_text.setEnabled(false);
        right_text.append("Card Info\n=========\n");
        right_text.append("Rarity:\n" + rarity + "\n");
        right_text.append("Set:\n" + set + "\n");
        right_text.append("Artist:\n" + artist + "\n");

        right.setName("Info");
        right.add(right_text);

        frame.getContentPane().add(right, BorderLayout.EAST);
    }

    public void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        menuBar.add(file);

        JMenuItem _new = new JMenuItem("New");
        file.add(_new);
        _new.addActionListener(this);
        _new.setActionCommand("new");
        file.addSeparator();

        JMenuItem exit = new JMenuItem("Exit", new ImageIcon("images/middle.gif"));
        file.add(exit);
        exit.addActionListener(this);
        exit.setActionCommand("exit");

        JMenu tool = new JMenu("Tools");
        menuBar.add(tool);

        JMenuItem help = new JMenuItem("Help");
        tool.add(help);
        tool.addSeparator();
        help.addActionListener(this);
        help.setActionCommand("help");

        JMenuItem loadData = new JMenuItem("Load Card Data");
        tool.add(loadData);
        loadData.addActionListener(this);
        loadData.setActionCommand("rebuildData");

        frame.setJMenuBar(menuBar);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("add"))
        {
            app.addCard();
        }
        else if (e.getActionCommand().equals("remove"))
        {
            app.removeCard();
        }
        else if (e.getActionCommand().equals("text"))
        {
            app.searchCard();
        }
        else if (e.getActionCommand().equals("exit"))
        {
            int result = JOptionPane.showConfirmDialog(frame,"Do you wish to exit Magical tutor?","Exit",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION)
            {
                app.exitApp();
            }

        }
        else if (e.getActionCommand().equals("rebuildData"))
        {
            int result = JOptionPane.showConfirmDialog(frame, "Loading card data can take up to 30 minutes.\nContinue?","Warning",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION)
            {
                app.getAllCards();
            }
        }
        else if (e.getActionCommand().equals("help"))
        {
            JOptionPane.showMessageDialog(frame,"Type in the textfield and click search to search for a card.\n" +
                                                         "Use the Add and Remove buttons to add the displayed card to your list of cards.\n" +
                                                         "Use File>Exit or the red X in the corner of the screen to exit.\n");
        }
        else if (e.getActionCommand().equals("new"))
        {
            app.database = new File(JOptionPane.showInputDialog("Please enter a file name:"));
        }
    }

}
