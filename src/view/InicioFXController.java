package view;

import businessLogic.*;
import static businessLogic.ClienteManagerFactory.createClienteManager;
import static businessLogic.UserManagerFactory.createUserManager;
import encriptaciones.Encriptador;
import encriptaciones.EncriptarException;
import exceptions.LoginNotFoundException;
import exceptions.NoEsUserException;
import exceptions.PasswordWrongException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.glassfish.hk2.utilities.reflection.Logger;
import transferObjects.ClienteBean;
import transferObjects.UserBean;

/**
 * El controlador de la ventana InicioFx para iniciar sesión.
 * The Iniciofx window controller to log in.
 * @author Luis
 */
public class InicioFXController extends ControladorGeneral{
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.GestorDeApuntesFXController");
    private Encriptador encriptador=new Encriptador();
    /* MODIFICACIÓN DIN 14/11/2019 */
    /**
     * Botón Ayuda.
     * Ayuda button.
     */
    //   @FXML
    // private Button btnAyuda;
    /**
     * Campo de texto Nombre de usuario.
     * Nombre de usuario text field.
     */
    @FXML
    private TextField tfNombreUsuario;
    /**
     * Campo de contraseña Contraseña.
     * Contraseña password field.
     */
    @FXML
    private PasswordField tfContra;
    /**
     * Botón Registrar.
     * Registrar button.
     */
    @FXML
    private Button btnRegistrar;
    /**
     * Botón Acceder.
     * Acceder button.
     */
    @FXML
    private Button btnAcceder;
    /**
     * Botón Salir.
     * Salir button.
     */
    @FXML
    private Button btnSalir;
    /**
     * Etiqueta Nombre de usuario.
     * Nombre de usuario label.
     */
    @FXML
    private Label lblNombreUsuario;
    /**
     * Etiqueta Contraseña.
     * Contraseña label.
     */
    @FXML
    private Label lblContra;
    /**
     * Ventana que se muestra.
     * Window to show.
     */
    private Stage stage;
    
    /* MODIFICACIÓN DIN 14/11/2019 */
    /**
     * Ventana de ayuda.
     * Window help.
     */
    private Stage helpStage = new Stage();
    
    /**
     * Muestra mensajes en el log de la aplicación.
     * Show message in the aplication log.
     */
    public UserManager userLogic = createUserManager("real");
    public ClienteManager clienteLogic = createClienteManager("real");
    /**
     * Variable para controlar los errores de Nombre de usuario.
     * Variable to control Nombre de usuario error.
     */
    private Boolean errorNombre = false;
    /**
     * Variable para controlar los errores de Contraseña.
     * Variable to control Contraseña error.
     */
    private Boolean errorContra = false;
    
    /* MODIFICACIÓN DIN 14/11/2019 */
    /**
     * Constructor vacio.
     * Void constructor.
     */
    public InicioFXController(){
        
    }
    
