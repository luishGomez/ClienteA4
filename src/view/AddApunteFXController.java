package view;

import businessLogic.ApunteManager;
import businessLogic.ApunteManagerFactory;
import businessLogic.BusinessLogicException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
import static view.ControladorGeneral.showErrorAlert;

/**
 * El controlador de la ventana AddApunteFx para gestionar los apuntes de los packs.
 * @author Luis
 */
public class AddApunteFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.AddApunteFXController");
    private Stage stage;
    private ApuntePackFXController fxController = null;
    private ObservableList<ApunteBean> apuntesObv = null;
    private Set<ApunteBean> apuntes = null;
    private ApunteBean apunte = null;
    private ApunteManager managerApunte = ApunteManagerFactory.createApunteManager("real");
    private int opcion;
    
    @FXML
    private Button btnBuscarAddApunte;
    @FXML
    private TextField tfFiltarAddApunte;
    @FXML
    private TableView tvApuntesAddApunte;
    @FXML
    private TableColumn cTitulo;
    @FXML
    private TableColumn cDescripcion;
    @FXML
    private TableColumn cMateria;
    @FXML
    private Button btnSalirAddApunte;
    
    /**
     * Asigna un valor a fxController.
     * @param fxController Controlador de Pack.
     */
    public void setFXApuntePack(ApuntePackFXController fxController){
        this.fxController = fxController;
    }
    /**
     * Método que le da un valor a opcion.
     * @param opcion Valor de opcion.
     */
    public void setOpcion(int opcion){
        this.opcion = opcion;
    }
    /**
     * Método que le da un valor a apuntes.
     * @param apuntes Valor de apuntes.
     */
    public void setApuntes(Set<ApunteBean> apuntes){
        this.apuntes = apuntes;
    }
    
    /**
     * Método que inicializa la ventana.
     * @param root Nodo raiz.
     */
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando la ventana Add Apunte");
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Seleccione un apunte");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setOnShowing(this::handleWindowShowing);
            //Tabla
            cTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            cDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            cMateria.setCellValueFactory(new PropertyValueFactory<>("materia"));
            cargarDatos();
            tvApuntesAddApunte.getSelectionModel().selectedItemProperty().addListener(this::apunteClicked);
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
            LOGGER.info("handleWindowShowing -> Add Apunte");
            tfFiltarAddApunte.requestFocus();
        }catch(Exception e){
            LOGGER.severe("Error(handleWindowShowing)" + e.getMessage());
        }
    }
    
    /**
     * Método que se ejecuta cuando se hace click en el botón btnBuscarAddApunte.
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionBtnBuscarAddApunte(){
        if(!tfFiltarAddApunte.getText().trim().isEmpty()){
            cargarDatos(tfFiltarAddApunte.getText().trim());
        }else{
            cargarDatos();
        }
    }
    /**
     * Método que se ejecuta cuando se hace click en el botón btnSalirAddApunte.
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionBtnSalirAddApunte(){
        fxController.setOpcion(0);
        stage.hide();
    }
    /**
     * Método para cargar todos los apuntes.
     */
    private void cargarDatos(){
        try{
            Set<ApunteBean> apuns = managerApunte.findAll();
            if(apuntes != null){
                for(ApunteBean a : apuntes){
                    for(ApunteBean b : apuns){
                        if(a.getIdApunte() == b.getIdApunte()){
                            apuns.remove(b);
                            break;
                        }
                    }
                }
            }
            List<ApunteBean> apunteList = apuns.stream().sorted(Comparator.comparing(ApunteBean::getIdApunte)).collect(Collectors.toList());
            apuntesObv = FXCollections.observableArrayList(new ArrayList<>(apunteList));
            tvApuntesAddApunte.setItems(apuntesObv);
        }catch(BusinessLogicException e) {
            LOGGER.severe("Error al intentar cargar los apuntes(cargarDatos()): " + e.getMessage());
            showErrorAlert("Ha ocurrido un error cargando los apuntes.");
        }
    }
    /**
     * Método para cargar todos los apuntes y las filtra según el parámetro.
     * @param string Cadena de caracteres a filtrar.
     */
    private void cargarDatos(String string){
        try{
            Set<ApunteBean> apuns = managerApunte.findAll();
            if(apuntes != null){
                for(ApunteBean a : apuntes){
                    for(ApunteBean b : apuns){
                        if(a.getIdApunte() == b.getIdApunte()){
                            apuns.remove(b);
                            break;
                        }
                    }
                }
            }
            List<ApunteBean> apunteList = apuns.stream().filter(ap -> ap.getTitulo().toLowerCase().contains(string.toLowerCase())).sorted(Comparator.comparing(ApunteBean::getIdApunte)).collect(Collectors.toList());
            apuntesObv = FXCollections.observableArrayList(new ArrayList<>(apunteList));
            tvApuntesAddApunte.setItems(apuntesObv);
        }catch(BusinessLogicException e) {
            LOGGER.severe("Error al intentar cargar los apuntes(cargarDatos(String s)): " + e.getMessage());
            showErrorAlert("Ha ocurrido un error cargando los apuntes.");
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
            fxController.setApunte(apunte);
            fxController.setOpcion(1);
            stage.hide();
        }
    }
}
