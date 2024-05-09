import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Publisher extends Application {

    public static void main(String[] args) {
        launch(args);

//        while (true) {
//            sendRequestToServer();
//        }
    }

    //TODO tutaj ma być GUI

    //TODO Tutaj mamy tworzyć nowe tematy

    //TODO Tutaj mamy tworzyć nowe artykuły

    static Socket clientSocket;
    private static PrintWriter out = null;
    static BufferedReader in;
    private static final int MAIN_SERVER_PORT = 5000;
    private static final String SERVER_NAME = "localhost";


//    public static void sendRequestToServer() {
//
//        String inputRequestType = null;
//
//        log("1 - Dodaj nowy Topic");
//        log("2 - Dodaj usuń Topic");
//        Scanner scanner = new Scanner(System.in);
//
//        inputRequestType = scanner.nextLine();
//
//        switch (Integer.parseInt(inputRequestType)) {
//            case 1:
//                log("Wprowadz nazwe topica do dodania");
//                String inputTopicNameToAdd = scanner.nextLine().toLowerCase();
//                AddNewTopic(inputTopicNameToAdd);
//                break;
//            case 2:
//                log("Wprowadz nazwe topica do usuiniecia");
//                String inputTopicNameToRemove = scanner.nextLine().toLowerCase();
//                RemoveTopic(inputTopicNameToRemove);
//                break;
//            case 3:
//                log("Aktualne topici");
//
//                getExistingTopicsFromServer();
//                break;
//        }
//        //scanner.close();  // Jak zamykam to więcej w pętli nie mogę użyć scannera
//    }


    public void AddNewTopic(String topicName) {
        String commandType = "addnewtopic";
        try {
            Charset charset = StandardCharsets.UTF_8;
            SocketChannel publisherSocket = SocketChannel.open(new InetSocketAddress("localhost", 5000));
            publisherSocket.configureBlocking(false);
            publisherSocket.write(charset.encode(CharBuffer.wrap("Publisher:" + commandType + ":" + topicName + "\n")));
            publisherSocket.close();                      // - zamknięcie kanału
            publisherSocket.socket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void RemoveTopic(String topicName) {
        String commandType = "removetopic";
        try {
            Charset charset = StandardCharsets.UTF_8;
            SocketChannel publisherSocket = SocketChannel.open(new InetSocketAddress("localhost", 5000));
            publisherSocket.configureBlocking(false);
            publisherSocket.write(charset.encode(CharBuffer.wrap("Publisher:" + commandType + ":" + topicName + "\n")));
            publisherSocket.close();                      // - zamknięcie kanału
            publisherSocket.socket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getExistingTopicsFromServer() {

        String commandType = "getexistingtopics";
        List<String> existingTopicsToReturn = new ArrayList<>();

        try {
            Charset charset = StandardCharsets.UTF_8;
            SocketChannel clientSocket = SocketChannel.open(new InetSocketAddress("localhost", 5000));
            clientSocket.configureBlocking(false);
            clientSocket.write(charset.encode(CharBuffer.wrap("Publisher:" + commandType + "\n")));


            String sss = readFromServer(clientSocket);

            if(!sss.equals("none")){
                String[] serverResponse = sss.split(" ");

                System.out.println("jestem tutaj");

                for (String topic : serverResponse) {
                    existingTopicsToReturn.add(topic);
                    System.out.println(topic);
                }

            }



            clientSocket.close();
            clientSocket.socket().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return existingTopicsToReturn;
    }


    public void AddNewNews(String text, String topicName) {
        String commandType = "addnewnews";
        try {
            Charset charset = Charset.forName("ISO-8859-2");
            SocketChannel publisherSocket = SocketChannel.open(new InetSocketAddress("localhost", 5000));
            publisherSocket.configureBlocking(false);
            publisherSocket.write(charset.encode(CharBuffer.wrap("Publisher:" + commandType + ":" + topicName + ":"+ text +"\n")));
            publisherSocket.close();                      // - zamknięcie kanału
            publisherSocket.socket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String readFromServer(SocketChannel clientSocket) {

        try {

            ByteBuffer inBuf = ByteBuffer.allocateDirect(1024);
            Charset charset = StandardCharsets.UTF_8;
            CharBuffer cbuf = null;

            while (true) {

                inBuf.clear();
                int readBytes = clientSocket.read(inBuf);

                if (readBytes == 0) {

                    continue;

                } else if (readBytes == -1) {

                    break;
                } else {

                    inBuf.flip();

                    cbuf = charset.decode(inBuf);

                    String odSerwera = cbuf.toString();

                    System.out.println("Publisher: serwer właśnie odpisał ... " + odSerwera);
                    cbuf.clear();


                    if (odSerwera.equals("Bye")) break;

                    return odSerwera;
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }


    public static void log(String msg) {
        System.out.println("[Publisher]: " + msg);
    }

    @Override
    public void start(Stage stage) throws Exception {
        PublisherUI simpleUI = new PublisherUI();
        simpleUI.uI(stage);
    }

}
