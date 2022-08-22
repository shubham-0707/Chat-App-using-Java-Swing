import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import java.awt.*;

public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Declare Components..
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("TimesNewRoman" , Font.PLAIN ,20);



    // Constructor
    public Client(){
        try{
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1" , 8080);
            System.out.println("Connection Done...");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());
//

            createGUI();
            handleEvents();
            startReading();
//            startWriting();



        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10){
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }

    private void createGUI(){
        // gui code...

        this.setTitle("Client Handle");
        this.setSize(600 , 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // coding for component....

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon(""));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20 , 20 , 20 , 20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //setting frame layout..
        this.setLayout(new BorderLayout());

        // adding the components to frame..
        this.add(heading , BorderLayout.NORTH);
        JScrollPane jscrollpane = new JScrollPane(messageArea);
        this.add(jscrollpane, BorderLayout.CENTER);
        this.add(messageInput , BorderLayout.SOUTH);




        this.setVisible(true);
    }

    // Start reading [Method]
    public void startReading(){
        // thread read karke deta rahega...
        Runnable r1 = ()->{
            System.out.println("Reader Started...");
            try {
                while (!socket.isClosed()) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat..");
                        JOptionPane.showMessageDialog(this , "Server terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                    //System.out.println("Server : " + msg);
                    messageArea.append("Server : "+msg+"\n");
                }
            }catch(Exception e){
                //e.printStackTrace();
                System.out.println("connection Closed...");
            }
        };
        new Thread(r1).start();
    }

    // Start Writing [Method]
    public void startWriting(){


        Runnable r2 = ()-> {
            System.out.println("Writer Started...");

            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                }

            }catch(Exception e){
                //e.printStackTrace();

                System.out.println("Connection closed...");
            }
        };

        new Thread(r2).start();


    }


    public static void main(String[] args) {
        System.out.println("This is client...");
        new Client();
    }
}
