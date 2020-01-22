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

/**
 *
 * @author Luis
 */
public class AddApunteFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.AddApunteFXController");
    private Stage stage;
    private ApuntePackFXController fxModificarPack = null;
    private ObservableList<ApunteBean> apuntesObv = null;
    private Set<ApunteBean> apuntes = null;
    private ApunteBean apunte = null;
    private ApunteManager managerApunte = ApunteManagerFactory.createApunteManager("real");
    private int opcion;
    
    @FXML
    private Button btnBuscarApuntePack;
    @FXML
    private TextField tfFiltarApuntePack;
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
    
    public void setFXApuntePack(ApuntePackFXController fxController){
        this.fxModificarPack = fxController;
    }
    
    public void setOpcion(int opc){
        this.opcion = opc;
    }
    
    public void setApuntes(Set<ApunteBean> apuntes){
        this.apuntes = apuntes;
    }
    
    @FXML
    public void initStage(Parent root) {
        try{
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
            e.printStackTrace();
            ControladorGeneral.showErrorAlert("Ha ocurrido un error."+ e.getMessage());
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
    private void onActionBtnBuscarAddApunte(){
        
    }
    
    @FXML
    private void onActionBtnSalirAddApunte(){
        fxModificarPack.setOpcion(0);
        stage.hide();
    }
    
    private void cargarDatos(){
        try{
            Set<ApunteBean> apuns = managerApunte.findAll();
            for(ApunteBean a : apuntes){
                for(ApunteBean b : apuns){
                    if(a.getIdApunte() == b.getIdApunte()){
                        apuns.remove(b);
                        break;
                    }
                }
            }
            List<ApunteBean> apunteList = apuns.stream().sorted(Comparator.comparing(ApunteBean::getIdApunte)).collect(Collectors.toList());
            apuntesObv = FXCollections.observableArrayList(new ArrayList<>(apunteList));
            tvApuntesAddApunte.setItems(apuntesObv);
        }catch(BusinessLogicException e) {
            e.printStackTrace();
        }
    }
    
    private void apunteClicked(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue != null){
            apunte = (ApunteBean) newValue;
            fxModificarPack.setApunte(apunte);
            fxModificarPack.setOpcion(1);
            stage.hide();
        }
    }
}
