package view;

import businessLogic.BusinessLogicException;
import businessLogic.PackManager;
import businessLogic.PackManagerFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ApunteBean;
import transferObjects.PackBean;
import transferObjects.UserBean;
import static view.ControladorGeneral.showErrorAlert;

/**
 *
 * @author Luis
 */
public class GestorDePacksFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.GestorDePacksFXController");
    private UserBean user;
    private Stage stage;
    private PackManager manager = PackManagerFactory.createPackManager("real");
    private Set<PackBean> packs = null;
    private ObservableList<PackBean> packsObv = null;
    private int opcion;
    private ApunteBean apunte;
    
    @FXML
    private Menu menuCuenta;
    @FXML
    private MenuItem menuCuentaCerrarSesion;
    @FXML
    private MenuItem menuCuentaSalir;
    @FXML
    private Menu menuVentanas;
    @FXML
    private MenuItem menuVentanasGestorApuntes;
    @FXML
    private MenuItem menuVentanasGestorPacks;
    @FXML
    private MenuItem menuVentanasGestorOfertas;
    @FXML
    private MenuItem menuVentanasGestorMaterias;
    @FXML
    private Menu menuHelp;
    @FXML
    private MenuItem menuHelpAbout;
    @FXML
    private Button btnBCrearGestorPack;
    @FXML
    private Button btnInformeGestorPack;
    @FXML
    private TextField tfFiltrarGestorPack;
    @FXML
    private Button btnBuscarGestorPack;
    @FXML
    private TableView tablaPack;
    @FXML
    private TableColumn cId;
    @FXML
    private TableColumn cTitulo;
    @FXML
    private TableColumn cDescripcion;
    @FXML
    private TableColumn cPrecio;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setUser(UserBean user){
        this.user = user;
    }
    
    public void setOpc(int opcion){
        this.opcion = opcion;
    }
    
    public void setApunte(ApunteBean apunte){
        this.apunte = apunte;
    }
    
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando la ventana Gestor de Pack");
            Scene scene=new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Gestor de Pack");
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
            cId.setCellValueFactory(new PropertyValueFactory<>("idPack"));
            cTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            cDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            cargarDatos();
            tablaPack.getSelectionModel().selectedItemProperty().addListener(this::packClicked);
            stage.show();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> Gestor de Pack");
            tfFiltrarGestorPack.requestFocus();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    
    //Parte comun
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
    @FXML
    private void onActionAbrirGestorApuntes(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("gestor_de_apuntes.fxml"));
            Parent root = (Parent)loader.load();
            GestorDeApuntesFXController controller =
                    ((GestorDeApuntesFXController)loader.getController());
            controller.setUser(user);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        }catch(Exception e){
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor."+e.getMessage());
        }
    }
    
    @FXML
    private void onActionAbrirGestorPacks(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("gestor_de_packs.fxml"));
            Parent root = (Parent)loader.load();
            GestorDePacksFXController controller =
                    ((GestorDePacksFXController)loader.getController());
            controller.setUser(user);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        }catch(Exception e){
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor."+e.getMessage());
        }
    }
    
    @FXML
    private void onActionAbrirGestorOfertas(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("gestor_de_ofertas.fxml"));
            Parent root = (Parent)loader.load();
            GestorDeOfertasFXController controller =
                    ((GestorDeOfertasFXController)loader.getController());
            controller.setUser(user);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        }catch(Exception e){
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor."+e.getMessage());
        }
    }
    
    @FXML
    private void onActionAbrirGestorMaterias(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("gestor_de_materias.fxml"));
            Parent root = (Parent)loader.load();
            GestorDeMateriasFXController controller =
                    ((GestorDeMateriasFXController)loader.getController());
            controller.setUser(user);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        }catch(Exception e){
            e.printStackTrace();
            showErrorAlert("A ocurrido un error, reinicie la aplicación por favor.");
        }
    }
    //Fin de los metodos de navegación de la aplicación
    
    @FXML
    private void onActionAbout(ActionEvent event){
        
    }
    
    //Metodos de la escena.
    @FXML
    private void onActionCrearGestorPack(ActionEvent event){
        PackBean pack = new PackBean();
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("crear_pack.fxml"));
            Parent root = (Parent)loader.load();
            CrearPackFXController controller =
                    ((CrearPackFXController)loader.getController());
            controller.setFXPack(this);
            controller.setPack(pack);
            controller.setOpcion(opcion);
            controller.initStage(root);
            if(opcion == 1){
                createPack(pack);
                cargarDatos();
                tablaPack.refresh();
            }
        }catch(Exception e){
            e.printStackTrace();
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor."+e.getMessage());
        }
    }
    
    @FXML
    private void onActionInformeGestorPack(ActionEvent event){
        
    }
    
    @FXML
    private void onActionBuscarGestorPack(ActionEvent event){
        if(!tfFiltrarGestorPack.getText().trim().isEmpty()){
            cargarDatos(tfFiltrarGestorPack.getText().trim());
        }else{
            cargarDatos();
        }
    }
    
    private void cargarDatos() {
        try {
            packs = manager.findAllPack();
            List packList = packs.stream().sorted(Comparator.comparing(PackBean::getIdPack)).collect(Collectors.toList());
            packsObv = FXCollections.observableArrayList(new ArrayList<>(packList));
            tablaPack.setItems(packsObv);
        }catch (BusinessLogicException ex) {
            LOGGER.severe("Error al cargar los packs :"+ex.getMessage());
            showErrorAlert("Ha ocurrido un error cargando los packs.");
        }
    }
    
    private void cargarDatos(String string) {
        try {
            packs = manager.findAllPack();
            List packList = packs.stream().filter(pack -> pack.getTitulo().contains(string)).sorted(Comparator.comparing(PackBean::getIdPack)).collect(Collectors.toList());
            packsObv = FXCollections.observableArrayList(new ArrayList<>(packList));
            tablaPack.setItems(packsObv);
        }catch (BusinessLogicException ex) {
            LOGGER.severe("Error al cargar los packs :"+ex.getMessage());
            showErrorAlert("Ha ocurrido un error cargando los packs.");
        }
    }
    
    private void packClicked(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue != null){
            PackBean pack = (PackBean) newValue;
            try{
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("modificar_pack.fxml"));
                Parent root = (Parent)loader.load();
                ModificarPackFXController controller =
                        ((ModificarPackFXController)loader.getController());
                controller.setFXPack(this);
                controller.setPack(pack);
                controller.initStage(root);
                if(opcion == 1){
                    //COMPROBAR QUE NO EXISTEN APUNTES EN ESTA MATERIA.
                    manager.removePack(pack);
                    tablaPack.getItems().remove(pack);
                    //cargarDatos();
                    tablaPack.refresh();
                }else if(opcion == 2){
                    manager.editPack(pack);
                    cargarDatos();
                    tablaPack.refresh();
                }else if(opcion == 3){
                    manager.removeApunte(pack, Integer.toString(apunte.getIdApunte()));
                }else if(opcion == 4){
                    manager.addApunte(pack, Integer.toString(apunte.getIdApunte()));
                }
            }catch(Exception e){
                e.printStackTrace();
                showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor. "+e.getMessage());
            }
        }
    }
    
    private void createPack(PackBean pack){
        try{
            manager.createPack(pack);
        }catch(BusinessLogicException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}