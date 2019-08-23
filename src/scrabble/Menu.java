package scrabble;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Menu extends JFrame implements ActionListener{
    private JButton play;
    private JButton exit;
    private JButton help;
    private  JButton easy = new JButton("Easy");
    private JButton medium = new JButton("Medium");
    private JButton hard = new JButton("Hard");
    private JButton back = new JButton("Back");

    private JLabel prompt = new JLabel("Select a difficulty");
    private JLabel helpText = new JLabel("Please refer to User's Guide: goo.gl/QvERjJ", SwingConstants.CENTER);

    public JFrame mainFrame;

    public Menu() {
        mainFrame = new JFrame("Scrabble");
        mainFrame.setSize(720, 720);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });

        JLabel title = new JLabel("SCRABBLE");
        title.setBounds(150, 5, 500, 70);
        title.setFont(new Font("Arial", Font.BOLD, 72));
        title.setForeground(new Color(53, 50, 56));
        JLabel credits = new JLabel("by Victor, Fatima, and Anthony");
        credits.setBounds(215, 60, 500, 50);
        credits.setFont(new Font("Arial", Font.BOLD, 18));
        credits.setForeground(new Color(53, 50, 56));

        play = new JButton("Play");
        help = new JButton("Help");
        exit = new JButton("Exit");

        prompt.setFont(new Font("Arial", Font.BOLD, 24));
        prompt.setVisible(false);
        prompt.setBounds(250, 150 ,300, 100);
        helpText.setVisible(false);
        helpText.setFont(new Font("Arial", Font.PLAIN, 12));

        play.setActionCommand("play");
        play.addActionListener(this);
        help.setActionCommand("help");
        help.addActionListener(this);
        exit.setActionCommand("exit");
        exit.addActionListener(this);
        easy.setActionCommand("easy");
        easy.addActionListener(this);
        easy.setVisible(false);
        medium.setActionCommand("medium");
        medium.addActionListener(this);
        medium.setVisible(false);
        hard.setActionCommand("hard");
        hard.addActionListener(this);
        hard.setVisible(false);
        back.setActionCommand("back");
        back.addActionListener(this);
        back.setVisible(false);

        play.setBounds(300, 300, 100, 50);
        play.setBackground(new Color(190, 124, 77));
        play.setForeground(Color.WHITE);
        play.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        help.setBounds(300, 360, 100, 50);
        help.setBackground(new Color(193, 180, 174));
        help.setForeground(Color.WHITE);
        help.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exit.setBounds(300, 420, 100, 50);
        exit.setBackground(new Color(146, 20, 12));
        exit.setForeground(Color.WHITE);
        exit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        easy.setBounds(300, 300, 100, 50);
        easy.setBackground(new Color(230, 25, 14));
        easy.setForeground(Color.WHITE);
        easy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        medium.setBounds(300, 360, 100, 50);
        medium.setBackground(new Color(180, 24, 13));
        medium.setForeground(Color.WHITE);
        medium.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hard.setBounds(300, 420, 100, 50);
        hard.setBackground(new Color(131, 24, 13));
        hard.setForeground(Color.WHITE);
        hard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        back.setBounds(310, 640, 80, 40);
        back.setBackground(new Color(53, 50, 56));
        back.setForeground(Color.WHITE);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        mainFrame.add(title);
        mainFrame.add(credits);
        mainFrame.add(play);
        mainFrame.add(help);
        mainFrame.add(exit);
        mainFrame.add(prompt);
        mainFrame.add(back);
        mainFrame.add(easy);
        mainFrame.add(medium);
        mainFrame.add(hard);
        mainFrame.add(helpText);
        mainFrame.setVisible(true);

    }
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("play")) {
            play.setVisible(false);
            help.setVisible(false);
            exit.setVisible(false);
            helpText.setVisible(false);
            //mainFrame.setLayout(null);

            back.setVisible(true);
            prompt.setVisible(true);
            easy.setVisible(true);
            medium.setVisible(true);
            hard.setVisible(true);
        }
        else if (e.getActionCommand().equals("help")) {
            play.setVisible(false);
            help.setVisible(false);
            exit.setVisible(false);
            easy.setVisible(false);
            medium.setVisible(false);
            hard.setVisible(false);

            back.setVisible(true);
            helpText.setVisible(true);
        }
        else if (e.getActionCommand().equals("back")) {
            play.setVisible(true);
            help.setVisible(true);
            exit.setVisible(true);
            helpText.setVisible(false);
            prompt.setVisible(false);
            back.setVisible(false);
        }
        else if (e.getActionCommand().equals("easy")) {
            //TODO easy mode
            mainFrame.dispose();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        Scrabble s=new Scrabble(1);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        else if (e.getActionCommand().equals("medium")) {
            //TODO medium mode
            mainFrame.dispose();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        Scrabble s=new Scrabble(2);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        else if (e.getActionCommand().equals("hard")) {
            //TODO hard mode
            mainFrame.dispose();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        Scrabble s=new Scrabble(3);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        else if (e.getActionCommand().equals("exit")) {
            mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
        }
    }
    public static void main(String[] args) throws IOException{

    }
}
