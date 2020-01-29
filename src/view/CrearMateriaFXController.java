package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.MateriaBean;
import static view.ControladorGeneral.showWarningAlert;

/**
 * El controlador de la ventana CrearMateriaFx para crear las materias.
 * @author Luis
 */
public class CrearMateriaFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.CrearMateriaFXController");
    GestorDeMateriasFXController fxMateria = null;
    private Stage stage;
    private MateriaBean materia;
    
    @FXML
    public Button btnCrearCrearMateria;
    @FXML
    private Button btnSalirCrearMateria;
    @FXML
    private TextField tfTituloCrearMateria;
    @FXML
    private TextField tfDescripcionCrearMateria;
    
    /**
     * Método que le da un valor a materia.
     * @param materia Valor de materia.
     */
    public void setMateria(MateriaBean materia){
        this.materia = materia;
    }
    /**
     * Método que le da un valor a fxController.
     * @param fxController Valor de fxController.
     */
    public void setFXMateria(GestorDeMateriasFXController fxController){
        this.fxMateria = fxController;
    }
    
    /**
     * Método que inicializa la ventana.
     * @param root Nodo raiz.
     */
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando la ventana Crear Materia");
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Crear Materia");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setOnShowing(this::handleWindowShowing);
            stage.showAndWait();
        }catch(Exception e){
            LOGGER.severe("Error(initStage)" + e.getMessage());
        }
    }
    /**
     * Método que se ejecuta cuando se muestra la ventana.
     * @param event Evento que se ha lanzado.
     */
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> Crear Materia");
            tfTituloCrearMateria.requestFocus();
        }catch(Exception e){
            LOGGER.severe("Error(handleWindowShowing)" + e.getMessage());
        }
    }
    
    /**
     * Método que se ejecuta cuando se hace click en el botón btnEliminarGestorMateria.
     * @param event Evento que se ha lanzado.
     */
    @FXML
    public void onActionBtnCrearCrearMateria(ActionEvent event){
        if(!tfTituloCrearMateria.getText().isEmpty() && !tfDescripcionCrearMateria.getText().isEmpty()){
            materia.setTitulo(tfTituloCrearMateria.getText());
            materia.setDescripcion(tfDescripcionCrearMateria.getText());
            fxMateria.setOpc(1);
            stage.hide();
        }else{
            showWarningAlert("Debes rellenar todos los campos.");
        }
    }
    /**
     * Método que se ejecuta cuando se hace click en el botón btnSalirCrearMateria.
     * @param event Evento que se ha lanzado.
     */
    @FXML
    public void onActionBtnSalirCrearMateria(ActionEvent event){
        fxMateria.setOpc(0);
        stage.hide();
    }
}
