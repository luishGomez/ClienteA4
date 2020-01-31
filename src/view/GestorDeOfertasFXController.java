/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package view;

import businessLogic.BusinessLogicException;
import businessLogic.OfertaManager;
import static businessLogic.OfertaManagerFactory.createOfertaManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
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
import transferObjects.OfertaBean;
import transferObjects.PackBean;
import transferObjects.UserBean;
import static view.ControladorGeneral.showErrorAlert;

/**
 * Administrador podra gestionar las ofertas, creando, eliminando o actualizandolas.
 * @author Sergio
 */
public class GestorDeOfertasFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.GestorDeOfertasFXController");
    private final String RUTA_AYUDA = getClass().getResource("/ayuda/ayuda_perfil.html").toExternalForm();
    private final String INFO_MODIFI = "Seleccionar previamente una fila de la tabla..";
    private ObservableList<String>  packs_titulo = FXCollections.observableArrayList();
    private ObservableList<PackBean>  packs;
    public OfertaManager ofertaLogic = createOfertaManager("real");
    private int opcion=0;
    private UserBean user;
    private Stage stage;
    private int clickModificars=0;
    private int clickInsertar=0;
    OfertaBean ofertaBorrar = null;
    
    @FXML private Menu menuCuenta;
    @FXML private MenuItem menuCuentaCerrarSesion;
    @FXML private MenuItem menuCuentaSalir;
    @FXML private Menu menuVentanas;
    @FXML private MenuItem menuVentanasGestorApuntes;
    @FXML private MenuItem menuVentanasGestorPacks;
    @FXML private MenuItem menuVentanasGestorOfertas;
    @FXML private MenuItem menuVentanasGestorMaterias;
    @FXML private Menu menuHelp;
    @FXML private MenuItem menuHelpAbout;
    private ObservableList<OfertaBean> ofertas;
    @FXML private TableView<OfertaBean> tablaGestorOfertas;
    @FXML private TableColumn columnaId;
    @FXML private TableColumn columnaFechaInicio;
    @FXML private TableColumn columnaFechaFin;
    @FXML private TableColumn columnaOferta;
    @FXML private TableColumn columnaDescuento;
    @FXML private DatePicker dateInicio;
    @FXML private DatePicker dateFin;
    @FXML private TextField txtOfertaNombre;
    @FXML private TextField txtDescuento;
    @FXML private ComboBox<String> comboPacks;
    @FXML private Button btnAceptar;
    @FXML private Button btnBuscar;
    @FXML private Button btnModificar;
    @FXML private Button btnBorrar;
    @FXML private TextField txtBuscar;
    public void setUser(UserBean user){this.user=user;}
    
    public void initStage(Parent root) {
        try{
            Scene scene=new Scene(root);
            stage=new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Gestor ofertas");
            stage.setResizable(true);
            stage.setMaximized(true);
            //Vamos a rellenar los datos en la ventana.
            columnaId.setCellValueFactory(new PropertyValueFactory<>("idOferta"));
            columnaFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
            columnaFechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
            columnaOferta.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            columnaDescuento.setCellValueFactory(new PropertyValueFactory<>("rebaja"));
            ofertas = FXCollections.observableArrayList(ofertaLogic.todasOfertas());
            tablaGestorOfertas.setItems(ofertas);
            
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
            //eventos
            stage.addEventHandler(MouseEvent.MOUSE_CLICKED, this::clicks);
            txtDescuento.addEventHandler(MouseEvent.MOUSE_CLICKED, this::clicks);
            txtOfertaNombre.addEventHandler(MouseEvent.MOUSE_CLICKED, this::clicks);
            dateInicio.addEventHandler(MouseEvent.MOUSE_CLICKED, this::clicks);
            dateFin.addEventHandler(MouseEvent.MOUSE_CLICKED, this::clicks);
            
            txtBuscar.setOnKeyPressed(this::keyPress);
            btnBuscar.setOnKeyPressed(this::keyPress);
            txtBuscar.setPromptText("Buscar por titulo");
            btnModificar.setTooltip(new Tooltip(INFO_MODIFI));
            btnBorrar.setTooltip(new Tooltip(INFO_MODIFI));
            stage.show();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
        
    }
    /**
     * Castea una fecha de date a localDate
     * @param date
     * @return
     */
    public LocalDate dateToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    /**
     * castea una fecha de LocalDate a Date
     * @param date
     * @return
     */
    public Date localDateToDate(LocalDate date) {
        return java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
    /**
     * Atajos
     * @param key
     */
    public void keyPress(KeyEvent key){
        if(key.getCode().equals(KeyCode.ENTER))
            btnBuscar.fire();
    }
    /**
     * Modifica y actualiza en la BBDD los campos de la fila seleccionada
     */
    @FXML public void modificar(){
        clickInsertar=0;
        if(clickModificars==0 && tablaGestorOfertas.getSelectionModel().getSelectedIndex()>=0 && tablaGestorOfertas.getSelectionModel().getSelectedIndex()<ofertas.size()){
            opcion = 1;
            int indiceFilaSeleccionada = tablaGestorOfertas.getSelectionModel().getSelectedIndex();
            LOGGER.info(String.valueOf(indiceFilaSeleccionada));
            
            txtOfertaNombre.setText(ofertas.get(indiceFilaSeleccionada).getTitulo());
            txtDescuento.setText(Float.toString(ofertas.get(indiceFilaSeleccionada).getRebaja()));
            dateInicio.setValue(dateToLocalDate(ofertas.get(indiceFilaSeleccionada).getFechaInicio()));
            dateFin.setValue(dateToLocalDate(ofertas.get(indiceFilaSeleccionada).getFechaFin()));
            
            txtOfertaNombre.setVisible(true);
            txtDescuento.setVisible(true);
            dateInicio.setVisible(true);
            dateFin.setVisible(true);
            btnAceptar.setVisible(true);
            
            clickModificars++;
        }else{
            clickModificars=0;
            txtOfertaNombre.setVisible(false);
            txtDescuento.setVisible(false);
            dateInicio.setVisible(false);
            dateFin.setVisible(false);
            btnAceptar.setVisible(false);
            txtOfertaNombre.setText("");
            txtDescuento.setText("");
            dateInicio.setValue(null);
            dateFin.setValue(null);
        }
        
    }
    /**
     * Hace visible los campos necesarios a insertar oferta.
     */
    @FXML public void insertar(){
        clickModificars=0;
        opcion =2;
        if(clickInsertar==0){
            txtOfertaNombre.setVisible(true);
            txtDescuento.setVisible(true);
            dateInicio.setVisible(true);
            dateFin.setVisible(true);
            btnAceptar.setVisible(true);
            txtOfertaNombre.setText("");
            txtDescuento.setText("");
            dateInicio.setValue(null);
            dateFin.setValue(null);
            clickInsertar++;
        }else{
            limpiarDatos();
            clickInsertar=0;
        }
        
    }
    /**
     * Elimina oferta de la BBDD respecto a la fila seleccionada en la tabla.
     */
    @FXML public void borrar(){
        
        LOGGER.info("he clicado remplazar fila");
        int indiceFilaSeleccionada = tablaGestorOfertas.getSelectionModel().getSelectedIndex();
        
        if(tablaGestorOfertas.getSelectionModel().getSelectedIndex()>=0 && tablaGestorOfertas.getSelectionModel().getSelectedIndex()<ofertas.size()){
            
            Alert alertIngresarSaldo = new Alert(Alert.AlertType.CONFIRMATION,"¿Seguro que quiere borrar esta oferta?.",ButtonType.NO,ButtonType.YES);
            alertIngresarSaldo.setTitle("¨Borrado de oferta");
            alertIngresarSaldo.setHeaderText("Borrando la oferta.");
            alertIngresarSaldo.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    ofertaBorrar = ofertas.get(indiceFilaSeleccionada);
                    try {
                        ofertaLogic.borrarOferta(ofertaBorrar.getIdOferta());
                    } catch (BusinessLogicException ex) {
                        Logger.getLogger(GestorDeOfertasFXController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    tablaGestorOfertas.getItems().remove(tablaGestorOfertas.getSelectionModel().getSelectedItem());
                    tablaGestorOfertas.refresh();
                    limpiarDatos();
                }
            });
            
        }
    }
    /**
     * Acepta la accion dependiendo del caso de uso
     * CASE 1: Actualiza los datos de la oferta seleccionada | CASE 2: Crea oferta nueva.
     */
    @FXML public void aceptar(){
        Boolean correcto = false;
        Date fFin  = localDateToDate(dateInicio.getValue());
        Date fInicio  = localDateToDate(dateFin.getValue());
        String oofertaNombre  = txtOfertaNombre.getText();
        if(oofertaNombre.length()<251)
            correcto=true;
        else{
            showErrorAlert("esceso de caracteres, limite 250.");
            correcto=false;
            txtOfertaNombre.requestFocus();
        }
        Float descuento = null;
        try {
            descuento=Float.parseFloat(txtDescuento.getText());
            correcto=true;
        }
        catch(NumberFormatException e) {
            showErrorAlert("numero de decuento erronea, (contiene caracteres erroneos)");
            txtDescuento.requestFocus();
            correcto=false;
        }
        if(correcto){
            switch(opcion){
                case 1:
                    OfertaBean ofertaMOdificada = null;
                    int indiceFilaSeleccionada = tablaGestorOfertas.getSelectionModel().getSelectedIndex();
                    ofertaMOdificada = ofertas.get(indiceFilaSeleccionada);
                    ofertaMOdificada.setFechaFin(fFin);
                    ofertaMOdificada.setFechaInicio(fInicio);
                    ofertaMOdificada.setRebaja(descuento);
                    ofertaMOdificada.setTitulo(oofertaNombre);
                    try {
                        ofertaLogic.actualizarOferta(ofertaMOdificada);
                        ofertas.set(indiceFilaSeleccionada, ofertaMOdificada);
                        limpiarDatos();
                        clickModificars=0;
                        tablaGestorOfertas.refresh();
                        opcion = 0;
                    } catch (BusinessLogicException ex) {
                        LOGGER.log(Level.SEVERE,
                                "GestorDeOfertasFXController: Error Actualizar oferta",
                                ex.getMessage());
                    }
                    break;
                case 2:
                    OfertaBean nuevaOferta = new OfertaBean();
                    nuevaOferta.setFechaFin(fFin);
                    nuevaOferta.setFechaInicio(fInicio);
                    nuevaOferta.setRebaja(descuento);
                    nuevaOferta.setTitulo(oofertaNombre);
                    LOGGER.info(nuevaOferta.getIdOferta().toString());
                    try {
                        ofertaLogic.createOferta(nuevaOferta);
                        try {
                            ofertas = FXCollections.observableArrayList(ofertaLogic.todasOfertas());
                            tablaGestorOfertas.setItems(ofertas);
                            tablaGestorOfertas.refresh();
                            limpiarDatos();
                            clickInsertar=0;
                            opcion = 0;
                        } catch (BusinessLogicException ex) {
                            LOGGER.log(Level.SEVERE,
                                    "GestorDeOfertasFXController: Error recoger todas las ofertas",
                                    ex.getMessage());
                        }
                    } catch (BusinessLogicException ex) {
                        LOGGER.log(Level.SEVERE,
                                "GestorDeOfertasFXController: Error crear oferta.",
                                ex.getMessage());
                    }
                    break;
            }
        }
        
    }
    /**
     * Limpia (vacia) los campos.
     */
    private void limpiarDatos() {
        txtOfertaNombre.setVisible(false);
        txtDescuento.setVisible(false);
        dateInicio.setVisible(false);
        dateFin.setVisible(false);
        btnAceptar.setVisible(false);
        txtOfertaNombre.setText("");
        txtDescuento.setText("");
        dateInicio.setValue(null);
        dateFin.setValue(null);
    }
    /**
     * Filtra los apuntes segun la palabra o palabras a buscar.
     * @param event
     */
    @FXML private void buscar(ActionEvent event){
        String palabraBusqueda;
        ObservableList<OfertaBean> apuntesFiltrados = FXCollections.observableArrayList();
        if(txtBuscar.getText().isEmpty()){
            LOGGER.info("primer if");
            tablaGestorOfertas.setItems(ofertas);
            tablaGestorOfertas.refresh();
        }
        else{
            palabraBusqueda = txtBuscar.getText().toUpperCase();
            ofertas.stream().filter((apunte) -> (apunte.getTitulo().toUpperCase().contains(palabraBusqueda))).forEachOrdered((apunte) -> {
                apuntesFiltrados.add(apunte);
            });
            if(apuntesFiltrados.size()>0){
                tablaGestorOfertas.setItems(apuntesFiltrados);
                tablaGestorOfertas.refresh();
            }else{
                tablaGestorOfertas.setItems(null);
                tablaGestorOfertas.refresh();
            }
        }
    }
    /**
     * evento para deselccionar fila.
     * @param m
     */
    public void clicks(MouseEvent m){
        if(m.getButton()==MouseButton.PRIMARY){
            if(!(!txtDescuento.isFocused() || !txtOfertaNombre.isFocused()|| !dateFin.isFocused() || !dateInicio.isFocused()))
                tablaGestorOfertas.getSelectionModel().select(null);
        }
        
    }
    /**
     * Crea y muestra la ventana ayuda.
     */
    public void ayuda(){
        Stage stageAyuda = new Stage();
        WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();
        webEngine.load(RUTA_AYUDA);
        VBox root = new VBox();
        root.getChildren().add(webView);
        Scene scene = new Scene(root);
        stageAyuda.setScene(scene);
        stageAyuda.setTitle("Ventana de ayuda");
        stageAyuda.setResizable(false);
        stageAyuda.setMinWidth(1285);
        stageAyuda.initModality(Modality.APPLICATION_MODAL);
        stageAyuda.show();
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
    private void onActionAbout(ActionEvent event){
        ayuda();
    }
    //Fin de los metodos de navegación de la aplicación
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
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor."+e.getMessage());
        }
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    
    
    
}
