import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Publisher extends Application {

    public static void main(String[] args) {
        //launch(args);

        while (true) {
            sendRequestToServer();
        }
    }

    //TODO tutaj ma być GUI

    //TODO Tutaj mamy tworzyć nowe tematy

    //TODO Tutaj mamy tworzyć nowe artykuły

    static Socket clientSocket;
    private static PrintWriter out = null;
    static BufferedReader in;
    private static final int MAIN_SERVER_PORT = 5000;
    private static final String SERVER_NAME = "localhost";


    public static void sendRequestToServer() {

        String inputRequestType = null;

        log("1 - Dodaj nowy Topic");
        log("2 - Dodaj usuń Topic");
        Scanner scanner = new Scanner(System.in);

        inputRequestType = scanner.nextLine();

        switch (Integer.parseInt(inputRequestType)) {
            case 1:
                log("Wprowadz nazwe topica do dodania");
                String inputTopicNameToAdd = scanner.nextLine().toLowerCase();
                AddNewTopic(inputTopicNameToAdd);
                break;
            case 2:
                log("Wprowadz nazwe topica do usuiniecia");
                String inputTopicNameToRemove = scanner.nextLine().toLowerCase();
                RemoveTopic(inputTopicNameToRemove);
                break;
        }
        //scanner.close();  // Jak zamykam to więcej w pętli nie mogę użyć scannera
    }


    public static void AddNewTopic(String topicName) {
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

    public static void RemoveTopic(String topicName) {
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


    public static void AddNewNews(String topicName) {
        String commandType = "removetopic";
        try {
            Charset charset = Charset.forName("ISO-8859-2");
            SocketChannel publisherSocket = SocketChannel.open(new InetSocketAddress("localhost", 5000));
            publisherSocket.configureBlocking(false);
            publisherSocket.write(charset.encode(CharBuffer.wrap("Publisher:" + commandType + ":" + topicName + "\n")));
            publisherSocket.close();                      // - zamknięcie kanału
            publisherSocket.socket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public static String readMessageFromServer(SocketChannel socketChannel) throws IOException {
//
//
//        String odSerwera = null;
//
//        Charset charset = Charset.forName("ISO-8859-2");
//
//        ByteBuffer inBuf = ByteBuffer.allocateDirect(1024);
//        CharBuffer cbuf = null;
//
//        while (true) {
//            inBuf.clear();    // opróżnienie bufora wejściowego
//            int readBytes = socketChannel.read(inBuf); // czytanie nieblokujące
//
//            if (readBytes == 0) {
//                continue;
//            } else if (readBytes == -1) {
//                break;
//            } else {        // dane dostępne w buforze
//                System.out.println("coś jest od serwera, dane dostępne w buforze");
//
//                inBuf.flip();
//
//                cbuf = charset.decode(inBuf);
//
//                odSerwera = cbuf.toString();
//
//                log("serwer właśnie odpisał ... " + odSerwera);
//                cbuf.clear();
//
//
//                if (odSerwera.equals("Bye")) break;
//                return odSerwera;
//
//            }
//
//        }
//        return odSerwera;
//    }


    public static void log(String msg) {
        System.out.println("[Publisher]: " + msg);
    }

    @Override
    public void start(Stage stage) throws Exception {
        PublisherUI simpleUI = new PublisherUI();
        simpleUI.uI(stage);
    }

}
