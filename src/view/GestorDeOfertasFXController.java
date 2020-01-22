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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.OfertaBean;
import transferObjects.UserBean;
import static view.ControladorGeneral.showErrorAlert;

/**
 *
 * @author Sergio
 */
public class GestorDeOfertasFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.GestorDeOfertasFXController");
    public OfertaManager ofertaLogic = createOfertaManager("real");
    private int opcion=0;
    private UserBean user;
    private Stage stage;
    
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
    
    
    public void initStage(Parent root) {
        
        try{
            LOGGER.info("Iniciando la ventana LogOut");
            Scene scene=new Scene(root);
            stage=new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Tienda de apuntes");
            stage.setResizable(true);
            stage.setMaximized(true);
            //Vamos a rellenar los datos en la ventana.
            stage.setOnShowing(this::handleWindowShowing);
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
            //<-Menu
            stage.show();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
        
    }
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> LogOut");
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    public void setUser(UserBean user){
        this.user=user;
    }
    public LocalDate dateToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    public Date localDateToDate(LocalDate date) {
        
        return java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
    @FXML public void modificar(){
        opcion = 1;
        LOGGER.info("he clicado remplazar fila");
        int indiceFilaSeleccionada = tablaGestorOfertas.getSelectionModel().getSelectedIndex();
        
        if(tablaGestorOfertas.getSelectionModel().getSelectedIndex()>=0 && tablaGestorOfertas.getSelectionModel().getSelectedIndex()<ofertas.size()){
            txtOfertaNombre.setText(ofertas.get(indiceFilaSeleccionada).getTitulo());
            txtDescuento.setText(Float.toString(ofertas.get(indiceFilaSeleccionada).getRebaja()));
            
            dateInicio.setValue(dateToLocalDate(ofertas.get(indiceFilaSeleccionada).getFechaInicio()));
            dateFin.setValue(dateToLocalDate(ofertas.get(indiceFilaSeleccionada).getFechaFin()));
            
            /*for(PackBean pack:ofertas.get(indiceFilaSeleccionada).getPacks()){
            comboPacks.setItems(FXCollections.observableArrayList(pack.getNombre));
            }*/
            txtOfertaNombre.setVisible(true);
            txtDescuento.setVisible(true);
            dateInicio.setVisible(true);
            dateFin.setVisible(true);
            btnAceptar.setVisible(true);
        }
        
    }
    @FXML public void insertar() throws BusinessLogicException{
        opcion =2;
        txtOfertaNombre.setVisible(true);
        txtDescuento.setVisible(true);
        dateInicio.setVisible(true);
        dateFin.setVisible(true);
        btnAceptar.setVisible(true);
        
        
    }
    @FXML public void borrar() throws BusinessLogicException{
        OfertaBean ofertaBorrar = null;
        LOGGER.info("he clicado remplazar fila");
        int indiceFilaSeleccionada = tablaGestorOfertas.getSelectionModel().getSelectedIndex();
        
        if(tablaGestorOfertas.getSelectionModel().getSelectedIndex()>=0 && tablaGestorOfertas.getSelectionModel().getSelectedIndex()<ofertas.size()){
            ofertaBorrar = ofertas.get(indiceFilaSeleccionada);
            ofertaLogic.borrarOferta(ofertaBorrar.getIdOferta());
            tablaGestorOfertas.getItems().remove(tablaGestorOfertas.getSelectionModel().getSelectedItem());
            tablaGestorOfertas.refresh();

        }
    }
    @FXML public void aceptar() throws BusinessLogicException{
        Date fFin  = localDateToDate(dateInicio.getValue());
        Date fInicio  = localDateToDate(dateFin.getValue());
        String oofertaNombre  = txtOfertaNombre.getText();
        Float descuento  = Float.valueOf(txtDescuento.getText());
        LOGGER.info(Float.valueOf(txtDescuento.getText()).toString());
        switch(opcion){
            case 1:
                OfertaBean ofertaMOdificada = null;
                //ObservableSet<PackBean> comboPacks = null;
                
                
                int indiceFilaSeleccionada = tablaGestorOfertas.getSelectionModel().getSelectedIndex();
                ofertaMOdificada = ofertas.get(indiceFilaSeleccionada);
                ofertaMOdificada.setFechaFin(fFin);
                ofertaMOdificada.setFechaInicio(fInicio);
                ofertaMOdificada.setRebaja(descuento);
                ofertaMOdificada.setTitulo(oofertaNombre);
                
                ofertaLogic.actualizarOferta(ofertaMOdificada);
                ofertas.set(indiceFilaSeleccionada, ofertaMOdificada);
                txtOfertaNombre.setText("");
                txtDescuento.setText("");
                dateInicio.setValue(null);
                dateFin.setValue(null);
                //  btnAceptar.setVisible(false);
                tablaGestorOfertas.refresh();
                opcion = 0;
                break;
            case 2:
                OfertaBean nuevaOferta = new OfertaBean();
               //nuevaOferta.setIdOferta(null);
                LOGGER.info("-------------------------"+nuevaOferta.getIdOferta().toString());
                // nuevaOferta = new OfertaBean(, oofertaNombre, fInicio, fFin, null, descuento);
                //ofertaMOdificada = ofertas.get(indiceFilaSeleccionada);
                //nuevaOferta.setIdOferta(null);
                nuevaOferta.setFechaFin(fFin);
                nuevaOferta.setFechaInicio(fInicio);
                nuevaOferta.setRebaja(descuento);
                nuevaOferta.setTitulo(oofertaNombre);
                LOGGER.info(nuevaOferta.getIdOferta().toString());
                ofertaLogic.createOferta(nuevaOferta);
                //ofertas.add(nuevaOferta);
                ofertas = FXCollections.observableArrayList(ofertaLogic.todasOfertas());
                tablaGestorOfertas.setItems(ofertas);
                tablaGestorOfertas.refresh();
                txtOfertaNombre.setText("");
                txtDescuento.setText("");
                dateInicio.setValue(null);
                dateFin.setValue(null);
                opcion = 0;
                
                break;
                
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
    private void onActionAbout(ActionEvent event){
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
