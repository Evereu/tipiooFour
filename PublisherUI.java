import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class PublisherUI {
    public void uI(Stage stage) throws Exception {

        Publisher publisher = new Publisher();
        ComboBox<String> existingTopicsDropdown = new ComboBox<>();

        Button addNewTopic = new Button("Dodaj nowy temat");
        Button removeTopic = new Button("Usuń temat");
        Button addNewNews = new Button("Dodaj nowy news");
        TextField inputNewTopic = new TextField("");
        inputNewTopic.setPromptText("Wprowadź nowy topic");
        TextArea textArea = new TextArea();


        List<String> existingTopics = publisher.getExistingTopicsFromServer();

        for (String topic : existingTopics) {

            existingTopicsDropdown.getItems().addAll(topic);

        }


        existingTopicsDropdown.setPromptText("Istniejące topici");


        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        textArea.setPrefSize(600, 400);

        gridPane.add(inputNewTopic, 0, 0, 1, 1);
        gridPane.add(addNewTopic, 1, 0, 1, 1);
        gridPane.add(textArea, 0, 1, 3, 1);

        gridPane.add(removeTopic, 2, 2, 3, 1); //2, 2, 3, 1
        gridPane.add(addNewNews, 1, 2, 3, 1);


        gridPane.add(existingTopicsDropdown, 0, 2, 3, 1);


        addNewTopic.setOnAction(event -> {

            publisher.AddNewTopic(inputNewTopic.getText());

            existingTopicsDropdown.getItems().clear();

            List<String> eexistingTopics = publisher.getExistingTopicsFromServer();

            for (String topic : eexistingTopics) {

                existingTopicsDropdown.getItems().addAll(topic);

            }
            inputNewTopic.clear();
            existingTopicsDropdown.getSelectionModel().select(0);

        });



        removeTopic.setOnAction(event -> {

            publisher.RemoveTopic(existingTopicsDropdown.getValue());

            existingTopicsDropdown.getItems().clear();

            List<String> eexistingTopics = publisher.getExistingTopicsFromServer();

            for (String topic : eexistingTopics) {

                existingTopicsDropdown.getItems().addAll(topic);

            }
            existingTopicsDropdown.getSelectionModel().select(0);

        });





        addNewNews.setOnAction(event -> {

            publisher.AddNewNews(textArea.getText(), existingTopicsDropdown.getValue());


            textArea.clear();

        });





        Scene scene = new Scene(gridPane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Publisher s24149");
        stage.show();
    }
}

