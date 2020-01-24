package view;

import businessLogic.BusinessLogicException;
import businessLogic.PackManager;
import static businessLogic.PackManagerFactory.createPackManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ClienteBean;
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
    private PackBean pack = null;
    private PackManager managerPack = createPackManager("real");
    private ObservableList<PackBean> packObv = null;
    private boolean esta = false;
    private ObservableList<String> comboObv = null;
    private String [] opciones={"Sin filtro","ABC...","ZYX...","Precio asc.","Precio desc."};
    private String filtro = "Sin filtro";
    private int opcion;
    
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
    
    public void setOpcion(int opcion){
        this.opcion = opcion;
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
            //List
            cargarDatos();
            lvPack.getSelectionModel().selectedItemProperty().addListener(this::packClicked);
            comboObv = FXCollections.observableArrayList(Arrays.asList(this.opciones));
            cbFiltrado.setItems(comboObv);
            cbFiltrado.getSelectionModel().selectedItemProperty().addListener(this::comboClicked);
            cbFiltrado.getSelectionModel().selectFirst();
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
    
    @FXML
    private void onActionBtnBuscarTiendaPack(ActionEvent event){
        if(!tfFiltradoBuscar.getText().trim().isEmpty()){
            cargarDatos(tfFiltradoBuscar.getText().trim());
        }else{
            cargarDatos();
        }
    }
    
    @FXML
    private void onActionBtnComprarTiendaPack(ActionEvent event){
        if(pack != null){
            try{
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("compra_pack.fxml"));
                Parent root = (Parent)loader.load();
                CompraPackFXController controller =
                        ((CompraPackFXController)loader.getController());
                controller.setFXController(this);
                controller.setCliente(cliente);
                controller.setPack(pack);
                controller.setStage(stage);
                controller.initStage(root);
                if(opcion == 1){
                    cargarDatos();
                }
            }catch(Exception e){
                showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor."+e.getMessage());
            }
        }else{
            showErrorAlert("Debes seleccionar un Pack.");
        }
    }
    
    private void cargarDatos(){
        try{
            Set<PackBean> packs = managerPack.findAllPack();
            packObv = FXCollections.observableArrayList(new ArrayList<>(packs.stream().collect(Collectors.toList())));
        }catch (BusinessLogicException e) {
            e.printStackTrace();
        }
        ordenar(packObv);
    }
    
    private void cargarDatos(String string){
        try {
            Set<PackBean> packs = managerPack.findAllPack();
            packObv = FXCollections.observableArrayList(new ArrayList<>(packs.stream().filter(p -> p.getTitulo().toLowerCase().contains(string.toLowerCase())).collect(Collectors.toList())));
        }catch (BusinessLogicException e) {
            e.printStackTrace();
        }
        ordenar(packObv);
    }
    
    private void packClicked(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue != null){
            pack = (PackBean) newValue;
        }
    }
    
    private void comboClicked(ObservableValue observable, Object oldValue, Object newValue){
        if(newValue!=null && newValue!=oldValue){
            filtro = (String) newValue;
            ordenar(packObv);
        }
    }

    private void ordenar(ObservableList<PackBean> p) {
        switch(filtro){
            case "ABC...":
                p = FXCollections.observableArrayList(new ArrayList<>(p.stream().sorted(Comparator.comparing(PackBean::getTitulo)).collect(Collectors.toList())));
                break;
            case "ZYX...":
                p = FXCollections.observableArrayList(new ArrayList<>(p.stream().sorted(Comparator.comparing(PackBean::getTitulo, Comparator.reverseOrder())).collect(Collectors.toList())));
                break;
            case "Precio asc.":
                p = FXCollections.observableArrayList(new ArrayList<>(p.stream().sorted(Comparator.comparing(PackBean::getPrecio)).collect(Collectors.toList())));
                break;
            case "Precio desc.":
                p = FXCollections.observableArrayList(new ArrayList<>(p.stream().sorted(Comparator.comparing(PackBean::getPrecio, Comparator.reverseOrder())).collect(Collectors.toList())));
                break;
        }
        lvPack.setItems(p);
        lvPack.setCellFactory(param -> new CellTiendaPack());
        lvPack.refresh();
    }
}
