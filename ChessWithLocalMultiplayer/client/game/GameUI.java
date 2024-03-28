package client.game;

import base.GameSaveSystem;
import base.SWTWindow;
import base.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import server.game.GameState;
import server.game.gamemodes.GameMode;
import server.game.gamemodes.Standard;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a GameUI observer that handles the UI for the game while in the menu, while
 * playing, or while paused.
 *
 * @author John Gauci
 * @author Kareem Khalidi
 */

public class GameUI {

    static final boolean DEBUG = false;
    private final GameManager gameManager;
    private final Display display;
    private final Shell shell;
    private final List<Widget> widgets;


    /**
     * Initializes a GameUI.
     *
     * @author John Gauci
     */
    public GameUI() {
        super();
        this.gameManager = new GameManager();
        this.display = Display.getDefault();
        this.shell = new Shell(this.display, SWT.SHELL_TRIM & ~(SWT.RESIZE | SWT.MIN | SWT.MAX | SWT.CLOSE));
        final int backgroundColor;
        if (Display.isSystemDarkTheme()) backgroundColor = Window.COLOR_BLACK;
        else backgroundColor = Window.COLOR_WHITE;
        this.shell.setBackground(this.display.getSystemColor(backgroundColor));
        this.widgets = new ArrayList<>(10);
        SWTWindow.addImage("assets/graphics/white_pawn.png");
        SWTWindow.addImage("assets/graphics/white_knight.png");
        SWTWindow.addImage("assets/graphics/white_bishop.png");
        SWTWindow.addImage("assets/graphics/white_rook.png");
        SWTWindow.addImage("assets/graphics/white_queen.png");
        SWTWindow.addImage("assets/graphics/white_king.png");
        SWTWindow.addImage("assets/graphics/black_pawn.png");
        SWTWindow.addImage("assets/graphics/black_knight.png");
        SWTWindow.addImage("assets/graphics/black_bishop.png");
        SWTWindow.addImage("assets/graphics/black_rook.png");
        SWTWindow.addImage("assets/graphics/black_queen.png");
        SWTWindow.addImage("assets/graphics/black_king.png");
    }

    public static void printDebugInfo() {
        System.out.println("DEBUG: " + Thread.activeCount() + " active Java threads: ");
        final Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        for (final Thread thread : threads) {
            System.out.println("Thread name: " + thread.getName());
        }
        System.out.println();
    }

    /**
     * Clears the widgets on the main menu
     *
     * @author John Gauci
     */
    void clear() {
        this.widgets.forEach(Widget::dispose);
        this.widgets.clear();
    }

