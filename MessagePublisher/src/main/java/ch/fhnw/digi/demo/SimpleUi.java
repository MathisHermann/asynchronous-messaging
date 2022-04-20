package ch.fhnw.digi.demo;

import javax.annotation.PostConstruct;
import javax.swing.*;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.fhnw.digi.demo.Publisher;

@Component
public class SimpleUi extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextArea lbl;
    private JButton btnNewIncident;

    @PostConstruct
    void init() {
        setSize(850, 400);
        setTitle("Message Publisher");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container c = getContentPane();
        //Creating a JPanel for the JFrame
        JPanel panel = new JPanel();
        //setting the panel layout as null
        panel.setLayout(null);
        //adding a label element to the panel
        lbl = new JTextArea();
        lbl.setBounds(50, 80, 750, 100);
        lbl.setEditable(false);
        panel.add(lbl);
        // changing the background color of the panel to yellow
        btnNewIncident = new JButton("New Incident");
        btnNewIncident.setBounds(70,200,200,50);
        btnNewIncident.addActionListener(event -> {
            Publisher.addMessage();
            System.out.println("New Incident created.");
        });

        panel.add(btnNewIncident);
        //adding the panel to the Container of the JFrame
        c.add(panel);

        setVisible(true);
    }

    public void setMessage(String string) {
        lbl.setText("Message Publisher running ...\n\n" + string + "\n\nClose window to terminate");
    }

}
