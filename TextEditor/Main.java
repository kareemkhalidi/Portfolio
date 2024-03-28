import mvc.DocumentController;
import mvc.DocumentModel;
import mvc.DocumentView;

public class Main {
    public static void main(String[] args) {
        DocumentModel model = new DocumentModel();
        DocumentController controller = new DocumentController(model);
        DocumentView view = new DocumentView(model, controller);
        model.attachObserver(view);
        view.start();
    }
}
