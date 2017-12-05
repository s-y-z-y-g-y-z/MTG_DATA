package com.mtg.app;

import com.mtg.http.HttpReq;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class Swing implements ActionListener {
    //Class calls
    App app = new App();
    HttpReq req = new HttpReq();
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

    TextArea left_text = new TextArea(5,10);
    TextArea right_text = new TextArea(10,10);


    public void buildFrame() {

        frame.setBackground(Color.black);
        frame.setLayout(new BorderLayout());

        buildTextField();
        buildImage(app.card.getImageUrl());
        buildPrice(app.getCardPrice());
        buildButtons();
        buildInfo();
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
        left.setName("Price:");
        left_text.append("Price\n");
        left_text.append("$" + price);
        left.add(left_text);
        frame.getContentPane().add(left, BorderLayout.WEST);
    }

    public void buildInfo()
    {
        right_text.setText(null);
        right.setName("Info");
        right_text.append("Card Info:\n");

        right.add(right_text);
        frame.getContentPane().add(right, BorderLayout.EAST);
    }

    public void buildMenuBar() {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

//Create the menu bar.
        menuBar = new JMenuBar();

//Build the first menu.
        menu = new JMenu("A Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(menu);

//a group of JMenuItems
        menuItem = new JMenuItem("A text-only menu item",
                KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        menu.add(menuItem);

        menuItem = new JMenuItem("Both text and icon",
                new ImageIcon("images/middle.gif"));
        menuItem.setMnemonic(KeyEvent.VK_B);
        menu.add(menuItem);

        menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menu.add(menuItem);

//a group of radio button menu items
        menu.addSeparator();
        ButtonGroup group = new ButtonGroup();
        rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_R);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Another one");
        rbMenuItem.setMnemonic(KeyEvent.VK_O);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

//a group of check box menu items
        menu.addSeparator();
        cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
        cbMenuItem.setMnemonic(KeyEvent.VK_C);
        menu.add(cbMenuItem);

        cbMenuItem = new JCheckBoxMenuItem("Another one");
        cbMenuItem.setMnemonic(KeyEvent.VK_H);
        menu.add(cbMenuItem);

//a submenu
        menu.addSeparator();
        submenu = new JMenu("A submenu");
        submenu.setMnemonic(KeyEvent.VK_S);

        menuItem = new JMenuItem("An item in the submenu");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, ActionEvent.ALT_MASK));
        submenu.add(menuItem);

        menuItem = new JMenuItem("Another item");
        submenu.add(menuItem);
        menu.add(submenu);

//Build second menu in the menu bar.
        menu = new JMenu("Another Menu");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription(
                "This menu does nothing");
        menuBar.add(menu);

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
    }
}
