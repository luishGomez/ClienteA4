/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package view;

import clientea4.ClienteA4;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 *
 * @author Sergio
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PerfilFXMLControllerIT extends ApplicationTest{
    private final String NOMBRE_ACTUALIZADO="pedro";
    private final String LOGIN_ACTUALIZADO="testSergioActualizado";
    private final String LOGIN_REPETIDO="carlita";
    private final String EMAIL_ACTUALIZADO="testSergio@gmail.com";
    private final String CONTRASENIA_NUEVA="1234";
    private final String CONTRASENIA_NUEVA_CONFIRMACION="1234";
    private ImageView imagen;
    private Label lblCambiarContrasenia;
    private TextField pswContrasenia;
    private TextField pswConfirmarContrasenia;
    private Button btnAceptarContrasenia;
    private Button btnModificarPerfil;
    private Button btnAceptarPerfil;
    private Button btnIngresarSaldo;
    private Label lblCantidadSaldo;
    private Button btnCancelarPerfil;
    private Button btnModificarFotoPerfil;
    private TextField txtNombre;
    private TextField txtLogin;
    private TextField txtEmail;
    
    @Override
    public void start(Stage stage) throws Exception{
        new ClienteA4().start(stage);
        txtNombre = lookup("#txtNombre").query();
        txtLogin = lookup("#txtLogin").query();
        txtEmail = lookup("#txtEmail").query();
        pswContrasenia = lookup("#pswContrasenia").query();
        pswConfirmarContrasenia = lookup("#pswConfirmarContrasenia").query();
        btnAceptarContrasenia = lookup("#btnAceptarContrasenia").query();
        btnAceptarPerfil = lookup("#btnAceptarPerfil").query();
        btnCancelarPerfil = lookup("#btnCancelarPerfil").query();
        btnModificarPerfil = lookup("#btnModificarPerfil").query();
        lblCambiarContrasenia = lookup("#lblCambiarContrasenia").query();
        btnIngresarSaldo = lookup("#btnIngresarSaldo").query();
        lblCantidadSaldo = lookup("#lblCantidadSaldo").query();
    }
    @Test
    public void testA_Login() {
        clickOn("#tfNombreUsuario");
        write("test19993");
        clickOn("#tfContra");
        write("123");
        clickOn("#btnAcceder");
        press(KeyCode.ALT);
        press(KeyCode.RIGHT);
        press(KeyCode.DOWN);
        for(int i=0;i<2;i++){
            push(KeyCode.UP);
        }
        press(KeyCode.ENTER);
    }
    @Test
    public void testB_ComprobacionDeCamposDeshabilitadosEinvisibles() {
        Assert.assertFalse(pswContrasenia.isVisible());
        Assert.assertFalse(pswConfirmarContrasenia.isVisible());
        Assert.assertFalse(btnAceptarContrasenia.isVisible());
        Assert.assertFalse(btnAceptarPerfil.isVisible());
        Assert.assertFalse(btnCancelarPerfil.isVisible());
        
        Assert.assertTrue(txtNombre.isDisabled());
        Assert.assertTrue(txtLogin.isDisabled());
        Assert.assertTrue(txtEmail.isDisabled());
    }
    @Test
    public void testC_actualizacionDatos(){
        clickOn(btnModificarPerfil);
        Assert.assertFalse(txtNombre.isDisabled());
        Assert.assertFalse(txtLogin.isDisabled());
        Assert.assertFalse(txtEmail.isDisabled());
        
        clickOn(txtNombre);
        write(NOMBRE_ACTUALIZADO);
        clickOn(txtLogin);
        write(LOGIN_ACTUALIZADO);
        clickOn(txtEmail);
        write(EMAIL_ACTUALIZADO);
        clickOn(btnAceptarPerfil);
        
        Assert.assertTrue(txtNombre.isDisabled());
        Assert.assertTrue(txtLogin.isDisabled());
        Assert.assertTrue(txtEmail.isDisabled());
        
    }
    @Test
    public void testD_alertaDeLoginYaExisteAlActualizarLogin(){
        clickOn(btnModificarPerfil);
        clickOn(txtLogin);
        write(LOGIN_REPETIDO);
        clickOn(btnAceptarPerfil);
        verifyThat("Nombre de usuario ya existente", isVisible());
        press(KeyCode.ENTER);
        clickOn(btnCancelarPerfil);
    }
    @Test
    public void testE_actualizacionDeContraseña(){
        clickOn(lblCambiarContrasenia);
        Assert.assertTrue(pswContrasenia.isVisible());
        Assert.assertTrue(pswConfirmarContrasenia.isVisible());
        Assert.assertTrue(btnAceptarContrasenia.isVisible());
        
        clickOn(pswContrasenia);
        write(CONTRASENIA_NUEVA);
        clickOn(pswConfirmarContrasenia);
        write(CONTRASENIA_NUEVA_CONFIRMACION);
        clickOn(btnAceptarContrasenia);
        
        Assert.assertFalse(pswContrasenia.isVisible());
        Assert.assertFalse(pswConfirmarContrasenia.isVisible());
        Assert.assertFalse(btnAceptarContrasenia.isVisible());
    }
    @Test
    public void testF_ingresarSaldo() throws Exception{
        String saldoActual = lblCantidadSaldo.getText().trim();
        String saldoActualizado;
        clickOn(btnIngresarSaldo);
        write("10");
        press(KeyCode.ENTER);
        saldoActualizado=lblCantidadSaldo.getText();
        if(Float.valueOf(saldoActual)==Float.valueOf(saldoActualizado))
            throw new Exception("Saldo no actualizado");
        
    }
    @Test
    public void testD_volviendoDatos(){
        clickOn(btnModificarPerfil);
        clickOn(txtLogin);
        write("test19993");      
        clickOn(btnAceptarPerfil);
        clickOn(lblCambiarContrasenia);
        clickOn(pswContrasenia);
        write("123");
        clickOn(pswConfirmarContrasenia);
        write("123");
        clickOn(btnAceptarContrasenia);
    }
}
