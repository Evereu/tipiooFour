
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Server {
    static Map<String, HashSet<String>> clientTopics = new HashMap<>(); // nazwa klienta i jego tematy
    static Map<String, List<String>> topicNews = new HashMap<>(); // temat i newsy do niego przypisane
    //static HashSet<String> existingTopics = new HashSet<>(); // wszystkie tematy


    public static void main(String[] args) throws IOException, InterruptedException {
        new Server();
    }

    Server() throws IOException {

        String host = "localhost";
        int port = 5000;
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(host, port));

        serverChannel.configureBlocking(false);

        Selector selector = Selector.open();

        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Serwer: czekam ... ");


        while (true) {

            selector.select();

            Set<SelectionKey> keys = selector.selectedKeys();

            Iterator<SelectionKey> iter = keys.iterator();

            while (iter.hasNext()) {

                SelectionKey key = iter.next();

                iter.remove();

                if (key.isAcceptable()) {
                    System.out.println("Serwer: ktoś się połączył ..., akceptuję go ... ");
                    SocketChannel socketChannel = serverChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    continue;
                }

                if (key.isReadable()) {

                    System.out.println("Server czyta");
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    serviceRequest(socketChannel);

                    continue;
                }
            }
        }
    }


    // Strona kodowa do kodowania/dekodowania buforów
    private static Charset charset = StandardCharsets.UTF_8;
    private static final int BSIZE = 1024;

    // Bufor bajtowy - do niego są wczytywane dane z kanału
    private ByteBuffer bbuf = ByteBuffer.allocate(BSIZE);

    // Tu będzie zlecenie do pezetworzenia
    private StringBuffer reqString = new StringBuffer();


    private void serviceRequest(SocketChannel sc) {
        if (!sc.isOpen()) return; // jeżeli kanał zamknięty

        System.out.print("Serwer: czytam komunikat od klienta ... ");
        // Odczytanie zlecenia
        reqString.setLength(0);
        bbuf.clear();

        try {
            readLoop:
            // Czytanie jest nieblokujące
            while (true) {               // kontynujemy je dopóki
                int n = sc.read(bbuf);   // nie natrafimy na koniec wiersza
                if (n > 0) {
                    bbuf.flip();
                    CharBuffer cbuf = charset.decode(bbuf);
                    while (cbuf.hasRemaining()) {
                        char c = cbuf.get();
                        if (c == '\r' || c == '\n') break readLoop;
                        else {
                            reqString.append(c);
                        }
                    }
                }
            }

            String command = reqString.toString();
            System.out.println(reqString);

//
//            else if (cmd.equals("Bye")) {           // koniec komunikacji
//
//                sc.write(charset.encode(CharBuffer.wrap("Bye")));
//                System.out.println("Serwer: mówię \"Bye\" do klienta ...\n\n");
//
//                sc.close();                      // - zamknięcie kanału
//                sc.socket().close();			 // i gniazda
//
//            }


            String[] msg = command.split(":");

//            System.out.println(msg[0]); //skąd pochodzi
//            System.out.println(msg[1]); // commandType
//            System.out.println(msg[2]); // topicName

            if (msg[0].equals("Publisher")) {

                System.out.println("Publisher wchodzi");

                if (msg[1].equals("addnewtopic")) {

                    //od razu dodać do topicNews

                    topicNews.put(msg[2], new ArrayList<>());

                    System.out.println("dodano nowy topic");
//
//                    existingTopics.add(msg[2]);
//
//                    for (var a : existingTopics) {
//                        System.out.println("existingTopics: " + a);
//                    }

                    for (String entry : topicNews.keySet()) {


                            System.out.println("addnewtopicaaa: "+entry);

                        System.out.println();

                    }

                    sc.close();
                    sc.socket().close();

                } else if (msg[1].equals("removetopic")) {

                    System.out.println("usunieto topic");

                    topicNews.remove(msg[2]);

                    for (var a : topicNews.keySet()) {
                        System.out.println("existingTopics: " + a);
                    }

                    sc.close();
                    sc.socket().close();

                } else if (msg[1].equals("getexistingtopics")) {

                    System.out.println("getexistingtopics");

                    StringBuilder stringBuilder = new StringBuilder();


                    if(topicNews.isEmpty()){

                        System.out.println("topici puste");

                        sc.write(charset.encode(CharBuffer.wrap("none")));


                    }else {

                        for (var topic : topicNews.keySet()) {
                            stringBuilder.append(topic).append(" ");
                        }
                        String listaTopicowString = stringBuilder.toString().trim();
                        System.out.println("Wysyłam topici");

                        sc.write(charset.encode(CharBuffer.wrap(listaTopicowString)));

                    }



                    sc.close();
                    sc.socket().close();

                } else if (msg[1].equals("addnewnews")) {

                    System.out.println("addnewnews");

                    for (Map.Entry<String, List<String>> entry : topicNews.entrySet()) {

                        if (entry.getKey().equals(msg[2])) {
                            List<String> topics = entry.getValue();

                            topics.add(msg[3]);

                        }
                    }
                    for (Map.Entry<String, List<String>> entry : topicNews.entrySet()) {
                        List<String> topics = entry.getValue();
                        for (String topic : topics) {
                            System.out.println("addnewnews: "+topic);
                        }
                        System.out.println();

                    }

                    sc.close();
                    sc.socket().close();
                }
            }




            // tu będą polecenia od klienta



            if (msg[0].equals("Client")) {
                System.out.println("Client wchodzi");

                //sprawdzenie czy klient istnieje, jeśli nie to dodanie go do listy


                //TODO zastanowić się czy dodawanie nowego klienta można jakoś opakować by skrócić requesty


                HashSet<String> clientTopicList = new HashSet<String>();

                //TODO DODAWANIE NOWEGO KILENTA, ODKOMENTOWAĆ PÓŹNIEJ

                if(!clientTopics.containsKey(msg[1])){
                    clientTopics.put(msg[1], clientTopicList);
                    System.out.println("dodano nowego klienta");
                }

                for (String entry : clientTopics.keySet()) {
                    System.out.println("Aktualni klienci: " + entry);
                }

                if(msg[2].equals("getexistingtopics")){

                    StringBuilder stringBuilder = new StringBuilder();


                    if(topicNews.isEmpty()){
                        System.out.println("topici puste");

                        sc.write(charset.encode(CharBuffer.wrap("none")));
                    }else {
                        for (var topic : topicNews.keySet()) {
                            stringBuilder.append(topic).append(" ");
                        }
                        String listaTopicowString = stringBuilder.toString().trim();

                        System.out.println("Wysyłam topici");

                        sc.write(charset.encode(CharBuffer.wrap(listaTopicowString)));
                    }




                    sc.close();
                    sc.socket().close();

                } else if (msg[2].equals("subscribetopic")) {

                    if(topicNews.keySet().contains(msg[3]) && clientTopics.containsKey(msg[1])){

                        clientTopics.get(msg[1]).add(msg[3]);

                        System.out.println("Zasubskrybowano");
                        sc.write(charset.encode(CharBuffer.wrap("Zasubskrybowano to od serwera")));


                    }else {
                        System.out.println("Taki topik nie istnieje");
                        sc.write(charset.encode(CharBuffer.wrap("Taki topik nie istnieje to od serwera")));

                    }


                    for (Map.Entry<String, HashSet<String>> entry : clientTopics.entrySet()) {
                        String clientId = entry.getKey();
                        HashSet<String> topics = entry.getValue();
                        System.out.println("Klient: " + clientId);
                        System.out.println("Tematy:");
                        for (String topic : topics) {
                            System.out.println(topic);
                        }
                        System.out.println();


                    }
                    sc.close();
                    sc.socket().close();

                } else if (msg[2].equals("unsubscribetopic")){

                    System.out.println("un subscribetopiccccccccccc");

                    System.out.println("Przed usunięciem");

                    for (Map.Entry<String, HashSet<String>> entry : clientTopics.entrySet()) {
                        String clientId = entry.getKey();
                        HashSet<String> topics = entry.getValue();
                        System.out.println("Klient: " + clientId);
                        System.out.println("Tematy:");
                        for (String topic : topics) {
                            System.out.println(topic);
                        }
                        System.out.println();
                    }

                    System.out.println("Po usunięciu");


                    for (Map.Entry<String, HashSet<String>> entry : clientTopics.entrySet()) {
                        entry.getValue().remove(msg[3]);
                        HashSet<String> topics = entry.getValue();
                        for (String topic : topics) {
                            System.out.println(topic);
                        }
                        System.out.println();
                    }

                    sc.close();
                    sc.socket().close();


                }else if (msg[2].equals("gettopicnews")) {

                    System.out.println("gettopicnews");


                    StringBuilder stringBuilder = new StringBuilder();

//
//                    for (Map.Entry<String, List<String>> entry : topicNews.entrySet()) {
//
//                        List<String> topics = entry.getValue();
//                        for (String topic : topics) {
//                            System.out.println("ffffffffff"+topic);
//                        }
//                        System.out.println();
//                    }


                    if(clientTopics.containsKey(msg[1])) {
                        for (String news : topicNews.get(msg[3])) {
                            stringBuilder.append(news).append(" ");
                            System.out.println("- " + news);
                        }

                        String listaTopicowString = stringBuilder.toString().trim();
                        System.out.println("Wysyłam topici");

                        sc.write(charset.encode(CharBuffer.wrap(listaTopicowString)));

                    }else {
                        sc.write(charset.encode(CharBuffer.wrap("none")));
                    }

                    sc.close();
                    sc.socket().close();

                } else if (msg[2].equals("getclienttopics")) {


                    StringBuilder stringBuilder = new StringBuilder();

                    if(clientTopics.containsKey(msg[1]) && clientTopics.get(msg[1]) != null) {
                        for (String topic : clientTopics.get(msg[1])) {
                            stringBuilder.append(topic).append(" ");
                            System.out.println("- " + topic);
                        }

                        String listaTopicowString = stringBuilder.toString().trim();
                        System.out.println("Wysyłam topici");

                        sc.write(charset.encode(CharBuffer.wrap(listaTopicowString)));

                    }else {
                        sc.write(charset.encode(CharBuffer.wrap("none")));
                    }

                    sc.close();
                    sc.socket().close();
                }
            }


        } catch (Exception exc) {
            exc.printStackTrace();
            try {
                sc.close();
                sc.socket().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void addNewTopic() {

    }


}
