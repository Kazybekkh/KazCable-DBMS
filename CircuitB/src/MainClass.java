/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */

import com.kazcables.forms.Login;
import javafx.application.Application;
import javafx.stage.Stage;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;

public class MainClass extends Application {

    @Override
    public void start(Stage primaryStage) {
        // We won't be using the primary stage provided by JavaFX since this is a Swing application
        invokeLater(() -> {
            try {
                // Set the Nimbus Look and Feel
                setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            }catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) { 
            }
            // Now that the Look and Feel is set, make the Home window visible
            new Login().setVisible(true);
        });
    }

    public static void main(String[] args) {
        try {
                // Set System L&F
            setLookAndFeel(getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
           // handle exception
        }
        // handle exception
        // handle exception
        // handle exception

        launch(args);
    }
}


