/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package view;

import clientea4.ClienteA4;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

/**
 *
 * @author 2dam
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GestorDeApuntesFXControllerIT extends ApplicationTest  {
    
    private final int MIN_CARACTERES=3;
    private final int MAX_CARACTERES=250;
    private String fraseErrorTitulo="Titulo (Min "+MIN_CARACTERES+" car. | Max "+MAX_CARACTERES+" car.)";
    private String fraseErrorDesc="Descripción (Min "+MIN_CARACTERES+" car. | Max "+MAX_CARACTERES+" car.)";
    private  final String MAX_TEXT="ASDASDASDASDASDASDASDASDASDASDASDASD"+
            "ASDASDASDASDASDASDASDASDASDASDASDASD"+
            "ASDASDASDASDASDASDASDASDASDASDASDASD"+
            "ASDASDASDASDASDASDASDASDASDASDASDASD"+
            "ASDASDASDASDASDASDASDASDASDASDASDASD"+
            "ASDASDASDASDASDASDASDASDASDASDASDASD"+
            "ASDASDASDASDASDASDASDASDASDASDASDASD"+
            "ASDASDASDASDASDASDASDASDASDASDASDASD"+
            "ASDASDASDASDASDASDASDASDASDASDASDASD";
    @Override
    public void start(Stage stage) throws Exception{
        new ClienteA4().start(stage);
    }
    public GestorDeApuntesFXControllerIT() {
    }
    
    @Test
    public void testA_testIniciaTodoBien() {
        //Iniciar sesión
        verifyThat("#btnAcceder",isDisabled());
        clickOn("#tfNombreUsuario");
        write("admin");
        clickOn("#tfContra");
        write("123");
        verifyThat("#btnAcceder",isEnabled());
        clickOn("#btnAcceder");
        //GESTOR DE APUNTES
        verifyThat("#btnModificar",isDisabled());
        verifyThat("#btnBorrar",isDisabled());
        verifyThat("#labelTitulo",hasText("Titulo"));
        verifyThat("#labelDesc",hasText("Descripción"));
        
        
        
    }
    @Test
    public void testB_testIniciaTodoBien(){
        //Modificar minimo
        push(KeyCode.DOWN);
        doubleClickOn("#textFieldTitulo");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("1");
        doubleClickOn("#textFieldDesc");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("1");
        clickOn("#btnModificar");
        verifyThat("#labelTitulo",hasText(fraseErrorTitulo));
        verifyThat("#labelDesc",hasText(fraseErrorDesc));
    }
    @Test
    public void testC_testIniciaTodoBien(){
        //Maximo
        doubleClickOn("#textFieldTitulo");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(MAX_TEXT);
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.CONTROL, KeyCode.C);
        doubleClickOn("#textFieldDesc");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        push(KeyCode.CONTROL, KeyCode.V);
        clickOn("#btnModificar");
        verifyThat("#labelTitulo",hasText(fraseErrorTitulo));
        verifyThat("#labelDesc",hasText(fraseErrorDesc));
    }
    @Test
    public void testD_testIniciaTodoBien(){
        //DATEPICKER vacio & combo de materias
        doubleClickOn("#textFieldTitulo");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("Titulo del apunte editado");
        doubleClickOn("#textFieldDesc");
        write("La descripción del apunte editado.");
        clickOn("#comboBoxMaterias");
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.UP);
        push(KeyCode.UP);
        clickOn("#btnModificar");
        verifyThat("#labelTitulo",hasText("Titulo"));
        verifyThat("#labelDesc",hasText("Descripción"));
    }
    @Test
    public void testE_testIniciaTodoBien(){
        //Comprueba que funcione el datePicker
        clickOn("#datePickerFecha");
        write("asdasdasd");
        clickOn("#btnModificar");
        clickOn("#datePickerFecha");
        write("24/01/2020");
        clickOn("#btnModificar");
        push(KeyCode.SPACE);
        
    }
    @Test
    public void testF_testIniciaTodoBien(){
        //Comprobar borrado
        clickOn("#clPrecio");
        push(KeyCode.DOWN);
        push(KeyCode.ENTER);
        clickOn("#btnBorrar");
        push(KeyCode.SPACE);
        push(KeyCode.SPACE);
        clickOn("#clPrecio");
        push(KeyCode.DOWN);
        clickOn("#btnBorrar");
        push(KeyCode.SPACE);     
        
    }
    
}