    /**
     * Sets the shell with the main menu widgets
     *
     * @author Kareem Khalidi
     * @author John Gauci
     * @author Alexander Cooper
     */
    public void set() {
        this.shell.setText("Chess Game");
        this.shell.setSize(300, 200);
        this.shell.setLayout(new GridLayout(1, false));
        this.shell.requestLayout();
        final GridData widgetLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        final Button localMenuButton = new Button(this.shell, SWT.PUSH);
        localMenuButton.setText("Start Local Game");
        localMenuButton.setLayoutData(widgetLayoutData);
        localMenuButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                GameUI.this.clear();
                GameUI.this.setLocalMainMenu();
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {
            }

        });
        final Button networkMenuButton = new Button(this.shell, SWT.PUSH);
        networkMenuButton.setText("Join/Host Networked Game");
        networkMenuButton.setLayoutData(widgetLayoutData);
        networkMenuButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                GameUI.this.clear();
                GameUI.this.setNetworkMainMenu();
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {
            }
        });
        final Button quitButton = new Button(this.shell, SWT.PUSH);
        quitButton.setText("Quit");
        quitButton.setLayoutData(widgetLayoutData);
        quitButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                GameUI.this.clear();
                System.exit(0);
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {
            }

        });
        this.widgets.add(networkMenuButton);
        this.widgets.add(localMenuButton);
        this.widgets.add(quitButton);
    }

    private void setLocalMainMenu() {
        this.shell.setSize(300, 475);
        final GridData widgetLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        this.shell.requestLayout();
        final Label name1EntryLabel = new Label(this.shell, SWT.NULL);
        name1EntryLabel.setText("Player 1 Name:");
        name1EntryLabel.setLayoutData(widgetLayoutData);
        final Text name1EntryField = new Text(this.shell, SWT.BORDER);
        name1EntryField.setSize(100, 10);
        name1EntryField.setLayoutData(widgetLayoutData);
        final Label name2EntryLabel = new Label(this.shell, SWT.NULL);
        name2EntryLabel.setText("Player 2 Name:");
        name2EntryLabel.setLayoutData(widgetLayoutData);
        final Text name2EntryField = new Text(this.shell, SWT.BORDER);
        name2EntryField.setSize(100, 10);
        name2EntryField.setLayoutData(widgetLayoutData);
        final Label timerLabel = new Label(this.shell, SWT.NULL);
        timerLabel.setText("Select Game Time:");
        timerLabel.setLayoutData(widgetLayoutData);
        final Combo timerCombo = new Combo(this.shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        timerCombo.setItems("No Limit", "30:00", "15:00", "10:00",
                "5:00", "3:00", "1:00");
        timerCombo.select(0);
        timerCombo.setLayoutData(widgetLayoutData);
        final Label saveFileEntryLabel = new Label(this.shell, SWT.NULL);
        saveFileEntryLabel.setText("Select Save File:");
        saveFileEntryLabel.setLayoutData(widgetLayoutData);
        final Combo saveFileCombo = new Combo(this.shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        saveFileCombo.add("New Game");
        for (final int slot : GameSaveSystem.getSavedGameSlots())
            saveFileCombo.add("Save File " + slot);

        saveFileCombo.select(0);
        saveFileCombo.setLayoutData(widgetLayoutData);
        final Button startGameButton = new Button(this.shell, SWT.PUSH);
        startGameButton.setText("Start New Local Game");
        startGameButton.setLayoutData(widgetLayoutData);
        startGameButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                String player1Name = name1EntryField.getText();
                if (player1Name.isBlank()) player1Name = "Player 1";
                String player2Name = name2EntryField.getText();
                if (player2Name.isBlank()) player2Name = "Player 2";

                GameMode gameMode = switch (timerCombo.getSelectionIndex()){
                    case 1 -> new Standard(2, 30);
                    case 2 -> new Standard(2, 15);
                    case 3 -> new Standard(2, 10);
                    case 4 -> new Standard(2, 5);
                    case 5 -> new Standard(2, 3);
                    case 6 -> new Standard(2, 1);
                    default -> new Standard();
                };

                final GameState gameState;
                if (saveFileCombo.getSelectionIndex() == 0)
                    gameState = GameSaveSystem.getNewGame(gameMode);
                else
                    gameState = GameSaveSystem.loadFromSaveFile(Integer.parseInt(saveFileCombo.getText().split(" ")[2]), gameMode);

                GameUI.this.gameManager.newLocalGame(GameUI.this, player1Name, player2Name,
                        gameState);
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {
            }
        });
        final Button mainMenuButton = new Button(this.shell, SWT.PUSH);
        mainMenuButton.setText("Back to Main Menu");
        mainMenuButton.setLayoutData(widgetLayoutData);
        mainMenuButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                GameUI.this.clear();
                GameUI.this.set();
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {
            }
        });
        this.widgets.add(name1EntryLabel);
        this.widgets.add(name1EntryField);
        this.widgets.add(name2EntryLabel);
        this.widgets.add(name2EntryField);
        this.widgets.add(timerLabel);
        this.widgets.add(timerCombo);
        this.widgets.add(saveFileEntryLabel);
        this.widgets.add(saveFileCombo);
        this.widgets.add(startGameButton);
        this.widgets.add(mainMenuButton);

    }


    private void setNetworkMainMenu() {
        this.shell.setSize(300, 450);
        final GridData widgetLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        this.shell.requestLayout();
        final Label nameEntryLabel = new Label(this.shell, SWT.NULL);
        nameEntryLabel.setText("Name:");
        nameEntryLabel.setLayoutData(widgetLayoutData);
        final Text nameEntryField = new Text(this.shell, SWT.BORDER);
        nameEntryField.setSize(100, 10);
        nameEntryField.setLayoutData(widgetLayoutData);
        final Label ipEntryLabel = new Label(this.shell, SWT.NULL);
        ipEntryLabel.setText("IP:");
        ipEntryLabel.setLayoutData(widgetLayoutData);
        final Text ipEntryField = new Text(this.shell, SWT.BORDER);
        ipEntryField.setSize(100, 10);
        ipEntryField.setLayoutData(widgetLayoutData);
        final Label timerLabel = new Label(this.shell, SWT.NULL);
        timerLabel.setText("Select Game Time:");
        timerLabel.setLayoutData(widgetLayoutData);
        final Combo timerCombo = new Combo(this.shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        timerCombo.setItems("No Limit", "30:00", "15:00", "10:00",
                "5:00", "3:00", "1:00");
        timerCombo.select(0);
        timerCombo.setLayoutData(widgetLayoutData);
        final Button joinNetworkButton = new Button(this.shell, SWT.PUSH);
        joinNetworkButton.setText("Join Network Game");
        joinNetworkButton.setLayoutData(widgetLayoutData);
        joinNetworkButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                final String playerName = nameEntryField.getText();
                String ipText = ipEntryField.getText();
                if (ipText.isBlank()) ipText = "127.0.0.1";
                try {
                    GameUI.this.gameManager.joinNetworkGame(GameUI.this, playerName, ipText);
                } catch (final RuntimeException ex) {
                    if (GameUI.DEBUG) ex.printStackTrace();
                    GameUI.this.gameManager.shutDown(GameUI.this);
                    final MessageBox ipErrorBox = new MessageBox(GameUI.this.shell);
                    ipErrorBox.setText("IP ERROR");
                    ipErrorBox.setMessage("Invalid IP was entered, please try again with a " +
                            "valid IP.");
                    ipErrorBox.open();
                }
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {
            }
        });
        final Button hostNetworkButton = new Button(this.shell, SWT.PUSH);
        hostNetworkButton.setText("Host Network Game");
        hostNetworkButton.setLayoutData(widgetLayoutData);
        hostNetworkButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                final String playerName = nameEntryField.getText();
                final GameMode gameMode = switch (timerCombo.getSelectionIndex()){
                    case 1 -> new Standard(2, 30);
                    case 2 -> new Standard(2, 15);
                    case 3 -> new Standard(2, 10);
                    case 4 -> new Standard(2, 5);
                    case 5 -> new Standard(2, 3);
                    case 6 -> new Standard(2, 1);
                    default -> new Standard();
                };
                GameUI.this.shell.setText("Chess Game - Player: " + playerName + " Server: " + "Host");
                GameUI.this.gameManager.hostNetworkGame(GameUI.this, playerName,
                        GameSaveSystem.getNewGame(gameMode));
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {
            }
        });
        final Button mainMenuButton = new Button(this.shell, SWT.PUSH);
        mainMenuButton.setText("Back to Main Menu");
        mainMenuButton.setLayoutData(widgetLayoutData);
        mainMenuButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                GameUI.this.clear();
                GameUI.this.set();
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {
            }
        });

        final Button debugButton = new Button(this.shell, SWT.PUSH);
        debugButton.setText("Debug");
        debugButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                GameUI.printDebugInfo();

            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {

            }
        });
        debugButton.setVisible(GameUI.DEBUG);

        this.widgets.add(debugButton);
        this.widgets.add(nameEntryLabel);
        this.widgets.add(nameEntryField);
        this.widgets.add(ipEntryLabel);
        this.widgets.add(ipEntryField);
        this.widgets.add(timerLabel);
        this.widgets.add(timerCombo);
        this.widgets.add(joinNetworkButton);
        this.widgets.add(hostNetworkButton);
        this.widgets.add(mainMenuButton);
    }

    /**
     * Opens the GameUI, setting the main menu and waiting for events
     *
     * @author John Gauci
     */
    public void open() {
        this.set();
        this.shell.open();
        while (!this.shell.isDisposed()) {
            if (!this.display.readAndDispatch()) {
                this.display.sleep();
            }
        }
    }


    /**
     * Returns the shell for the UI.
     *
     * @return UI shell
     * @author John Gauci
     */
    public Shell getShell() {
        return this.shell;
    }


}

