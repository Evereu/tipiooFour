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


        //Wyświetlanie topiców w dropdown
        List<String> existingTopics = client.getExistingTopicsFromServer("abc");

        for (String topic : existingTopics) {

            existingTopicsDropdown.getItems().addAll(topic);

        }


        existingTopicsDropdown.setPromptText("Istniejące topici");

        Button button1 = new Button("Pokaż aktualne topici");
        Button button2 = new Button("Przycisk 2");

        textArea.setEditable(false);

        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);

        root.add(existingTopicsDropdown, 0, 0, 2, 1);
        root.add(button1, 0, 1);
        root.add(button2, 1, 1);
        root.add(textArea, 0, 2, 2, 1);



        button1.setOnAction(event -> {

            textArea.appendText(existingTopicsDropdown.getValue());

            client.getClientTopics("client15");

        });

        button2.setOnAction(event -> {
            textArea.appendText("Kliknięto przycisk 2\n");
        });


        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client s24149");
        primaryStage.show();
    }


}
