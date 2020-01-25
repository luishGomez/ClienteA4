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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ApunteBean;
import transferObjects.ClienteBean;
import transferObjects.MateriaBean;
import static view.ControladorGeneral.showErrorAlert;
import static view.MisApuntesClienteFXController.setResultadoApunteModificado;

/**
 * La clase controladora de subir apunte.
 * @author Ricardo Peinado Lastra
 */
public class SubirApunteFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.SubirApunteFXController");
    private ClienteBean cliente=null;
    private ApunteBean apunte=null;
    private ApunteManager apunteLogic = createApunteManager("real");
    private Stage stage;
    private MateriaManager materiaLogic = createMateriaManager("real");
    private ObservableList<MateriaBean> materiasData=null;
    private final int MAX_PRECIO=100000;
    private final float MIN_PRECIO=(float) 0.30;
    private File selectedFile =null;
    
    @FXML
    private Label lblPrecio;
    @FXML
    private Label lblDesc;
    @FXML
    private Label lblMateria;
    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblArchivo;
    @FXML
    private TextField textFieldTitulo;
    @FXML
    private TextField textFieldPrecio;
    @FXML
    private TextArea textDesc;
    @FXML
    private ComboBox comboBoxMateria;
    @FXML
    private Label labelRuta;
    @FXML
    private Button btnSeleccionarArchivo;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnSubirElApunte;
    
    /**
     * Inicializa la ventana subir apunte.
     * @param El nodo raiz.
     */
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando la ventana SubirApunteFXController");
            Scene scene=new Scene(root);
            stage=new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Subir Apunte");
            stage.setResizable(false);
            stage.setOnShowing(this::handleWindowShowing);
            
            cargarMaterias();
            
            btnSubirElApunte.setMnemonicParsing(true);
            btnSubirElApunte.setText("_Subir apunte");
            btnCancelar.setMnemonicParsing(true);
            btnCancelar.setText("_Cancelar");
            btnSeleccionarArchivo.setMnemonicParsing(true);
            btnSeleccionarArchivo.setText("S_eleccionar archivo");
            labelRuta.setText("");
            
            stage.showAndWait();
        }catch(Exception e){
            LOGGER.severe("Erro al abrira la ventan de SubirApunteFXController: "+e.getMessage());
        }
    }
    /**
     * El metodo que se ejecuta al mostrar la ventana y selecciona el foco.
     * @param event El propio evento.
     */
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> LogOut");
            textFieldTitulo.requestFocus();
            
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    /**
     * Inserta el cliente.
     * @param cliente Un objeto ClienteBean con sus datos.
     */
    public void setCliente(ClienteBean cliente){
        this.cliente=cliente;
    }
    /**
     * Comprueba y sube el apunte.
     * @param event El evento de pulsación del botón.
     */
    @FXML
    private void onActionSubirElApunte(ActionEvent event){
        try{
            boolean todoBien=true;
            if(!precioValido()){
                todoBien=false;
            }
            if(this.labelRuta.getText().trim().equals("")){
                todoBien=false;
                this.lblArchivo.setText("Archivo (Tienes que seleccionar algun pdf para subir un apunte)");
                this.lblArchivo.setTextFill(Color.web("red"));
            }else{
                this.lblArchivo.setText("Archivo");
                this.lblArchivo.setTextFill(Color.web("black"));
            }
            if(!esValido(this.textFieldTitulo.getText().trim(),3,250)){
                this.lblTitulo.setText("Titulo (Min 3 car. | Max 250 car.)");
                this.lblTitulo.setTextFill(Color.web("red"));
                todoBien=false;
            }else{
                this.lblTitulo.setText("Titulo");
                this.lblTitulo.setTextFill(Color.web("black"));
            }
            if(!esValido(this.textDesc.getText().trim(),3,250)){
                this.lblDesc.setText("Descripción (Min 3 car. | Max 250 car.)");
                this.lblDesc.setTextFill(Color.web("red"));
                todoBien=false;
            }else{
                this.lblDesc.setText("Descripción");
                this.lblDesc.setTextFill(Color.web("black"));
            }
            if(this.comboBoxMateria.getSelectionModel().getSelectedItem()==null){
                this.lblMateria.setText("Materia (Tienes que seleccionar una materia)");
                this.lblMateria.setTextFill(Color.web("red"));
                todoBien=false;
            }else{
                this.lblMateria.setText("Materia");
                this.lblMateria.setTextFill(Color.web("black"));
            }
            if(todoBien){
                ApunteBean nuevoApunte = new ApunteBean();
                nuevoApunte.setTitulo(this.textFieldTitulo.getText().trim());
                nuevoApunte.setDescripcion(this.textDesc.getText().trim());
                String elPrecio =this.textFieldPrecio.getText();
                elPrecio=elPrecio.replace(",", ".");
                float precio=Float.parseFloat(elPrecio);
                nuevoApunte.setPrecio(precio);
                nuevoApunte.setMateria((MateriaBean) this.comboBoxMateria.getSelectionModel().getSelectedItem());
                nuevoApunte.setCreador(cliente);
                
                FileInputStream fileInputStream = null;
                try{
                    byte[] bFile = new byte[(int) selectedFile.length()];
                    fileInputStream = new FileInputStream(selectedFile);
                    fileInputStream.read(bFile);
                    nuevoApunte.setArchivo(bFile);
                }catch(IOException e){
                    LOGGER.severe("Error al leer los bytes "+e.getMessage());
                    todoBien=false;
                } finally {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(todoBien){
                    Alert alertCerrarSesion = new Alert(Alert.AlertType.CONFIRMATION);
                    alertCerrarSesion.setTitle("Subir apunte");
                    alertCerrarSesion.setHeaderText("Una vez subido y comprado por alguna persona el apunte no se podra eliminarlo.\n ¿Estas segur@ de que todos los datos son correctos?");
                    //Si acepta se cerrara esta ventana.
                    alertCerrarSesion.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                apunteLogic.create(nuevoApunte);
                                stage.hide();
                                setResultadoApunteModificado(1);
                            } catch (BusinessLogicException e) {
                                LOGGER.severe("Error al crear el apunte "+e.getMessage());
                            }
                        }
                    });
                    
                }
                
            }
        }catch(Exception e){
            LOGGER.severe("Error al subir un apunte: "+e.getMessage());
            showErrorAlert("A ocurrido un error al subir el apunte.");
        }
        
    }
    /**
     * El metodo que cancela la subida de un apunte.
     * @param event El evento de pulsación del botón.
     */
    @FXML
    private void onActionCancelar(ActionEvent event){
        Alert alertCerrarSesion = new Alert(Alert.AlertType.CONFIRMATION);
        alertCerrarSesion.setTitle("Cerrar subir apunte");
        alertCerrarSesion.setHeaderText("Si cierras esta ventana se perdera todo lo que hayas escrito.");
        //Si acepta se cerrara esta ventana.
        alertCerrarSesion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                stage.hide();
                setResultadoApunteModificado(0);
            }
        });
    }
    /**
     * El metodo que selecciona el archivo.
     * @param event El evento de pulsaciónd del botón.
     */
    @FXML
    private void onActionSeleccionarArchivo(ActionEvent event){
        try{
            FileChooser fc=new FileChooser();
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Archivos PDF","*.PDF"));
            selectedFile = fc.showOpenDialog(null);
            if(selectedFile!=null){
                this.labelRuta.setText(selectedFile.getPath());
                
            }else{
                this.labelRuta.setText("");
            }
        }catch(Exception e){
            LOGGER.severe("Error al seleccionar un apunte: "+e.getMessage());
            showErrorAlert("A ocurrido un error al intentar leer el apunte.");
        }
    }
    /**
     * Carga todas las materias en el comboBox.
     */
    private void cargarMaterias() {
        try {
            Set<MateriaBean> materias = materiaLogic.findAllMateria();
            materiasData=FXCollections.observableArrayList(new ArrayList<>(materias));
            this.comboBoxMateria.setItems(materiasData);
            
        } catch (BusinessLogicException ex) {
            LOGGER.severe("Error al cargar las materias: "+ex.getMessage());
        }
    }
    /**
     * Valida que el precio sea correcto.
     * @return TRUE si es un precio valido | FALSE en los demas casos.
     */
    private boolean precioValido() {
        boolean resultado=true;
        this.lblPrecio.setText("Precio");
        this.lblPrecio.setTextFill(Color.web("black"));
        try{
            String elPrecio =this.textFieldPrecio.getText();
            elPrecio=elPrecio.replace(",", ".");
            float precio=Float.parseFloat(elPrecio);
            if(elPrecio.indexOf(".")!=-1){
                if((elPrecio.length()-elPrecio.indexOf("."))>3){
                    this.lblPrecio.setText("Precio (No puede tener más de dos decimales)");
                    this.lblPrecio.setTextFill(Color.web("red"));
                    resultado=false;
                }
            }
            if(MAX_PRECIO-precio<0){
                this.lblPrecio.setText("Precio (No puede superar los "+MAX_PRECIO+"€)");
                this.lblPrecio.setTextFill(Color.web("red"));
                resultado=false;
            }else if(precio-MIN_PRECIO<0){
                this.lblPrecio.setText("Precio (No puede valer menos de  "+MIN_PRECIO+"€)");
                this.lblPrecio.setTextFill(Color.web("red"));
                resultado=false;
            }
            
        }catch(Exception e){
            resultado=false;
            this.lblPrecio.setText("Precio (Tiene que ser un valor numerico)");
            this.lblPrecio.setTextFill(Color.web("red"));
        }
        return resultado;
    }
    /**
     * Valida si es o no valido una cadena de caracteres.
     * @param frase La cadena de caracteres.
     * @param minimo El minimo de caracteres.
     * @param maximo El maximo de caracteres.
     * @return
     */
    public boolean esValido(String frase,int minimo, int maximo){
        boolean resultado=true;
        if(frase.length()>maximo || frase.length()<minimo)
            resultado=false;
        return resultado;
    }
    
}
