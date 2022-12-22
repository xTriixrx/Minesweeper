package com.qfi.minesweeper;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import javafx.application.Application;

/**
 *
 * @author Vincent.Nigro
 * @version 1.0.0
 */
public class GUIDriver extends Application
{
    private String m_level = "";
    private String m_layout = "";
    private Controller m_controller = null;

    private static final int EXPERT = 2; // 16x30 w/ 99 mines
    private static final int BEGINNER = 0; // 16x16 w/ 40 mines
    private static final int INTERMEDIATE = 1; // 9x9 w/ 10 mines
    private static final String EXPERT_LEVEL = "EXPERT";
    private String LEVEL = System.getProperty("gameLevel");
    private static final String BEGINNER_LEVEL = "BEGINNER";
    private static final String MINESWEEPER_TITLE = "Minesweeper";
    private static final String INTERMEDIATE_LEVEL = "INTERMEDIATE";
    private static final String EXPERT_LAYOUT_PATH = "layout/Expert.fxml";
    private static final String BEGINNER_LAYOUT_PATH = "layout/Beginne.fxml";
    private static final String INTERMEDIATE_LAYOUT_PATH = "layout/Intermediate.fxml";
    private static final Logger m_logger = LogManager.getLogger(GUIDriver.class);

    /**
     * This method is called immediately after the Application class is loaded and constructed and prior to the
     * start method being called. This method is used to initialize data members needed prior to the construction
     * of the scene/stage.
     */
    @Override
    public void init()
    {
        if (LEVEL == null)
        {
            LEVEL = BEGINNER_LEVEL;
        }

        if (LEVEL.equalsIgnoreCase(EXPERT_LEVEL))
        {
            m_level = EXPERT_LEVEL;
            m_layout = EXPERT_LAYOUT_PATH;
        }
        else if (LEVEL.equalsIgnoreCase(INTERMEDIATE_LEVEL))
        {
            m_level = INTERMEDIATE_LEVEL;
            m_layout = INTERMEDIATE_LAYOUT_PATH;
        }
        else if (LEVEL.equalsIgnoreCase(BEGINNER_LEVEL))
        {
            m_level = BEGINNER_LEVEL;
            m_layout = BEGINNER_LAYOUT_PATH;
        }
        else
        {
            m_level = BEGINNER_LEVEL;
            m_layout = BEGINNER_LAYOUT_PATH;
        }

        m_controller = new Controller(m_level);
        Thread controllerThread = new Thread(m_controller);
        controllerThread.setName("Controller Runnable Thread");
        controllerThread.setDaemon(true);
        controllerThread.start();
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
        loader.setLocation(getClass().getResource(m_layout));

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
            Scene scene = new Scene(root);
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
