package view;

import businessLogic.ApunteManager;
import static businessLogic.ApunteManagerFactory.createApunteManager;
import businessLogic.BusinessLogicException;
import businessLogic.MateriaManager;
import static businessLogic.MateriaManagerFactory.createMateriaManager;
import businessLogic.PackManager;
import static businessLogic.PackManagerFactory.createPackManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ApunteBean;
import transferObjects.ClienteBean;
import transferObjects.MateriaBean;
import transferObjects.PackBean;
import static view.ControladorGeneral.showErrorAlert;

/**
 *
 * @author Luis
 */
public class TiendaPackFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.TiendaPackFXController");
    private ClienteBean cliente;
    private Stage stage;
    
    private ApunteManager managerApunte = createApunteManager("real");
    private MateriaManager managerMateria = createMateriaManager("real");
    private PackManager managerPack = createPackManager("real");
    private ObservableList<MateriaBean> materiaObv = null;
    private ObservableList<PackBean> packObv = null;
    private ObservableList<PackBean> packFiltradoObv = null;
    private List<PackBean> packsFiltrados = null;
    private boolean esta = false;
    
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuCuenta;
    @FXML
    private MenuItem menuCuentaCerrarSesion;
    @FXML
    private MenuItem menuCuentaSalir;
    @FXML
    private Menu menuVentanas;
    @FXML
    private MenuItem menuVentanasMiBiblioteca;
    @FXML
    private MenuItem menuVentanasTiendaApuntes;
    @FXML
    private MenuItem menuVentanasTiendaPacks;
    @FXML
    private MenuItem menuVentanasMiPerfil;
    @FXML
    private MenuItem menuVentanasSubirApunte;
    @FXML
    private Menu menuHelp;
    @FXML
    private MenuItem menuHelpAbout;
    @FXML
    private ListView lvMateria;
    @FXML
    private Button btnRefrescarTiendaPack;
    @FXML
    private TextField tfFiltradoBuscar;
    @FXML
    private Button btnBuscarTiendaPack;
    @FXML
    private ComboBox cbFiltrado;
    @FXML
    private ListView lvPack;
    @FXML
    private Button btnComprarTiendaPack;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setCliente(ClienteBean cliente){
        this.cliente = cliente;
    }
    
    
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando la ventana Tienda de Pack");
            Scene scene=new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Tienda de Packs");
            stage.setResizable(true);
            stage.setMaximized(true);
            //Vamos a rellenar los datos en la ventana.
            stage.setOnShowing(this::handleWindowShowing);
            //Menu
            menuCuenta.setMnemonicParsing(true);
            menuCuenta.setText("_Cuenta");
            menuVentanas.setMnemonicParsing(true);
            menuVentanas.setText("_Ventanas");
            menuHelp.setMnemonicParsing(true);
            menuHelp.setText("_Help");
            //Tabla
            cargarDatos();
            lvMateria.getSelectionModel().selectedItemProperty().addListener(this::materiaListSelected);
            stage.show();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> Tienda de Pack");
            tfFiltradoBuscar.requestFocus();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    
    //Parte comun
    /**
     * Metodo que permite cerrar sesión.
     * @param event El evento de pulsación del botón.
     */
    @FXML
    private void onActionCerrarSesion(ActionEvent event){
        try{
            //Creamos la alerta del tipo confirmación.
            Alert alertCerrarSesion = new Alert(Alert.AlertType.CONFIRMATION);
            //Ponemos titulo de la ventana como titulo para la alerta.
            alertCerrarSesion.setTitle("Cerrar sesión");
            alertCerrarSesion.setHeaderText("¿Quieres cerrar sesión?");
            //Si acepta se cerrara esta ventana.
            alertCerrarSesion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    LOGGER.info("Cerrando sesión.");
                    stage.hide();
                }
            });
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    /**
     * Metodo que permite salirse de la aplicación.
     * @param event El evento de pulsación del botón.
     */
    @FXML
    private void onActionSalir(ActionEvent event){
        try{
            //Creamos la alerta con el tipo confirmación, con su texto y botones de
            //aceptar y cancelar.
            Alert alertCerrarAplicacion = new Alert(Alert.AlertType.CONFIRMATION,"Si sale de la aplicación cerrara\nautomáticamente la sesión.",ButtonType.NO,ButtonType.YES);
            //Añadimos titulo a la ventana como el alert.
            alertCerrarAplicacion.setTitle("Cerrar la aplicación");
            alertCerrarAplicacion.setHeaderText("¿Quieres salir de la aplicación?");
            //Si acepta cerrara la aplicación.
            alertCerrarAplicacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    LOGGER.info("Cerrando la aplicación.");
                    Platform.exit();
                }
            });
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    
    //Inicio de los metodos de navegación de la aplicación
    /**
     * Abre la ventana mis apuntes.
     * @param event El evento de pulsación del botón.
     */
    @FXML
    private void onActionAbrirMisApuntes(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("cliente_misApuntes.fxml"));
            Parent root = (Parent)loader.load();
            MisApuntesClienteFXController controller =
                    ((MisApuntesClienteFXController)loader.getController());
            
            controller.setCliente(cliente);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        }catch(Exception e){
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor."+e.getMessage());
        }
    }
    /**
     * Metodo que abre la tienda de apuntes.
     * @param event El evento de pulsación del botón.
     */
    @FXML
    private void onActionAbrirTiendaApuntes(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("tienda_apuntes.fxml"));
            Parent root = (Parent)loader.load();
            TiendaApuntesFXController controller =
                    ((TiendaApuntesFXController)loader.getController());
            
            controller.setCliente(cliente);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        }catch(Exception e){
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor."+e.getMessage());
        }
    }
    @FXML
    private void onActionAbrirMiBiblioteca(ActionEvent event){
    }
    @FXML
    private void onActionAbrirTiendaPacks(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("tienda_pack.fxml"));
            Parent root = (Parent)loader.load();
            TiendaPackFXController controller =
                    ((TiendaPackFXController)loader.getController());
            
            controller.setCliente(cliente);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        }catch(Exception e){
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor."+e.getMessage());
        }
    }
    @FXML
    private void onActionAbrirMiPerfil(ActionEvent event){
    }
    @FXML
    private void onActionAbout(ActionEvent event){
    }
    
    private void cargarDatos(){
        /*opcionesData=FXCollections.observableArrayList(Arrays.asList(this.opciones));
        this.comboBoxOrdenar.setItems(opcionesData);*/
        try{
            Set<MateriaBean> materias = managerMateria.findAllMateria();
            Set<PackBean> packs = managerPack.findAllPack();
            materiaObv = FXCollections.observableArrayList(new ArrayList<>(materias));
            packObv = FXCollections.observableArrayList(new ArrayList<>(packs));
            lvMateria.setItems(materiaObv);
            lvPack.setItems(packObv);
        }catch (BusinessLogicException e) {
            e.printStackTrace();
        }
    }
    
    private void materiaListSelected(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue != null){
            MateriaBean materia = (MateriaBean) newValue;
            //Set<ApunteBean> apunts = new HashSet<ApunteBean>();
            List<ApunteBean> apunts = new ArrayList<ApunteBean>();
            try{
                Set<PackBean> packs = managerPack.findAllPack();
                packs.stream().forEach(pack -> pack.getApuntes().stream().forEach(apunte -> {
                    //ApunteBean ap = apunte;
                    if(materia.getIdMateria() == apunte.getMateria().getIdMateria()){
                        //apunts.add(ap);
                        if(apunts == null){
                            apunts.add(apunte);
                        }else{
                            this.esta= false;
                            apunts.stream().forEach(a -> {if(a.getIdApunte() == apunte.getIdApunte()){esta = true;}});
                            if(!esta){
                                apunts.add(apunte);
                            }
                        }
                    }
                }));
                packs.stream().forEach(p -> apunts.stream().forEach(ap ->{
                    if(p.getApuntes().contains(ap)){
                        packsFiltrados.add(p);
                }}));
            }catch (BusinessLogicException e) {
                e.printStackTrace();
            }
        }
        ordenado();
    }
    
    private void ordenado(){
        ObservableList<PackBean> packsObvs = null;
        packsObvs = FXCollections.observableArrayList(new ArrayList<>(packsFiltrados));
        lvPack.setItems(packsObvs);
        lvPack.refresh();
    }
}
