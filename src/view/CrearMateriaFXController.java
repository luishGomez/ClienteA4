package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.MateriaBean;

/**
 *
 * @author Luis
 */
public class CrearMateriaFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.GestorDePacksFXController");
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
    private TextArea taDescripcionCrearMateria;
    
    public void setMateria(MateriaBean materia){
        this.materia = materia;
    }
    
    public void setFXMateria(GestorDeMateriasFXController fxController){
        this.fxMateria = fxController;
    }
    
    @FXML
    public void initStage(Parent root) {
        try{
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
            ControladorGeneral.showErrorAlert("Ha ocurrido un error.");
        }
    }
    
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> Gestor de Materia CREAR");
            tfTituloCrearMateria.requestFocus();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    
    @FXML
    public void onActionBtnCrearCrearMateria(ActionEvent event){
        if(!tfTituloCrearMateria.getText().isEmpty() && !taDescripcionCrearMateria.getText().isEmpty()){
            materia.setTitulo(tfTituloCrearMateria.getText());
            materia.setDescripcion(taDescripcionCrearMateria.getText());
            fxMateria.setOpc(1);
            stage.hide();
        }else{
            ControladorGeneral.showErrorAlert("Debes rellenar todos los campos.");
        }
    }
    
    @FXML
    public void onActionBtnSalirCrearMateria(ActionEvent event){
        fxMateria.setOpc(0);
        stage.hide();
    }
}
