
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main launcher for the Grocery app (JavaFX).
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        GroceryModel model = new GroceryModel();
        GroceryController controller = new GroceryController(model);
        GroceryView view = new GroceryView(controller, model);

        view.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Local Fresh Groceries");
        primaryStage.setScene(view.getMainScene());
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
