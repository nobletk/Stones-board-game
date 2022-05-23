package boardGame;

import com.google.gson.GsonBuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PlayerNameController {

    @FXML
    private TextField firstPlayerInput;

    @FXML
    private TextField secondPlayerInput;

    @FXML
    protected void sessionInfoToJson() throws IOException {

        var gson = new GsonBuilder().setPrettyPrinting().create();

        var gameInfo = new GameSessionInformation();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z");
        gameInfo.setZonedDateTime(ZonedDateTime.now().format(formatter));
        gameInfo.setFirstPlayerName(firstPlayerInput.getText());
        gameInfo.setSecondPlayerName(secondPlayerInput.getText());

        System.out.println(gson.toJson(gameInfo));

        try (var writer = new FileWriter("sessionInfo.json")) {
            gson.toJson(gameInfo, writer);
        }
        System.out.println(gson.fromJson(new FileReader("sessionInfo.json"), GameSessionInformation.class));
    }

    @FXML
    private void switchScene(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ui.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        sessionInfoToJson();
    }
}
