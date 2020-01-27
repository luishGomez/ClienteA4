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
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import static org.hamcrest.CoreMatchers.is;
import transferObjects.ApunteBean;
/**
 *  Prueba las funcionalidades del gestor de apuntes del administrador.
 * @author Ricardo Peinado Lastra
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GestorDeApuntesFXControllerIT extends ApplicationTest  {
    private static ResourceBundle configFile=ResourceBundle.getBundle("view.testConfig");
    private static final String urlFichero = configFile.getString("fichero");
    private final String tituloApunte="El apunte test";
    private final String descApunte="La descripción del apunte test";
    private final String tituloEditado="Titulo del apunte editado";
    private final String precio="12";
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
    private TableView table;
    @Override
    public void start(Stage stage) throws Exception{
        new ClienteA4().start(stage);
    }
    public GestorDeApuntesFXControllerIT() {
    }
    /**
     * Inicia la sesión y crea un apunte.
     */
    @Test
    public void testA_crearApunte(){
        //Iniciar sesión
        verifyThat("#btnAcceder",isDisabled());
        clickOn("#tfNombreUsuario");
        write("test19993");
        clickOn("#tfContra");
        write("123");
        verifyThat("#btnAcceder",isEnabled());
        clickOn("#btnAcceder");
        //Acceder a mis apuntes para subir apunte
        push(KeyCode.ALT);
        push(KeyCode.RIGHT);
        for(int i=0;i<6;i++){
            push(KeyCode.DOWN);
        }
        push(KeyCode.SPACE);
        clickOn("#btnSubirApunte");
        write(tituloApunte);
        clickOn("#comboBoxMateria");
        push(KeyCode.DOWN);
        push(KeyCode.ENTER);
        clickOn("#textDesc");
        write(descApunte);
        clickOn("#btnSeleccionarArchivo");
        applyPath(urlFichero);
        clickOn("#textFieldPrecio");
        write(precio);
        clickOn("#btnSubirElApunte");
        push(KeyCode.SPACE);
        push(KeyCode.CONTROL,KeyCode.ALT,KeyCode.C);
        push(KeyCode.SPACE);
        
    }
    /**
     * Inicia sesión como administrador y comprueba que el inicio de la ventana
     * este bien.
     */
    @Test
    public void testB_crearApunte() {
        //Iniciar sesión
        clickOn("#tfNombreUsuario");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("admin");
        clickOn("#tfContra");
        write("abcd*1234");
        verifyThat("#btnAcceder",isEnabled());
        clickOn("#btnAcceder");
        //GESTOR DE APUNTES
        verifyThat("#btnModificar",isDisabled());
        verifyThat("#btnBorrar",isDisabled());
        verifyThat("#labelTitulo",hasText("Titulo"));
        verifyThat("#labelDesc",hasText("Descripción"));
        verifyThat("#labelFecha",hasText("Fecha de validación"));
        verifyThat("#labelMateria",hasText("Materia"));
        
        
        
    }
    /**
     * Permite poner la ruta de un fichero en el fileChooser.
     * @param filePath El path del fichero a leer.
     */
    private void applyPath(String filePath){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(filePath);
        clipboard.setContents(stringSelection, stringSelection);
        press(KeyCode.CONTROL).press(KeyCode.V).release(KeyCode.V).release(KeyCode.CONTROL);
        push(KeyCode.ENTER);
    }
    
    /**
     * Selecciona el apunte creado y prueba los campos editables con los valores
     * inferiores al minimo.
     */
    @Test
    public void testC_crearApunte(){
        //CARGAR LA TABLA
        table=lookup("#tableApuntes").queryTableView();
        //Modificar minimo
        push(KeyCode.SPACE);
        buscarApunte(tituloApunte);
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
    /**
     * Prueba a modificar el apunte con valores mayores a los maximos.
     */
    @Test
    public void testD_crearApunte(){
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
    /**
     * Comprueba que funciona el combobox de materias y comprueba que no deja
     * modficar sino a metido datos en el datepicker.
     */
    @Test
    public void testE_crearApunte(){
        //DATEPICKER vacio & combo de materias
        doubleClickOn("#textFieldTitulo");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(tituloEditado);
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
        Label labelFecha=lookup("#labelFecha").queryAs(Label.class);
        assertThat(labelFecha.getTextFill(),is(Color.web("red")));
    }
    /**
     * Compreba que solo al meter un campo de fecha puede pulsar el mensaje
     * de que ya a sido modificado.
     */
    @Test
    public void testF_crearApunte(){
        //Comprueba que funcione el datePicker
        clickOn("#datePickerFecha");
        write("asdasdasd");
        clickOn("#btnModificar");
        Label labelFecha=lookup("#labelFecha").queryAs(Label.class);
        assertThat(labelFecha.getTextFill(),is(Color.web("red")));
        clickOn("#datePickerFecha");
        write("24/01/2020");
        clickOn("#btnModificar");
        assertThat(labelFecha.getTextFill(),is(Color.web("black")));
        push(KeyCode.SPACE);
        
    }
    /**
     * Comprueba el borrado
     */
    @Test
    public void testG_crearApunte(){
        //Comprobar borrado
        push(KeyCode.TAB);
        push(KeyCode.DOWN);
        table=lookup("#tableApuntes").queryTableView();
        buscarApunte(tituloEditado);
        clickOn("#btnBorrar");
        push(KeyCode.SPACE);
        push(KeyCode.SPACE);
        verifyThat("#btnModificar",isDisabled());
        verifyThat("#btnBorrar",isDisabled());
        verifyThat("#labelTitulo",hasText("Titulo"));
        verifyThat("#labelDesc",hasText("Descripción"));
        verifyThat("#labelFecha",hasText("Fecha de validación"));
        Label labelFecha=lookup("#labelFecha").queryAs(Label.class);
        assertThat(labelFecha.getTextFill(),is(Color.web("black")));
        verifyThat("#labelMateria",hasText("Materia"));
        
    }
    /**
     * Comprueba en este punto de todas las pruebas a dar al botón de refrescar.
     */
    @Test
    public void testH_crearApunte(){
        clickOn("#btnRefrescar");
        
    }
    /**
     * Permite buscar un apunte en la tabla de apuntes.
     * @param frase El titulo de apunte a buscar.
     */
    private void buscarApunte(String frase) {
        boolean encontrado=false;
        while(!encontrado){
            ApunteBean apunteSeleccionado=(ApunteBean) table.getSelectionModel().getSelectedItem();
            if(apunteSeleccionado.getTitulo().equals(frase)){
                break;
            }else{
                push(KeyCode.DOWN);
            }
        }
    }
    
}
