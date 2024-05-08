import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PublisherUI {
    public void uI(Stage stage) throws Exception {

        // Dropdown lista
        ComboBox<String> dropdown = new ComboBox<>();
        dropdown.getItems().addAll("Opcja 1", "Opcja 2", "Opcja 3");
        dropdown.setPromptText("Wybierz opcję");

        // Przyciski
        Button button1 = new Button("Przycisk 1");
        Button button2 = new Button("Przycisk 2");

        // Pole do wpisywania tekstu
        TextArea textArea = new TextArea();

        // Obszar do rozmieszczenia elementów
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Dodanie elementów do obszaru
        root.setTop(dropdown);

        HBox buttonsBox = new HBox(10);
        buttonsBox.getChildren().addAll(button1, button2);
        root.setCenter(buttonsBox);

        root.setBottom(textArea);

        // Ustawienie akcji dla przycisków
        button1.setOnAction(event -> {
            textArea.appendText("Kliknięto przycisk 1\n");
        });

        button2.setOnAction(event -> {
            textArea.appendText("Kliknięto przycisk 2\n");
        });

        // Utworzenie sceny i ustawienie jej na etapie głównym
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Publisher s24149");
        stage.show();
    }
}

