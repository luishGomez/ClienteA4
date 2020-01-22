package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
public class ModificarMateriaFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.ModificarMateriaFXController");
    GestorDeMateriasFXController fxMateria = null;
    private Stage stage;
    private MateriaBean materia;
    
    @FXML
    public Button btnModificarModificarMateria;
    @FXML
    private Button btnSalirModificarMateria;
    @FXML
    private TextField tfTituloModificarMateria;
    @FXML
    private TextArea taDescripcionModificarMateria;
    @FXML
    private Button btnEliminarModificarMateria;
    
    @FXML
    public void initStage(Parent root) {
        try{
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Modificar Materia");
            stage.setResizable(false);
            stage.setMaximized(false);
            tfTituloModificarMateria.setText(materia.getTitulo());
            taDescripcionModificarMateria.setText(materia.getDescripcion());
            stage.setOnShowing(this::handleWindowShowing);
            stage.showAndWait();
        }catch(Exception e){
            ControladorGeneral.showErrorAlert("Ha ocurrido un error.");
        }
    }
    
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> Gestor de Materia MODIFICAR");
            tfTituloModificarMateria.requestFocus();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    
    public void setMateria(MateriaBean materia){
        this.materia = materia;
    }
    
    public void setFXMateria(GestorDeMateriasFXController fxController){
        this.fxMateria = fxController;
    }
    
    @FXML
    public void onActionBtnModificarModificarMateria(ActionEvent event){
        if(!tfTituloModificarMateria.getText().isEmpty() || !taDescripcionModificarMateria.getText().isEmpty()){
            materia.setTitulo(tfTituloModificarMateria.getText());
            materia.setDescripcion(taDescripcionModificarMateria.getText());
            fxMateria.setOpc(2);
            stage.hide();
        }else{
            ControladorGeneral.showErrorAlert("Debes rellenar todos los campos.");
        }
    }
    
    @FXML
    public void onActionBtnEliminarModificarMateria(ActionEvent event){
        Alert alertCerrarAplicacion = new Alert(AlertType.CONFIRMATION,"",ButtonType.NO,ButtonType.YES);
        //Añadimos titulo a la ventana como el alert.
        alertCerrarAplicacion.setTitle("Eliminar");
        alertCerrarAplicacion.setHeaderText("¿Estas seguro que lo deseas eliminar?");
        //Si acepta cerrara la aplicación.
        alertCerrarAplicacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                fxMateria.setOpc(1);
                stage.hide();
            }
        });
    }
    
    @FXML
    public void onActionBtnSalirModificarMateria(ActionEvent event){
        fxMateria.setOpc(0);
        stage.hide();
    }
}