    /**
     * Da valor al stage.
     * Set value to stage.
     * @param stage Nuevo valor. / New value.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /**
     * Función que muestra nuestra escena en el stage.
     * Function that shows our scene in the stage.
     * @param root El nodo raíz de la vista. / The root node of view.
     */
    public void initStage(Parent root){
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setTitle("Sign Up Sign In");
        //Add Listener
        tfNombreUsuario.textProperty().addListener(this::textChanged);
        tfContra.textProperty().addListener(this::textChanged);
        tfNombreUsuario.setOnKeyPressed(this::keyPressLogin);
        tfContra.setOnKeyPressed(this::keyPressLogin);
        btnRegistrar.setOnAction(this::btnRegistrarOnClick);
        btnRegistrar.setOnKeyPressed(this::keyPressRegistrar);
        btnAcceder.setOnAction(this::btnLoginOnClick);
        btnAcceder.setOnKeyPressed(this::keyPressLogin);
        btnSalir.setOnAction(this::btnSalirOnClick);
        btnSalir.setOnKeyPressed(this::keyPressSalir);
        /* MODIFICACIÓN DIN 14/11/2019*/
        KeyCombination keyCombination = new KeyCodeCombination(KeyCode.F1);
        // Runnable run = ()-> btnAyuda.fire();
        //scene.getAccelerators().put(keyCombination, run);
        //btnAyuda.setOnAction(this::btnAyudaOnClick);
        btnRegistrar.setMnemonicParsing(true);
        btnRegistrar.setText("_Registrar");
        btnAcceder.setMnemonicParsing(true);
        btnAcceder.setText("_Acceder");
        btnSalir.setMnemonicParsing(true);
        btnSalir.setText("_Salir");
        tfNombreUsuario.focusedProperty().addListener(this::focusChangedNombre);
        tfContra.focusedProperty().addListener(this::focusChangedContra);
        stage.show();
    }
    /**
     * Añade las propiedades a los controladores de la escena.
     * Adds properties to the scene drivers.
     * @param w El propio evento. / The current event.
     */
    private void handleWindowShowing(WindowEvent w){
        //El botón Iniciar sesión estarán deshabilitado.
        btnAcceder.setDisable(true);
        //El foco estará en el campo Nombre de usuario.
        tfNombreUsuario.requestFocus();
        //PromtText
        tfNombreUsuario.setPromptText("Introduzca el nombre de usuario");
        tfContra.setPromptText("Introduzca la contraseña");
        //Tooltips
        btnRegistrar.setTooltip(
                new Tooltip("Pulse para abrir formulario de registro."));
        btnAcceder.setTooltip(new Tooltip("Pulse para iniciar sesión."));
        /* MODIFICACIÓN DIN 14/11/2019*/
        tfNombreUsuario.setTooltip(new Tooltip("Requisitos:\n-Mínimo caracteres: "
                +MIN_CARACTERES+"\n-Máximo caracteres: "+MAX_CARACTERES));
        tfContra.setTooltip(new Tooltip("Requisitos:\n-Mínimo caracteres: "
                +MIN_CARACTERES+"\n-Máximo caracteres: "+MAX_CARACTERES));
        btnSalir.setTooltip(
                new Tooltip("Pulse para salir de la aplicación."));
        //La ventana no puede cambiar de tamaño.
        stage.setResizable(false);
    }
    /**
     * Comprueba que los controladores label estén bien informados.
     * Check that label controllers are well informed.
     * @param observable
     * @param oldValue Valor antiguo. / Old value.
     * @param newValue Valor nuevo. / New value.
     */
    private void textChanged(ObservableValue observable,
            String oldValue,
            String newValue){
        /* MODIFICACIÓN DIN 13/11/2019 */
        if(newValue.equals(tfNombreUsuario.getText().trim()) && errorNombre){
            lblNombreUsuario.setTextFill(Color.web("black"));
            errorNombre = false;
        }
        if(newValue.equals(tfContra.getText().trim()) && errorContra){
            lblContra.setTextFill(Color.web("black"));
            errorContra = false;
        }
        if(tfNombreUsuario.getText().trim().length() >= MIN_CARACTERES &&
                tfContra.getText().trim().length() >= MIN_CARACTERES){
            btnAcceder.setDisable(false);
        }else{
            btnAcceder.setDisable(true);
        }
        if(tfNombreUsuario.getText().trim().length() > MAX_CARACTERES){
            tfNombreUsuario.setText(tfNombreUsuario.getText().trim()
                    .substring(0, MAX_CARACTERES));
            showErrorAlert("Has superado el máximo tamaño de nombre de usuario"
                    + ", "+MAX_CARACTERES+".");
        }
        if(tfContra.getText().trim().length() > MAX_CARACTERES){
            tfContra.setText(tfContra.getText().trim()
                    .substring(0, MAX_CARACTERES));
            showErrorAlert("Has superado el máximo tamaño de contraseña, "+MAX_CARACTERES+".");
        }
    }
    /**
     * Atajo para iniciar sesión.
     * Shortcut to log in.
     * @param key Tecla pulsada. / Key press.
     */
    private void keyPressLogin(KeyEvent key){
        if(key.getCode().equals(KeyCode.ENTER)) {
            btnAcceder.fire();
        }else if(key.getCode().equals(KeyCode.ESCAPE)){
            btnSalir.fire();
        }else if(key.getCode().equals(KeyCode.F1)){
            // btnAyuda.fire();
        }
    }
    /**
     * Atajo para registrase.
     * Shortcut to sign up.
     * @param key Tecla pulsada. / Key press.
     */
    private void keyPressRegistrar(KeyEvent key){
        if(key.getCode().equals(KeyCode.ENTER)) {
            btnRegistrar.fire();
        }else if(key.getCode().equals(KeyCode.ESCAPE)){
            btnSalir.fire();
        }
    }
    /**
     * Atajo para salir de la aplicación.
     * Shortcut to exit from the application.
     * @param key Tecla pulsada. / Key press.
     */
    private void keyPressSalir(KeyEvent key){
        if(key.getCode().equals(KeyCode.ENTER)) {
            btnSalir.fire();
        }else if(key.getCode().equals(KeyCode.ESCAPE)){
            btnSalir.fire();
        }
    }
    /**
     * Acción activada por el botón Registrar. Muestra la ventana Registrar.
     * Action enabled by the Register button. Displays the Register window.
     * @param event El propio evento. / The current event.
     */
    private void btnRegistrarOnClick(ActionEvent event) {
        
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("Registrarse.fxml"));
            
