package mvc;

import base.Observer;
import base.Subject;
import base.Window;
import composite.Glyph;
import composite.SWTWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

/**
 * Document View class that handles the SWT code and holds all SWT glyphs
 *
 * @author John Gauci
 * @author Kareem Khalidi
 */
public class DocumentView extends Observer {

    public static int CANVAS_WIDTH = 700;
    public static int CANVAS_HEIGHT = 700;
    private final Display display;
    private final Shell shell;
    private final DocumentController controller;
    private Canvas canvas;
    private Composite upperComposite;
    private Composite lowerComposite;
    private Slider slider;
    private Rectangle rectangle;

    private MessageBox spellCheckMsgBox;

    /**
     * Constructor for a new DocumentView object
     *
     * @param subject  the subject for the view
     * @param mediator the mediator for the view
     * @author John Gauci
     */
    public DocumentView(Subject subject, DocumentController mediator) {
        super(subject);
        Display.setAppName("Editor");
        display = new Display();
        shell = new Shell(display);
        shell.setText("Editor");
        shell.setSize(CANVAS_WIDTH + 50, CANVAS_HEIGHT + 150);
        shell.setLayout(new GridLayout());
        this.controller = mediator;
    }

    /**
     * Updates and redraws the canvas
     *
     * @author John Gauci
     */
    @Override
    public void update() {
        super.update();
        CANVAS_WIDTH = shell.getSize().x - 50;
        CANVAS_HEIGHT = shell.getSize().y - 150;
        canvas.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        upperComposite.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        lowerComposite.setBounds(20, CANVAS_HEIGHT + 10, CANVAS_WIDTH, 40);
        slider.setBounds(CANVAS_WIDTH - 35, 10, 32, CANVAS_WIDTH - 25);
        canvas.redraw();
    }

    /**
     * Returns the state of the Document
     *
     * @return CurrentDocumentState
     * @author John Gauci
     */
    @Override
    public DocumentState getState() {
        return (DocumentState) super.getState();
    }

