package client.game;

import base.GameOverType;
import base.GameSaveSystem;
import base.User;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import server.game.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a PauseUI that handles the UI for the game while in the pause menu or game
 * over screen.
 *
 * @author John Gauci
 * @author Kareem Khalidi
 */

public class PauseUI {
    private final GameUI gameUI;
    private final List<Widget> widgets;
    private final GameManager gameManager;
    private final Shell shell;

    /**
     * Initialization of a PauseUI with
     *
     * @param gameManager the Game Manager
     * @param gameUI      the Game UI
     * @param shell       the UI shell
     * @param gameState   the current GameState
     * @author Kareem Khalidi
     * @author John Gauci
     */
    public PauseUI(final GameManager gameManager, final GameUI gameUI, final Shell shell,
                   final GameState gameState) {
        super();
        this.gameManager = gameManager;
        this.gameUI = gameUI;
        this.shell = gameUI.getShell();
        this.widgets = new ArrayList<>(5);
        this.shell.setSize(PlayingUI.CANVAS_DIMENSIONS, PlayingUI.CANVAS_DIMENSIONS);
        this.shell.setFocus();
        this.shell.setLayout(new GridLayout(1, false));
        final Label pauseTitle = new Label(this.shell, SWT.NONE);
        final Display display = this.shell.getDisplay();
        if (gameState.hasGameEnded()) {
            pauseTitle.setText("Game Over");
        } else {
            pauseTitle.setText("Pause");
        }
        final Table scoreBoardTable = new Table(this.shell, SWT.BORDER | SWT.V_SCROLL);
        scoreBoardTable.setSize(400, 500);
        scoreBoardTable.setHeaderVisible(true);
        scoreBoardTable.setBackground(this.shell.getBackground());
        final TableColumn nameColumn = new TableColumn(scoreBoardTable, SWT.NULL);
        nameColumn.setText("NAME");
        final TableColumn scoreColumn = new TableColumn(scoreBoardTable, SWT.NULL);
        scoreColumn.setText("SCORE");
        final TableColumn pingColumn = new TableColumn(scoreBoardTable, SWT.NULL);
        pingColumn.setText("PING");
        int i = 0;
        for (final User player : gameState.getPlayers()) {
            final TableItem item = new TableItem(scoreBoardTable, SWT.NULL);
            item.setText(0, player.getName());
            final GameOverType gameStatus = gameState.getGameMode().isOver(gameState);
            if(gameStatus == GameOverType.CHECKMATE || gameStatus == GameOverType.TIME_OUT){
                if(gameState.getActivePlayer().isPresent() && !gameState.getActivePlayer().get().equals(player)){
                    item.setText(1, "Winner");
                }
                else{
                    item.setText(1, "Loser");
                }
            }
            else if(gameStatus == GameOverType.DRAW || gameStatus == GameOverType.STALEMATE){
                item.setText(1, "Tied");
            }
            else{
                item.setText(1, "In Progress");
            }
            item.setText(2, Long.toString(player.getPing()));
            if (i % 2 == 0) {
                item.setBackground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
            } else {
                item.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
            }
            i++;
        }
        for (final TableColumn column : scoreBoardTable.getColumns()) {
            column.pack();
        }
        scoreBoardTable.addListener(SWT.EraseItem, event -> scoreBoardTable.deselectAll());
        if (!gameState.hasGameEnded()) {
            final Button resumeButton = new Button(shell, SWT.PUSH);
            resumeButton.setText("Resume");
            resumeButton.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(final SelectionEvent e) {
                    PauseUI.this.gameManager.pauseResumeGameOver();
                }

                @Override
                public void widgetDefaultSelected(final SelectionEvent e) {
                }
            });
            this.widgets.add(resumeButton);
        }
        final Label saveFileLabel = new Label(this.shell, SWT.NULL);
        saveFileLabel.setText("Select Save File:");
        //saveFileEntryLabel.setLayoutData(widgetLayoutData);
        final Combo saveFileCombo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        saveFileCombo.setItems("Save File 1", "Save File 2", "Save File 3", "Save File 4",
                "Save File 5", "Save File 6", "Save File 7", "Save File 8", "Save File 9", "Save File 10");
        saveFileCombo.select(0);
        //saveFileCombo.setLayoutData(widgetLayoutData);
        final Button saveButton = new Button(this.shell, SWT.PUSH);
        saveButton.setText("Save Game to Selected Save Profile");
        //startGameButton.setLayoutData(widgetLayoutData);
        saveButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                GameSaveSystem.saveToFile(saveFileCombo.getSelectionIndex() + 1, gameState);
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {
            }
        });


        final Button mainMenuButton = new Button(shell, SWT.PUSH);
        mainMenuButton.setText("Main Menu");
        mainMenuButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                PauseUI.this.gameManager.mainMenu(PauseUI.this.gameUI, PauseUI.this);
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {

            }
        });

        final Button debugButton = new Button(shell, SWT.PUSH);
        debugButton.setText("DebugActiveThreads");
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
        shell.layout(true, true);
        this.widgets.add(pauseTitle);
        this.widgets.add(scoreBoardTable);
        this.widgets.add(saveFileLabel);
        this.widgets.add(saveFileCombo);
        this.widgets.add(saveButton);
        this.widgets.add(mainMenuButton);
        this.widgets.add(debugButton);
    }

    /**
     * Clears the PauseUI
     *
     * @author John Gauci
     */
    public void clear() {
        this.widgets.forEach(Widget::dispose);
        this.widgets.clear();
    }

    /**
     * Gets the display of the PauseUI
     *
     * @return PauseUI display
     * @author John Gauci
     */
    public Display getDisplay() {
        return this.shell.getDisplay();
    }

    /**
     * Sets the PauseUI with the given GameState
     *
     * @param gameState the current game state
     * @author Kareem Khalidi
     * @author John Gauci
     */
    public void set(final GameState gameState) {
        this.shell.setSize(PlayingUI.CANVAS_DIMENSIONS, PlayingUI.CANVAS_DIMENSIONS);
        this.shell.setFocus();
        this.shell.setLayout(new GridLayout(1, false));
        final Label pauseTitle = new Label(this.shell, SWT.NONE);
        final Display display = this.shell.getDisplay();
        if (gameState.hasGameEnded()) {
            pauseTitle.setText("Game Over");
        } else {
            pauseTitle.setText("Pause");
        }
        final Table scoreBoardTable = new Table(this.shell, SWT.BORDER | SWT.V_SCROLL);
        scoreBoardTable.setSize(400, 500);
        scoreBoardTable.setHeaderVisible(true);
        scoreBoardTable.setBackground(this.shell.getBackground());
        final TableColumn nameColumn = new TableColumn(scoreBoardTable, SWT.NULL);
        nameColumn.setText("NAME");
        final TableColumn scoreColumn = new TableColumn(scoreBoardTable, SWT.NULL);
        scoreColumn.setText("SCORE");
        final TableColumn pingColumn = new TableColumn(scoreBoardTable, SWT.NULL);
        pingColumn.setText("PING");
        int i = 0;
        for (final User player : gameState.getPlayers()) {
            final TableItem item = new TableItem(scoreBoardTable, SWT.NULL);
            item.setText(0, player.getName());
            final GameOverType gameStatus = gameState.getGameMode().isOver(gameState);
            if(gameStatus == GameOverType.CHECKMATE || gameStatus == GameOverType.TIME_OUT){
                if(gameState.getActivePlayer().isPresent() && !gameState.getActivePlayer().get().equals(player)){
                    item.setText(1, "Winner");
                }
                else{
                    item.setText(1, "Loser");
                }
            }
            else if(gameStatus == GameOverType.DRAW || gameStatus == GameOverType.STALEMATE){
                item.setText(1, "Tied");
            }
            else{
                item.setText(1, "In Progress");
            }
            item.setText(2, Long.toString(player.getPing()));
            if (i % 2 == 0) {
                item.setBackground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
            } else {
                item.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
            }
            i++;
        }
        for (final TableColumn column : scoreBoardTable.getColumns()) {
            column.pack();
        }
        scoreBoardTable.addListener(SWT.EraseItem, event -> scoreBoardTable.deselectAll());
        if (!gameState.hasGameEnded()) {
            final Button resumeButton = new Button(this.shell, SWT.PUSH);
            resumeButton.setText("Resume");
            resumeButton.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(final SelectionEvent e) {
                    PauseUI.this.gameManager.pauseResumeGameOver();
                }

                @Override
                public void widgetDefaultSelected(final SelectionEvent e) {
                }
            });
            this.widgets.add(resumeButton);
            final Label saveFileLabel = new Label(this.shell, SWT.NULL);
            saveFileLabel.setText("Select Save File:");
            //saveFileEntryLabel.setLayoutData(widgetLayoutData);
            final Combo saveFileCombo = new Combo(this.shell, SWT.DROP_DOWN | SWT.READ_ONLY);
            saveFileCombo.setItems("Save File 1", "Save File 2", "Save File 3", "Save File 4",
                    "Save File 5", "Save File 6", "Save File 7", "Save File 8", "Save File 9", "Save File 10");
            saveFileCombo.select(0);
            //saveFileCombo.setLayoutData(widgetLayoutData);
            final Button saveButton = new Button(this.shell, SWT.PUSH);
            saveButton.setText("Save Game to Selected Save Profile");
            //startGameButton.setLayoutData(widgetLayoutData);
            saveButton.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(final SelectionEvent e) {
                    GameSaveSystem.saveToFile(saveFileCombo.getSelectionIndex() + 1, gameState);
                }

                @Override
                public void widgetDefaultSelected(final SelectionEvent e) {
                }
            });
            this.widgets.add(saveFileLabel);
            this.widgets.add(saveFileCombo);
            this.widgets.add(saveButton);
        }

        final Button mainMenuButton = new Button(this.shell, SWT.PUSH);
        mainMenuButton.setText("Main Menu");
        mainMenuButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                PauseUI.this.gameManager.mainMenu(PauseUI.this.gameUI, PauseUI.this);
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {

            }
        });

        final Button debugButton = new Button(this.shell, SWT.PUSH);
        debugButton.setText("DebugActiveThreads");
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
        this.shell.layout(true, true);
        this.widgets.add(pauseTitle);
        this.widgets.add(scoreBoardTable);
        this.widgets.add(mainMenuButton);
        this.widgets.add(debugButton);
    }
}