            Parent root = (Parent) loader.load();
            
            RegistrarseFXMLController controller
                    = ((RegistrarseFXMLController) loader.getController());
            
            controller.initStage(root);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(InicioFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    /**
     * Comprobación de Login y Password y inicio de sesión.
     * Login and Password check and login.
     * @param event El propio evento. / The current event.
     */
    private void btnLoginOnClick(ActionEvent event){;
    
    /*
    try{
    FXMLLoader loader = new FXMLLoader(getClass()
    .getResource("tienda_apuntes.fxml"));
    Parent root = (Parent)loader.load();
    TiedaApuntesFXController controller =
    ((TiedaApuntesFXController)loader.getController());
    
    //controller.setUser(user);
    controller.initStage(root);
    }catch(Exception e){
    showErrorAlert("ERROR AL INTENTAR ABRIR LA SIGUIENTE VENTANA "+e.getMessage());
    }
    */
    
    String nombre = tfNombreUsuario.getText().toString();
    String contra = tfContra.getText().toString();
    Object user=null;
    try{
        user = userLogic.iniciarSesion(nombre, encriptador.encriptar(contra));
        //user = userLogic.iniciarSesion(nombre, contra);
        if(user != null){
            lblNombreUsuario.setTextFill(Color.web("black"));
            lblContra.setTextFill(Color.web("black"));
            UserBean usuario= new UserBean();
            usuario=(UserBean) user;
            
            LOGGER.info("-----------------------------"+usuario.getPrivilegio().toString());
            //abrir admin
            showErrorAlert("admin");
            try{
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("gestor_de_apuntes.fxml"));
                
                Parent root = (Parent)loader.load();
                
                GestorDeApuntesFXController controller =
                        ((GestorDeApuntesFXController)loader.getController());
                controller.setUser(usuario);
                controller.initStage(root);
                tfContra.setText("");
            }catch(IOException e){
                showErrorAlert("Error al cargar la ventana de Login.");
                LOGGER.severe("Error "+e.getMessage());
                
            }
            
            
        }else{
            showErrorAlert("Nombre de usuario o contraseña incorrecto.");
        }
    }catch(PasswordWrongException e){
        showErrorAlert("Contraseña incorrecta.");
        
        /* MODIFICACIÓN DIN 13/11/2019*/
        tfContra.requestFocus();
        errorContra = true;
        
        lblContra.setTextFill(Color.web("red"));
    }catch(LoginNotFoundException e){
        showErrorAlert("Nombre de usuario incorrecto.");
        
        /* MODIFICACIÓN DIN 13/11/2019*/
        tfNombreUsuario.requestFocus();
        errorNombre = true;
        errorContra = true;
        
        lblNombreUsuario.setTextFill(Color.web("red"));
        lblContra.setTextFill(Color.web("red"));
    } catch (NoEsUserException ex) {
        try {
            user=clienteLogic.iniciarSesion(nombre, encriptador.encriptar(contra));
            //user=clienteLogic.iniciarSesion(nombre, contra);
            ClienteBean cliente=new ClienteBean();
            cliente=(ClienteBean) user;
            
            try{
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("biblioteca.fxml"));
                
                Parent root = (Parent)loader.load();
                BibliotecaClienteFXController controller =
                        ((BibliotecaClienteFXController)loader.getController());
                controller.setUser(cliente);
                controller.setStage(stage);
                controller.initStage(root);
                tfContra.setText("");
            }catch(IOException e){
                showErrorAlert("Error al cargar la ventana de Login.");
                LOGGER.severe("Error "+e.getMessage());
                
            }
            } catch (BusinessLogicException | EncriptarException  ex1) {
        //} catch (BusinessLogicException   ex1) {
            showErrorAlert("Ha ocurrido un error en el servidor, intentelo otra vez o vuelva mas tarde.");
        } catch (PasswordWrongException ex1) {
            showErrorAlert("Contraseña incorrecta.");
            
            /* MODIFICACIÓN DIN 13/11/2019*/
            tfContra.requestFocus();
            errorContra = true;
            
            lblContra.setTextFill(Color.web("red"));
        } catch (LoginNotFoundException ex1) {
            showErrorAlert("Nombre de usuario incorrecto.");
            tfNombreUsuario.requestFocus();
            errorNombre = true;
            errorContra = true;
            
            lblNombreUsuario.setTextFill(Color.web("red"));
            lblContra.setTextFill(Color.web("red"));
        }
        } catch (BusinessLogicException | EncriptarException  ex) {
    //} catch (BusinessLogicException ex) {
        showErrorAlert("Ha ocurrido un error en el servidor, intentelo otra vez o vuelva mas tarde.");
    }
    }
    
    /* MODIFICACIÓN DIN 14/11/2019*/
    /**
     * Muestra la ventana de ayuda.
     * Show help window.
     * @param event El propio evento. / The current event.
     */
    public void btnAyudaOnClick(ActionEvent event){
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        
        URL url = this.getClass().getResource("ayuda.html");
        webEngine.load(url.toString());
        helpStage.setTitle(webEngine.getTitle());
        
        Button btnCerrarHelp=new Button("Cerrar");
        btnCerrarHelp.setOnAction(this::btnCerrarHelpOnAction);
        
        VBox root = new VBox();
        root.getChildren().addAll(browser,btnCerrarHelp);
        
        Scene helpScene = new Scene(root);
        helpStage.setScene(helpScene);
        
        helpStage.show();
    }
    /**
     * Comprueba el cambio de foco del campo de texto Nombre de usuario.
     * Check Nombre de usuario text field focus changed.
     * @param arg0
     * @param oldValue Valor antiguo. / Old value.
     * @param newValue Valor nuevo. / New value.
     */
    public void focusChangedNombre(ObservableValue arg0, Boolean oldValue, Boolean newValue){
        if(!newValue && tfNombreUsuario.getText().trim().length() < MIN_CARACTERES){
            lblNombreUsuario.setTextFill(Color.web("red"));
            errorNombre = true;
        }
    }
    /**
     * Comprueba el cambio de foco del campo de contraseña Contraseña.
     * Check Contraseña password field focus changed.
     * @param arg0
     * @param oldValue Valor antiguo. / Old value.
     * @param newValue Valor nuevo. / New value.
     */
    public void focusChangedContra(ObservableValue arg0, Boolean oldValue, Boolean newValue){
        if(!newValue && tfContra.getText().trim().length() < MIN_CARACTERES){
            lblContra.setTextFill(Color.web("red"));
            errorContra = true;
        }
    }
    /**
     * Cierra la ventana de ayuda.
     * Close help window.
     * @param event propio evento. / The current event.
     */
    public void btnCerrarHelpOnAction(ActionEvent event){
        helpStage.hide();
    }
    /**
     * Botón para salir de la aplicación.
     * Button to exit the application.
     * @param event El propio evento. / The current event.
     */
    public void btnSalirOnClick(ActionEvent event){
        String mensaje = "¿Estás seguro de que desea cerrar la aplicación?";
        Alert alertCerrarAplicacion = new Alert(AlertType.CONFIRMATION,mensaje,ButtonType.NO,ButtonType.YES);
        //Añadimos titulo a la ventana como el alert.
        alertCerrarAplicacion.setTitle("Cerrar la aplicación");
        alertCerrarAplicacion.setHeaderText("¿Quieres salir de la aplicación?");
        //Si acepta cerrara la aplicación.
        alertCerrarAplicacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                Platform.exit();
            }
        });
    }
}
