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

public class ClientUI {


    public void uI(Stage primaryStage) {
        // Dropdown lista

        Client client = new Client();
        ComboBox<String> existingTopicsDropdown = new ComboBox<>();
        ComboBox<String> clientTopicsDropdown = new ComboBox<>();
        TextArea textArea = new TextArea();


        List<String> existingTopics = client.getExistingTopicsFromServer("client15");

        for (String topic : existingTopics) {

            existingTopicsDropdown.getItems().addAll(topic);

        }

        List<String> clientTopics = client.getClientTopics("client15");

        for (String topic : clientTopics) {

            clientTopicsDropdown.getItems().addAll(topic);

        }


        existingTopicsDropdown.setPromptText("Istniejące topici");

        clientTopicsDropdown.setPromptText("Topici clienta");

        Button button1 = new Button("xxx");
        Button showTopicNews = new Button("showTopicNews");

        Button subscribe = new Button("subscribe");
        Button unsubscribe = new Button("unsubscribe");

        textArea.setEditable(false);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        textArea.setPrefSize(600, 400);

        gridPane.add(existingTopicsDropdown, 1, 0, 2, 1);
        gridPane.add(clientTopicsDropdown, 2, 0);
        gridPane.add(button1, 0, 1);
        gridPane.add(showTopicNews, 1, 1);
        gridPane.add(subscribe, 2, 1);
        gridPane.add(unsubscribe, 3, 1);

        gridPane.add(textArea, 0, 2, 2, 1);


        subscribe.setOnAction(actionEvent -> {

            client.subscribeTopic(existingTopicsDropdown.getValue(), "client15");

                clientTopicsDropdown.getItems().clear();

            List<String> cclientTopics = client.getClientTopics("client15");


            for (String topic : cclientTopics) {

                clientTopicsDropdown.getItems().addAll(topic);

            }

        });

        unsubscribe.setOnAction(actionEvent -> {

            client.unSubscribeTopic(clientTopicsDropdown.getValue(),"client15");

            clientTopicsDropdown.getItems().clear();

            List<String> cclientTopics = client.getClientTopics("client15");

            for (String topic : cclientTopics) {

                clientTopicsDropdown.getItems().addAll(topic);

            }
        });



        button1.setOnAction(event -> {

            textArea.appendText(existingTopicsDropdown.getValue());


        });

        showTopicNews.setOnAction(event -> {

            textArea.clear();

            List<String> newsResult =  client.getTopicNews(clientTopicsDropdown.getValue(), "client15");

            for (String news : newsResult) {
                textArea.appendText(news + "\n");
            }
        });


        Scene scene = new Scene(gridPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client s24149");
        primaryStage.show();
    }


}
