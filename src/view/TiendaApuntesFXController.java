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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ApunteBean;
import transferObjects.ClienteBean;
import transferObjects.MateriaBean;
import static view.ControladorGeneral.showErrorAlert;

/**
 *
 * @author Usuario
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
            //<-Menu
            btnRefrescar.setMnemonicParsing(true);
            btnRefrescar.setText("_Refrescar");
            btnComprar.setMnemonicParsing(true);
            btnComprar.setText("C_omprar");
            
            //CARGAR MATERIAS
            cargarMaterias();
            //DEJARLO EN TODAS LAS MATERIAS
            
            //CARGAR LISTVIEW DE PAUNTES
            cargarApuntes();
            //CARGAR EL COMBO BOX
            cargarComboBox();
            //DEJARLO EN EL PRIMERO
            
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
    private void keyPressBuscar(KeyEvent key){
        if(key.getCode().equals(KeyCode.ENTER)) {
            btnBuscar.fire();
        }
        
    }
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> LogOut");
            textFieldBuscar.requestFocus();
            
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    
    
    @FXML
    private void onActionRefrescar(ActionEvent event){
        cargarMaterias();
        cargarApuntes();
        cargarComboBox();
        this.listViewMateria.getSelectionModel().select(0);
        this.comboBoxOrdenar.getSelectionModel().select(0);
        this.textFieldBuscar.setText("");
        filtrarLosApuntes();
    }
    @FXML
    private void onActionBuscar(ActionEvent event){
        filtrarLosApuntes();
    }
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
            }catch(IOException e){
                showErrorAlert("Error al cargar la ventana de Login.");
                LOGGER.severe("Error "+e.getMessage());
                
            }
            
        }else{
            showErrorAlert("No has seleccionado ninguna apunte para comprar.");
        }
        
    }
    
    //Parte comun
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
    }
    @FXML
    private void onActionAbrirMiPerfil(ActionEvent event){
    }
    @FXML
    private void onActionAbout(ActionEvent event){
    }
    //Fin de los metodos de navegación de la aplicación
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setCliente(ClienteBean cliente){
        this.cliente=cliente;
    }
    
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
    
    private void cargarApuntes() {
        try {
            apuntes=apunteLogic.findAll();//SE PUDE BAJAR
            
            //List <ApunteBean> apuntesList = Lists.newArrayList(apuntes);
            Set <ApunteBean> apuntesNoCreadosPorMi = new HashSet <ApunteBean>();
            Set <ApunteBean> apuntesComprados = apunteLogic.getApuntesByComprador(cliente.getId());
            apuntesDeTienda = new HashSet <ApunteBean>(); //PASARLO A GLOBAL
            //apuntesList=apuntesList.stream().filter(apunte -> apunte.getIdApunte()==)
            
            apuntes.stream().forEach(apunte ->{
                if(apunte.getCreador().getId()!=cliente.getId()){
                    apuntesNoCreadosPorMi.add(apunte);
                }
            });
            apuntesNoCreadosPorMi.stream().forEach(apunte ->{
                boolean comprado=false;
                for(ApunteBean apunteComprado:apuntesComprados){
                    if(apunte==apunteComprado){
                        comprado=true;
                        break;
                    }
                }
                if(!comprado)
                    apuntesDeTienda.add(apunte);
            });
            
            //ApunteBean apunte=apunteLogic.find(4);
            //MateriaRESTClient mrc=new MateriaRESTClient();
            //MateriaBean materia=mrc.find(MateriaBean.class, "2");
            
            apuntesData=FXCollections.observableArrayList(new ArrayList<>(apuntesDeTienda));
            this.listViewApuntes.setItems(apuntesData);
            this.listViewApuntes.setCellFactory(param -> new CellTiendaApunte());
            //ArrayList <ApunteBean> apuntesInfo=new ArrayList<>(apuntes);
            //LOGGER.info("INf. "+apuntesInfo.get(0).getCreador().getNombreCompleto());
        } catch (BusinessLogicException ex) {
            LOGGER.severe("Error al intentar cargar los apuntes :"+ex.getMessage());
            showErrorAlert("No se ha podido cargar los apuntes");
        }
    }
    
    private void cargarComboBox() {
        opcionesData=FXCollections.observableArrayList(Arrays.asList(this.opciones));
        this.comboBoxOrdenar.setItems(opcionesData);
        
    }
    
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
    
    private void handleOpcionesComboBoxSelectionChanged(ObservableValue obvservable, Object oldValue, Object newValue){
        if(newValue!=null && newValue!=oldValue){
            this.tipoFiltrado=(String) newValue;
            ordenarApuntes();
        }
    }
    
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
}
