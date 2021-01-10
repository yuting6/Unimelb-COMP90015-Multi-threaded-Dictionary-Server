import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.*;

public class Client extends JFrame {

    private static String ip;
    private static int port;

    private static Socket socket = null;

    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("Need one IP number and one port number");
            System.exit(-1);
        }

        ip = args[0];
        // check IP
        if(!ip.equals("localhost")){
            System.out.println("wrong ip address");
            System.exit(0);
        }

        if (Integer.parseInt(args[1]) <= 1024 || Integer.parseInt(args[1]) >= 49151) {
            System.out.println("Invalid Port Number");
            System.out.println("Should be a number between 1024 and 49151");
            System.exit(0);
        }
        try{
            port = Integer.parseInt(args[1]);
        }
        catch(Exception e){
            System.out.println("Invalid port number");
            System.exit(0);
        }

        clientFrame();
        clientSocket(ip, port);

    }

    private static void clientSocket(String ip, int port){
        try {
            socket = new Socket(ip, port);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("here");
        }
    }

    //

    private static String connnect(String action, String word, String meaning) {


            try {
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                String req = (action + "///" + word + "///" + meaning);
                pw.println(req);
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                BufferedReader br = new BufferedReader(inputStreamReader);

                String result = br.readLine();
                return result;
            } catch (Exception e) {
                return "Unable to connect";
            }
    }

    private static void checkRequest(TextField input_word){

    }

    private static void clientFrame(){
        JFrame frame = new JFrame("Dictionary");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1));
        panel.setBounds(20, 5, 550, 400);


        JLabel userEnter = new JLabel("User Enter:");
        //userEnter.setLayout(null);
        //userEnter.setBounds(10,20,300, 20);
        panel.add(userEnter);

        JLabel description1 = new JLabel("Enter the word you want to search, add or remove");
        //description1.setLayout(null);
        //description1.setBounds(10,70,300, 20);
        panel.add(description1);

        JTextField inputWord = new JTextField();
        //inputWord.setBounds(10,100,300, 20);
        inputWord.setText("");
        panel.add(inputWord);

        JLabel description2 = new JLabel("Enter the meaning if you want to add a word into the dictionary");
        //description2.setBounds(10,130,300, 20);
        panel.add(description2);

        JTextField inputMeaning = new JTextField();
        //inputMeaning.setBounds(10,160,300, 100);
        inputMeaning.setText("");
        panel.add(inputMeaning);

        JLabel description3 = new JLabel("Result:");
        //description2.setBounds(10,130,300, 20);
        panel.add(description3);

        JTextArea output = new JTextArea();
        //output.setBounds(10, 290, 300, 100);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        panel.add(output);

        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(1, 3));
        panel1.setBounds(20, 435, 550, 40);

        JButton searchButton = new JButton("Search");
        //searchButton.setBounds(350, 20, 80, 30);
        panel1.add(searchButton);

        JButton addButton = new JButton("Add");
        //searchButton.setBounds(350, 60, 80, 30);
        panel1.add(addButton);

        JButton removeButton = new JButton("Remove");
        //searchButton.setBounds(350, 100, 80, 30);
        panel1.add(removeButton);

        frame.add(panel);
        frame.add(panel1);
        frame.setVisible(true);

        searchButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {

                String input_word = inputWord.getText();
                String input_meaning = inputMeaning.getText();
                String action = "search";

                clientSocket(ip, port);

                output.setText("");
                output.append(connnect(action, input_word, input_meaning));
            }
        });

        addButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {

                String input_word = inputWord.getText();
                String input_meaning = inputMeaning.getText();
                String action = "add";

                clientSocket(ip, port);

                output.setText("");
                output.append(connnect(action, input_word, input_meaning));
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String input_word = inputWord.getText();
                String input_meaning = inputMeaning.getText();
                String action = "remove";

                clientSocket(ip, port);

                output.setText("");
                output.append(connnect(action, input_word, input_meaning));
            }
        });
    }


}
