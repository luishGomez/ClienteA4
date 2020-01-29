package view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ApunteBean;
import static view.ControladorGeneral.showErrorAlert;
import static view.ControladorGeneral.showWarningAlert;

/**
 * El controlador de la ventana ApuntePackFx para gestionar los apuntes de los packs.
 * @author Luis
 */
public class ApuntePackFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.ApuntePackFXController");
    private Stage stage;
    private GestorDePacksFXController fxGestorPack = null;
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
    private Button btnAñadirApuntePack;
    @FXML
    private Button btnEliminarApuntePack;
    @FXML
    private Button btnSalirApuntePack;
    
    /**
     * Asigna un valor a fxController.
     * @param fxController Controlador de Pack.
     */
    public void setFXModificarPack(GestorDePacksFXController fxController){
        this.fxGestorPack = fxController;
    }
    /**
     * Método que le da un valor a apunte.
     * @param apunte Valor de apunte.
     */
    public void setApunte(ApunteBean apunte){
        this.apunte = apunte;
    }
    /**
     * Método que le da un valor a apuntes.
     * @param apuntes Valor de apuntes.
     */
    public void setApuntes(Set<ApunteBean> apuntes){
        this.apuntes = apuntes;
    }
    /**
     * Método que le da un valor a opcion.
     * @param opcion Valor de opcion.
     */
    public void setOpcion(int opcion){
        this.opcion = opcion;
    }
    
    /**
     * Método que inicializa la ventana.
     * @param root Nodo raiz.
     */
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando la ventana Apunte Pack");
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Modificar Apuntes de Pack");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setOnShowing(this::handleWindowShowing);
            //Tabla
            cTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            cDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            cMateria.setCellValueFactory(new PropertyValueFactory<>("materia"));
            cargarDatos();
            tvApuntesApuntePack.getSelectionModel().selectedItemProperty().addListener(this::apunteClicked);
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
            LOGGER.info("handleWindowShowing -> Apunte Pack");
            btnAñadirApuntePack.requestFocus();
        }catch(Exception e){
            LOGGER.severe("Error(handleWindowShowing)" + e.getMessage());
        }
    }
    
    /**
     * Método que se ejecuta cuando se hace click en el botón btnAñadirApuntePack.
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionBtnAñadirAddApunte(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("add_apunte.fxml"));
            Parent root = (Parent)loader.load();
            AddApunteFXController controller =
                ((AddApunteFXController)loader.getController());
            controller.setFXApuntePack(this);
            controller.setApuntes(apuntes);
            controller.initStage(root);
            if(opcion == 1){
                fxGestorPack.setOpc(2);
                fxGestorPack.setApunte(apunte);
                stage.hide();
            }
        }catch(Exception e){
            LOGGER.severe("Error al intentar abrir la ventana(onActionBtnAñadirAddApunte): " + e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor.");
        }
    }
    /**
     * Método que se ejecuta cuando se hace click en el botón btnEliminarApuntePack.
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionBtnEliminarAddApunte(ActionEvent event){
        if(apunte != null){
            fxGestorPack.setOpc(1);
            fxGestorPack.setApunte(apunte);
            stage.hide();
        }else{
            showWarningAlert("Seleccione un apunte, después, vuelva a pulsar el botón.");
        }
    }
    /**
     * Método que se ejecuta cuando se hace click en el botón btnSalirApuntePack.
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionBtnSalirAddApunte(ActionEvent event){
        fxGestorPack.setOpc(0);
        stage.hide();
    }
    /**
     * Carga todos los apuntes en la tabla.
     */
    private void cargarDatos(){
        if(apuntes != null){
            apuntesObv = FXCollections.observableArrayList(new ArrayList<>(apuntes.stream().sorted(Comparator.comparing(ApunteBean::getIdApunte)).collect(Collectors.toList())));
            tvApuntesApuntePack.setItems(apuntesObv);
        }
    }
    /**
     * Método que se ejecuta cuando cambia algún valor de una fila de la tabla.
     * @param obvservable Valor observable.
     * @param oldValue Valor antiguo.
     * @param newValue Valor nuevo.
     */
    private void apunteClicked(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue != null){
            apunte = (ApunteBean) newValue;
        }
    }
}
