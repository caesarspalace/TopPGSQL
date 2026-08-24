package speco.toppgsql;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author adrian
 */
public class TopPGSQL extends Application {
    public static  Stage owner;
    @Override
    public void start(Stage stage) throws Exception {
         owner=stage;
        Parent root = FXMLLoader.load(getClass().getResource("FXMLTopPGSQL.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("TopPGSQL");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
