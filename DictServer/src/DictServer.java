import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ServerSocketFactory;

public class DictServer {

    private static int port;
    private static File dict, tempDict;
    private static int count = 0;

    public static void main(String[] args) {
        if (args.length != 2){
            System.out.println("Need one port number and the dictionary's file path.");
            System.exit(-1);
        }
        else if (Integer.parseInt(args[0]) <= 1024 || Integer.parseInt(args[0]) >= 49151) {
            System.out.println("Invalid Port Number");
            System.out.println("Should be a number between 1024 and 49151");
            System.exit(-1);
        }
        try{
            port = Integer.parseInt(args[0]);
        }
        catch(Exception e){
            System.out.println("Invalid port number");
            System.exit(-1);
        }

        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try(ServerSocket server = factory.createServerSocket(port)){
            //還沒創建字典//要改成input dictpath?
            dict = new File(args[1]);
            tempDict = new File("tempDict.txt");

            if(!dict.exists()){
                System.out.println("Dictionary does not exist");
                System.exit(0);
            }

            System.out.println("Dictionary is ready");
            System.out.println("Server is waiting for connection");

            while(true){
                Socket client = server.accept();
                count++;
                System.out.println("Client "+count+": Applying for connection.");
                System.out.println("Connected");
                // assign to a new thread
                //還沒寫這個class
                HandleNewThread newThread = new HandleNewThread(client, dict, tempDict);
                Thread t = new Thread(newThread);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println(e.getMessage());   //需要嗎？
        }

    }

    //public synchronized void clientDisconnect
}