    /**
     * Initializes all the SWT glyphs required for the Document View
     *
     * @author John Gauci
     */
    public void start() {
        set();
        update();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    /**
     * Sets up the SWT glyphs
     *
     * @author John Gauci
     */
    private void set() {
        upperComposite = new Composite(shell, SWT.NO_FOCUS);
        lowerComposite = new Composite(shell, SWT.NO_FOCUS);
        canvas = new Canvas(upperComposite, SWT.DOUBLE_BUFFERED);
        canvas.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        lowerComposite.setLayout(new RowLayout());
        lowerComposite.setBounds(20, CANVAS_HEIGHT + 10, CANVAS_WIDTH, 40);

        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                controller.processKeyInput(new SWTKeyInput(event));
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        setMenu();

        slider = new Slider(canvas, SWT.VERTICAL | SWT.NO_FOCUS);
        Rectangle clientArea = canvas.getClientArea();
        slider.setBounds(clientArea.width - 40, clientArea.y + 10, 32, clientArea.height);
        slider.addListener(SWT.Selection, event -> controller.processScrollInput(slider.getSelection()));

        canvas.addPaintListener(event -> {
            rectangle = shell.getClientArea();
            event.gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
            event.gc.fillRectangle(rectangle.x, rectangle.y, upperComposite.getSize().x, upperComposite.getSize().y);
            Glyph masterGlyph = getState().getScrollDecorator();
            getState().getComposite().repair();
            controller.runHyphenation();
            masterGlyph.draw(new SWTWindow(event, getState().getFontWidth(), getState().getFontWidth()));
        });

        spellCheckMsgBox = new MessageBox(shell);
        spellCheckMsgBox.setText("Spell Checker Results");

    }


    /**
     * Sets up the menu for the Doc View
     *
     * @author John Gauci
     * @author Kareem Khalidi
     */
    private void setMenu() {

        //---- main menu
        Menu menuBar, insertMenu, toolsMenu;
        MenuItem insertMenuHeader;
        MenuItem insertImageItem, insertRectItem, toolsMenuHeader, toolsSpellCheckItem, toolsUndoItem, toolsRedoItem;
        MenuItem insertSmallRectItem, insertMediumRectItem, insertLargeRectItem, insertSmallRedItem, insertSmallGreenItem;
        MenuItem insertSmallBlueItem, insertMediumRedItem, insertMediumGreenItem, insertMediumBlueItem, insertLargeRedItem;
        MenuItem insertLargeGreenItem, insertLargeBlueItem, insertCatImgItem, insertDogImgItem, insertFishImgItem;

        menuBar = new Menu(shell, SWT.BAR);


        insertMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        insertMenuHeader.setText("Insert");
        insertMenu = new Menu(shell, SWT.DROP_DOWN);
        insertMenuHeader.setMenu(insertMenu);

        insertImageItem = new MenuItem(insertMenu, SWT.CASCADE);
        insertImageItem.setText("Image");
        insertRectItem = new MenuItem(insertMenu, SWT.CASCADE);
        insertRectItem.setText("Rectangle");

        Menu imgMenu = new Menu(menuBar);
        insertImageItem.setMenu(imgMenu);

        insertDogImgItem = new MenuItem(imgMenu, SWT.DROP_DOWN);
        insertDogImgItem.setText("Dog");
        insertCatImgItem = new MenuItem(imgMenu, SWT.DROP_DOWN);
        insertCatImgItem.setText("Cat");
        insertFishImgItem = new MenuItem(imgMenu, SWT.DROP_DOWN);
        insertFishImgItem.setText("Fish");

        Menu sizeMenu = new Menu(menuBar);
        insertRectItem.setMenu(sizeMenu);

        insertSmallRectItem = new MenuItem(sizeMenu, SWT.CASCADE);
        insertSmallRectItem.setText("Small");
        insertMediumRectItem = new MenuItem(sizeMenu, SWT.CASCADE);
        insertMediumRectItem.setText("Medium");
        insertLargeRectItem = new MenuItem(sizeMenu, SWT.CASCADE);
        insertLargeRectItem.setText("Large");

        Menu smallColorMenu = new Menu(sizeMenu);
        insertSmallRectItem.setMenu(smallColorMenu);

        insertSmallRedItem = new MenuItem(smallColorMenu, SWT.DROP_DOWN);
        insertSmallRedItem.setText("Red");
        insertSmallGreenItem = new MenuItem(smallColorMenu, SWT.DROP_DOWN);
        insertSmallGreenItem.setText("Green");
        insertSmallBlueItem = new MenuItem(smallColorMenu, SWT.DROP_DOWN);
        insertSmallBlueItem.setText("Blue");

        Menu mediumColorMenu = new Menu(sizeMenu);
        insertMediumRectItem.setMenu(mediumColorMenu);

        insertMediumRedItem = new MenuItem(mediumColorMenu, SWT.DROP_DOWN);
        insertMediumRedItem.setText("Red");
        insertMediumGreenItem = new MenuItem(mediumColorMenu, SWT.DROP_DOWN);
        insertMediumGreenItem.setText("Green");
        insertMediumBlueItem = new MenuItem(mediumColorMenu, SWT.DROP_DOWN);
        insertMediumBlueItem.setText("Blue");

        Menu largeColorMenu = new Menu(sizeMenu);
        insertLargeRectItem.setMenu(largeColorMenu);

        insertLargeRedItem = new MenuItem(largeColorMenu, SWT.DROP_DOWN);
        insertLargeRedItem.setText("Red");
        insertLargeGreenItem = new MenuItem(largeColorMenu, SWT.DROP_DOWN);
        insertLargeGreenItem.setText("Green");
        insertLargeBlueItem = new MenuItem(largeColorMenu, SWT.DROP_DOWN);
        insertLargeBlueItem.setText("Blue");

        toolsMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        toolsMenuHeader.setText("Tools");
        toolsMenu = new Menu(shell, SWT.DROP_DOWN);
        toolsMenuHeader.setMenu(toolsMenu);

        toolsSpellCheckItem = new MenuItem(toolsMenu, SWT.PUSH);
        toolsSpellCheckItem.setText("Run Spell Checker");
        toolsUndoItem = new MenuItem(toolsMenu, SWT.PUSH);
        toolsUndoItem.setText("Undo");
        toolsRedoItem = new MenuItem(toolsMenu, SWT.PUSH);
        toolsRedoItem.setText("Redo");

        int smallWidth = 24;
        int mediumWidth = 48;
        int largeWidth = 72;

        int smallHeight = 40;
        int mediumHeight = 80;
        int largeHeight = 120;

        int red = Window.COLOR_RED;
        int green = Window.COLOR_GREEN;
        int blue = Window.COLOR_BLUE;

        insertDogImgItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.insertImage("assets\\dog.jpg");
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        insertCatImgItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.insertImage("assets\\cat.jpg");
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        insertFishImgItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.insertImage("assets\\fish.jpg");
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        insertSmallRedItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.insertRectangle(smallWidth, smallHeight, red);
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        insertSmallGreenItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.insertRectangle(smallWidth, smallHeight, green);
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        insertSmallBlueItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.insertRectangle(smallWidth, smallHeight, blue);
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        insertMediumRedItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.insertRectangle(mediumWidth, mediumHeight, red);
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        insertMediumGreenItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.insertRectangle(mediumWidth, mediumHeight, green);
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        insertMediumBlueItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.insertRectangle(mediumWidth, mediumHeight, blue);
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        insertLargeRedItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.insertRectangle(largeWidth, largeHeight, red);
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        insertLargeGreenItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.insertRectangle(largeWidth, largeHeight, green);
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        insertLargeBlueItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.insertRectangle(largeWidth, largeHeight, blue);
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        insertImageItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {

            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        toolsSpellCheckItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                String result = controller.runSpellCheck();
                spellCheckMsgBox(result);
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        toolsUndoItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.undo();
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        toolsRedoItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                controller.redo();
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });


        shell.setMenuBar(menuBar);

    }

    /**
     * Sets the message of the spell check message box to the results of the spell
     * checker and then displays the message box to the user
     *
     * @param result the string holding the results of the spell checker
     * @author Kareem Khalidi
     */
    private void spellCheckMsgBox(String result) {

        spellCheckMsgBox.setMessage(result);
        spellCheckMsgBox.open();

    }

}