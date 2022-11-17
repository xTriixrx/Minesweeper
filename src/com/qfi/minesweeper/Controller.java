package com.qfi.minesweeper;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import org.apache.log4j.LogManager;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import javax.swing.*;

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

    private Background m_oneBackground = null;
    private Background m_twoBackground = null;
    private Background m_sixBackground = null;
    private Background m_fourBackground = null;
    private Background m_fiveBackground = null;
    private Background m_bombBackground = null;
    private Background m_flagBackground = null;
    private Background m_blankBackground = null;
    private Background m_threeBackground = null;
    private Background m_sevenBackground = null;
    private Background m_eightBackground = null;
    private Background m_hitBombBackground = null;

    private static final String ONE_URL = "images/1.png";
    private static final String TWO_URL = "images/2.png";
    private static final String SIX_URL = "images/6.png";
    private static final String FOUR_URL = "images/4.png";
    private static final String FIVE_URL = "images/5.png";
    private static final String SEVEN_URL = "images/7.png";
    private static final String EIGHT_URL = "images/8.png";
    private static final String THREE_URL = "images/3.png";
    private static final String BOMB_URL = "images/bomb.png";
    private static final String FLAG_URL = "images/flag.png";
    private static final String BLANK_URL = "images/blank.png";
    private static final String HIT_BOMB_URL = "images/hit-bomb.png";
    private static final String BLACK_BORDER_STYLE = "-fx-border-color: black";

    private Board m_board = null;
    private boolean m_shutdown = false;
    private final Object m_shutdownMutex = new Object();
    private List<String> m_imageUrls = new ArrayList<>();

    private static final Logger m_logger = LogManager.getLogger(Controller.class);

    public Controller()
    {
        m_board = new Board(9, 9);

        m_imageUrls.add(ONE_URL);
        m_imageUrls.add(TWO_URL);
        m_imageUrls.add(THREE_URL);
        m_imageUrls.add(FOUR_URL);
        m_imageUrls.add(FIVE_URL);
        m_imageUrls.add(SIX_URL);
        m_imageUrls.add(SEVEN_URL);
        m_imageUrls.add(EIGHT_URL);
        m_imageUrls.add(FLAG_URL);
        m_imageUrls.add(BOMB_URL);
        m_imageUrls.add(BLANK_URL);
        m_imageUrls.add(HIT_BOMB_URL);
    }

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
                node.setStyle(BLACK_BORDER_STYLE);
                initMouseEvent((Button) node);
            }
        }

        for (String url : m_imageUrls)
        {
            BackgroundImage backgroundImage = null;
            BackgroundSize bgSize = new BackgroundSize(100, 100, true, true, true, true);

            try
            {
                backgroundImage = new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResource(url)).toExternalForm()),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);
            }
            catch (Exception e)
            {
                m_logger.error(e, e);
            }

            if (backgroundImage != null)
            {
                setBackgroundImage(backgroundImage, url);
            }
        }
    }

    private void setBackgroundImage(BackgroundImage img, String url)
    {
        switch (url)
        {
            case ONE_URL:
                m_oneBackground = new Background(img);
                break;
            case TWO_URL:
                m_twoBackground = new Background(img);
                break;
            case THREE_URL:
                m_threeBackground = new Background(img);
                break;
            case FOUR_URL:
                m_fourBackground = new Background(img);
                break;
            case FIVE_URL:
                m_fiveBackground = new Background(img);
                break;
            case SIX_URL:
                m_sixBackground = new Background(img);
                break;
            case SEVEN_URL:
                m_sevenBackground = new Background(img);
                break;
            case EIGHT_URL:
                m_eightBackground = new Background(img);
                break;
            case FLAG_URL:
                m_flagBackground = new Background(img);
                break;
            case BOMB_URL:
                m_bombBackground = new Background(img);
                break;
            case BLANK_URL:
                m_blankBackground = new Background(img);
                break;
            case HIT_BOMB_URL:
                m_hitBombBackground = new Background(img);
                break;
            default:
                m_logger.error("Unknown url type received of: " + url + ".");
        }
    }

    private Node getNode(int row, int col)
    {
        for (Node node : boardGrid.getChildren())
        {
            String id = "_" + row + "_" + col;

            if (node.getId().equals(id))
            {
                return node;
            }
        }

        return null;
    }

    /**
     *
     */
    private final EventHandler<MouseEvent> mouseClickEvent = event ->
    {
        String position = ((Node) event.getTarget()).getId().substring(1);
        m_logger.debug("Button: " + position);
        String[] rowCol = position.split("_");
        int row = Integer.parseInt(rowCol[0]);
        int col = Integer.parseInt(rowCol[1]);

        m_board.submitMove(row - 1, col - 1);
        int[][] selections = m_board.getSelectionArray();

        for (int i = 0; i < 9; i++)
        {
//            System.out.print((i+1 + " "));
            for (int j = 0; j < 9; j++)
            {
                if (selections[i][j] == 10)
                {
//                    System.out.print("| ");
                    Node button = getNode(i + 1, j + 1);
                    if (button != null)
                    {
                        button.setDisable(true);
                        button.setOpacity(1);
                        button.setStyle("");
                        ((Button) button).setBackground(m_blankBackground);
                    }
                }
                else if (selections[i][j] == 11)
                {

                }
                else if (selections[i][j] == 9)
                {
                    Node button = getNode(i + 1, j + 1);
                    if (button != null)
                    {
                        button.setDisable(true);
                        button.setOpacity(1);
                        button.setStyle("");
                        ((Button) button).setBackground(m_hitBombBackground);
//                        ((Button) button).setText("Q");
                    }
                }
                else if (selections[i][j] != 0)
                {
//                    System.out.print("|" + selections[i][j]);
                    Node button = getNode(i + 1, j + 1);
                    if (button != null)
                    {
                        int count = selections[i][j];

                        button.setDisable(true);
                        button.setOpacity(1);
                        button.setStyle("");
                        setImage(((Button) button), count);
//                        ((Button) button).setText(Integer.toString(count));
                    }
                }
//                else
//                {
//                    System.out.print("|-");
//                }
            }
//            System.out.print("|");
//            System.out.println();

        }

        m_board.printBoard();

        if (m_board.lostGame(row - 1, col - 1))
        {
//            DecimalFormat df = new DecimalFormat("###.##");
//            EndTime = System.nanoTime();
//            Gameplay = false;
//            double seconds = (EndTime-StartTime) / 1000000000.0;
//            int minutes = (int) (seconds / 60);
//            seconds = seconds - (minutes * 60);
//            b.printEndBoard();
//            System.out.println("Your time was: " + minutes + " Minute(s), " +  df.format(seconds) + " second(s).");
            infoBox("Sorry, you lost.", "Lost");
            displayEndGame(m_board.getBoardArray(), m_board.getSelectionArray(), position);
//            System.out.println("Sorry, you lost");
        }
        if (m_board.wonGame())
        {
//            DecimalFormat df = new DecimalFormat("###.##");
//            EndTime = System.nanoTime();
//            Gameplay = false;
//            double seconds = (EndTime-StartTime) / 1000000000.0;
//            int minutes = (int) (seconds / 60);
//            seconds = seconds - (minutes * 60);
            infoBox("Congratulations, you won!", "Won");
            displayEndGame(m_board.getBoardArray(), m_board.getSelectionArray(), position);
//            System.out.println("Your time was: " + minutes + " Minute(s), " +  df.format(seconds) + " second(s).");
        }
    };

    private void setImage(Button button, int count)
    {
        switch (count)
        {
            case 1:
                button.setBackground(m_oneBackground);
                break;
            case 2:
                button.setBackground(m_twoBackground);
                break;
            case 3:
                button.setBackground(m_threeBackground);
                break;
            case 4:
                button.setBackground(m_fourBackground);
                break;
            case 5:
                button.setBackground(m_fiveBackground);
                break;
            case 6:
                button.setBackground(m_sixBackground);
                break;
            case 7:
                button.setBackground(m_sevenBackground);
                break;
            case 8:
                button.setBackground(m_eightBackground);
                break;
            default:
                m_logger.error("Unknown button count received of: " + count + ".");
        }
    }

    private void displayEndGame(int[][] boardArray, int[][] selectionsArray, String hitPosition)
    {
        String[] rowCol = hitPosition.split("_");
        int row = Integer.parseInt(rowCol[0]);
        int col = Integer.parseInt(rowCol[1]);

        for (int i = 0; i < 9; i++)
        {
//            System.out.print((i+1 + " "));
            for (int j = 0; j < 9; j++)
            {
                if (boardArray[i][j] == 0 || boardArray[i][j] == 10 || selectionsArray[i][j] == 10)
                {
//                    System.out.print("| ");
                    Node button = getNode(i + 1, j + 1);
                    if (button != null)
                    {
                        button.setDisable(true);
                        button.setOpacity(1);
                        button.setStyle("");
                        ((Button) button).setBackground(m_blankBackground);
                    }
                }
                else if (boardArray[i][j] == 9 && (i + 1) != row && (j + 1) != col)
                {
                    Node button = getNode(i + 1, j + 1);
                    if (button != null)
                    {
                        button.setDisable(true);
                        button.setOpacity(1);
                        button.setStyle("");
                        ((Button) button).setBackground(m_bombBackground);
                    }
                }
                else if (boardArray[i][j] != 0)
                {
//                    System.out.print("|" + selections[i][j]);
                    Node button = getNode(i + 1, j + 1);
                    if (button != null)
                    {
                        int count = boardArray[i][j];

                        button.setDisable(true);
                        button.setOpacity(1);
                        button.setStyle("");
                        setImage(((Button) button), count);
                    }
                }
                else
                {
                    Node button = getNode(i + 1, j + 1);
                    if (button != null)
                    {
                        button.setDisable(true);
                        button.setOpacity(1);
                        button.setStyle("");
                        ((Button) button).setBackground(m_blankBackground);
                    }
                }
            }
//            System.out.print("|");
//            System.out.println();

        }
    }

    /**
     * A wrapper for calling the Thread class's sleep method within a try catch block.
     *
     * @param milliseconds - Amount of seconds to sleep the calling thread.
     */
    private void sleep(int milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        }
        catch (Exception e)
        {
            m_logger.error(e, e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * A Java Swing pop up info box for showing basic pop up information to the user during the game.
     *
     * @param infoMessage - The message to be inserted into the pop-up message.
     * @param titleBar - The partial title of the pop-up box.
     */
    public void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

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
