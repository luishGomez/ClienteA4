/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package view;

import businessLogic.BusinessLogicException;
import businessLogic.ClienteManager;
import static businessLogic.ClienteManagerFactory.createClienteManager;
import encriptaciones.Encriptador;
import encriptaciones.EncriptarException;
import exceptions.LoginNotFoundException;
import exceptions.PasswordWrongException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import transferObjects.ClienteBean;
import static view.ControladorGeneral.showErrorAlert;

/**
 * Perfil del Cliente donde podra modificar su informacion personal mas añadir saldo a su cuenta.
 * @author sergio
 */
public class PerfilFXMLController{
    private static final Logger LOGGER = Logger.getLogger("escritorio.view.Perfil");
    private final String RUTA_AYUDA = getClass().getResource("/ayuda/ayuda_perfil.html").toExternalForm();
    private final ClienteManager logicCliente = createClienteManager("real");
    private final String CANCELAR_CONTRASENIA = "Cancelar cambio de contraseña";
    private final String CAMBIAR_CONTRASENIA = "Cambiar contraseña";
    private Stage stage;
    private ClienteBean user;
    private int toques = 0;
    private byte[] bytes;
    private Encriptador encriptador;
    
    @FXML private Circle imgCircle;
    @FXML private ImageView imagen;
    @FXML private Label lblCambiarContrasenia;
    @FXML private TextField pswContrasenia;
    @FXML private TextField pswConfirmarContrasenia;
    @FXML private Button btnAceptarContrasenia;
    @FXML private Button btnModificarPerfil;
    @FXML private Button btnAceptarPerfil;
    @FXML private Button btnIngresarSaldo;
    @FXML private Label lblCantidadSaldo;
    @FXML private Button btnCancelarPerfil;
    @FXML private Button btnModificarFotoPerfil;
    @FXML private TextField txtNombre;
    @FXML private TextField txtLogin;
    @FXML private TextField txtEmail;
    @FXML private AnchorPane paneCentro;
    @FXML private MenuBar menuBar;
    @FXML private Menu menuCuenta;
    @FXML private MenuItem menuCuentaCerrarSesion;
    @FXML private MenuItem menuCuentaSalir;
    @FXML private Menu menuVentanas;
    @FXML private MenuItem menuVentanasMiBiblioteca;
    @FXML private MenuItem menuVentanasTiendaApuntes;
    @FXML private MenuItem menuVentanasTiendaPacks;
    @FXML private MenuItem menuVentanasMiPerfil;
    @FXML private MenuItem menuVentanasSubirApunte;
    @FXML private Menu menuHelp;
    @FXML private MenuItem menuHelpAbout;
    
    public void setUser(ClienteBean user){this.user = user;}
    public void setStage(Stage stage) {this.stage = stage;}
    
