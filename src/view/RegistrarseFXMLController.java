package view;


import businessLogic.BusinessLogicException;
import businessLogic.ClienteManager;
import static businessLogic.ClienteManagerFactory.createClienteManager;
import encriptaciones.Encriptador;
import encriptaciones.EncriptarException;
import exceptions.LoginNotFoundException;
import exceptions.PasswordWrongException;
import java.rmi.ServerException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ClienteBean;
import transferObjects.UserPrivilege;
import transferObjects.UserStatus;
import static view.ControladorGeneral.showErrorAlert;

/**
 * El controlador de la ventana RegistrarseFX para registrar nuevo usuario.
 * The controller of the Registrarsefx window to sign up new user.
 * @author sergio
 */
public class RegistrarseFXMLController{
    private static final Logger LOGGER = Logger.getLogger("reto1_escritorio.view.Registro");
    private final String CONTRASENA_MENSAJE_DEFAULT="Confirmar contraseña";
    private final String EMAIL_MENSAJE_DEFAULT="Email";
    private final String EMAIL_MENSAJE_ERROR="EMAIL ERRONEO";
    private final String CONTRASENA_MENSAJE_ERROR="CONTRASEÑAS ERRONEAS";
    /* MODIFICACION DIN fecha: 13/11/2019 */
    private final String MINIMO_CARACTERES = "Minimo 3 caracteres.";
    /* MODIFICACION DIN fecha: 14/11/2019 */
    private final String RUTA_AYUDA = getClass().getResource("/view/ayuda_registro.html").toExternalForm();
    /*--------------------fin--------------------------*/
    private Stage stage;
    private ClienteBean user=new ClienteBean();
    private ClienteManager logic = createClienteManager("REAL");
    Tooltip tooltip = new Tooltip(MINIMO_CARACTERES);
    private Encriptador encriptador;
    
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtNombreUsuario;
    @FXML
    private PasswordField pswContrasena;
    @FXML
    private PasswordField pswConfirmarContrasena;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnRegistrar2;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblNombreUsuario;
    @FXML
    private Label lblContrasena;
    @FXML
    private Label lblConfirmarContrasena;
    @FXML
    private Button btnAyuda;
    
    /**
     * Constructor de la ventana resgistro.
     */
    public RegistrarseFXMLController(){}
    
    /**
     * Retorna a la ventana anterior.
     * Returns to the previous window.
     * @param event El evento en cuestión. / The current event.
     */
    @FXML
    private void btnVolver(ActionEvent event){
        stage.hide();
    }
    
