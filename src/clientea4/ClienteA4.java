package clientea4;

import java.io.IOException;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import view.ControladorGeneral;
import view.InicioFXController;


/**
 * La clase de inicio de la aplicación que inicia la primera ventana.
 * The start class of the application that starts the first window.
 * @author Luis
 */
public class ClienteA4 extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
       // try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/inicio.fxml"));

            Parent root = (Parent)loader.load();

            InicioFXController controller = ((InicioFXController)loader.getController());
            controller.setStage(stage);
            controller.initStage(root);
       
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
