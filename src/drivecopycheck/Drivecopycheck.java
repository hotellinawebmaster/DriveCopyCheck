/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivecopycheck;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
 
public class Drivecopycheck extends Application {
    
    public static Stage mainStage;
    public static Drive service;
    public static List<File> globalRepoList;
    public static String emailAddressLogin;
    public static Boolean firstStageEmailEntered;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.mainStage = stage;
        this.firstStageEmailEntered = false;
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        final Parent root = (Parent) loader.load();
        
        Scene scene = new Scene(root);
        stage.setTitle("Google Drive Cloud object finder");
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("------------------------------------------SYSTEM PROPERTIES----------------------------------------------------");
        System.out.println( "OS name " + System.getProperty("os.name"));
        System.out.println( "Java Version " + System.getProperty("java.version")); 
        System.out.println("javafx.runtime.version: " + com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
        System.out.println( "Use Working directory " + System.getProperty("user.dir")); 
        System.out.println( "Java home installation directory " + System.getProperty("java.home")); 
        System.out.println( "Java class path for JAR and CLASS files " + System.getProperty("java.class.path")); 
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        launch(args);
    }
    
     public static Stage getStagefromMain() {
        return mainStage;
    }
    
}
