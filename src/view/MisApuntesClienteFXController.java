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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ApunteBean;
import transferObjects.ClienteBean;
import transferObjects.MateriaBean;
import static view.ControladorGeneral.showErrorAlert;

/**
 * La clase controladora de la ventana mis apuntes.
 * @author Ricardo Peinado Lastra.
 */
public class MisApuntesClienteFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.MisApuntesClienteFXController");
    private ClienteBean cliente;
    private Stage stage;
    private Stage stageAyuda;
    
    
    private ApunteManager apunteLogic = createApunteManager("real");
    private MateriaManager materiaLogic = createMateriaManager("real");
    private ObservableList<MateriaBean> materiasData=null;
    private ObservableList<String> opcionesData=null;
    private Set <ApunteBean> apuntesDeMisApunteCliente = null;
    private Set <ApunteBean> apuntesDeTiendaFiltrado = null;
    private boolean filtrado=false;
    private boolean ordenado=false;
    private Set<ApunteBean> apuntes=null;
    private ObservableList<ApunteBean> apuntesData=null;
    private ObservableList<ApunteBean> apuntesDataFiltrado=null;
    private String [] opciones={"Sin filtro","ABC...","ZYZ...","Precio asc.","Precio desc."};
    private String tipoFiltrado="Sin filtro";
    private MateriaBean materiaFiltrada = null;
    private static int resultado=0; /////////////////////////////////////////
    
    @FXML
    private Button btnRefrescar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnSubirApunte;
    @FXML
    private Button btnDescargarElApunte;
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
     * El metodo que inicializa la ventana MisApuntes.
     * @param root El nodo raiz.
     */
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando la ventna MisApunteClienteFXController");
            Scene scene=new Scene(root);
            stage=new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Mis apuntes");
            stage.setResizable(true);
            stage.setMaximized(true);
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
            //Mnemonicos de la ventana
            btnRefrescar.setMnemonicParsing(true);
            btnRefrescar.setText("_Refrescar");
            btnBuscar.setMnemonicParsing(true);
            btnBuscar.setText("_Buscar");
            btnModificar.setMnemonicParsing(true);
            btnModificar.setText("_Modificar");
            btnSubirApunte.setMnemonicParsing(true);
            btnSubirApunte.setText("_Subir apunte");
            
            //CARGAR MATERIAS & APUNTES
            cargarMaterias();
            cargarApuntes();
            //CARGAR EL COMBO BOX
            cargarComboBox();
            
            textFieldBuscar.setOnKeyPressed(this::keyPressBuscar);
            btnBuscar.setOnKeyPressed(this::keyPressBuscar);
            this.listViewMateria.getSelectionModel().selectedItemProperty().addListener(this::handleMateriaListSelectionChanged);
            this.comboBoxOrdenar.getSelectionModel().selectedItemProperty().addListener(this::handleOpcionesComboBoxSelectionChanged);
            //SELECCIONAR LAS OPCIONES DE INICIO
            this.comboBoxOrdenar.getSelectionModel().selectFirst();
            this.listViewMateria.getSelectionModel().select(0);
            //PREPARAR EL MENU DE CONTEXTO
            MenuItem menuItemSubirApunte = new MenuItem("Subir Apunte");
            MenuItem menuItemModificarApunte = new MenuItem("Modificar Apunte");
            MenuItem menuItemDescargarApunte = new MenuItem("Descargar el apunte");
            MenuItem menuItemRefrescar = new MenuItem("Refrescar");
            menuItemSubirApunte.setOnAction((ActionEvent e )->{
                onActionSubirApunte(e);
            });
            menuItemModificarApunte.setOnAction((ActionEvent e )->{
                onActionModificar(e);
            });
            menuItemDescargarApunte.setOnAction((ActionEvent e )->{
                onActionDescargarElApunte(e);
            });
            menuItemRefrescar.setOnAction((ActionEvent e )->{
                onActionRefrescar(e);
            });
            contextMenu.getItems().add(menuItemSubirApunte);
            contextMenu.getItems().add(menuItemModificarApunte);
            contextMenu.getItems().add(menuItemDescargarApunte);
            contextMenu.getItems().add(menuItemRefrescar);
            listViewApuntes.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
                if(e.getButton() == MouseButton.SECONDARY){
                    contextMenu.show(listViewApuntes, e.getScreenX(), e.getScreenY());
                }
            });
            stage.show();
        }catch(Exception e){
            LOGGER.severe("Error al iniacializar la ventana: "+e.getMessage());
        }
        
    }
    /**
     * Permite que al dar a ENTER minetras el foco este sobre el botón buscar,
     * haga su acción.
     * @param key La tecla pulsada.
     */
    private void keyPressBuscar(KeyEvent key){
        if(key.getCode().equals(KeyCode.ENTER)) {
            btnBuscar.fire();
        }
    }
    /**
     * Metodo que se ejecuta cuando se esta mostrando la venta.
     * Permite dar el foco a textFieldBuscar.
     * @param event El propio evevnto.
     */
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> LogOut");
            textFieldBuscar.requestFocus();
            
        }catch(Exception e){
            LOGGER.severe("Error al enseñar la ventana: "+e.getMessage());
        }
    }
    /**
     * Ejecuta el metodo refrescar.
     * @param event El evento de pulsación.
     */
    @FXML
    private void onActionRefrescar(ActionEvent event){
        refrescar();
    }
    /**
     * Refresca todos los datos de Apuntes & materias y quita todos los filtros.
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
     * Aplica los filtros de busqueda.
     * @param event El evento de pulsación.
     */
    @FXML
    private void onActionBuscar(ActionEvent event){
        filtrarLosApuntes();
    }
    /**
     * Abre una ventana para guardar el apunte en local.
     * @param event El evento de pulsación.
     */
    @FXML
    private void onActionDescargarElApunte(ActionEvent event){
        try{
            if(!this.listViewApuntes.getSelectionModel().isEmpty()){
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
                fileChooser.getExtensionFilters().add(extFilter);
                File fileC = fileChooser.showSaveDialog(stage);
                ApunteBean elApunte =(ApunteBean) this.listViewApuntes.getSelectionModel().getSelectedItem();
                writeBytesToFile(elApunte.getArchivo(),fileC);
                ensenarAlertaInfo("Archivo descargado","Su apunte a sido descargado correctamente.");
            }else{
                showErrorAlert("Primero selecciona un apunte.");
            }
        }catch(Exception e){
            LOGGER.severe("ERROR Al descargar el archivo: "+e.getMessage());
            showErrorAlert("Hubo un error al descargar el fichero.");
        }
    }
    /**
     * Permite subir el apunte abriendo la ventana correspondiente.
     * @param event El evento de pulsación.
     */
    @FXML
    private void onActionSubirApunte(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("subir_apunte.fxml"));
            
            Parent root = (Parent)loader.load();
            
            SubirApunteFXController controller =
                    ((SubirApunteFXController)loader.getController());
            controller.setCliente(cliente);
            controller.initStage(root);
            if(resultado!=0){
                refrescar();
                resultado=0;
            }
        }catch(IOException e){
            showErrorAlert("Error al cargar la ventana subir apunte.");
            LOGGER.severe("Error "+e.getMessage());
            
        }
    }
    /**
     * Permite modificar el apunte, abriendo la ventana de modificación de apunte.
     * @param event El evento de pulsación.
     */
    @FXML
    private void onActionModificar(ActionEvent event){
        if(!this.listViewApuntes.getSelectionModel().isEmpty()){
            try{
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("modificar_apunte.fxml"));
                
                Parent root = (Parent)loader.load();
                
                ModificarApunteFXController controller =
                        ((ModificarApunteFXController)loader.getController());
                controller.setApunte((ApunteBean) this.listViewApuntes.getSelectionModel().getSelectedItem());
                controller.initStage(root);
                if(resultado!=0){
                    refrescar();
                    resultado=0;
                }
                
            }catch(IOException e){
                showErrorAlert("Error al cargar la ventana subir apunte.");
                LOGGER.severe("Error abrir la ventana de modificar apunte."+e.getMessage());
            }
        }else{
            showErrorAlert("Primero selecciona un apunte.");
        }
    }
    /**
     * Carga los apuntes creador por el cliente.
     */
    private void cargarApuntes() {
        try {
            apuntesDeMisApunteCliente=apunteLogic.getApuntesByCreador(cliente.getId());
            
            apuntesData=FXCollections.observableArrayList(new ArrayList<>(apuntesDeMisApunteCliente));
            this.listViewApuntes.setItems(apuntesData);
            this.listViewApuntes.setCellFactory(param -> new CellTiendaApunte());
            
        } catch (BusinessLogicException ex) {
            LOGGER.severe("Error al intentar cargar los apuntes :"+ex.getMessage());
            showErrorAlert("No se ha podido cargar los apuntes");
        }
    }
    /**
     * Carga las opciones de ordenación.
     */
    private void cargarComboBox() {
        opcionesData=FXCollections.observableArrayList(Arrays.asList(this.opciones));
        this.comboBoxOrdenar.setItems(opcionesData);
        
    }
    /**
     * Carga el listView de materias, con las materias existentes.
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
            showErrorAlert("No se pudo cargar las materias");
            LOGGER.severe("Error al cargar las materias: "+ex.getMessage());
        }
    }
    /**
     * El manejador del cambio de seleccion de materias.
     * @param obvservable El valor observable.
     * @param oldValue El antiguo valor.
     * @param newValue El nuevo valor.
     */
    private void handleMateriaListSelectionChanged(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue!=null && newValue!=oldValue){
            MateriaBean materia=(MateriaBean) newValue;
            if(materia.getIdMateria()!=-1){
                //Filtrar
                //this.filtrarMateria=true;
                this.materiaFiltrada=materia;
            }else{
                //this.filtrarMateria=false;
                this.materiaFiltrada=null;
                //quitar filtrado por materia
            }
            filtrarLosApuntes();
        }else{
            //No filtrar
            //PROVISIONAL
            this.listViewMateria.getSelectionModel().select(0);
        }
    }
    /**
     * El manejador del comboBox de los tipos de ordenes que existen.
     * @param obvservable El valor observable.
     * @param oldValue El antiguo valor.
     * @param newValue El valor nuevo.
     */
    private void handleOpcionesComboBoxSelectionChanged(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue!=null && newValue!=oldValue){
            this.tipoFiltrado=(String) newValue;
            ordenarApuntes();
        }
    }
    /**
     * Ordena la lista de apuntes.
     */
    private void ordenarApuntes() {
        List <ApunteBean> apuntesParaOrdenar=null;
        if(this.filtrado)
            apuntesParaOrdenar=Lists.newArrayList(this.apuntesDeTiendaFiltrado);
        else
            apuntesParaOrdenar=Lists.newArrayList(this.apuntesDeMisApunteCliente);
        switch(this.tipoFiltrado){
            case "Sin filtro":
                
                break;
            case "ABC...":
                apuntesParaOrdenar=apuntesParaOrdenar.stream().sorted(Comparator.comparing(ApunteBean::getTitulo)).collect(Collectors.toList());
                break;
            case "ZYZ...":
                apuntesParaOrdenar=apuntesParaOrdenar.stream().sorted(Comparator.comparing(ApunteBean::getTitulo, Comparator.reverseOrder())).collect(Collectors.toList());
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
     * Filtra los apuntes.
     */
    private void filtrarLosApuntes(){
        
        this.filtrado=false;
        //Para coger los apuntes de una materia
        Set <ApunteBean> apuntesDeLaMateria = new HashSet <ApunteBean>();
        if(this.materiaFiltrada!=null){
            this.filtrado=true;
            this.apuntesDeMisApunteCliente.forEach(apunte ->{
                if(this.materiaFiltrada.getIdMateria()==apunte.getMateria().getIdMateria()){
                    apuntesDeLaMateria.add(apunte);
                }
            });
        }else{
            this.apuntesDeMisApunteCliente.forEach(apunte ->{
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
        //ESTA PARTE SE CAMBIA  Y LO QUE HARA SERA LLAMAR AL METODO ORDENAR
        if(this.filtrado){
            
            this.apuntesDeTiendaFiltrado=apuntesConElNombre;//quizas quitar
            /*
            this.apuntesDataFiltrado=FXCollections.observableArrayList(new ArrayList<>(apuntesConElNombre));
            this.listViewApuntes.setItems(this.apuntesDataFiltrado);
            */
            ordenarApuntes();
            
        }else{
            //this.listViewApuntes.setItems(apuntesData);
            ordenarApuntes();
        }
        
    }
    
    //Inicio de los metodos de navegación de la aplicación
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
            LOGGER.severe("Error al cerrar sesion: "+e.getMessage());
            showErrorAlert("Error al cerrar sesion.");
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
            LOGGER.severe("Error al cerrar la aplicación: "+e.getMessage());
            showErrorAlert("Error al cerrar la aplicación");
        }
    }
    
    //Inicio de los metodos de navegación de la aplicación
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
            LOGGER.severe("Error al abrir la ventana MisApuntes: "+e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor."+e.getMessage());
        }
    }
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
            LOGGER.severe("Error al abrir la ventana Tienda apuntes: "+e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor."+e.getMessage());
        }
    }
    @FXML
    private void onActionAbrirMiBiblioteca(ActionEvent event){
    }
    @FXML
    private void onActionAbrirTiendaPacks(ActionEvent event){
    }
    @FXML
    private void onActionAbrirMiPerfil(ActionEvent event){
    }
    @FXML
    private void onActionAbout(ActionEvent event){final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        
        URL url = this.getClass().getResource("/ayuda/ayuda_subir_apunte.html");
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
    public void cerrarAyuda(ActionEvent event){
        stageAyuda.hide();
    }
    //Fin de los metodos de navegación de la aplicación
    /**
     * Metodo para insertar el stage.
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /**
     * Metodo para insertar el cliente.
     * @param cliente
     */
    public void setCliente(ClienteBean cliente){
        this.cliente=cliente;
    }
    public static void setResultadoApunteModificado(int resultado){/////////////////////////////////////
        MisApuntesClienteFXController.resultado=resultado;
    }
    
    //Metodos utiles
    /**
     * Escribe un array de bytes en un directorio y tipo de fichero especifico.
     * @param bFile El array de bytes.
     * @param fileC La ruta, el nombre y el tipo del archivo.
     */
    private void writeBytesToFile(byte[] bFile, File fileC) {
        
        FileOutputStream fileOuputStream = null;
        
        try {
            fileOuputStream = new FileOutputStream(fileC.getPath());
            fileOuputStream.write(bFile);
            
        } catch (IOException e) {
            LOGGER.severe("Error al descargar el apunte en un fichero: "+e.getMessage());
        } finally {
            if (fileOuputStream != null) {
                try {
                    fileOuputStream.close();
                } catch (IOException e) {
                    LOGGER.severe("Error al intentar cerrar el stream para descargar el fichero: "+e.getMessage());
                }
            }
        }
    }
    /**
     * Enseña una alerta de información por pantalla.
     * @param titulo El titulo de la alerta.
     * @param elMensaje El mensaje de la alerta.
     */
    private void ensenarAlertaInfo(String titulo, String elMensaje) {
        Alert alertCerrarSesion = new Alert(Alert.AlertType.INFORMATION);
        alertCerrarSesion.setTitle(titulo);
        alertCerrarSesion.setHeaderText(elMensaje);
        alertCerrarSesion.show();
    }
    
}
