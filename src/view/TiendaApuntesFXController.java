/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package view;

import businessLogic.ApunteManager;
import static businessLogic.ApunteManagerFactory.createApunteManager;
import businessLogic.BusinessLogicException;
import businessLogic.MateriaManager;
import static businessLogic.MateriaManagerFactory.createMateriaManager;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ApunteBean;
import transferObjects.ClienteBean;
import transferObjects.MateriaBean;
import static view.ControladorGeneral.showErrorAlert;

/**
 * La clase controladora de la tienda de apuntes.
 * @author Ricardo Peinado Lastra
 */
public class TiendaApuntesFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.TiedaApuntesFXController");
    private ClienteBean cliente;
    private Stage stage;
    
    private ApunteManager apunteLogic = createApunteManager("real");
    private MateriaManager materiaLogic = createMateriaManager("real");
    private ObservableList<MateriaBean> materiasData=null;
    private ObservableList<String> opcionesData=null;
    private Set <ApunteBean> apuntesDeTienda = null;
    private Set <ApunteBean> apuntesDeTiendaFiltrado = null;
    //private boolean filtrarMateria=false;
    // private boolean filtrarNombre=false;
    private boolean filtrado=false;
    private boolean ordenado=false;
    private Set<ApunteBean> apuntes=null;
    private ObservableList<ApunteBean> apuntesData=null;
    private ObservableList<ApunteBean> apuntesDataFiltrado=null;
    private String [] opciones={"Sin filtro","ABC...","ZYZ...","Precio asc.","Precio desc."};
    private String tipoFiltrado="Sin filtro";
    private MateriaBean materiaFiltrada = null;
    private static int resultado=0;
    private Stage stageAyuda;
    
    @FXML
    private Button btnRefrescar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnComprar;
    @FXML
    private TextField textFieldBuscar;
    @FXML
    private ComboBox comboBoxOrdenar;
    @FXML
    private ListView listViewMateria;
    @FXML
    private ListView listViewApuntes;
    @FXML
    private Label labelMateria;
    @FXML
    private Label labelTitulo;
    
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
    private ContextMenu contextMenu=new ContextMenu();
    
    /**
     * El metodo que inicializa la ventana.
     * @param root El nodo raiz.
     */
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando la TiendaApuntesFXController");
            Scene scene=new Scene(root);
            stage=new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Tienda de apuntes");
            stage.setResizable(true);
            stage.setMaximized(true);
            //Vamos a rellenar los datos en la ventana.
            stage.setOnShowing(this::handleWindowShowing);
            
            //Mnemonicos
            //Menu->
            menuCuenta.setMnemonicParsing(true);
            menuCuenta.setText("_Cuenta");
            menuVentanas.setMnemonicParsing(true);
            menuVentanas.setText("_Ventanas");
            menuHelp.setMnemonicParsing(true);
            menuHelp.setText("_Help");
            menuHelpAbout.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+A"));
            menuCuentaCerrarSesion.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+C"));
            menuCuentaSalir.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+S"));
            menuVentanasMiBiblioteca.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+B"));
            menuVentanasMiPerfil.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+P"));
            menuVentanasSubirApunte.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
            menuVentanasTiendaApuntes.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+A"));
            menuVentanasTiendaPacks.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+T"));
            //<-Menu
            btnRefrescar.setMnemonicParsing(true);
            btnRefrescar.setText("_Refrescar");
            btnComprar.setMnemonicParsing(true);
            btnComprar.setText("C_omprar");
            
            //CARGAR MATERIAS
            cargarMaterias();
            
            //CARGAR LISTVIEW DE APUNTES
            cargarApuntes();
            //CARGAR EL COMBO BOX
            cargarComboBox();
            //PREPARAR EL MENU DE CONTEXTO
            MenuItem menuItemRefrescar = new MenuItem("Refresca");
            MenuItem menuItemCompra = new MenuItem("Comprar apunte");
            menuItemRefrescar.setOnAction((ActionEvent e )->{
                onActionRefrescar(e);
            });
            menuItemCompra.setOnAction((ActionEvent e )->{
                onActionComprar(e);
            });
            contextMenu.getItems().add(menuItemRefrescar);
            contextMenu.getItems().add(menuItemCompra);
            listViewApuntes.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
                if(e.getButton() == MouseButton.SECONDARY){
                    contextMenu.show(listViewApuntes, e.getScreenX(), e.getScreenY());
                }
            });
            //Pulsación de enter funcionando
            textFieldBuscar.setOnKeyPressed(this::keyPressBuscar);
            btnBuscar.setOnKeyPressed(this::keyPressBuscar);
            this.listViewMateria.getSelectionModel().selectedItemProperty().addListener(this::handleMateriaListSelectionChanged);
            this.comboBoxOrdenar.getSelectionModel().selectedItemProperty().addListener(this::handleOpcionesComboBoxSelectionChanged);
            this.comboBoxOrdenar.getSelectionModel().selectFirst();
            this.listViewMateria.getSelectionModel().select(0);
            stage.show();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
        
    }
    /**
     * Metodo que permimte al dar a enter a buscar ejecute el evento de busqueda.
     * @param key La tecla pulsada.
     */
    private void keyPressBuscar(KeyEvent key){
        if(key.getCode().equals(KeyCode.ENTER)) {
            btnBuscar.fire();
        }
        
    }
    /**
     * Metodo que se ejecuta al mostrar la ventana.
     * @param event
     */
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> LogOut");
            textFieldBuscar.requestFocus();
            
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    
    /**
     * Metodo que activa el refrescar la pagina.
     * @param event El evento de pulsación del botón.
     */
    @FXML
    private void onActionRefrescar(ActionEvent event){
        refrescar();
    }
    /**
     * Refresca todas las materias y apuntes.
     */
    public void refrescar(){
        cargarMaterias();
        cargarApuntes();
        cargarComboBox();
        this.listViewMateria.getSelectionModel().select(0);
        this.comboBoxOrdenar.getSelectionModel().select(0);
        this.textFieldBuscar.setText("");
        filtrarLosApuntes();
    }
    /**
     * El metodo que activa el filtrado por nombre.
     * @param event El evento de pulsación del botón.
     */
    @FXML
    private void onActionBuscar(ActionEvent event){
        filtrarLosApuntes();
    }
    /**
     * El metodo que abre la ventana de compra de un apunte.
     * @param event El evento de pulsación del botón.
     */
    @FXML
    private void onActionComprar(ActionEvent event){
        if(!this.listViewApuntes.getSelectionModel().isEmpty()){
            ApunteBean apunte=(ApunteBean)this.listViewApuntes.getSelectionModel().getSelectedItem();
            try{
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("compra_apunte.fxml"));
                
                Parent root = (Parent)loader.load();
                
                CompraApunteFXController controller =
                        ((CompraApunteFXController)loader.getController());
                controller.setDatos(cliente, apunte);
                controller.initStage(root);
                if(resultado!=0){
                    refrescar();
                    resultado=0;
                }
            }catch(IOException e){
                showErrorAlert("Error al cargar la ventana de Login.");
                LOGGER.severe("Error "+e.getMessage());
                
            }
            
        }else{
            showErrorAlert("No has seleccionado ninguna apunte para comprar.");
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
            Alert alertCerrarSesion = new Alert(AlertType.CONFIRMATION);
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
            Alert alertCerrarAplicacion = new Alert(AlertType.CONFIRMATION,"Si sale de la aplicación cerrara\nautomáticamente la sesión.",ButtonType.NO,ButtonType.YES);
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
    /**
     * Abre la ventana mi biblioteca.
     * @param event El evento de pulsación del botón.
     */
    @FXML
    private void onActionAbrirMiBiblioteca(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("biblioteca.fxml"));
            
            Parent root = (Parent)loader.load();
            
            BibliotecaClienteFXController controller =
                    ((BibliotecaClienteFXController)loader.getController());
            controller.setUser(cliente);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        } catch (IOException ex) {
            Logger.getLogger(TiendaApuntesFXController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BusinessLogicException ex) {
            Logger.getLogger(TiendaApuntesFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    /**
     * Abre la ventana tienda packs.
     * @param event El evento de pulsación del botón.
     */
    @FXML
    private void onActionAbrirTiendaPacks(ActionEvent event){
         try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("tienda_pack.fxml"));
            
            Parent root = (Parent)loader.load();
            
            TiendaPackFXController controller =
                    ((TiendaPackFXController)loader.getController());
            controller.setCliente(cliente);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        } catch (IOException ex) {
            Logger.getLogger(TiendaApuntesFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Abre la ventna mi perfil.
     * @param event El evento de pulsación del botón.
     */
    @FXML
    private void onActionAbrirMiPerfil(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("perfil.fxml"));
            
            Parent root = (Parent)loader.load();
            
            PerfilFXMLController controller =
                    ((PerfilFXMLController)loader.getController());
            controller.setUser(cliente);
            controller.setStage(stage);
            controller.initStage(root);
            //stage.hide();
        } catch (IOException ex) {
            Logger.getLogger(TiendaApuntesFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Abre la ventana de ayuda.
     * @param event  El evento de pulsación del botón.
     */
    @FXML
    private void onActionAbout(ActionEvent event){
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        
        URL url = this.getClass().getResource("/ayuda/ayuda_tienda_de_apuntes.html");
        webEngine.load(url.toString());
        
        stageAyuda=new Stage();
        stageAyuda.setTitle(webEngine.getTitle());
        
        Button ayudaCerrar=new Button("Cerrar");
        ayudaCerrar.setOnAction(this::cerrarAyuda);
        ayudaCerrar.setMnemonicParsing(true);
        ayudaCerrar.setText("_Cerrar");
        /* Al pulsar enter encima del boton se ejecute */
        ayudaCerrar.setOnKeyPressed((key) ->{
            if(key.getCode().equals(KeyCode.ENTER)) {
                ayudaCerrar.fire();
            }
        });
        stageAyuda.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                ayudaCerrar.requestFocus();
            }
        });
        VBox root = new VBox();
        root.getChildren().addAll(browser,ayudaCerrar);
        
        Scene escenaAyuda=new Scene(root);
        stageAyuda.setScene(escenaAyuda);
        stageAyuda.initModality(Modality.APPLICATION_MODAL);
        
        stageAyuda.show();
    }
    /**
     * Cierra la ventana de ayuda.
     * @param event El evento de pulsación del botón.
     */
    public void cerrarAyuda(ActionEvent event){
        stageAyuda.hide();
    }
    //Fin de los metodos de navegación de la aplicación
    /**
     * Metodo que inserta el escenario.
     * @param stage El escenario.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /**
     * Metodo que inserta el cliente.
     * @param cliente Los datos del cliente.
     */
    public void setCliente(ClienteBean cliente){
        this.cliente=cliente;
    }
    /**
     * Carga todas las materias existentes en el comboBox de materias.
     */
    private void cargarMaterias() {
        try {
            Set<MateriaBean> materias = materiaLogic.findAllMateria();
            List <MateriaBean> materiasList = Lists.newArrayList(materias);
            //El titulo es 0 ya que siempre va a ser el primero ya que todos los
            // apuntes minimo tienen que tener 3 caracteres y al ser este un
            // apunte con el titulo 0 siempre va a ser el primero al ordenarlo.
            materiasList.add(new MateriaBean(-1,"0","Todas las materias"));
            materiasList=materiasList.stream().sorted(Comparator.comparing(MateriaBean::getTitulo)).collect(Collectors.toList());
            materiasData=FXCollections.observableArrayList(new ArrayList<>(materiasList));
            materiasData.get(0).setTitulo("Todas las materias");
            this.listViewMateria.setItems(materiasData);
            
        } catch (BusinessLogicException ex) {
            LOGGER.severe("Error al cargar las materias: "+ex.getMessage());
        }
    }
    /**
     * Carga todos los apuntes que no se haya comprado el cliente.
     */
    private void cargarApuntes() {
        try {
            apuntes=apunteLogic.findAll();//SE PUDE BAJAR
            
            //List <ApunteBean> apuntesList = Lists.newArrayList(apuntes);
            Set <ApunteBean> apuntesNoCreadosPorMi = new HashSet <ApunteBean>();
            Set <ApunteBean> apuntesComprados = apunteLogic.getApuntesByComprador(cliente.getId());
            apuntesDeTienda = new HashSet <ApunteBean>();
            
            apuntes.stream().forEach(apunte ->{
                if(apunte.getCreador().getId()!=cliente.getId()){
                    apuntesNoCreadosPorMi.add(apunte);
                }
            });
            apuntesNoCreadosPorMi.stream().forEach(apunte ->{
                boolean comprado=false;
                for(ApunteBean apunteComprado:apuntesComprados){
                    if(apunte.getIdApunte()==apunteComprado.getIdApunte()){
                        comprado=true;
                        break;
                    }
                }
                if(!comprado)
                    apuntesDeTienda.add(apunte);
            });
            
            apuntesData=FXCollections.observableArrayList(new ArrayList<>(apuntesDeTienda));
            this.listViewApuntes.setItems(apuntesData);
            this.listViewApuntes.setCellFactory(param -> new CellTiendaApunte());
            
        } catch (BusinessLogicException ex) {
            LOGGER.severe("Error al intentar cargar los apuntes :"+ex.getMessage());
            showErrorAlert("No se ha podido cargar los apuntes");
        }
    }
    /**
     * Carga el comboBox de las opciones de filtrado.
     */
    private void cargarComboBox() {
        opcionesData=FXCollections.observableArrayList(Arrays.asList(this.opciones));
        this.comboBoxOrdenar.setItems(opcionesData);
        
    }
    /**
     * El manejador del cambio de selección de materias.
     * @param obvservable El valor observable.
     * @param oldValue El antiguo valor.
     * @param newValue El nuevo valor.
     */
    private void handleMateriaListSelectionChanged(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue!=null && newValue!=oldValue){
            MateriaBean materia=(MateriaBean) newValue;
            if(materia.getIdMateria()!=-1){
                
                this.materiaFiltrada=materia;
            }else{
                this.materiaFiltrada=null;
            }
            filtrarLosApuntes();
        }else{
            
            this.listViewMateria.getSelectionModel().select(0);
            
        }
        
    }
    /**
     * El manejador de las opciones del comboBox de los tipos de filtrado.
     * @param obvservable El valor observable.
     * @param oldValue El antiguo valor.
     * @param newValue El nuevo valor.
     */
    private void handleOpcionesComboBoxSelectionChanged(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue!=null && newValue!=oldValue){
            this.tipoFiltrado=(String) newValue;
            ordenarApuntes();
        }
    }
    /**
     * Filtra los apuntes.
     */
    private void filtrarLosApuntes(){
        
        this.filtrado=false;
        //Para coger los apuntes de una materia
        Set <ApunteBean> apuntesDeLaMateria = new HashSet <ApunteBean>();
        if(this.materiaFiltrada!=null){
            this.filtrado=true;
            this.apuntesDeTienda.forEach(apunte ->{
                if(this.materiaFiltrada.getIdMateria()==apunte.getMateria().getIdMateria()){
                    apuntesDeLaMateria.add(apunte);
                }
            });
        }else{
            this.apuntesDeTienda.forEach(apunte ->{
                apuntesDeLaMateria.add(apunte);
            });
        }
        //Para coger los apuntes con el nombre
        Set <ApunteBean> apuntesConElNombre = new HashSet <ApunteBean>();
        if(this.textFieldBuscar.getText().trim()!=""){
            this.filtrado=true;
            apuntesDeLaMateria.stream().forEach(apunte ->{
                if(apunte.getTitulo().toLowerCase().contains(this.textFieldBuscar.getText().trim().toLowerCase())){
                    apuntesConElNombre.add(apunte);
                }
            });
        }else{
            apuntesDeLaMateria.stream().forEach(apunte ->{
                apuntesConElNombre.add(apunte);
            });
        }
        if(this.filtrado){
            
            this.apuntesDeTiendaFiltrado=apuntesConElNombre;
            
            ordenarApuntes();
            
        }else{
            ordenarApuntes();
        }
        
    }
    /**
     * Ordena los apuntes.
     */
    private void ordenarApuntes() {
        List <ApunteBean> apuntesParaOrdenar=null;
        if(this.filtrado)
            apuntesParaOrdenar=Lists.newArrayList(this.apuntesDeTiendaFiltrado);
        else
            apuntesParaOrdenar=Lists.newArrayList(this.apuntesDeTienda);
        switch(this.tipoFiltrado){
            case "Sin filtro":
                
                break;
            case "ABC...":
                apuntesParaOrdenar=apuntesParaOrdenar.stream().sorted(Comparator.comparing(ApunteBean::getTituloMin)).collect(Collectors.toList());
                break;
            case "ZYZ...":
                apuntesParaOrdenar=apuntesParaOrdenar.stream().sorted(Comparator.comparing(ApunteBean::getTituloMin, Comparator.reverseOrder())).collect(Collectors.toList());
                break;
            case "Precio asc.":
                apuntesParaOrdenar=apuntesParaOrdenar.stream().sorted(Comparator.comparing(ApunteBean::getPrecio)).collect(Collectors.toList());
                break;
            case "Precio desc.":
                apuntesParaOrdenar=apuntesParaOrdenar.stream().sorted(Comparator.comparing(ApunteBean::getPrecio, Comparator.reverseOrder())).collect(Collectors.toList());
                break;
        }
        this.listViewApuntes.setItems(FXCollections.observableArrayList(new ArrayList<>(apuntesParaOrdenar)));
    }
    /**
     * Inserta el resultado de la ventana modal que abre esta ventana.
     * @param resultado El resultado de la ventana modal.
     */
    public static void setResultadoTiendaApuntes(int resultado){
        TiendaApuntesFXController.resultado=resultado;
    }
}
