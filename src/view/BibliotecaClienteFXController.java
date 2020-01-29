/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package view;

import businessLogic.ApunteManager;
import static businessLogic.ApunteManagerFactory.createApunteManager;
import businessLogic.BusinessLogicException;
import businessLogic.ClienteManager;
import static businessLogic.ClienteManagerFactory.createClienteManager;
import businessLogic.MateriaManager;
import static businessLogic.MateriaManagerFactory.createMateriaManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import transferObjects.ApunteBean;
import transferObjects.ClienteBean;
import transferObjects.MateriaBean;
import static view.ControladorGeneral.showErrorAlert;

/**
 *
 * @author Sergio
 */
public class BibliotecaClienteFXController{
    private static final Logger LOGGER = Logger.getLogger("escritorio.view.MisPuntesClienteFXController");
    public ClienteManager clienteLogic = createClienteManager("real");
    private final ApunteManager apuntesLogic = createApunteManager("real");
    private final MateriaManager materiaLogic = createMateriaManager("real");
    private ClienteBean user;
    private Stage stage;
    private Boolean voyAVotar = false;
    private Parent root;
    private ObservableList<ApunteBean>  apuntes;
    private ObservableList<MateriaBean>  materias;
    private ObservableList<String>  materias_titulo = FXCollections.observableArrayList();
    
    @FXML private TableView<ApunteBean> tablaBiblioteca;
    @FXML private TableColumn columnaTitulo;
    @FXML private TableColumn columnaMateria;
    @FXML private TableColumn columnaAutor;
    @FXML private TableColumn columnaDescripcion;
    @FXML private TextField txtBuscar;
    @FXML private Text txtDescripcion;
    @FXML private ComboBox comboFiltroBiblioteca;
    @FXML private ImageView imgLike;
    @FXML private ImageView imgDislike;
    @FXML private ImageView imgDescarga;
    
    
    private final ContextMenu popupMenu = new ContextMenu();; //llamado tmb popup-menu, es decir lo declaramos
    private final MenuItem menu1 = new MenuItem("Añadir");
    private final Menu menu2 = new Menu("Borrar Fila");
    private final MenuItem submenu1 = new MenuItem("Fila seleccionada");
    private final MenuItem submenu2 = new MenuItem("todas las filas");
    private final MenuItem menu3 = new MenuItem("Modificar");
    
