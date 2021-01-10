import java.io.*;
import java.net.Socket;

public class HandleNewThread implements Runnable {

    private File dict;
    private Socket client;
    private File tempDict;

    public HandleNewThread(Socket client, File dict, File tempDict){
        this.dict = dict;
        this.client = client;
        this.tempDict = tempDict;
    }

    public void run() {
        //while (true){
            try (Socket clientSocket = client) {
                BufferedReader input = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream(), "UTF-8"));
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(
                        clientSocket.getOutputStream(), "UTF-8"));

                //待修改, 加上gui command
                //還有有關input的指令
                String req;
                req = input.readLine();
                String[] divide = req.split("///");
                String action;
                String word;
                String meaning = "";
                //while (input.readLine()!=null) {
                    if (divide.length == 3) {
                        action = divide[0];
                        word = divide[1];
                        meaning = divide[2];
                    } else {
                        action = divide[0];
                        word = divide[1];
                    }


                    switch (action) {
                        case "search":
                            synchronized (this) {
                                try {
                                    FileReader fr = new FileReader(dict);
                                    BufferedReader br = new BufferedReader(fr);
                                    String line = "";
                                    String[] find = null;
                                    boolean exist = false;
                                    while ((line = br.readLine()) != null) {
                                        find = line.split(" : ", 2);
                                        if (find[0].equals(word)) {
                                            output.write("Word found!" + "   " + line + "\n");
                                            output.flush();
                                            exist = true;
                                        }
                                    }
                                    if (!exist) {
                                        output.write("Word not found." + "\n");
                                        output.flush();
                                    }
                                    br.close();
                                    fr.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        case "add":
                            synchronized (this) {
                                if (word.equals("") || meaning.equals("")) {
                                    output.write("Need to enter one word and it's meaning." + "\n");
                                    output.flush();
                                    break;
                                }
                                if (checkExist(word)) {
                                    output.write("Word already exist." + "\n");
                                    output.flush();
                                    break;
                                }
                                try {
                                    FileWriter fw = new FileWriter(dict, true);
                                    BufferedWriter bw = new BufferedWriter(fw);

                                    bw.newLine();
                                    bw.write(word);
                                    bw.write(" : ");
                                    bw.write(meaning);
                                    bw.flush();

                                    bw.close();
                                    fw.close();

                                    output.write("Word added" + "\n");
                                    output.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        case "remove":
                            synchronized (this) {
                                if (word.equals("")) {
                                    output.write("Need to enter the word you want to remove." + "\n");
                                    output.flush();
                                    break;
                                }
                                if (!checkExist(word)) {
                                    output.write("The remove word doesn't exist!" + "\n");
                                    output.flush();
                                    break;
                                }
                                try {
                                    boolean exist = false;
                                    FileReader fr = new FileReader(dict);
                                    BufferedReader br = new BufferedReader(fr);
                                    String line = "";
                                    String[] find = null;

                                    FileWriter fw = new FileWriter(tempDict);
                                    BufferedWriter bw = new BufferedWriter(fw);

                                    while ((line = br.readLine()) != null) {
                                        find = line.split(" : ", 2);
                                        if (find[0].equals(word)) {
                                            exist = true;
                                        }
                                        //把不是要remove的字暫存到tempDict中
                                        else {
                                            bw.write(line);
                                            bw.write("\r\n");
                                        }
                                    }
                                    br.close();
                                    fr.close();
                                    bw.close();
                                    fw.close();

                                    if (exist) {
                                        try {
                                            FileReader fr1 = new FileReader(tempDict);
                                            BufferedReader br1 = new BufferedReader(fr1);

                                            FileWriter fw1 = new FileWriter(dict);
                                            BufferedWriter bw1 = new BufferedWriter(fw1);

                                            while ((line = br1.readLine()) != null) {
                                                bw1.write(line);
                                                bw1.write("\r\n");
                                            }
                                            br1.close();
                                            fr1.close();
                                            bw1.close();
                                            fw1.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        output.write("the word has been removed." + "\n");
                                        output.flush();
                                    }

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                    }

                //}

                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        //}//until the client close the application
    }

    private boolean checkExist(String word){
        boolean checkExist = false;
        try {
            FileReader fr = new FileReader(dict);
            BufferedReader br = new BufferedReader(fr);
            String line="";
            String [] find = null;

            while ((line=br.readLine())!=null) {
                find = line.split(" : ", 2);
                if (find[0].equals(word)){
                    checkExist = true;
                    break;
                }
            }
            br.close();
            fr.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return checkExist;
    }

}
