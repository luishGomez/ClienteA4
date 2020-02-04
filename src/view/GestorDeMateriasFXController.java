package view;

import businessLogic.ApunteManager;
import businessLogic.ApunteManagerFactory;
import businessLogic.BusinessLogicException;
import businessLogic.MateriaManager;
import businessLogic.MateriaManagerFactory;
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
import transferObjects.MateriaBean;
import transferObjects.UserBean;
import static view.ControladorGeneral.showErrorAlert;
import static view.ControladorGeneral.showWarningAlert;

/**
 * El controlador de la ventana GestorMateriaFx para gestionar las materias.
 * @author Luis
 */
public class GestorDeMateriasFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.GestorDeMateriasFXController");
    private UserBean user;
    private Stage stage;
    private MateriaManager manager = MateriaManagerFactory.createMateriaManager("real");
    private Set<MateriaBean> materias = null;
    private ObservableList<MateriaBean> materiasObv = null;
    private int opcion;
    private MateriaBean materia = null;
    
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
    private Button btnCrearGestorMateria;
    @FXML
    private Button btnInformeGestorMateria;
    @FXML
    private TextField tfFiltrarGestorMateria;
    @FXML
    private Button btnBuscarGestorMateria;
    @FXML
    private TableView tablaMateria;
    @FXML
    private TableColumn cId;
    @FXML
    private TableColumn cTitulo;
    @FXML
    private TableColumn cDescripcion;
    @FXML
    private TextField tfTituloGestorMateria;
    @FXML
    private TextField tfDescripcionGestorMateria;
    @FXML
    private Button btnModificarGestorMateria;
    @FXML
    private Button btnEliminarGestorMateria;
    
    /**
     * Método que le da un valor a stage.
     * @param stage Valor de stage.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /**
     * Método que le da un valor a user.
     * @param user Valor de usuario.
     */
    public void setUser(UserBean user){
        this.user = user;
    }
    /**
     * Método que le da un valor a opcion.
     * @param opcion Valor de opcion.
     */
    public void setOpc(int opcion){
        this.opcion = opcion;
    }
    
    /**
     * Método que inicializa la ventana.
     * @param root Nodo raiz.
     */
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando la ventana Gestor De Materias");
            Scene scene=new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Gestor de Materia");
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.setOnShowing(this::handleWindowShowing);
            //Menu
            menuCuenta.setMnemonicParsing(true);
            menuCuenta.setText("_Cuenta");
            menuVentanas.setMnemonicParsing(true);
            menuVentanas.setText("_Ventanas");
            menuHelp.setMnemonicParsing(true);
            menuHelp.setText("_Help");
            //Tabla
            cId.setCellValueFactory(new PropertyValueFactory<>("idMateria"));
            cTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            cDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            cargarDatos();
            tablaMateria.getSelectionModel().selectedItemProperty().addListener(this::materiaClicked);
            stage.show();
        }catch(Exception e){
            LOGGER.severe("Error(initStage)" + e.getMessage());
        }
    }
    /**
     * Método que se ejecuta cuando se muestra la ventana.
     * @param event Evento que se ha lanzado.
     */
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handleWindowShowing -> Gestor De Materias");
            tfFiltrarGestorMateria.requestFocus();
        }catch(Exception e){
            LOGGER.severe("Error(handleWindowShowing)" + e.getMessage());
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
    
    /**
     * Método que se ejecuta cuando se hace click en el botón btnCrearGestorMateria.
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionCrearGestorMateria(ActionEvent event){
        MateriaBean materia = new MateriaBean();
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("crear_materia.fxml"));
            Parent root = (Parent)loader.load();
            CrearMateriaFXController controller =
                    ((CrearMateriaFXController)loader.getController());
            controller.setFXMateria(this);
            controller.setMateria(materia);
            controller.initStage(root);
            if(opcion == 1){
                createMateria(materia);
                cargarDatos();
                tablaMateria.refresh();
            }
        }catch(Exception e){
            LOGGER.severe("Error al intentar abrir la ventana(onActionCrearGestorMateria): " + e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor.");
        }
    }
    /**
     * Método que se ejecuta cuando se hace click en el botón btnInformeGestorMateria.
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionInformeGestorMateria(ActionEvent event){
        
    }
    /**
     * Método que se ejecuta cuando se hace click en el botón btnBuscarGestorMateria.
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionBuscarGestorMateria(ActionEvent event){
        if(!tfFiltrarGestorMateria.getText().trim().isEmpty()){
            cargarDatos(tfFiltrarGestorMateria.getText().trim());
        }else{
            cargarDatos();
        }
    }
    /**
     * Método que se ejecuta cuando se hace click en el botón btnModificarGestorMateria.
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionModificarGestorMateria(ActionEvent event){
        if(materia != null){
            if(tfTituloGestorMateria.getText().trim().isEmpty() || tfDescripcionGestorMateria.getText().trim().isEmpty()){
                showErrorAlert("Los campos de texto no pueden estar vacios.");
            }else{
                MateriaBean mat = new MateriaBean(materia.getIdMateria(),
                        tfTituloGestorMateria.getText().trim(),
                        tfDescripcionGestorMateria.getText().trim());
                if(!(mat.getTitulo().equals(materia.getTitulo()) && mat.getDescripcion().equals(materia.getDescripcion()))){
                    try{
                        manager.editMateria(mat);
                        cargarDatos();
                        tablaMateria.refresh();
                        tfTituloGestorMateria.setText("");
                        tfDescripcionGestorMateria.setText("");
                        materia = null;
                    }catch(BusinessLogicException e){
                        LOGGER.severe("Error al intentar editar una materia(onActionModificarGestorMateria): " + e.getMessage());
                        showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor. " + e.getMessage());
                    }
                }
            }
        }else{
            showWarningAlert("Seleccione una materia, después, vuelva a pulsar el botón.");
        }
    }
    /**
     * Método que se ejecuta cuando se hace click en el botón btnEliminarGestorMateria.
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionEliminarGestorMateria(ActionEvent event){
        if(materia != null){
            Alert alertCerrarAplicacion = new Alert(Alert.AlertType.CONFIRMATION,"",ButtonType.NO,ButtonType.YES);
            //Añadimos titulo a la ventana como el alert.
            alertCerrarAplicacion.setTitle("Eliminar");
            alertCerrarAplicacion.setHeaderText("¿Estas seguro que lo deseas eliminar?");
            //Si acepta cerrara la aplicación.
            alertCerrarAplicacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try{
                        if(comprobar(this.materia)){
                            manager.removeMateria(this.materia);
                            tablaMateria.getItems().remove(this.materia);
                            tablaMateria.refresh();
                            tfTituloGestorMateria.setText("");
                            tfDescripcionGestorMateria.setText("");
                        }else{
                            showWarningAlert("No puedes borrar esta materia, existen apuntes perteneciente a ella.");
                        }
                    }catch(BusinessLogicException e){
                        LOGGER.severe("Error al intentar borrar una materia(onActionEliminarGestorMateria): " + e.getMessage());
                        showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor. " + e.getMessage());
                    }
                }
            });
        }else{
            showWarningAlert("Seleccione una materia, después, vuelva a pulsar el botón.");
        }
    }
    /**
     * Método para cargar todas las materias.
     */
    private void cargarDatos() {
        try {
            materias = manager.findAllMateria();
            List<MateriaBean> matList = materias.stream().sorted(Comparator.comparing(MateriaBean::getTitulo)).collect(Collectors.toList());
            materiasObv = FXCollections.observableArrayList(new ArrayList<>(matList));
            tablaMateria.setItems(materiasObv);
        }catch (BusinessLogicException e) {
            LOGGER.severe("Error al intentar cargar las materias(cargarDatos()): " + e.getMessage());
            showErrorAlert("Ha ocurrido un error cargando las materias.");
        }
    }
    /**
     * Método para cargar todas las materias y las filtra según el parámetro.
     * @param string Cadena de caracteres a filtrar.
     */
    private void cargarDatos(String string) {
        try {
            materias = manager.findAllMateria();
            List<MateriaBean> matList = materias.stream().filter(materia -> materia.getTitulo().toLowerCase().contains(string.toLowerCase())).sorted(Comparator.comparing(MateriaBean::getIdMateria)).collect(Collectors.toList());
            materiasObv = FXCollections.observableArrayList(new ArrayList<>(matList));
            tablaMateria.setItems(materiasObv);
        }catch (BusinessLogicException e) {
            LOGGER.severe("Error al intentar cargar las materias(cargarDatos(String s)): " + e.getMessage());
            showErrorAlert("Ha ocurrido un error cargando las materias.");
        }
    }
    /**
     * Método que se ejecuta cuando cambia algún valor de una fila de la tabla.
     * @param obvservable Valor observable.
     * @param oldValue Valor antiguo.
     * @param newValue Valor nuevo.
     */
    private void materiaClicked(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue != null){
            materia = (MateriaBean) newValue;
            tfTituloGestorMateria.setText(materia.getTitulo());
            tfDescripcionGestorMateria.setText(materia.getDescripcion());
            LOGGER.info("Método materiaClicked");
        }
    }
    /**
     * Método que utiliza el manager para crear la materia.
     * @param materia Objeto materia para crear.
     */
    private void createMateria(MateriaBean materia){
        try{
            manager.createMateria(materia);
        }catch(BusinessLogicException e){
            LOGGER.severe("Error al intentar crear una materia(createMateria): " + e.getMessage());
            showErrorAlert("Ha ocurrido un error.");
        }
    }
    /**
     * Método para comprobar si existe algún apunte en la materia que se desea eliminar.
     * @param materia Objeto materia para filtrar.
     * @return Valor true si se puede borrar la materia.
     */
    private boolean comprobar(MateriaBean materia){
        ApunteManager managerApunte = ApunteManagerFactory.createApunteManager("real");
        boolean estaVacia = false;
        try{
            List<ApunteBean> apuntes = (List<ApunteBean>) managerApunte.findAll().stream().filter(apunte -> apunte.getMateria().getIdMateria() == materia.getIdMateria()).collect(Collectors.toList());
            if(apuntes.size() == 0){
                estaVacia = true;
            }
        }catch(BusinessLogicException e){
            LOGGER.severe("Error al intentar cargar los apuntes(comprobar): " + e.getMessage());
            showErrorAlert("Ha ocurrido un error.");
        }
        return estaVacia;
    }
}