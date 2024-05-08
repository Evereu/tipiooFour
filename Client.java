
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Client extends Application {

    static List<String> clientTopics = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        launch(args);

        Random random = new Random();
        int clientNumber = random.nextInt(100) + 1;

        System.out.println("Client number: " + clientNumber);

//        while (true) {
//            clientOptions(clientNumber);
//        }
    }

//    public static void clientOptions(int clientNumber) {
//
//        String topicName = null;
//
//        log("1 - subscribe topic");
//        log("2 - unsubscribe topic");
//        log("3 - get topic news");
//
//        //TODO Do zrobienia dodawanie jakiegoś identyfikatora klienta
//
//        Scanner scanner = new Scanner(System.in);
//        String inputRequestType = scanner.nextLine();
//
//        System.out.println("clientNumber: " + clientNumber);
//
//        String clientName = "client" + clientNumber;
//
//        switch (Integer.parseInt(inputRequestType)) {
//
//            case 1:
//                getExistingTopicsFromServer(clientName);
//
//                log("Wprowadź topik do zasubskrybowania");
//
//                topicName = scanner.nextLine();
//
//                subscribeTopic(topicName, clientName);
//
//                break;
//
//            case 2:
//
//                log("Wprowadź topik do odsubskrybowania");
//
//                topicName = scanner.nextLine();
//
//                unSubscribeTopic(topicName, clientName);
//
//                break;
//            case 3:
//
//                getExistingTopicsFromServer(clientName);
//                log("Wprowadź topik którego newsy chcesz zobaczyć");
//
//                topicName = scanner.nextLine();
//
//
//                getTopicNews(topicName, clientName);
//
//                break;
//        }
//    }


    public List<String> getClientTopics(String clientName){

        String commandType = "getclienttopics";
        List<String> clientTopicsToReturn = new ArrayList<>();

        try {
            Charset charset = StandardCharsets.UTF_8;
            SocketChannel clientSocket = SocketChannel.open(new InetSocketAddress("localhost", 5000));
            clientSocket.configureBlocking(false);
            clientSocket.write(charset.encode(CharBuffer.wrap("Client:" + clientName + ":" + commandType + "\n")));

            String sss = readFromServer(clientSocket);

            if(!sss.equals("none")){

                String[] serverResponse = sss.split(" ");

                for (String topic : serverResponse) {
                    clientTopicsToReturn.add(topic);
                    System.out.println(topic);
                }
            }

            clientSocket.close();
            clientSocket.socket().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientTopicsToReturn;
    }


    public List<String> getTopicNews(String topicName, String clientName){

        String commandType = "gettopicnews";
        List<String> clientNewsToReturn = new ArrayList<>();


        try {
            Charset charset = StandardCharsets.UTF_8;
            SocketChannel clientSocket = SocketChannel.open(new InetSocketAddress("localhost", 5000));
            clientSocket.configureBlocking(false);
            clientSocket.write(charset.encode(CharBuffer.wrap("Client:" + clientName + ":" + commandType + ":" + topicName + "\n")));


            String sss = readFromServer(clientSocket);

            if(!sss.equals("none")){

                String[] serverResponse = sss.split(" ");

                for (String topic : serverResponse) {
                    clientNewsToReturn.add(topic);
                    System.out.println(topic);
                }
            }


            clientSocket.close();
            clientSocket.socket().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientNewsToReturn;
    }






    public void subscribeTopic(String topicName, String clientName) {
        String commandType = "subscribetopic";

        try {
            Charset charset = StandardCharsets.UTF_8;
            SocketChannel clientSocket = SocketChannel.open(new InetSocketAddress("localhost", 5000));
            clientSocket.configureBlocking(false);
            clientSocket.write(charset.encode(CharBuffer.wrap("Client:" + clientName + ":" + commandType + ":" + topicName + "\n")));

            String serverResponse = readFromServer(clientSocket);

            System.out.println(serverResponse);

            clientSocket.close();
            clientSocket.socket().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unSubscribeTopic(String topicName, String clientName) {

        String commandType = "unsubscribetopic";

        try {
            Charset charset = StandardCharsets.UTF_8;
            SocketChannel clientSocket = SocketChannel.open(new InetSocketAddress("localhost", 5000));
            clientSocket.configureBlocking(false);
            clientSocket.write(charset.encode(CharBuffer.wrap("Client:" + clientName + ":" + commandType + ":" + topicName + "\n")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<String> getExistingTopicsFromServer(String clientName) {

        String commandType = "getexistingtopics";
         List<String> existingTopicsToReturn = new ArrayList<>();

        try {
            Charset charset = StandardCharsets.UTF_8;
            SocketChannel clientSocket = SocketChannel.open(new InetSocketAddress("localhost", 5000));
            clientSocket.configureBlocking(false);
            clientSocket.write(charset.encode(CharBuffer.wrap("Client:" + clientName + ":" + commandType + "\n")));

            String[] serverResponse = readFromServer(clientSocket).split(" ");

            System.out.println("jestem tutaj");

            for (String topic : serverResponse) {
                existingTopicsToReturn.add(topic);
                System.out.println(topic);
            }

            clientSocket.close();
            clientSocket.socket().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return existingTopicsToReturn;
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

                    System.out.println("Klient: serwer właśnie odpisał ... " + odSerwera);
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
        System.out.println("[Client]: " + msg);
    }


    @Override
    public void start(Stage stage) throws Exception {
        ClientUI simpleUI = new ClientUI();
        simpleUI.uI(stage);
    }
}












