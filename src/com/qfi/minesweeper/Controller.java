package com.qfi.minesweeper;

import java.net.URL;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import java.util.Objects;
import java.util.ArrayList;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;
import javax.swing.JOptionPane;
import javafx.scene.image.Image;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import org.apache.log4j.LogManager;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;

/**
 * Controller is JavaFx controller class which manages the game state of the Minesweeper instance. GUIDriver will
 * instantiate the controller and start the controller's main runnable thread. Once started, the main thread
 * will create another thread for managing the time state while the game is being played. Finally, once the game starts,
 * the controller will wait to be signaled to perform an update on the internal board object and update the GUI
 * appropriately.
 *
 * @author Vincent.Nigro
 * @version 1.0.0
 */
public class Controller implements Initializable, Runnable
{
    @SuppressWarnings("unused")
    @FXML
    private Text timeCount;

    @SuppressWarnings("unused")
    @FXML
    private Text bombCount;

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

    private static final int HALF_SEC = 500;
    private static final int BEGINNER_SIZE = 9;
    private static final int EXPERT_COL_SIZE = 30;
    private static final int EXPERT_ROW_SIZE =  16;
    private static final int INTERMEDIATE_SIZE = 16;
    private static final int EXPERT_BOMB_COUNT = 99;
    private static final int BEGINNER_BOMB_COUNT = 10;
    private static final int INTERMEDIATE_BOMB_COUNT = 40;

    private static final String STRING_ZERO = "0";
    private static final String EXPERT_LEVEL = "EXPERT";
    private static final String ONE_URL = "images/1.png";
    private static final String TWO_URL = "images/2.png";
    private static final String SIX_URL = "images/6.png";
    private static final String FOUR_URL = "images/4.png";
    private static final String FIVE_URL = "images/5.png";
    private static final String SEVEN_URL = "images/7.png";
    private static final String EIGHT_URL = "images/8.png";
    private static final String THREE_URL = "images/3.png";
    private static final String BEGINNER_LEVEL = "BEGINNER";
    private static final String BOMB_URL = "images/bomb.png";
    private static final String FLAG_URL = "images/flag.png";
    private static final String BLANK_URL = "images/blank.png";
    private static final String INTERMEDIATE_LEVEL = "INTERMEDIATE";
    private static final String HIT_BOMB_URL = "images/hit-bomb.png";
    private static final String BLACK_BORDER_STYLE = "-fx-border-color: black";
    private static final String DEFAULT_BACKGROUND_COLOR = "-fx-background-color: #e2e2e2";

    private int m_rowSize = 0;
    private int m_colSize = 0;
    private final Board m_board;
    private int m_bombCount = 0;
    private String m_choice = "";
    private long m_startTime = 0L;
    private boolean m_shutdown = false;
    private final Object m_choiceSignal = new Object();
    private final Object m_shutdownMutex = new Object();
    private final List<String> m_imageUrls = new ArrayList<>();

    private static final Logger m_logger = LogManager.getLogger(Controller.class);

