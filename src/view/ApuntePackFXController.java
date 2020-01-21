package view;

import businessLogic.ApunteManager;
import businessLogic.ApunteManagerFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ApunteBean;

/**
 *
 * @author Luis
 */
public class ApuntePackFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.ApuntePackFXController");
    private Stage stage;
    private ModificarPackFXController fxModificarPack = null;
    private ObservableList<ApunteBean> apuntesObv = null;
    private Set<ApunteBean> apuntes = null;
    private ApunteBean apunte = null;
    private int opcion;
    
    @FXML
    private TableView tvApuntesApuntePack;
    @FXML
    private TableColumn cTitulo;
    @FXML
    private TableColumn cDescripcion;
    @FXML
    private TableColumn cMateria;
    @FXML
    private Button btnModificarApuntePack;
    
    public void setFXModificarPack(ModificarPackFXController fxController){
        this.fxModificarPack = fxController;
    }
    
    public void setApunte(ApunteBean apunte){
        this.apunte = apunte;
    }
    
    public void setApuntes(Set<ApunteBean> apuntes){
        this.apuntes = apuntes;
    }
    
    public void setOpcion(int opc){
        this.opcion = opc;
    }
    
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
            //Tabla
            cTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            cDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            cMateria.setCellValueFactory(new PropertyValueFactory<>("materia"));
            cargarDatos();
            tvApuntesApuntePack.getSelectionModel().selectedItemProperty().addListener(this::ApunteClicked);
            stage.showAndWait();
        }catch(Exception e){
            e.printStackTrace();
            ControladorGeneral.showErrorAlert("Ha ocurrido un error.");
        }
    }
    
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> Gestor de Pack MODIFICAR Añadir apunte");
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    
    @FXML
    private void onActionBtnEliminarAddApunte(){
        fxModificarPack.setOpcion(1);
        fxModificarPack.setApunte(apunte);
        stage.hide();
    }
    
    @FXML
    private void onActionBtnAñadirAddApunte(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("add_apunte.fxml"));
            Parent root = (Parent)loader.load();
            AddApunteFXController controller =
                ((AddApunteFXController)loader.getController());
            controller.setFXApuntePack(this);
            controller.initStage(root);
            if(opcion == 1){
                fxModificarPack.setOpcion(2);
                fxModificarPack.setApunte(apunte);
                stage.hide();
            }
        }catch(Exception e){
            e.printStackTrace();
            ControladorGeneral.showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor. "+e.getMessage());
        }
    }
    
    @FXML
    private void onActionBtnSalirAddApunte(){
        fxModificarPack.setOpcion(0);
        stage.hide();
    }
    
    private void cargarDatos(){
        apuntesObv = FXCollections.observableArrayList(new ArrayList<>(apuntes.stream().sorted(Comparator.comparing(ApunteBean::getIdApunte)).collect(Collectors.toList())));
        tvApuntesApuntePack.setItems(apuntesObv);
        LOGGER.info("cargarDatos() --> DONE");
    }
    
    private void ApunteClicked(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue != null){
            apunte = (ApunteBean) newValue;
        }
    }
}
