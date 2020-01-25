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
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import transferObjects.ApunteBean;
import transferObjects.ClienteBean;
import transferObjects.MateriaBean;
import transferObjects.UserBean;
import static view.ControladorGeneral.showErrorAlert;

/**
 * La clase controladora de la ventana de gestion de apuntes.
 * @author Ricardo Peinado Lastra
 */
public class GestorDeApuntesFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.GestorDeApuntesFXController");
    private UserBean user;
    private Stage stage;
    
    private ApunteManager apunteLogic = createApunteManager("real");
    private MateriaManager materiaLogic = createMateriaManager("real");
    private Set<ApunteBean> apuntes=null;
    private ObservableList<ApunteBean> apuntesData=null;
    private ObservableList<MateriaBean> materiasData=null;
    private ApunteBean apunteProvisional;
    private final int MIN_CARACTERES=3;
    private final int MAX_CARACTERES=250;
    private Stage stageAyuda;
    @FXML
    private TableView tableApuntes;
    @FXML
    private TableColumn clId;
    @FXML
    private TableColumn clTitulo;
    @FXML
    private TableColumn clDesc;
    @FXML
    private TableColumn clFechaValidacion;
    @FXML
    private TableColumn clLikes;
    @FXML
    private TableColumn clPrecio;
    @FXML
    private TableColumn clCreador;
    @FXML
    private TableColumn clDislike;
    @FXML
    private TableColumn clMateria;
    @FXML
    private Button btnRefrescar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnBorrar;
    @FXML
    private Button btnInforme;
    @FXML
    private TextField textFieldTitulo;
    @FXML
    private TextArea textFieldDesc;
    @FXML
    private DatePicker datePickerFecha;
    @FXML
    private ComboBox comboBoxMaterias;
    @FXML
    private Label labelTitulo;
    @FXML
    private Label labelDesc;
    @FXML
    private Label labelFecha;
    @FXML
    private Label labelMateria;
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
    private ContextMenu contextMenu=new ContextMenu();
    /**
     * El metodo que inicializa la ventana.
     * @param root El nodo raiz de la vista.
     */
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando la ventana Gestion de apuntes.");
            Scene scene=new Scene(root);
            stage=new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Gestor de apuntes");
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
            menuVentanasGestorApuntes.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+A"));
            menuVentanasGestorMaterias.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+M"));
            menuVentanasGestorOfertas.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+O"));
            menuVentanasGestorPacks.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+P"));
            //<-Menu
            btnRefrescar.setMnemonicParsing(true);
            btnRefrescar.setText("_Refrescar");
            btnInforme.setMnemonicParsing(true);
            btnInforme.setText("_Informe");
            btnModificar.setMnemonicParsing(true);
            btnModificar.setText("_Modificar");
            btnBorrar.setMnemonicParsing(true);
            btnBorrar.setText("_Borrar");
            
            
            //Preparar la tabla tableView
            clId.setCellValueFactory(new PropertyValueFactory<>("idApunte"));
            clTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            clDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            clFechaValidacion.setCellValueFactory(new PropertyValueFactory<>("fechaValidacion"));
            clLikes.setCellValueFactory(new PropertyValueFactory<>("likeCont"));
            clPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            clCreador.setCellValueFactory(new PropertyValueFactory<ClienteBean,String>("creador"));
            clDislike.setCellValueFactory(new PropertyValueFactory<>("dislikeCont"));
            clMateria.setCellValueFactory(new PropertyValueFactory<MateriaBean,String>("materia"));
            //CARGAR DATOS
            cargarApuntes();
            cargarMaterias();
            tableApuntes.getSelectionModel().selectedItemProperty().addListener(this::handleApuntesTableSelectionChanged);
            //PREPARAR EL MENU DE CONTEXTO
            MenuItem menuItemBorrar = new MenuItem("Borrar apunte");
            MenuItem menuItemRefrescar = new MenuItem("Refrescar apuntes");
            menuItemBorrar.setOnAction((ActionEvent e) ->{
                onActionBorrar(e);
            });
            menuItemRefrescar.setOnAction((ActionEvent e) -> {
                onActionRefrescar(e);
            });
            contextMenu.getItems().add(menuItemRefrescar);
            contextMenu.getItems().add(menuItemBorrar);
            tableApuntes.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
                if(e.getButton() == MouseButton.SECONDARY){
                    contextMenu.show(tableApuntes, e.getScreenX(), e.getScreenY());
                }
            });
            //Desabilitar botones
            this.btnModificar.setDisable(true);
            this.btnBorrar.setDisable(true);
            
            //Poner TOOLTIPS
            this.textFieldTitulo.setTooltip(new Tooltip("Requisitos:\n-Mínimo caracteres: "
                    +MIN_CARACTERES+"\n-Máximo caracteres: "+MAX_CARACTERES));
            this.textFieldDesc.setTooltip(new Tooltip("Requisitos:\n-Mínimo caracteres: "
                    +MIN_CARACTERES+"\n-Máximo caracteres: "+MAX_CARACTERES));
            stage.show();
        }catch(Exception e){
            LOGGER.severe("Error > GestorDeApuntesFXController > initStage: "+e.getMessage());
        }
        
    }
    /**
     * Metodo que permite ejecutar codigo cuando se esta mostrando la ventana.
     * @param event
     */
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> Gestor apuntes");
            
        }catch(Exception e){
            LOGGER.severe("Error > GestorDeApuntesFXController > handleWindowShowing: "+e.getMessage());
        }
    }
    /**
     * Inserta el usuario adminitrador.
     * @param user Los datos del administrador.
     */
    public void setUser(UserBean user){
        this.user=user;
    }
    /**
     * Inserta el escenario.
     * @param stage El escenario que se utiliza para visualizar la ventana.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /**
     * Carga los apuntes en la tabla.
     */
    private void cargarApuntes() {
        try {
            apuntes=apunteLogic.findAll();
            apuntesData=FXCollections.observableArrayList(new ArrayList<>(apuntes));
            tableApuntes.setItems(apuntesData);
        } catch (BusinessLogicException ex) {
            LOGGER.severe("Error al intentar cargar los apuntes :"+ex.getMessage());
            showErrorAlert("No se ha podido cargar los apuntes.");
        }
    }
    /**
     * Carga las materias en el comboBox.
     */
    private void cargarMaterias() {
        try {
            Set<MateriaBean> materias = materiaLogic.findAllMateria();
            materiasData=FXCollections.observableArrayList(new ArrayList<>(materias));
            this.comboBoxMaterias.setItems(materiasData);
        } catch (BusinessLogicException ex) {
            LOGGER.severe("Error al cargar las materias: "+ex.getMessage());
            showErrorAlert("No se ha podido cargar las materias.");
        }
    }
    //onActionCrearInforme
    @FXML
    private void onActionCrearInforme(ActionEvent event){
        try{
            JasperReport report= JasperCompileManager.compileReport(getClass()
                    .getResourceAsStream("/view/informeDeApuntes.jrxml"));
            JRBeanCollectionDataSource dataItems=new JRBeanCollectionDataSource((Collection<ApunteBean>)this.tableApuntes.getItems());
            Map <String,Object> parameters=new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint);
            jasperViewer.setVisible(true);
            
        }catch(Exception e ){
            LOGGER.severe("Error iniciar el informe: "+e.getMessage());
            showErrorAlert("No se ha podido generar el informe.");
        }
    }
    /**
     * Metodo que se encarga el borrado de un apunte.
     * @param event El evento de pulsación del botón asignado.
     */
    @FXML
    private void onActionBorrar(ActionEvent event){
        if(this.apunteProvisional!=null){
            try{
                boolean sePuedeBorrar=true;
                int cuantasCompras=this.apunteLogic.cuantasCompras(this.apunteProvisional.getIdApunte());
                if(cuantasCompras==0){
                    try{
                        //Creamos la alerta del tipo confirmación.
                        Alert alertCerrarSesion = new Alert(AlertType.CONFIRMATION);
                        alertCerrarSesion.setTitle("Borrar apunte");
                        alertCerrarSesion.setHeaderText("¿Estas seguro de borrar el apunte "+this.apunteProvisional.getTitulo()+"?");
                        alertCerrarSesion.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                try {
                                    apunteLogic.remove(apunteProvisional.getIdApunte());
                                    Alert alert=new Alert(Alert.AlertType.INFORMATION,
                                            "El apunte "+this.apunteProvisional.getTitulo()+" fue eliminado.",
                                            ButtonType.OK);
                                    alert.showAndWait();
                                    this.tableApuntes.getItems().remove(this.tableApuntes.getSelectionModel().getSelectedItem());
                                    this.tableApuntes.refresh();
                                    vaciar();
                                    this.tableApuntes.getSelectionModel().select(null);
                                } catch (BusinessLogicException ex) {
                                    Logger.getLogger(GestorDeApuntesFXController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                    }catch(Exception e){
                        LOGGER.severe("ERROR al intentar borrar un apunte: "+e.getMessage());
                        showErrorAlert("Lo sentimos, hubo un error al intentar borrar el apunte.");
                    }
                }else{
                    showErrorAlert("Lo sentimos, no se puede borrar el apunte, ya que tiene una o más de una venta.");
                }
            }catch(BusinessLogicException ex){
                LOGGER.severe("ERROR al consultar cuantas compras tiene un apunte: "+ex.getMessage());
                showErrorAlert("Lo sentimos, hubo un error al consultar datos sobre el apunte.");
            }
        }else{
            showErrorAlert("Para borrar un apunte seleccione en la tabla el apunte correspondient porfavor.");
        }
    }
    /**
     * Refresca todos los datos de los apuntes como el de las materias.
     * @param event El propio evento de pulsación que lo activa.
     */
    @FXML
    private void onActionRefrescar(ActionEvent event){
        cargarApuntes();
        cargarMaterias();
        vaciar();
    }
    /**
     * Vacia los campos para de la ventana sobre los apuntes.
     */
    public void vaciar(){
        this.textFieldTitulo.setText("");
        this.textFieldDesc.setText("");
        this.datePickerFecha.setValue(null);
        this.apunteProvisional=null;
        this.comboBoxMaterias.getSelectionModel().select(null);
        this.btnModificar.setDisable(true);
        this.btnBorrar.setDisable(true);
        ponerTodoCorrecto();
        
    }
    /**
     * Pone todas las etiquetas sin errores.
     */
    public void ponerTodoCorrecto(){
        this.labelTitulo.setText("Titulo");
        this.labelTitulo.setTextFill(Color.web("black"));
        this.labelDesc.setText("Descripción");
        this.labelDesc.setTextFill(Color.web("black"));
        this.labelFecha.setTextFill(Color.web("black"));
        this.labelMateria.setTextFill(Color.web("black"));
    }
    /**
     * Metodo que permite modificar el apunte seleccionado.
     * @param event El propio evento de pulsacion que lo activa.
     */
    @FXML
    private void onActionModificar(ActionEvent event){
        if(this.apunteProvisional!=null){
            boolean todoBien = true;
            if(!esValido(this.textFieldTitulo.getText().trim(),MIN_CARACTERES,MAX_CARACTERES)){
                this.labelTitulo.setText("Titulo (Min "+MIN_CARACTERES+" car. | Max "+MAX_CARACTERES+" car.)");
                this.labelTitulo.setTextFill(Color.web("red"));
                todoBien=false;
            }else{
                this.labelTitulo.setText("Titulo");
                this.labelTitulo.setTextFill(Color.web("black"));
            }
            if(!esValido(this.textFieldDesc.getText().trim(),MIN_CARACTERES,250)){
                this.labelDesc.setText("Descripción (Min "+MIN_CARACTERES+" car. | Max "+MAX_CARACTERES+" car.)");
                this.labelDesc.setTextFill(Color.web("red"));
                todoBien=false;
            }else{
                this.labelDesc.setText("Descripción");
                this.labelDesc.setTextFill(Color.web("black"));
            }
            if(this.datePickerFecha.getValue()==null){
                this.labelFecha.setTextFill(Color.web("red"));
                todoBien=false;
            }else{
                this.labelFecha.setTextFill(Color.web("black"));
            }
            if(this.comboBoxMaterias.getSelectionModel().getSelectedItem()==null){
                this.labelMateria.setTextFill(Color.web("red"));
                todoBien=false;
            }else{
                this.labelMateria.setTextFill(Color.web("black"));
            }
            if(todoBien){
                apunteProvisional.setTitulo(this.textFieldTitulo.getText().trim());
                apunteProvisional.setDescripcion(this.textFieldDesc.getText().trim());
                apunteProvisional.setFechaValidacion(localDateToDate(this.datePickerFecha.getValue()));
                apunteProvisional.setMateria((MateriaBean) this.comboBoxMaterias.getSelectionModel().getSelectedItem());
                
                try {
                    apunteLogic.edit(apunteProvisional);
                    Alert alert=new Alert(Alert.AlertType.INFORMATION,
                            "El apunte "+this.apunteProvisional.getTitulo()+" fue modificado.",
                            ButtonType.OK);
                    alert.showAndWait();
                    apuntesData.set(this.tableApuntes.getSelectionModel().getFocusedIndex(), apunteProvisional);
                    this.tableApuntes.getSelectionModel().clearSelection();
                    this.tableApuntes.refresh();
                    vaciar();
                    
                } catch (BusinessLogicException ex) {
                    LOGGER.severe("Error enviar la modificación del apunte: "+ex.getMessage());
                    showErrorAlert("Hubo un error al modificar el apunte.");
                }
            }
        }else{
            showErrorAlert("Para modificar primero seleccione un apunte.");
        }
    }
    /**
     * El manejador de la selección de la tabla de apuntes.
     * @param obvservable El valor observable.
     * @param oldValue El antiguo valor de objeto seleccionado.
     * @param newValue El nuevo valor del objeto seleccionado.
     */
    private void handleApuntesTableSelectionChanged(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue!=null && newValue!=oldValue){
            apunteProvisional =(ApunteBean) newValue;
            this.textFieldTitulo.setText(apunteProvisional.getTitulo());
            this.textFieldDesc.setText(apunteProvisional.getDescripcion());
            if(apunteProvisional.getFechaValidacion()!=null)
                this.datePickerFecha.setValue(dateToLocalDate(apunteProvisional.getFechaValidacion()));
            else
                this.datePickerFecha.setValue(null);
            this.comboBoxMaterias.getSelectionModel().select(apunteProvisional.getMateria());
            this.btnModificar.setDisable(false);
            this.btnBorrar.setDisable(false);
            ponerTodoCorrecto();
        }else{
            vaciar();
            
        }
    }
    //Parte comun
    /**
     * Permite cerrar sesíón.
     * @param event El evento de pulsación.
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
            LOGGER.severe("Error al cerrar sesión: "+e.getMessage());
            showErrorAlert("Error al cerrar sesión.");
        }
    }
    /**
     * Permite salir de la aplicación,
     * @param event El evento de pulsación.
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
            LOGGER.severe("Error al cerrar la aplicación: "+e.getMessage());
            showErrorAlert("Error al cerrar la aplicación.");
        }
    }
    
    //Inicio de los metodos de navegación de la aplicación
    
    /**
     * Abre ala ventana de ayuda de la clase GestorDeApuntesFXController.
     * @param event El evento de pulsación.
     */
    @FXML
    private void onActionAbout(ActionEvent event){
        try{
            final WebView browser = new WebView();
            final WebEngine webEngine = browser.getEngine();
            
            URL url = this.getClass().getResource("/ayuda/ayuda_gestor_apuntes.html");
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
        }catch(Exception e){
            LOGGER.severe("Error al abrir la ventana de ayuda: "+e.getMessage());
            showErrorAlert("A ocurrido un error al abrir la ventana de ayuda");
        }
    }
    /**
     * Cierra la ventana de ayuda.
     * @param event El evento de pulsación.
     */
    public void cerrarAyuda(ActionEvent event){
        stageAyuda.hide();
    }
    //Metodos de navigacion entre ventanas de administrador
    /**
     * Abre la ventana de gestion de apuntes.
     * @param event  El evento de pulsación.
     */
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
            LOGGER.severe("Error al intentar abrir Gestor de apuntes."+e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor.");
        }
    }
    /**
     * Abre la ventana gestor de packs.
     * @param event  El evento de pulsación.
     */
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
            LOGGER.severe("Error al intentar abrir Gestor de packs"+e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor.");
        }
    }
    /**
     * Abre la ventna gestor de ofertas.
     * @param event El evento de pulsación.
     */
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
            LOGGER.severe("Error al intentar abrir Gestor de ofertas"+e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor.");
        }
    }
    /**
     * Abre la ventana de gestor de materias.
     * @param event El evento de pulsación.
     */
    @FXML
    private void onActionAbrirGesstorMaterias(ActionEvent event){
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
            LOGGER.severe("Error al intentar abrir Gestor de Materias"+e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor.");
        }
    }
    
    //Metodos utiles de la clase
    /**
     * Transforma una objecto date to LocalDate.
     * @param date El obeto date con su datos.
     * @return Retorna el LocalDate base a los datos de Date.
     */
    public LocalDate dateToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        //return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault()).toLocalDate();
    }
    /**
     * Tranforma LocalDate to date.
     * @param date El objeto LocalDate con sus datos.
     * @return Retorna el Date base a los datos del LocalDate
     */
    public Date localDateToDate(LocalDate date) {
        return java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
        //return new Date(date.toEpochDay());
    }
    /**
     * Valida que el texto que se le pase entra en los caractes minimos y maximos.
     * @param frase El texto a analizar.
     * @param minimo La cantidad de caracteres minimos a leer.
     * @param maximo La cantidad maaxima de cracteres a leer.
     * @return Ture si es valido | False en los demas casos.
     */
    public boolean esValido(String frase,int minimo, int maximo){
        boolean resultado=true;
        if(frase.length()>maximo || frase.length()<minimo)
            resultado=false;
        return resultado;
    }
    
}
