package client.game;

import base.Observer;
import base.SWTKeyInput;
import base.SWTMouseInput;
import base.SWTWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import server.game.GameState;
import server.game.gamemodes.GameMode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * * Implementation of a PlayingUI observer that handles the UI for the game while playing.
 */
public class PlayingUI extends Observer {

    public static final int CANVAS_DIMENSIONS = 800;
    private static final int FRAMERATE = 60;
    private static final long RENDER_DELAY = (long) ((1.0 / PlayingUI.FRAMERATE) * 1000.0);
    private final Shell shell;
    private final GameController gameController;
    private Canvas canvas;
    private MouseListener mouseListener;
    private KeyListener keyListener;
    private PaintListener paintListener;

    /**
     * Initializes the PlayingUI with the given parameters.
     *
     * @param subject        the ClientGameHandler to view
     * @param shell          the UI shell
     * @param gameController the Game Controller
     */
    public PlayingUI(final ClientGameHandler subject, final Shell shell,
                     final GameController gameController, final GameMode gameMode) {
        super(subject);
        this.shell = shell;
        this.gameController = gameController;
        this.shell.setSize(CANVAS_DIMENSIONS + 22, CANVAS_DIMENSIONS + 96);
        this.canvas = new Canvas(this.shell, SWT.DOUBLE_BUFFERED);
        this.canvas.setSize(CANVAS_DIMENSIONS, CANVAS_DIMENSIONS + 40);
        this.canvas.setFocus();
        this.canvas.setBackground(this.shell.getBackground());
        this.mouseListener = new MouseListener() {
            @Override
            public void mouseDoubleClick(final MouseEvent e) {

            }

            @Override
            public void mouseDown(final MouseEvent e) {
                gameController.processMouseInput(new SWTMouseInput(e));
            }

            @Override
            public void mouseUp(final MouseEvent e) {
            }
        };

        this.keyListener = new KeyListener() {
            @Override
            public void keyPressed(final KeyEvent e) {
                gameController.processKeyInput(new SWTKeyInput(e));
            }

            @Override
            public void keyReleased(final KeyEvent e) {

            }
        };
        this.paintListener = e -> this.getState().draw(new SWTWindow(e));
        this.canvas.addPaintListener(this.paintListener);
        this.canvas.addKeyListener(this.keyListener);
        this.canvas.addMouseListener(this.mouseListener);
    }

    /**
     * Gets the Game State of the ClientGameHandler.
     *
     * @return the state of the ClientGameHandler.
     * @author John Gauci
     */
    @Override
    public GameState getState() {
        return (GameState) super.getState();
    }

    /**
     * Code to be run in thread loop.
     *
     * @author John Gauci
     */
    @Override
    protected void loopExecute() {
        try {
            Thread.sleep(PlayingUI.RENDER_DELAY);
            Display.getDefault().asyncExec(this::render);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void render() {
        if (!this.canvas.isDisposed()) this.canvas.redraw();
    }

    /**
     * Clears the playing UI.
     *
     * @author John Gauci
     */
    public void clear() {
        if (!this.canvas.isDisposed()) {
            this.canvas.removePaintListener(this.paintListener);
            this.canvas.removeKeyListener(this.keyListener);
            this.canvas.removeMouseListener(this.mouseListener);
            this.canvas.dispose();
        }
    }


    /**
     * Sets the PlayingUI.
     */
    public void set() {
        this.shell.setSize(CANVAS_DIMENSIONS + 22, CANVAS_DIMENSIONS + 96);
        this.canvas = new Canvas(this.shell, SWT.DOUBLE_BUFFERED);
        this.canvas.setSize(CANVAS_DIMENSIONS, CANVAS_DIMENSIONS + 40);
        this.canvas.setFocus();
        this.canvas.setBackground(this.shell.getBackground());

        this.mouseListener = new MouseListener() {
            @Override
            public void mouseDoubleClick(final MouseEvent e) {

            }

            @Override
            public void mouseDown(final MouseEvent e) {
                PlayingUI.this.gameController.processMouseInput(new SWTMouseInput(e));
            }

            @Override
            public void mouseUp(final MouseEvent e) {
            }
        };

        this.keyListener = new KeyListener() {
            @Override
            public void keyPressed(final KeyEvent e) {
                PlayingUI.this.gameController.processKeyInput(new SWTKeyInput(e));
            }

            @Override
            public void keyReleased(final KeyEvent e) {

            }
        };
        this.paintListener = e -> this.getState().draw(new SWTWindow(e));
        this.canvas.addPaintListener(this.paintListener);
        this.canvas.addKeyListener(this.keyListener);
        this.canvas.addMouseListener(this.mouseListener);
    }

    void setTitleClient(final String blackPlayerName, final String host) {
        this.shell.setText("Chess Game: White: " + blackPlayerName + " Server: " + host);
    }

    void setTitleHost(final String whitePlayerName) {
        try (final Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));
            final var host = socket.getLocalAddress().getHostAddress();
            this.shell.setText("Chess Game: White: " + whitePlayerName + " Server: Hosting on " + host);
        } catch (final IOException e) {
            this.shell.setText("Chess Game: White: " + whitePlayerName + " Server: Host");
        }
    }

    void setTitleLocal(final String whitePlayerName, final String blackPlayerName) {
        this.shell.setText("Chess Game: White: " + whitePlayerName + " Black: " + blackPlayerName +
                " Server: Local Game");
    }
}
