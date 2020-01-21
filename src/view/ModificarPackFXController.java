package view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ApunteBean;
import transferObjects.PackBean;

/**
 *
 * @author Luis
 */
public class ModificarPackFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.ModificarPackFXController");
    private GestorDePacksFXController fxPack = null;
    private Stage stage;
    private PackBean pack;
    private ApunteBean apunte;
    private int opcion;
    
    @FXML
    public Button btnModificarModificarPack;
    @FXML
    private Button btnSalirModificarPack;
    @FXML
    private TextField tfTituloModificarPack;
    @FXML
    private TextArea taDescripcionModificarPack;
    @FXML
    private Button btnEliminarModificarPack;
    @FXML
    private Button btnModificarApunteModificarPack;
    @FXML
    private DatePicker dpModificarPack;
    
    @FXML
    public void initStage(Parent root) {
        try{
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Modificar Pack");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setOnShowing(this::handleWindowShowing);
            tfTituloModificarPack.setText(pack.getTitulo());
            taDescripcionModificarPack.setText(pack.getDescripcion());
            dpModificarPack.setValue(dateToLocalDate(pack.getFechaModificacion()));
            stage.showAndWait();
        }catch(Exception e){
            ControladorGeneral.showErrorAlert("Ha ocurrido un error.");
        }
    }
    
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> Gestor de Pack MODIFICAR");
            tfTituloModificarPack.requestFocus();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    
    public void setPack(PackBean pack){
        this.pack = pack;
    }
    
    public void setOpcion(int opc){
        this.opcion = opc;
    }
    
    public void setFXPack(GestorDePacksFXController fxController){
        this.fxPack = fxController;
    }
    
    public void setApunte(ApunteBean apunte){
        this.apunte = apunte;
    }
    
    @FXML
    public void onActionBtnModificarModificarPack(ActionEvent event){
        if(!tfTituloModificarPack.getText().isEmpty() && !taDescripcionModificarPack.getText().isEmpty()){
            pack.setTitulo(tfTituloModificarPack.getText());
            pack.setDescripcion(taDescripcionModificarPack.getText());
            pack.setFechaModificacion(localDateToDate(dpModificarPack.getValue()));
            fxPack.setOpc(2);
            stage.hide();
        }else{
            ControladorGeneral.showErrorAlert("Debes rellenar todos los campos.");
        }
    }
    
    @FXML
    public void onActionBtnSalirModificarPack(ActionEvent event){
        fxPack.setOpc(0);
        stage.hide();
    }
    @FXML
    public void onActionBtnEliminarModificarPack(ActionEvent event){
        Alert alertCerrarAplicacion = new Alert(Alert.AlertType.CONFIRMATION,"",ButtonType.NO,ButtonType.YES);
        //Añadimos titulo a la ventana como el alert.
        alertCerrarAplicacion.setTitle("Eliminar");
        alertCerrarAplicacion.setHeaderText("¿Estas seguro que lo deseas eliminar?");
        //Si acepta cerrara la aplicación.
        alertCerrarAplicacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                fxPack.setOpc(1);
                stage.hide();
            }
        });
    }
    @FXML
    public void onActionBtnModificarApuntesModificarPack(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("apunte_pack.fxml"));
            Parent root = (Parent)loader.load();
            ApuntePackFXController controller =
                ((ApuntePackFXController)loader.getController());
            controller.setFXModificarPack(this);
            controller.setApuntes(pack.getApuntes());
            controller.initStage(root);
            if(opcion == 1){
                Alert alertCerrarAplicacion = new Alert(Alert.AlertType.CONFIRMATION,"",ButtonType.NO,ButtonType.YES);
                //Añadimos titulo a la ventana como el alert.
                alertCerrarAplicacion.setTitle("Eliminar");
                alertCerrarAplicacion.setHeaderText("¿Estas seguro que lo deseas eliminar?");
                //Si acepta cerrara la aplicación.
                alertCerrarAplicacion.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        fxPack.setOpc(3);
                        fxPack.setApunte(apunte);
                        stage.hide();
                    }
                });
            }else if(opcion == 2){
                fxPack.setOpc(4);
                fxPack.setApunte(apunte);
                stage.hide();
            }
        }catch(Exception e){
            e.printStackTrace();
            ControladorGeneral.showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor. "+e.getMessage());
        }
    }
    
    public Date localDateToDate(LocalDate date) {
        return java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
    
    public LocalDate dateToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
