package com.qfi.minesweeper;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import org.apache.log4j.LogManager;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Vincent.Nigro
 * @version 1.0.0
 */
public class Controller implements Initializable
{
    @SuppressWarnings("unused")
    @FXML
    private Text timeCount;

    @SuppressWarnings("unused")
    @FXML
    private Text boardCount;

    @SuppressWarnings("unused")
    @FXML
    private GridPane boardGrid;

    private boolean m_shutdown = false;
    private final Object m_shutdownMutex = new Object();
    private static final Logger m_logger = LogManager.getLogger(Controller.class);

    /**
     * Initialization of the JavaFX GUI interface called through the JavaFX runtime.
     *
     * @param location - The URL where the FXML layout is being referenced.
     * @param resources - The resources bundle if any were provided.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Initialize each button to be clickable
        for (Node node : boardGrid.getChildren())
        {
            if (node.getId() != null)
            {
                initMouseEvent((Button) node);
            }
        }
    }

    /**
     *
     */
    private final EventHandler<MouseEvent> mouseClickEvent = event ->
    {
        m_logger.debug("Button: " + ((Node) event.getTarget()).getId());
    };

    /**
     * Initializes the passed button to add the mouseClickEvent event to its event handler.
     *
     * @param b - A button object to initialize and add a mouse event to.
     */
    private void initMouseEvent(Button b)
    {
        b.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, mouseClickEvent);
    }

    public void shutdown()
    {
        setShutdown(true);
    }

    private boolean isShutdown()
    {
        synchronized (m_shutdownMutex)
        {
            return m_shutdown;
        }
    }

    private void setShutdown(boolean shutdown)
    {
        synchronized (m_shutdownMutex)
        {
            m_shutdown = shutdown;
        }
    }
}