    public void setStage(Stage stage){
        this.stage= stage;
    }
    public void setUser(ClienteBean user){
        this.user=user;
    }
    
    
    public void initStage(Parent root) throws BusinessLogicException{
        stage = new Stage();
        Scene scene = new Scene(root);
        this.root = root;
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Mi Biblioteca");
        //popup-menu items definiendolas y añadiendolas al popup
        popupMenu.getItems().add(menu1);
        popupMenu.getItems().add(menu2);
        menu2.getItems().add(submenu1);
        menu2.getItems().add(submenu2);
        popupMenu.getItems().add(new SeparatorMenuItem());//añadimos linea separadora entre los items
        popupMenu.getItems().add(menu3);
        //--Tabla
        comboFiltroBiblioteca.getSelectionModel().selectedItemProperty().addListener(this::comboControl);
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        columnaMateria.setCellValueFactory(new PropertyValueFactory<>("materia"));
        columnaAutor.setCellValueFactory(new PropertyValueFactory<>("creador"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        apuntes = FXCollections.observableArrayList(apuntesLogic.getApuntesByComprador(user.getId()));
        tablaBiblioteca.setItems(apuntes);
        //combo
        materias = FXCollections.observableArrayList(materiaLogic.findAllMateria());
        materias_titulo.add("");
        materias.forEach((materia) -> {
            LOGGER.info(materia.getTitulo());
            materias_titulo.add(materia.getTitulo());
        });
        comboFiltroBiblioteca.setItems(materias_titulo);
        //--Events
        tablaBiblioteca.addEventHandler(MouseEvent.MOUSE_CLICKED, this::puntuacion);
        tablaBiblioteca.addEventHandler(MouseEvent.MOUSE_CLICKED, this::ocultarPopup);
        stage.addEventHandler(MouseEvent.MOUSE_CLICKED, this::clicks);
        imgLike.addEventHandler(MouseEvent.MOUSE_CLICKED, this::puntuacion);
        imgDislike.addEventHandler(MouseEvent.MOUSE_CLICKED, this::puntuacion);
        imgDescarga.addEventHandler(MouseEvent.MOUSE_CLICKED, this::puntuacion);
        
        
        stage.show();
    }
    /**
     * cerrado del PopupMenu
     * @param m
     */
    public void ocultarPopup(MouseEvent m){
        popupMenu.hide();
    }
    /**
     * Control de los click dados en la pantalla
     * @param m
     */
    public void clicks(MouseEvent m){
        if(m.getButton()== MouseButton.SECONDARY)
            popupMenu.show(root, m.getScreenX(),m.getScreenY());
        else{
            if(!voyAVotar){
                tablaBiblioteca.getSelectionModel().select(null);
                txtDescripcion.setText("");
                imgLike.setDisable(true);
                imgLike.setVisible(false);
                imgDislike.setDisable(true);
                imgDislike.setVisible(false);
                imgDescarga.setDisable(true);
                imgDescarga.setVisible(false);
            }
            popupMenu.hide();
            voyAVotar=false;
        }
    }/**
     * Control de las puntuciones de un apunte.
     * @param m
     */
    public void puntuacion(MouseEvent m){
        if(m.getButton()== MouseButton.PRIMARY){
            if(tablaBiblioteca.getSelectionModel().getSelectedIndex()>=0){
                imgLike.setDisable(false);
                imgLike.setVisible(true);
                imgDislike.setDisable(false);
                imgDislike.setVisible(true);
                imgDescarga.setDisable(false);
                imgDescarga.setVisible(true);
                txtDescripcion.setText(tablaBiblioteca.getSelectionModel().getSelectedItem().getDescripcion());
                if(m.getTarget().equals(imgLike)){
                    voyAVotar=true;
                    if(!coprobarVoto()){
                        try {
                            apuntesLogic.votacion(user.getId(), 1, tablaBiblioteca.getSelectionModel().getSelectedItem());
                        } catch (BusinessLogicException ex) {
                            Logger.getLogger(BibliotecaClienteFXController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                else if(m.getTarget().equals(imgDislike)){
                    voyAVotar=true;
                    if(!coprobarVoto()){
                        try {
                            apuntesLogic.votacion(user.getId(), -1, tablaBiblioteca.getSelectionModel().getSelectedItem());
                        } catch (BusinessLogicException ex) {
                            Logger.getLogger(BibliotecaClienteFXController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                else if(m.getTarget().equals(imgDescarga)){
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter =new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File fileC = fileChooser.showSaveDialog(stage);
                    
                    ApunteBean elApunte =tablaBiblioteca.getSelectionModel().getSelectedItem();
                    writeBytesToFile(elApunte.getArchivo(),fileC);
                }
            }
        }
        
    }
    /**
     * Comprueba que no haya votado ya antes a ese apunte
     * @return TRUE si ya ha votado || FALSE si no ha votado
     */
    private Boolean coprobarVoto() {
        Boolean votado = false;
        try {
            Set<ClienteBean> clientes=clienteLogic.getVotantesId(tablaBiblioteca.getSelectionModel().getSelectedItem().getIdApunte());
            if(clientes!=null){
                for(ClienteBean cliente:clientes){
                    if(cliente.getId().equals(user.getId()))
                        votado = true;
                }
            }
            
        } catch (BusinessLogicException ex) {
            Logger.getLogger(BibliotecaClienteFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return votado;
    }
    /**
     * Guarda el archivo deseado en la ruta deseada
     * @param archivo Apunte en bytes
     * @param fileC Archivo del cual sacamos la ruta de almacenaje
     */
    private void writeBytesToFile(byte[] archivo, File fileC) {
        FileOutputStream fileOuputStream = null;
        try {
            if(fileC!=null){
                fileOuputStream = new FileOutputStream(fileC.getPath());
                fileOuputStream.write(archivo);
            }
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
     * Control del combo en caso de filtrar por materias.
     * @param obvservable
     * @param oldValue
     * @param newValue
     */
    private void comboControl(ObservableValue obvservable, Object oldValue, Object newValue){
        ObservableList<ApunteBean> apuntesFiltrados = FXCollections.observableArrayList();
        if(newValue !=null){
            apuntes.stream().filter((apunte) -> (apunte.getMateria().toString().toUpperCase().equals(newValue.toString().toUpperCase()))).forEachOrdered((apunte) -> {
                apuntesFiltrados.add(apunte);
            });
            if(apuntesFiltrados.size()>0){
                tablaBiblioteca.setItems(apuntesFiltrados);
                tablaBiblioteca.refresh();
            }
        }
        if(comboFiltroBiblioteca.getSelectionModel().getSelectedIndex()==0){
            tablaBiblioteca.setItems(apuntes);
            tablaBiblioteca.refresh();
        }
    }
    /**
     * Filtra los apuntes segun la palabra o palabras a buscar.
     * @param event
     */
    @FXML private void buscar(ActionEvent event){
        String palabraBusqueda;
        ObservableList<ApunteBean> apuntesFiltrados = FXCollections.observableArrayList();
        if(txtBuscar.getText().isEmpty()){
            tablaBiblioteca.setItems(apuntes);
            tablaBiblioteca.refresh();
        }
        else{
            palabraBusqueda = txtBuscar.getText().toUpperCase();
            apuntes.stream().filter((apunte) -> (apunte.getTitulo().toUpperCase().contains(palabraBusqueda) ||
                    apunte.getMateria().toString().toUpperCase().contains(palabraBusqueda)||
                    apunte.getCreador().toString().toUpperCase().contains(palabraBusqueda) ||
                    apunte.getDescripcion().toUpperCase().contains(palabraBusqueda))).map((apunte) -> {
                        apuntesFiltrados.add(apunte);
                        return apunte;
                    });
            if(apuntesFiltrados.size()>0){
                tablaBiblioteca.setItems(apuntesFiltrados);
                tablaBiblioteca.refresh();
            }
            
        }
    }
    //Inicio de los metodos de navegación de la aplicación
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
            
            controller.setCliente(user);
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
            
            controller.setCliente(user);
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
            controller.setUser(user);
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
            controller.setCliente(user);
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
            controller.setUser(user);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        } catch (IOException ex) {
            Logger.getLogger(TiendaApuntesFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void onActionAbout(ActionEvent event){
    }
    //Fin de los metodos de navegación de la aplicación
    
    
    
}