    /**
     * Comprueba los campos y registra al usuario si es todo correcto.
     * Check the fields and register the user if everything is correct.
     * @param event  El evento en cuestión. / The current event.
     */
    @FXML
    private void btnRegistrar(ActionEvent event){
        user.setEmail(txtEmail.getText().trim());
        user.setNombreCompleto(txtNombre.getText());
        user.setLogin(txtNombreUsuario.getText());
        user.setContrasenia(pswContrasena.getText());
        user.setPrivilegio(UserPrivilege.USER);
        user.setStatus(UserStatus.ENABLED);
        
        if(!esEmail(txtEmail.getText().trim())){
            lblEmail.setTextFill(Color.web("red"));
            lblEmail.setText(EMAIL_MENSAJE_ERROR);
            
            /* MODIFICACION DIN fecha: 13/11/2019 */
            txtEmail.requestFocus();
            /*--------------------fin--------------------------*/
        }else{
            lblEmail.setTextFill(Color.web("black"));
            lblNombreUsuario.setTextFill(Color.web("black"));
            lblEmail.setText(EMAIL_MENSAJE_DEFAULT);
            try{
                logic.iniciarSesion(user.getLogin(),encriptador.encriptar("-1"));
            }catch (LoginNotFoundException ex1) {
                try {
                    user.setContrasenia(encriptador.encriptar(user.getContrasenia()));
                    logic.create(user);
                } catch (BusinessLogicException  ex) {
                    Logger.getLogger(RegistrarseFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncriptarException ex) {
                    Logger.getLogger(RegistrarseFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
                Alert alert=new Alert(AlertType.INFORMATION);
                alert.setTitle("Informacion de resgistro");
                alert.setHeaderText("Se ha registrado correctamente");
                alert.showAndWait();
                stage.hide();
            } catch (PasswordWrongException ex) {
                Alert alert=new Alert(AlertType.INFORMATION);
                alert.setTitle("Informacion de resgistro");
                alert.setHeaderText("Nombre de usuario ya existente");
                alert.showAndWait();
                txtNombreUsuario.requestFocus();
            }catch(BusinessLogicException e){
                showErrorAlert("Ha ocurrido un error en el servidor, intentelo otra vez o vuelva mas tarde.");
            } catch (EncriptarException ex) {
                Logger.getLogger(RegistrarseFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Función que muestra nuestra escena en el stage.
     * Function that shows our scene in the stage.
     * @param root El nodo raíz de la vista. / The root node of view.
     */
    public void initStage(Parent root){
        //Crear scena y stage
        Scene scene = new Scene(root);
        stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        //Enviar scena al stage.
        stage.setScene(scene);
        //Maximizable deshabilitado.
        stage.setResizable(false);
        stage.setTitle("Registro");
        //El foco estará en Nombre.
        txtNombre.requestFocus();
        //anadimos controladores.
        stage.setOnShowing(this::HandleWindowShowing);
        txtNombre.textProperty().addListener(this::HandleTextChanged);
        txtNombre.setOnKeyPressed(this::keyPressRegistrar);
        txtNombreUsuario.textProperty().addListener(this::HandleTextChanged);
        txtNombreUsuario.setOnKeyPressed(this::keyPressRegistrar);
        txtEmail.textProperty().addListener(this::HandleTextChanged);
        txtEmail.setOnKeyPressed(this::keyPressRegistrar);
        pswContrasena.textProperty().addListener(this::HandleTextChanged);
        pswContrasena.setOnKeyPressed(this::keyPressRegistrar);
        pswConfirmarContrasena.textProperty().addListener(this::HandleTextChanged);
        pswConfirmarContrasena.setOnKeyPressed(this::keyPressRegistrar);
        btnRegistrar2.setOnKeyPressed(this::keyPressRegistrar);
        btnCancelar.setOnKeyPressed(this::keyPressCancelar);
        /* MODIFICACION DIN fecha: 15/11/2019 */
        Image image = new Image(getClass().getResourceAsStream("help.png"),32,32,false,false);
        btnAyuda.setGraphic(new ImageView(image));
        /*--------------------fin--------------------------*/
        /* MODIFICACION DIN fecha: 15/11/2019 */
        btnAyuda.setOnKeyPressed(this::keyPressAyuda);
        //tooltips de ayuda para los botones
        btnCancelar.setTooltip(new Tooltip("Regresar al login"));
        /* MODIFICACION DIN fecha: 13/11/2019 */
        pswContrasena.setTooltip(tooltip);
        pswConfirmarContrasena.setTooltip(tooltip);
        txtNombreUsuario.setTooltip(tooltip);
        btnAyuda.setTooltip(new Tooltip("Ventana de ayuda"));
        /*--------------------fin--------------------------*/
        //textos de ayuda promptext
        txtNombre.setPromptText("ej. Aitor Sanchez");
        txtNombreUsuario.setPromptText("Min. 3 caracteres");
        txtEmail.setPromptText("ej. Aitor_Sanchez@algo.com");
        pswContrasena.setPromptText("Min. 3 caracteres");
        pswConfirmarContrasena.setPromptText("Min. 3 caracteres");
        
        /* MODIFICACION DIN fecha: 14/11/2019 */
        scene.getAccelerators().put(new  KeyCodeCombination(KeyCode.F1),this::ayuda);
        /*--------------------fin--------------------------*/
        encriptador=new Encriptador();
        //Mostrar ventana
        stage.show();
    }
    /* MODIFICACION DIN fecha: 14/11/2019 */
    @FXML
    private void btnAyuda(ActionEvent event){
        ayuda();
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
        stageAyuda.initModality(Modality.APPLICATION_MODAL);
        stageAyuda.show();
    }
    /*--------------------fin--------------------------*/
    /**
     * Controlador de boton ayuda en caso de ser pulsado.
     * @param key letra pulsada
     */
    public void keyPressAyuda(KeyEvent key){
        if(key.getCode().equals(KeyCode.ENTER))
            btnAyuda.fire();
    }
    /**
     * Configuración del inicio de la ventana.
     * Configuration of the window start.
     * @param e El propio evento. / The current event.
     */
    public void HandleWindowShowing(WindowEvent e){
        //boton registrar desabilitado
        btnRegistrar2.setDisable(true);
        
    }
    /**
     * Controlador de eventos de cambio de texto.
     * The controller of the changed text.
     * @param e
     * @param newValue
     * @param oldValue
     */
    public void HandleTextChanged(ObservableValue e, String newValue ,String oldValue){
        //comprobacion de Nombre completo su caracteres maximos.
        if(txtNombre.getText().length()>40 && txtNombre.isFocused()){
            LOGGER.info("El nombre tiene exceso de caracteres");
            txtNombre.setText(newValue.trim().substring(0,40));
        }
        //comprobacion de Nombre de usuario usuario caracteres maximos.
        if(txtNombreUsuario.isFocused()){
            if(txtNombreUsuario.getText().length()>15){
                LOGGER.info("El nombre de usuario tiene exceso de caracteres");
                txtNombreUsuario.setText(newValue.trim().substring(0,15));
            }
            /* MODIFICACION DIN fecha: 13/11/2019 */
            if(!newValue.equals(oldValue))
                lblNombreUsuario.setTextFill(Color.web("black"));
            /*--------------------fin--------------------------*/
            
        }
        //comprobacion de Email usuario caracteres maximos.
        if(txtEmail.isFocused()){
            if(txtEmail.getText().length()>40)
                txtEmail.setText(newValue.trim().substring(0,40));
            
            /* MODIFICACION DIN fecha: 13/11/2019 */
            if(!newValue.equals(oldValue)){
                lblEmail.setTextFill(Color.web("black"));
                lblEmail.setText(EMAIL_MENSAJE_DEFAULT);
            }
            /*--------------------fin--------------------------*/
        }
        //Comprobacion de contrasena caracteres maximos.
        if(pswContrasena.getText().length()>15 && pswContrasena.isFocused())
            pswContrasena.setText(newValue.trim().substring(0,15));
        if(pswConfirmarContrasena.getText().length()>15 && pswConfirmarContrasena.isFocused())
            pswConfirmarContrasena.setText(newValue.trim().substring(0,15));
        //Comprobacion de que todos los campos esten habilitados.
        if(!txtNombre.getText().trim().isEmpty() && !(txtNombreUsuario.getText().trim().length()<3)
                && !txtEmail.getText().trim().isEmpty() && !pswContrasena.getText().trim().isEmpty()
                && !pswConfirmarContrasena.getText().trim().isEmpty()
                && passwordsCorrect(pswContrasena.getText().trim(),pswConfirmarContrasena.getText().trim())){
            lblConfirmarContrasena.setTextFill(Color.web("black"));
            lblConfirmarContrasena.setText(CONTRASENA_MENSAJE_DEFAULT);
            btnRegistrar2.setDisable(false);
        }else{
            if(!passwordsCorrect(pswContrasena.getText().trim(),pswConfirmarContrasena.getText().trim()) && pswConfirmarContrasena.getText().trim().length()>=3 && pswContrasena.getText().trim().length()>=3){
                lblConfirmarContrasena.setTextFill(Color.web("red"));
                lblConfirmarContrasena.setText(CONTRASENA_MENSAJE_ERROR);
            }else{
                lblConfirmarContrasena.setTextFill(Color.web("black"));
                lblConfirmarContrasena.setText(CONTRASENA_MENSAJE_DEFAULT);
            }
            btnRegistrar2.setDisable(true);
        }
    }
    /**
     * Comprobación del formato del Email.
     * Verification of the email format.
     * @param email El propio email. / The current emai.
     * @return  True si el email es correcto | False en los demas casos /
     * True if correct | False in the other cases.
     */
    public  boolean esEmail(String email) {
        boolean resu=true;
        String firstPart,secondPart,thirdPart;
        if(email.length()<5 || email.length()>40){
            resu=false;
        }else{
            try{
                firstPart=email.substring(0, email.indexOf('@'));
                secondPart=email.substring(email.indexOf('@')+1,email.indexOf('.', email.indexOf('@')));
                thirdPart=email.substring(email.indexOf('.',email.indexOf('.', email.indexOf('@')))+1);
                if(!isEmailFirstPart(firstPart) || !isEmailSecondPart(secondPart) || !isEmailThridPart(thirdPart))
                    resu=false;
            }catch(StringIndexOutOfBoundsException e){
                resu=false;
            }
        }
        return resu;
    }
    /**
     * Comprobar primera parte del email (anterior al "@").
     * Check first part of email (before the ".").
     * @param cadena. El trozo de email | The email part.
     * @return True si esta correcto |False en todo los demás casos. / True if
     * correct | False in all other cases.
     */
    public  boolean isEmailFirstPart(String cadena) {
        boolean resu=true;
        for(int i=0;i<cadena.length();i++){
            if(!Character.isAlphabetic(cadena.charAt(i)) && !Character.isDigit(cadena.charAt(i)) && cadena.charAt(i)!='.' && cadena.charAt(i)!='-' && cadena.charAt(i)!='_'){
                resu=false;
                break;
            }
        }
        return resu;
    }
    /**
     * Comprobar segunda parte del email (despues del "@" y antes del punto).
     * Check second part of email (After the "@" and before the ".").
     * @param cadena. El trozo de email | The email part.
     * @return True si esta correcto |False en todo los demás casos. / True if
     * correct | False in all other cases.
     */
    public  boolean isEmailSecondPart(String cadena) {
        boolean resu=true;
        for(int i=0;i<cadena.length();i++){
            if(!Character.isAlphabetic(cadena.charAt(i)) && !Character.isDigit(cadena.charAt(i)) && cadena.charAt(i)!='-'){
                resu=false;
                break;
            }
        }
        return resu;
    }
    /**
     * Comprobar tercera parte del email (despues del ".").
     * Check third part of email (After the ".")
     * @param cadena. El trozo de email | The email part.
     * @return True si esta correcto |False en todo los demás casos. / True if
     * correct | False in all other cases.
     */
    public  boolean isEmailThridPart(String cadena) {
        boolean resu=true;
        for(int i=0;i<cadena.length();i++){
            if(!Character.isAlphabetic(cadena.charAt(i)) && !Character.isDigit(cadena.charAt(i))){
                resu=false;
                break;
            }
        }
        return resu;
    }
    /**
     * Comprobar que contrasena y confirmarContrasena sean idénticos y cumplan
     * el max tamaño de acuerdo a la base de datos.
     * Check that contrasena and confirfarContrasena are equals and have
     * the max size according to the database.
     * @param contrasena. La contraseña. / The password
     * @param contrasenaConfirmacion. La contraseña repetida. / Thre password
     * repeated.
     * @return True si esta correcto | False en todo los demás casos. /
     * True if correct | False in all other cases.
     */
    public boolean passwordsCorrect(String contrasena,String contrasenaConfirmacion){
        boolean resultado=true;
        if(contrasena.length()>=3 && contrasena.length()<=40 && contrasenaConfirmacion.length()>=3 && contrasenaConfirmacion.length()<=40){
            if(!contrasena.equals(contrasenaConfirmacion)){
                resultado=false;
                
            }
        }else{
            resultado=false;
            
        }
        return resultado;
        
    }
    /**
     * Atajo para registrarse.
     * Shortcut to sign up.
     * @param key
     */
    public void keyPressRegistrar(KeyEvent key){
        if(key.getCode().equals(KeyCode.ENTER)) {
            if(!txtNombre.getText().trim().isEmpty() && !txtNombreUsuario.getText().trim().isEmpty()
                    && !txtEmail.getText().trim().isEmpty() && !pswContrasena.getText().trim().isEmpty()
                    && !pswConfirmarContrasena.getText().trim().isEmpty()
                    && passwordsCorrect(pswContrasena.getText().trim(),pswConfirmarContrasena.getText().trim()))
                btnRegistrar2.fire();
        }else if(key.getCode().equals(KeyCode.ESCAPE)){
            String mensaje = "¿Estás seguro de cancelar su registro? Si lo haces se borraran todos los campos.";
            Alert alertCerrarAplicacion = new Alert(AlertType.CONFIRMATION,mensaje,ButtonType.NO,ButtonType.YES);
            //Añadimos titulo a la ventana como el alert.
            alertCerrarAplicacion.setTitle("Cancelar registro.");
            alertCerrarAplicacion.setHeaderText("¿Quieres cancelar registro?");
            //Si acepta cerrara la aplicación.
            alertCerrarAplicacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    btnCancelar.fire();
                }
            });
        }else if(key.getCode().equals(KeyCode.F1)){
            ayuda();
        }
    }
    /**
     * Atajo para cancelar el registro.
     * Shortcut to cancel te sign up.
     * @param key
     */
    public void keyPressCancelar(KeyEvent key){
        if(key.getCode().equals(KeyCode.ENTER)) {
            btnCancelar.fire();
        }else if(key.getCode().equals(KeyCode.ESCAPE)){
            String mensaje = "¿Estás seguro de cancelar su registro? Si lo haces se borraran todos los campos.";
            Alert alertCerrarAplicacion = new Alert(AlertType.CONFIRMATION,mensaje,ButtonType.NO,ButtonType.YES);
            //Añadimos titulo a la ventana como el alert.
            alertCerrarAplicacion.setTitle("Cancelar registro.");
            alertCerrarAplicacion.setHeaderText("¿Quieres cancelar registro?");
            //Si acepta cerrara la aplicación.
            alertCerrarAplicacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    btnCancelar.fire();
                }
            });
        }
    }
}

