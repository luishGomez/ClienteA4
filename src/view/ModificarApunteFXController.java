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
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ApunteBean;
import transferObjects.MateriaBean;
import static view.ControladorGeneral.showErrorAlert;
import static view.MisApuntesClienteFXController.setResultadoApunteModificado;

/**
 * La clase controladora de la ventana ModificarApunte.
 * @author Ricardo Peinado Lastra
 */
public class ModificarApunteFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.ModificarApunteFXController");
    private ApunteBean apunte=null;
    private ApunteManager apunteLogic = createApunteManager("real");
    private Stage stage;
    private MateriaManager materiaLogic = createMateriaManager("real");
    private ObservableList<MateriaBean> materiasData=null;
    private final int MAX_PRECIO=100000;
    private final float MIN_PRECIO=(float) 0.30;
    private File selectedFile =null;
    
    @FXML
    private Label labelTituloMod;
    @FXML
    private Label labelMateria;
    @FXML
    private Label labelPrecio;
    @FXML
    private ComboBox comboBoxMateriaMod;
    @FXML
    private TextField textFieldTitulo;
    @FXML
    private TextField textFieldPrecio;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnModificarMod;
    @FXML
    private Button btnEliminar;
    /**
     * El metodo que inicializa la ventana ModificarApunte.
     * @param root El nodo raiz.
     */
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando la ventana ModificarApunteFXController");
            Scene scene=new Scene(root);
            stage=new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Modificar Apunte");
            stage.setResizable(false);
            stage.setOnShowing(this::handleWindowShowing);
            
            cargarMaterias();
            
            btnModificarMod.setMnemonicParsing(true);
            btnModificarMod.setText("_Modificar");
            btnCancelar.setMnemonicParsing(true);
            btnCancelar.setText("_Cancelar");
            btnEliminar.setMnemonicParsing(true);
            btnEliminar.setText("_Eliminar");
            comboBoxMateriaMod.getSelectionModel().select(apunte.getMateria());
            textFieldTitulo.setText(apunte.getTitulo());
            textFieldPrecio.setText(apunte.getPrecio()+"");
            stage.showAndWait();
        }catch(Exception e){
            LOGGER.severe("Error al iniciar ModificarApunteFXController: "+e.getMessage());
        }
    }
    /**
     * El metodo que ejecuta el codigo al enseñar la ventana.
     * Pone el foco al textFieldTitulo.
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
     * Inserta el apunte a modificar.
     * @param apunte El objeto ApunteBean con sus datos.
     */
    public void setApunte(ApunteBean apunte){
        this.apunte=apunte;
    }
    /**
     * Carga las materias en el comboBox.
     */
    private void cargarMaterias() {
        try {
            Set<MateriaBean> materias = materiaLogic.findAllMateria();
            materiasData=FXCollections.observableArrayList(new ArrayList<>(materias));
            this.comboBoxMateriaMod.setItems(materiasData);
            
        } catch (BusinessLogicException ex) {
            LOGGER.severe("Error al cargar las materias: "+ex.getMessage());
        }
    }
    /**
     * Metodo que cancela la operación de modificar un apunte.
     * @param event El evento de pulsación del botón.
     */
    @FXML
    private void onActionCancelar(ActionEvent event){
        Alert alertCerrarSesion = new Alert(Alert.AlertType.CONFIRMATION);
        alertCerrarSesion.setTitle("Cerrar modificar apunte");
        alertCerrarSesion.setHeaderText("Si cierras esta ventana se perdera todo lo que hayas escrito.");
        alertCerrarSesion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                stage.hide();
                setResultadoApunteModificado(0);
            }
        });
    }
    /**
     * Metodo que realiza la modificación del evento.
     * @param event El evento de pulsacioón del bot´ón.
     */
    @FXML
    private void onActionModificar(ActionEvent event){
        Alert alertCerrarSesion = new Alert(Alert.AlertType.CONFIRMATION);
        alertCerrarSesion.setTitle("Modificar apunte");
        alertCerrarSesion.setHeaderText("¿Quieres modificar el apunte con los datos que has puesto?");
        alertCerrarSesion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                modificarElApunte();
                setResultadoApunteModificado(1);
            }
        });
    }
    /**
     * Metodo que elimina un apunte.
     * @param event El evento de la pulsación del botón.
     */
    @FXML
    private void onActionEliminar(ActionEvent event){
        Alert alertCerrarSesion = new Alert(Alert.AlertType.CONFIRMATION);
        alertCerrarSesion.setTitle("Eliminar apunte");
        alertCerrarSesion.setHeaderText("¿Estas seguro de eliminar el apunte?");
        alertCerrarSesion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try{
                    if(apunteLogic.cuantasCompras(apunte.getIdApunte())==0){
                        apunteLogic.remove(apunte.getIdApunte());
                        ensenarAlertaInfo("Apunte eliminado","El apunte a sido eliminado");
                        stage.hide();
                        setResultadoApunteModificado(1);
                    }else{
                        showErrorAlert("Lo sentimos el apunte ya a sido comprado\npor un usuario, ya no puedes eliminar el apunte.");
                    }
                }catch(BusinessLogicException e){
                    LOGGER.severe("Error al borrar el apunte :"+e.getMessage());
                }
            }
        });
        
    }
    /**
     * Valida si el campo de precio es valido o no.
     * @return TRUE si es un precio valido | False en los demñas casos.
     */
    private boolean precioValido() {
        boolean resultado=true;
        this.labelPrecio.setText("Precio");
        this.labelPrecio.setTextFill(Color.web("black"));
        try{
            String elPrecio =this.textFieldPrecio.getText();
            elPrecio=elPrecio.replace(",", ".");
            float precio=Float.parseFloat(elPrecio);
            if(elPrecio.indexOf(".")!=-1){
                if((elPrecio.length()-elPrecio.indexOf("."))>3){
                    this.labelPrecio.setText("Precio (No puede tener más de dos decimales)");
                    this.labelPrecio.setTextFill(Color.web("red"));
                    resultado=false;
                }
            }
            if(MAX_PRECIO-precio<0){
                this.labelPrecio.setText("Precio (No puede superar los "+MAX_PRECIO+"€)");
                this.labelPrecio.setTextFill(Color.web("red"));
                resultado=false;
            }else if(precio-MIN_PRECIO<0){
                this.labelPrecio.setText("Precio (No puede valer menos de  "+MIN_PRECIO+"€)");
                this.labelPrecio.setTextFill(Color.web("red"));
                resultado=false;
            }
            
        }catch(Exception e){
            resultado=false;
            this.labelPrecio.setText("Precio (Tiene que ser un valor numerico)");
            this.labelPrecio.setTextFill(Color.web("red"));
        }
        return resultado;
    }
    /**
     * Valida si entra en los valores minmos y maximo una cadena de caracteres.
     * @param frase La cadena.
     * @param minimo Minimo de caracteres.
     * @param maximo Maximo de caracteres.
     * @return
     */
    public boolean esValido(String frase,int minimo, int maximo){
        boolean resultado=true;
        if(frase.length()>maximo || frase.length()<minimo)
            resultado=false;
        return resultado;
    }
    /**
     * Metodo para modificar el apunte.
     */
    private void modificarElApunte() {
        boolean todoBien=true;
        if(!precioValido()){
            todoBien=false;
        }
        if(!esValido(this.textFieldTitulo.getText().trim(),3,250)){
            this.labelTituloMod.setText("Titulo (Min 3 car. | Max 250 car.)");
            this.labelTituloMod.setTextFill(Color.web("red"));
            todoBien=false;
        }else{
            this.labelTituloMod.setText("Titulo");
            this.labelTituloMod.setTextFill(Color.web("black"));
        }
        if(this.comboBoxMateriaMod.getSelectionModel().getSelectedItem()==null){
            this.labelMateria.setText("Materia (Tienes que seleccionar una materia)");
            this.labelMateria.setTextFill(Color.web("red"));
            todoBien=false;
        }else{
            this.labelMateria.setText("Materia");
            this.labelMateria.setTextFill(Color.web("black"));
        }
        if(todoBien){
            apunte.setTitulo(this.textFieldTitulo.getText().trim());
            apunte.setMateria((MateriaBean) this.comboBoxMateriaMod.getSelectionModel().getSelectedItem());
            String elPrecio =this.textFieldPrecio.getText();
            elPrecio=elPrecio.replace(",", ".");
            float precio=Float.parseFloat(elPrecio);
            apunte.setPrecio(precio);
            try {
                apunteLogic.edit(apunte);
                ensenarAlertaInfo("Apunte modificado","El apunte a sido modificado");
                stage.hide();
            } catch (BusinessLogicException e) {
                LOGGER.severe("Error al modificar el apunte: "+e.getMessage());
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
