package view;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ApunteBean;
import static view.ControladorGeneral.showErrorAlert;

/**
 * Clase controladora de la ventana de ApuntePack.
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
    private Button btnSalirApuntePack;
    @FXML
    private Button btnEliminarApuntePack;
    @FXML
    private Button btnAñadirApuntePack;
    
    /**
     * Asigna un valor a fxModificarPack.
     * @param fxController Controlador de Pack.
     */
    public void setFXModificarPack(ModificarPackFXController fxController){
        this.fxModificarPack = fxController;
    }
    
    /**
     * Asigna un valor a apunte.
     * @param apunte Apunte seleccionado.
     */
    public void setApunte(ApunteBean apunte){
        this.apunte = apunte;
    }
    
    /**
     * Asigna un valor a apuntes.
     * @param apuntes Apuntes del pack.
     */
    public void setApuntes(Set<ApunteBean> apuntes){
        this.apuntes = apuntes;
    }
    
    /**
     * Asigna un valor a opcion.
     * @param opc Opción elegida.
     */
    public void setOpcion(int opc){
        this.opcion = opc;
    }
    
    /**
     * El método initStage inicializa la ventana.
     * @param root Nodo raiz de la ventana.
     */
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
            tvApuntesApuntePack.getSelectionModel().selectedItemProperty().addListener(this::apunteClicked);
            stage.showAndWait();
        }catch(Exception e){
            e.printStackTrace();
            ControladorGeneral.showErrorAlert("Ha ocurrido un error.");
        }
    }
    
    /**
     * El método se ejecuta cuando se esta mostrando la ventana.
     * @param event Evento de la ventana.
     */
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> Gestor de Pack MODIFICAR Añadir apunte");
            btnAñadirApuntePack.requestFocus();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    
    /**
     * Se activa cuando se hace click en el botón Eliminar.
     */
    @FXML
    private void onActionBtnEliminarAddApunte(){
        if(apunte != null){
            fxModificarPack.setOpcion(1);
            fxModificarPack.setApunte(apunte);
            stage.hide();
        }else{
            showErrorAlert("Debes seleccionar algun apunte.");
        }
    }
    
    /**
     * Se activa cuando se hace click en el botón Añadir.
     */
    @FXML
    private void onActionBtnAñadirAddApunte(){
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
                fxModificarPack.setOpcion(2);
                fxModificarPack.setApunte(apunte);
                stage.hide();
            }
        }catch(Exception e){
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor. "+e.getMessage());
        }
    }
    
    /**
     * Se activa cuando se hace click en el botón Salir.
     */
    @FXML
    private void onActionBtnSalirAddApunte(){
        fxModificarPack.setOpcion(0);
        stage.hide();
    }
    
    /**
     * Carga los datos en la tabla.
     */
    private void cargarDatos(){
        apuntesObv = FXCollections.observableArrayList(new ArrayList<>(apuntes.stream().sorted(Comparator.comparing(ApunteBean::getIdApunte)).collect(Collectors.toList())));
        tvApuntesApuntePack.setItems(apuntesObv);
    }
    
    /**
     * Cambia el valor de apunte, al nuevo.
     * @param obvservable Valor obvservable.
     * @param oldValue Antiguo valor.
     * @param newValue Nuevo valor.
     */
    private void apunteClicked(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue != null){
            apunte = (ApunteBean) newValue;
        }
    }
}