    public void initStage(Parent root){
        Scene scene = new Scene(root);
        stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Mi perfil.");
        stage.setResizable(true);
        stage.setMaximized(true);
        //--Imagen
        if(user.getFoto()!=null)
            imagen.setImage(new Image(new ByteArrayInputStream(user.getFoto())));
        Ellipse ceiling = new Ellipse();
        ceiling.radiusYProperty().bind(imagen.fitHeightProperty());
        ceiling.radiusXProperty().bind(imagen.fitHeightProperty());
        ceiling.radiusYProperty().bind(imagen.fitHeightProperty());
        //menu---rick esto es postu
        menuCuenta.setMnemonicParsing(true);
        menuCuenta.setText("_Cuenta");
        menuVentanas.setMnemonicParsing(true);
        menuVentanas.setText("_Ventanas");
        menuHelp.setMnemonicParsing(true);
        menuHelp.setText("_Help");
        menuHelpAbout.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+A"));
        menuCuentaCerrarSesion.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+C"));
        menuCuentaSalir.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+S"));
        menuVentanasMiBiblioteca.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+B"));
        menuVentanasMiPerfil.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+P"));
        menuVentanasSubirApunte.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        menuVentanasTiendaApuntes.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+A"));
        menuVentanasTiendaPacks.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+T"));
        //--
        //eventos
        txtNombre.textProperty().addListener(this::HandleTextChanged);
        txtEmail.textProperty().addListener(this::HandleTextChanged);
        txtLogin.textProperty().addListener(this::HandleTextChanged);
        scene.getAccelerators().put(new  KeyCodeCombination(KeyCode.F1),this::ayuda);
        txtNombre.setOnKeyPressed(this::keyPress);
        txtEmail.setOnKeyPressed(this::keyPress);
        txtLogin.setOnKeyPressed(this::keyPress);
        btnModificarFotoPerfil.setOnKeyPressed(this::keyPress);
        btnModificarPerfil.setOnKeyPressed(this::keyPress);
        btnAceptarPerfil.setOnKeyPressed(this::keyPress);
        btnCancelarPerfil.setOnKeyPressed(this::keyPress);
        btnIngresarSaldo.setOnKeyPressed(this::keyPress);
        lblCambiarContrasenia.setOnKeyPressed(this::keyPress);
        pswContrasenia.setOnKeyPressed(this::keyPress);
        pswConfirmarContrasenia.setOnKeyPressed(this::keyPress);
        btnAceptarContrasenia.setOnKeyPressed(this::keyPress);
        //rellenado de campos
        txtNombre.setText(user.getNombreCompleto());
        txtEmail.setText(user.getEmail());
        txtLogin.setText(user.getLogin());
        lblCantidadSaldo.setText(String.valueOf(user.getSaldo()));
        imagen.setClip(ceiling);
        encriptador=new Encriptador();
        stage.show();
    }
    /**
     * Modifica y actualiza foto de perfil por una nueva del dispositivo.
     */
    @FXML public void modificarFotoPerfil(){
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files(*.jpg;*.jpeg;*.png)","*.jpg","*.jpeg","*.png");
            fileChooser.getExtensionFilters().add(extFilter);
            File fileC = fileChooser.showOpenDialog(stage);
            
            if(fileC!=null){
                bytes=Files.readAllBytes(fileC.toPath());
                user.setFoto(bytes);
                try {
                    logicCliente.edit(user);
                } catch (BusinessLogicException ex) {
                    LOGGER.log(Level.SEVERE,
                            "PerfilFXMLController: Error actualizar usuario: {0}",
                            ex.getMessage());
                }
                
                imagen.setImage(new Image(new ByteArrayInputStream(user.getFoto())));
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE,
                    "PerfilFXMLController: Error leer Bytes",
                    ex.getMessage());
        }
    }
    /**
     * Habilita los campos del perfil a modificar.
     */
    @FXML public void modificarPerfil(){
        txtNombre.setDisable(false);
        txtNombre.setText("");
        txtNombre.requestFocus();
        txtNombre.setPromptText(user.getNombreCompleto());
        txtLogin.setDisable(false);
        txtLogin.setPromptText(user.getLogin());
        txtLogin.setText("");
        txtEmail.setDisable(false);
        txtEmail.setText("");
        txtEmail.setPromptText(user.getEmail());
        btnAceptarPerfil.setVisible(true);
        btnCancelarPerfil.setVisible(true);
    }
    /**
     * Cancela la modificacion del perfil deshabilitando y haciendo invisibles los campos necesarios.
     */
    public void cancelarPerfil(){
        txtNombre.setDisable(true);
        txtLogin.setDisable(true);
        txtEmail.setDisable(true);
        txtNombre.setText(user.getNombreCompleto());
        txtEmail.setText(user.getEmail());
        btnAceptarPerfil.setVisible(false);
        btnCancelarPerfil.setVisible(false);
    }
    /**
     * Acepta la accion de modificar el perfil.
     * Comprueba y verifica que todos los campos esten correctamente rellenados
     * previamente de actualizar la BBDD.
     */
    public void aceptarPerfil(){
        Boolean actualizado= false;
        if(!txtEmail.getText().isEmpty()){
            if(txtEmail.getText().length()<=250){
                if(esEmail(txtEmail.getText().trim())){
                    user.setEmail(txtEmail.getText());
                    txtEmail.setStyle("-fx-text-inner-color: " + "black" + ";");
                    actualizado= true;
                }else{
                    showErrorAlert("Formato de Email erroneo");
                    txtEmail.setStyle("-fx-text-inner-color: " + "red" + ";");
                    txtEmail.requestFocus();
                    actualizado= false;
                }
            }else{
                showErrorAlert("Has superado el máximo tamaño del Email"
                        + ", "+250+".");
                txtEmail.setStyle("-fx-text-inner-color: " + "red" + ";");
                txtEmail.requestFocus();
                actualizado= false;
            }
        }
        if(!txtNombre.getText().isEmpty()){
            if(txtNombre.getText().length()<=250){
                user.setNombreCompleto(txtNombre.getText());
                txtNombre.setStyle("-fx-text-inner-color: " + "black" + ";");
                actualizado= true;
            }else{
                txtNombre.setText(txtNombre.getText().trim()
                        .substring(0, 250));
                showErrorAlert("Has superado el máximo tamaño de nombre de usuario"
                        + ", "+250+".");
                txtNombre.setStyle("-fx-text-inner-color: " + "red" + ";");
                txtNombre.requestFocus();
                actualizado= false;
            }
        }
        if(!txtLogin.getText().isEmpty()){
            if(txtLogin.getText().length()<=250){
                user.setLogin(txtLogin.getText());
                txtLogin.setStyle("-fx-text-inner-color: " + "black" + ";");
                actualizado= true;
            }else{
                txtLogin.setText(txtLogin.getText().trim()
                        .substring(0, 250));
                showErrorAlert("Has superado el máximo tamaño del login"
                        + ", "+250+".");
                txtLogin.setStyle("-fx-text-inner-color: " + "red" + ";");
                txtLogin.requestFocus();
                actualizado= false;
            }
        }
        if(actualizado){
            try{
                logicCliente.iniciarSesion(user.getLogin(),"-1");
            }catch (LoginNotFoundException ex1) {
                try {                 
                    logicCliente.edit(user);
                    limpiarCamposPerfil();
                } catch (BusinessLogicException ex) {
                    Logger.getLogger(RegistrarseFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (PasswordWrongException ex) {                
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informacion de resgistro");
                alert.setHeaderText("Nombre de usuario ya existente");
                alert.showAndWait();
                txtLogin.requestFocus();
            }catch(BusinessLogicException e){
                showErrorAlert("Ha ocurrido un error en el servidor, intentelo otra vez o vuelva mas tarde.");
            }

        }
    }
    /**
     * Limpiamos los campos a rellenar de Perfil.
     */
    private void limpiarCamposPerfil() {
        txtNombre.setDisable(true);
        txtLogin.setDisable(true);
        txtEmail.setDisable(true);
        txtNombre.setText(user.getNombreCompleto());
        txtEmail.setText(user.getEmail());
        txtLogin.setText(user.getLogin());
        btnAceptarPerfil.setVisible(false);
        btnCancelarPerfil.setVisible(false);
    }/**
     * Recolorea los campos previamente erroneos al color default.
     * @param e
     * @param newValue
     * @param oldValue
     */
    public void HandleTextChanged(ObservableValue e, String newValue ,String oldValue){
        if(txtNombre.isFocused()){
            if(!newValue.equals(oldValue))
                txtNombre.setStyle("-fx-text-inner-color: " + "black" + ";");
        }
        else if(txtEmail.isFocused()){
            txtEmail.setStyle("-fx-text-inner-color: " + "black" + ";");
        }
        else if(txtLogin.isFocused()){
            txtLogin.setStyle("-fx-text-inner-color: " + "black" + ";");
        }
    }
    /**
     * Habilita y deshabilita los campos para actualizar contrasenia
     */
    @FXML public void cambiarContrasenia(){
        pswContrasenia.setVisible(true);
        pswConfirmarContrasenia.setVisible(true);
        btnAceptarContrasenia.setVisible(true);
        lblCambiarContrasenia.setText(CANCELAR_CONTRASENIA);
        toques++;
        if(toques >1){
            pswContrasenia.setVisible(false);
            pswConfirmarContrasenia.setVisible(false);
            btnAceptarContrasenia.setVisible(false);
            toques=0;
            lblCambiarContrasenia.setText(CAMBIAR_CONTRASENIA);
        }
    }
    /**
     * Acepta la accion de modificar y actualizar la contraseña.
     */
    @FXML public void aceptarContrasenia(){
        if(!pswContrasenia.getText().isEmpty() && !pswConfirmarContrasenia.getText().isEmpty() && pswContrasenia.getText().equals(pswConfirmarContrasenia.getText())){
            try {
                user.setContrasenia(encriptador.encriptar(pswContrasenia.getText().trim()));
                logicCliente.actualizarContrasenia(user);
            } catch (BusinessLogicException | EncriptarException ex) {
                Logger.getLogger(PerfilFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
            pswContrasenia.setVisible(false);
            pswConfirmarContrasenia.setVisible(false);
            btnAceptarContrasenia.setVisible(false);
            toques=0;
            lblCambiarContrasenia.setText(CAMBIAR_CONTRASENIA);
        }else{
            showErrorAlert("Contraseñas no coinciden");
            pswContrasenia.setText("");
            pswConfirmarContrasenia.setText("");
            pswContrasenia.requestFocus();
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
     * Abre ventana en la cual se actualiza el saldo del usuario.
     * @param e
     * @throws IOException
     * @throws InterruptedException
     */
    @FXML private void ingresarSaldo(){
        try {
            LOGGER.info("Voy a ingresar saldo");
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("perfil_saldo.fxml"));
            
            Parent root_saldo = (Parent)loader.load();
            perfil_saldoFXMLController controller =
                    ((perfil_saldoFXMLController)loader.getController());
            controller.initStage(root_saldo);
            controller.setUser(user);
            Stage stage_saldo=new Stage();
            stage_saldo.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root_saldo);
            stage_saldo.setTitle("Ventana de ayuda");
            stage_saldo.setResizable(false);
            stage_saldo.setScene(scene);
            controller.setStage(stage_saldo);
            stage_saldo.showAndWait();
            this.user=controller.getUser();
            lblCantidadSaldo.setText(String.valueOf(user.getSaldo()));
        } catch (IOException ex) {
            Logger.getLogger(PerfilFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Atajos
     * @param key
     */
    public void keyPress(KeyEvent key){
        if(key.getCode().equals(KeyCode.ENTER)) {
            if(btnAceptarPerfil.isFocused() || txtNombre.isFocused() || txtLogin.isFocused() || txtEmail.isFocused())
                btnAceptarPerfil.fire();
        }else if(key.getCode().equals(KeyCode.F) && key.isControlDown()){
            btnModificarFotoPerfil.fire();
        }else if(key.getCode().equals(KeyCode.U) && key.isControlDown()){
            if(txtNombre.isFocused() || txtLogin.isFocused() || txtEmail.isFocused())
                btnCancelarPerfil.fire();
            else
                btnModificarPerfil.fire();
        }else if(key.getCode().equals(KeyCode.I) && key.isControlDown()){
            btnIngresarSaldo.fire();
        }else if(key.getCode().equals(KeyCode.P) && key.isControlDown()){
            if(pswConfirmarContrasenia.isFocused()){
                cambiarContrasenia();
            }else{
                cambiarContrasenia();
                pswContrasenia.requestFocus();
            }
        }else if(key.getCode().equals(KeyCode.S) && key.isControlDown()){
            if(pswConfirmarContrasenia.isFocused() || pswConfirmarContrasenia.isFocused() || btnAceptarContrasenia.isFocused())
                btnAceptarContrasenia.fire();
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
    
    //----------------------------
    
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
        ayuda();
    }
    //Fin de los metodos de navegación de la aplicación
    
    
    
}

