import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;

public class ClientUI {


    public void uI(Stage primaryStage) {


        Random random = new Random();
        int clientNumber = random.nextInt(100) + 1;

        String clientName = "client" + clientNumber;

        System.out.println(clientName);

        Client client = new Client();
        ComboBox<String> existingTopicsDropdown = new ComboBox<>();
        ComboBox<String> clientTopicsDropdown = new ComboBox<>();
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);

        List<String> existingTopics = client.getExistingTopicsFromServer(clientName);

        for (String topic : existingTopics) {

            existingTopicsDropdown.getItems().addAll(topic);

        }

        List<String> clientTopics = client.getClientTopics(clientName);

        for (String topic : clientTopics) {

            clientTopicsDropdown.getItems().addAll(topic);

        }


        existingTopicsDropdown.setPromptText("Wszystkie tematy");

        clientTopicsDropdown.setPromptText("Tematy zasubskrybowane");

        Button showTopicNews = new Button("Pokaż newsy z wybranego tematu");

        Button subscribe = new Button("Subskrybuj temat");
        Button unsubscribe = new Button("Odsubskrybuj temat");

        textArea.setEditable(false);


        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        textArea.setPrefSize(600, 400);

        gridPane.add(existingTopicsDropdown, 0, 0, 1, 1);
        gridPane.add(subscribe, 1, 0, 1, 1);

        gridPane.add(textArea, 0, 1, 3, 1);


        gridPane.add(clientTopicsDropdown, 0, 2, 1, 1);
        gridPane.add(showTopicNews, 1, 2, 1, 1);
        gridPane.add(unsubscribe, 2, 2, 1, 1);



        existingTopicsDropdown.setOnMouseClicked(dragEvent -> {

            List<String> existingTopicss = client.getExistingTopicsFromServer(clientName);

            existingTopicsDropdown.getItems().clear();

            for (String topic : existingTopicss) {

                existingTopicsDropdown.getItems().addAll(topic);

            }
        });

        clientTopicsDropdown.setOnMouseClicked(dragEvent ->{


            List<String> clientTopicss = client.getClientTopics(clientName);

            clientTopicsDropdown.getItems().clear();

            for (String topic : clientTopicss) {

                clientTopicsDropdown.getItems().addAll(topic);

            }
        });

        subscribe.setOnAction(actionEvent -> {

            client.subscribeTopic(existingTopicsDropdown.getValue(), clientName);

                clientTopicsDropdown.getItems().clear();

            List<String> cclientTopics = client.getClientTopics(clientName);


            for (String topic : cclientTopics) {

                clientTopicsDropdown.getItems().addAll(topic);

            }
            clientTopicsDropdown.getSelectionModel().select(0);

        });

        unsubscribe.setOnAction(actionEvent -> {

            client.unSubscribeTopic(clientTopicsDropdown.getValue(),clientName);

            clientTopicsDropdown.getItems().clear();

            List<String> cclientTopics = client.getClientTopics(clientName);

            for (String topic : cclientTopics) {

                clientTopicsDropdown.getItems().addAll(topic);

            }
            clientTopicsDropdown.getSelectionModel().select(0);

        });




        showTopicNews.setOnAction(event -> {

            textArea.clear();

            List<String> newsResult =  client.getTopicNews(clientTopicsDropdown.getValue(), clientName);

            for (String news : newsResult) {
                textArea.appendText(news + " ");
            }
        });


        Scene scene = new Scene(gridPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client s24149");
        primaryStage.show();
    }


}
