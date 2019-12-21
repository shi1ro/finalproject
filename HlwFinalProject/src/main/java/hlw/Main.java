package hlw;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{
    App app = new App();
    public static void main(String[] args) {
       launch(args);
    }
    @Override
    public void start(Stage stage)
    {
      app.initUI(stage);
    }
}