import java.net.*;
import java.io.*;

public class Server {

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;


    // Constructor
    public Server(){

        try{
           server =  new ServerSocket(8080);

            System.out.println("The Server is ready to accept connection...");
            System.out.println("Waiting...");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    // Start Reading [Method]
    public void startReading(){
        // thread read karke deta rahega...
        Runnable r1 = ()->{
            System.out.println("Reader Started...");

            try {
                while (!socket.isClosed()) {


                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client terminated the chat..");
                        socket.close();
                        break;
                    }

                    System.out.println("Client : " + msg);
                }
            }catch(Exception e){
                //e.printStackTrace();
                System.out.println("connection Closed..");
            }
        };
        new Thread(r1).start();
    }


    // Start Writing [Method]
    public void startWriting(){


        Runnable r2 = ()->{
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
                System.out.println("Connection Closed...");
            }
        };

        new Thread(r2).start();


    }

    public static void main(String[] args) {
        System.out.println("This is server and this is going to start...");
        new Server();
    }
}