    /**
     * Controller constructor.
     *
     * @param levelType - The level which determines the board layout.
     */
    public Controller(String levelType)
    {
        if (levelType.equalsIgnoreCase(BEGINNER_LEVEL))
        {
            m_rowSize = BEGINNER_SIZE;
            m_colSize = BEGINNER_SIZE;
            m_bombCount = BEGINNER_BOMB_COUNT;
        }
        else if (levelType.equalsIgnoreCase(INTERMEDIATE_LEVEL))
        {
            m_rowSize = INTERMEDIATE_SIZE;
            m_colSize = INTERMEDIATE_SIZE;
            m_bombCount = INTERMEDIATE_BOMB_COUNT;
        }
        else if (levelType.equalsIgnoreCase(EXPERT_LEVEL))
        {
            m_rowSize = EXPERT_ROW_SIZE;
            m_colSize = EXPERT_COL_SIZE;
            m_bombCount = EXPERT_BOMB_COUNT;
        }

        // Instantiates board with row x col containing bombCount number of bombs
        m_board = new Board(m_rowSize, m_colSize, m_bombCount);

        // Adds all urls into a list of urls
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
                node.setStyle(BLACK_BORDER_STYLE + ";" + DEFAULT_BACKGROUND_COLOR + ";");
                initMouseEvent((Button) node);
            }
        }

        // Iterate over each url in list, create a BackgroundImage & set background image to associated member variable
        for (String url : m_imageUrls)
        {
            BackgroundImage backgroundImage = null;
            BackgroundSize bgSize = new BackgroundSize(100, 100, true, true, true, true);

            try
            {
                backgroundImage = new BackgroundImage(
                    new Image(Objects.requireNonNull(getClass().getResource(url)).toExternalForm()),
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

        // Set initial bomb count to expected bomb count for the layout
        bombCount.setText(STRING_ZERO + m_bombCount);
    }

    /**
     * Main controller's run method which spins up an anonymous thread for managing the timeCount
     * text field. The main thread itself will synchronously loop on updates sent by the user until
     * the game is over.
     */
    @Override
    public void run()
    {
        // Thread for managing timeCount text field
        new Thread(() ->
        {
            // Will block until first choice has been made initiating the game
            blockForChoice();

            // Will handle timeCount text field until game is over
            handleTimeCount();
        }).start();

        while (!isShutdown())
        {
            // wait for choice
            blockForChoice();

            // Perform choice update
            update(m_choice);
        }
    }

    /**
     * The update procedure will submit the move to the board object, retrieve the selections
     * array from the board object & will iterate through the selections, ensuring all selections
     * (including the new one) have been updated on the board GUI appropriately. Lastly, the
     * game status will be checked to determine if the game has finished.
     *
     * @param choice - The button position that was just recently selected.
     */
    private void update(String choice)
    {
        String[] rowCol = choice.split("_");
        int row = Integer.parseInt(rowCol[0]);
        int col = Integer.parseInt(rowCol[1]);

        m_board.submitMove(row - 1, col - 1);
        int[][] selections = m_board.getSelectionArray();

        // Iterate through each selection
        for (int i = 0; i < m_rowSize; i++)
        {
            for (int j = 0; j < m_colSize; j++)
            {
                Node button = getNode(i + 1, j + 1);

                // Skip over: signifies a bomb or a position that wasn't selected
                if (selections[i][j] == 11 || selections[i][j] == 0)
                {
                    continue;
                }

                // If selection was a selected blank
                if (selections[i][j] == 10)
                {
                    setButtonBackground(button, m_blankBackground);
                }
                else if (selections[i][j] == 9) // If selection position was a bomb
                {
                    setButtonBackground(button, m_hitBombBackground);
                }
                else if (selections[i][j] != 0) // If selection position was a count position
                {
                    setCountButtonBackground(button, selections[i][j]);
                }
            }
        }

        m_board.printBoard();
        checkGameStatus(choice, row, col);
    }

    /**
     * At the end of the game, this method is called in order to update the board GUI so that it
     * is updated to show the uncovered game board.
     *
     * @param hitPosition - The last position that was hit before the gaming ending.
     * @param won - Flag representing whether the game has been won.
     */
    private void displayEndGame(String hitPosition, boolean won)
    {
        int[][] boardArray = m_board.getBoardArray();
        String[] rowCol = hitPosition.split("_");
        int row = Integer.parseInt(rowCol[0]);
        int col = Integer.parseInt(rowCol[1]);

        for (int i = 0; i < m_rowSize; i++)
        {
            for (int j = 0; j < m_colSize; j++)
            {
                Node button = getNode(i + 1, j + 1);

                // If board array position is empty (either not selected or was selected)
                if (boardArray[i][j] == 0 || boardArray[i][j] == 10)
                {
                    setButtonBackground(button, m_blankBackground);
                }
                else if (boardArray[i][j] == 9) // If bomb position
                {
                    setButtonBackground(button, m_bombBackground);
                }
                else if (boardArray[i][j] != 0) // If count position
                {
                    setCountButtonBackground(button, boardArray[i][j]);
                }
            }
        }

        // Get latest guess node
        Node button = getNode(row, col);

        // If game was won, set as the associated count, otherwise place hit bomb background
        if (won)
        {
            setCountButtonBackground(button, boardArray[row - 1][col - 1]);
        }
        else
        {
            setButtonBackground(button, m_hitBombBackground);
        }
    }

    /**
     * Performs a game status check after an update was performed to determine if the game
     * has finished, and if so, did the user win or lose.
     *
     * @param choice - The button position that was just recently selected.
     * @param row - The row of the choice that was selected.
     * @param col - The column of the choice that was selected.
     */
    private void checkGameStatus(String choice, int row, int col)
    {
        // Check if game was lost based on row/col position (-1 for id's start at 1 not 0).
        if (m_board.lostGame(row - 1, col - 1))
        {
            setShutdown(true);
            infoBox("Sorry, you lost.", "Lost");
            displayEndGame(choice, false);
            m_board.printEndBoard();
        }
        else if (m_board.wonGame())
        {
            setShutdown(true);
            infoBox("Congratulations, you won!", "Won");
            displayEndGame(choice, true);
            m_board.printEndBoard();
        }
    }

    /**
     * An event handler for when a mouse click is registered on a button within the player grid.
     * If a right click button is registered (secondary), the flagHandler logic will be called.
     * If a left click button is registered (primary), the position is checked for a flag & if no
     * flag was placed on the position, the event signals to the main thread to perform an update.
     */
    private final EventHandler<MouseEvent> mouseClickEvent = event ->
    {
        String position = ((Node) event.getTarget()).getId().substring(1);
        Node button = getButton(position);

        // If right click was used, attempt to place or remove a flag at that position
        if (event.getButton().equals(MouseButton.SECONDARY))
        {
            flagHandler(position);
            return;
        }

        // If button is not null && a flag is not placed, signal thread to perform update
        if (button != null && !((Button) button).getBackground().equals(m_flagBackground))
        {
            m_choice = position;
            m_logger.debug("Selected button position: " + m_choice + ".");

            // If first choice, initialize startTime
            if (m_startTime == 0)
            {
                m_startTime = System.currentTimeMillis();
            }

            // Signal main thread to perform update with m_choice.
            performChoiceSignal();
        }
    };

    /**
     * The flagHandler method will determine if a flag is being placed or removed and based on that
     * determination will increment/decrement the bombCount text value on the GUI, respectively.
     *
     * @param position - The string based position containing some row and column.
     */
    private void flagHandler(String position)
    {
        Node button = getButton(position);

        if (button != null)
        {
            if (((Button) button).getBackground().equals(m_flagBackground))
            {
                m_logger.debug("Removing flag on button position: " + position + ".");

                button.setStyle(BLACK_BORDER_STYLE + ";" + DEFAULT_BACKGROUND_COLOR + ";");

                incrementBombCountProtocol();
            }
            else
            {
                m_logger.debug("Placing flag on button position: " + position + ".");

                button.setStyle(BLACK_BORDER_STYLE);
                ((Button) button).setBackground(m_flagBackground);

                decrementBombCountProtocol();
            }
        }
    }

    /**
     * A handler method which is utilized by the anonymous thread started by the controller's
     * run method & manages the updating of the timeCount text field containing the seconds
     * passed during the game.
     */
    private void handleTimeCount()
    {
        String time;
        long elapsed;
        long elapsedSeconds;
        String secondsText = " seconds.";
        String settingTimeCountText = "Setting timeCount text to: ";

        // Iterate until game is over
        while (!isShutdown())
        {
            // Get elapsed & convert to seconds
            elapsed = System.currentTimeMillis() - m_startTime;
            elapsedSeconds = elapsed / 1000;

            // If seconds is less than 10, should have 2 padded zeros: 001-009
            if (elapsedSeconds < 10)
            {
                time = STRING_ZERO.repeat(2) + elapsedSeconds;
            }
            else if (elapsedSeconds < 100) // If seconds is less than 100, should have 1 padded zero: 010-099
            {
                time = STRING_ZERO + elapsedSeconds;
            }
            else // If seconds is greater than 100, should not pad any zeros: 100-999+
            {
                time = "" + elapsedSeconds;
            }

            // Log & set time if seconds has increased
            if (!time.equalsIgnoreCase(timeCount.getText()))
            {
                m_logger.trace(settingTimeCountText + time + secondsText);
                timeCount.setText(time);
            }

            // Release cpu cycles by sleeping
            sleep(HALF_SEC);
        }
    }

    /**
     * Performs a background update on the provided button to set some arbitrary non-count
     * background as the button's background.
     *
     * @param button - The button object to perform the count background update to.
     * @param background - The background object that should be used.
     */
    private void setButtonBackground(Node button, Background background)
    {
        if (button != null)
        {
            button.setOpacity(1);
            button.setDisable(true);
            button.setStyle(BLACK_BORDER_STYLE);
            ((Button) button).setBackground(background);
        }
    }

    /**
     * Performs a background update on the provided button to set some bomb count figure as
     * the button's background.
     *
     * @param button - The button object to perform the count background update to.
     * @param count - The count associated with the update.
     */
    private void setCountButtonBackground(Node button, int count)
    {
        if (button != null)
        {
            button.setOpacity(1);
            button.setDisable(true);
            button.setStyle(BLACK_BORDER_STYLE);
            setImage(((Button) button), count);
        }
    }

    /**
     * Retrieves the current bomb count, logs the value, performs the increment, logs the new value &
     * then updates the bombCount text value on the GUI.
     */
    private void incrementBombCountProtocol()
    {
        int currBombCount = getCurrentBombCountText();

        m_logger.trace("Current Bomb Count Text Value: " + currBombCount);
        currBombCount++;
        m_logger.trace("Updated Bomb Count Text Value: " + currBombCount);

        updateBombCountText(currBombCount);
    }

    /**
     * Retrieves the current bomb count, logs the value, performs the decrement, logs the new value &
     * then updates the bombCount text value on the GUI.
     */
    private void decrementBombCountProtocol()
    {
        int currBombCount = getCurrentBombCountText();

        m_logger.trace("Current Bomb Count Text Value: " + currBombCount);
        currBombCount--;
        m_logger.trace("Updated Bomb Count Text Value: " + currBombCount);

        updateBombCountText(currBombCount);
    }

    /**
     * This method will retrieve the current bombCount value from its text & remove a prepended 0
     * if it exists, this is to handle the negative integer conversion case as a 0 before the negative
     * causes problems.
     *
     * @return int - Returns the current bomb count text that exists within the bombCount text field.
     */
    private int getCurrentBombCountText()
    {
        int currBombCount;

        if (bombCount.getText().substring(0, 1).equalsIgnoreCase(STRING_ZERO))
        {
            currBombCount = Integer.parseInt(bombCount.getText().substring(1));
        }
        else
        {
            currBombCount = Integer.parseInt(bombCount.getText());
        }

        return currBombCount;
    }

    /**
     * This method updates the bombCount JavaFX text reference to reflect how many bombs remain after
     * flagging (or unflagging) some set of bombs.
     *
     * @param count - The current count of the expected bombs still left on the game board.
     */
    private void updateBombCountText(int count)
    {
        // If count is in either [-9, -1] or [10, 99] sets, only prepend 1 zero.
        if ((count > -10 && count < 0) || (count > 9 && count < 99))
        {
            bombCount.setText(STRING_ZERO + count);
        }
        else if (count <= -10 || count > 99) // If count is in either (-Inf, -10] or [100, Inf) sets, prepend nothing.
        {
            bombCount.setText("" + count);
        }
        else // All other cases should prepend 2 zeros
        {
            bombCount.setText(STRING_ZERO.repeat(2) + count);
        }
    }

    /**
     * After selecting a button which was not a bomb, the background of the button must be
     * set to the count representing the number of bombs surrounding it. This method performs
     * that action for some button with some count.
     *
     * @param button - A button within the grid that has been selected and contains some bomb hint.
     * @param count - The count of bombs surrounding the button position.
     */
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

    /**
     * During the initialize call, each background type needs to be instantiated during the
     * game in order to set backgrounds properly for different game events.
     *
     * @param img - The BackgroundImage associated with the url.
     * @param url - The background url associated with the BackgroundImage.
     */
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

    /**
     * Synchronizes on the choice signal & executes a notifyAll in order to wake up all waiting
     * threads.
     */
    private void performChoiceSignal()
    {
        synchronized (m_choiceSignal)
        {
            m_choiceSignal.notifyAll();
        }
    }

    /**
     * A blocking synchronous method which will block the calling thread until a choice has been
     * made by the user.
     */
    private void blockForChoice()
    {
        synchronized (m_choiceSignal)
        {
            try
            {
                m_choiceSignal.wait();
            }
            catch (Exception e)
            {
                m_logger.error(e, e);
                Thread.currentThread().interrupt();
            }
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
     * Gets a button at some string based position.
     *
     * @param position - The string based position containing some row and column.
     * @return Node - A node which is some button within the playable boardGrid.
     */
    private Node getButton(String position)
    {
        String[] rowCol = position.split("_");
        int row = Integer.parseInt(rowCol[0]);
        int col = Integer.parseInt(rowCol[1]);

        return getNode(row, col);
    }

    /**
     * Retrieves a reference to some node at some row and column position.
     *
     * @param row - The row the node is located at.
     * @param col - The column the node is located at.
     * @return Node - The node associated with position at row:col.
     */
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

    /**
     * Public method for game controller to be shutdown from an outside component.
     */
    public void shutdown()
    {
        setShutdown(true);
    }

    /**
     * A synchronous method for determining if the game is still going or if it is over.
     *
     * @return boolean - A boolean representing whether the game has shutdown & finished or not.
     */
    private boolean isShutdown()
    {
        synchronized (m_shutdownMutex)
        {
            return m_shutdown;
        }
    }

    /**
     * A synchronous method for setting the shutdown flag, should be set to true when the game
     * is over.
     *
     * @param shutdown - A boolean to set the internal synchronized shutdown flag to.
     */
    private void setShutdown(boolean shutdown)
    {
        synchronized (m_shutdownMutex)
        {
            m_shutdown = shutdown;
        }
    }
}
