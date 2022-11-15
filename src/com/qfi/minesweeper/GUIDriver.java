package com.qfi.minesweeper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import javafx.application.Application;

public class GUIDriver extends Application
{
    private Controller m_controller = null;
    private static final String MINESWEEPER_TITLE = "Minesweeper";
    private static final Logger m_logger = LogManager.getLogger(GUIDriver.class);
    private static final String MINESWEEPER_LAYOUT_PATH = "layout/Minesweeper.fxml";

    /**
     * This method is called immediately after the Application class is loaded and constructed and prior to the
     * start method being called. This method is used to initialize data members needed prior to the construction
     * of the scene/stage.
     */
    @Override
    public void init()
    {
        m_controller = new Controller();
    }

    /**
     * The main entry point for all JavaFX applications the start method is called after the init method has returned
     * and, after the system is ready for the application to begin running. This method will set our instantiated controller,
     * load the Minesweeper layout FXML, and set the scene.
     *
     * @param primaryStage - The primaryStage that is passed from the JavaFX runtime.
     */
    @Override
    public void start(Stage primaryStage)
    {
        Parent root = null;

        // Instantiate and load controller
        FXMLLoader loader = new FXMLLoader();
        loader.setController(m_controller);

        // Set the stage title and set the location of the layout to be loaded
        primaryStage.setTitle(MINESWEEPER_TITLE);
        loader.setLocation(getClass().getResource(MINESWEEPER_LAYOUT_PATH));

        try
        {
            // Load the layout configured
            root = loader.load();
        }
        catch(Exception e)
        {
            m_logger.error(e, e);
        }

        if (root != null)
        {
            // Create a scene based on the loaded layout, grab css resources and set the scene for the primary stage.
            Scene scene = new Scene(root, 238, 280);
            primaryStage.setScene(scene);

            // Disables maximizing the primary stage
            primaryStage.maximizedProperty().addListener((observable, oldValue, newValue) ->
            {
                if (newValue)
                {
                    primaryStage.setMaximized(false);
                }
            });

            // On close of stage, trigger the controller's shutdown function.
            primaryStage.setOnCloseRequest(event -> m_controller.shutdown());

            // Show the scene
            primaryStage.show();
        }
    }

    /**
     * Called by the GUIDriverRunner class to inject command line arguments into the Application runtime.
     *
     * @param args - Keyword arguments from the command line injected by the GUIDriverRunner class.
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
