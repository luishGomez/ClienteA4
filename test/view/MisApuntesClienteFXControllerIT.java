/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package view;

import clientea4.ClienteA4;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import transferObjects.ApunteBean;

/**
 *
 * @author 2dam
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MisApuntesClienteFXControllerIT extends ApplicationTest{
    private final int MAX_PRECIO=100000;
    private final float MIN_PRECIO=(float) 0.30;
    private final String tituloApunte="El apunte test";
    private final String descApunte="La descripción del apunte test";
    private final String tituloEditado="Titulo del apunte editado";
    private final String precio="12";
    private ListView listViewApuntes;
    private final String fraseTitulo="Titulo";
    private final String frasePrecio="Precio";
    private final String fraseErrorTitulo="Titulo (Min 3 car. | Max 250 car.)";
    private final String fraseErrorPrecioNoNumerico="Precio (Tiene que ser un valor numerico)";
    private final String fraseErrorPrecioMinimo="Precio (No puede valer menos de  "+MIN_PRECIO+"€)";
    private final String fraseErrorPrecioMaximo="Precio (No puede superar los "+MAX_PRECIO+"€)";
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
    
    public MisApuntesClienteFXControllerIT() {
        
    }
    /**
     * Inicia sesión como cliente
     */
    @Test
    public void testA_MisApuntes() {
        //CrearBien
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
        
        
    }
    /**
     * Comprueba los valores menores al minimo
     */
    @Test
    public void testB_MisApuntes() {
        clickOn("#btnSubirApunte");
        verifyThat("#lblTitulo",hasText("Titulo"));
        verifyThat("#lblDesc",hasText("Descripción"));
        verifyThat("#lblMateria",hasText("Materia"));
        verifyThat("#lblPrecio",hasText("Precio"));
        verifyThat("#lblArchivo",hasText("Archivo"));
        write("1");
        clickOn("#textDesc");
        write("1");
        clickOn("#btnSubirElApunte");
        verifyThat("#lblTitulo",hasText("Titulo (Min 3 car. | Max 250 car.)"));
        Label labelTitulo=lookup("#lblTitulo").queryAs(Label.class);
        assertThat(labelTitulo.getTextFill(),is(Color.web("red")));
        
        verifyThat("#lblDesc",hasText("Descripción (Min 3 car. | Max 250 car.)"));
        Label labelDesc=lookup("#lblDesc").queryAs(Label.class);
        assertThat(labelDesc.getTextFill(),is(Color.web("red")));
        
        verifyThat("#lblMateria",hasText("Materia (Tienes que seleccionar una materia)"));
        Label labelMateria=lookup("#lblMateria").queryAs(Label.class);
        assertThat(labelMateria.getTextFill(),is(Color.web("red")));
        
        verifyThat("#lblPrecio",hasText("Precio (Tiene que ser un valor numerico)"));
        Label labelPrecio=lookup("#lblPrecio").queryAs(Label.class);
        assertThat(labelPrecio.getTextFill(),is(Color.web("red")));
        
        verifyThat("#lblArchivo",hasText("Archivo (Tienes que seleccionar algun pdf para subir un apunte)"));
        Label labelArchivo=lookup("#lblArchivo").queryAs(Label.class);
        assertThat(labelArchivo.getTextFill(),is(Color.web("red")));
        
        clickOn("#textFieldPrecio");
        write("0.2");
        clickOn("#btnSubirElApunte");
        verifyThat("#lblPrecio",hasText("Precio (No puede valer menos de  "+MIN_PRECIO+"€)"));
        assertThat(labelPrecio.getTextFill(),is(Color.web("red")));
        clickOn("#textFieldPrecio");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("0,2");
        clickOn("#btnSubirElApunte");
        verifyThat("#lblPrecio",hasText("Precio (No puede valer menos de  "+MIN_PRECIO+"€)"));
        assertThat(labelPrecio.getTextFill(),is(Color.web("red")));
        
    }
    @Test
    public void testC_MisApuntes() {
        clickOn("#textFieldTitulo");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(MAX_TEXT);
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.CONTROL,KeyCode.C);
        clickOn("#textDesc");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.CONTROL,KeyCode.V);
        clickOn("#textFieldPrecio");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("99999999999999999999999");
        clickOn("#btnSubirElApunte");
        verifyThat("#lblTitulo",hasText("Titulo (Min 3 car. | Max 250 car.)"));
        Label labelTitulo=lookup("#lblTitulo").queryAs(Label.class);
        assertThat(labelTitulo.getTextFill(),is(Color.web("red")));
        
        verifyThat("#lblDesc",hasText("Descripción (Min 3 car. | Max 250 car.)"));
        Label labelDesc=lookup("#lblDesc").queryAs(Label.class);
        assertThat(labelDesc.getTextFill(),is(Color.web("red")));
        
        verifyThat("#lblMateria",hasText("Materia (Tienes que seleccionar una materia)"));
        Label labelMateria=lookup("#lblMateria").queryAs(Label.class);
        assertThat(labelMateria.getTextFill(),is(Color.web("red")));
        
        verifyThat("#lblArchivo",hasText("Archivo (Tienes que seleccionar algun pdf para subir un apunte)"));
        Label labelArchivo=lookup("#lblArchivo").queryAs(Label.class);
        assertThat(labelArchivo.getTextFill(),is(Color.web("red")));
        
        verifyThat("#lblPrecio",hasText("Precio (No puede superar los "+MAX_PRECIO+"€)"));
        Label labelPrecio=lookup("#lblPrecio").queryAs(Label.class);
        assertThat(labelPrecio.getTextFill(),is(Color.web("red")));
        
        
    }
    /**
     * Crea un apunte
     */
    @Test
    public void testE_MisApuntes() {
        clickOn("#textFieldTitulo");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(tituloApunte);
        clickOn("#comboBoxMateria");
        push(KeyCode.DOWN);
        push(KeyCode.ENTER);
        clickOn("#textDesc");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(descApunte);
        clickOn("#btnSeleccionarArchivo");
        applyPath("F:\\Descargas\\PRUEBAS\\Nueva carpeta\\hi1.pdf");
        clickOn("#textFieldPrecio");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(precio);
        clickOn("#btnSubirElApunte");
        push(KeyCode.SPACE);
        
    }
    /**
     * Comprueba los valores menores al minimo en la modificación del apunte.
     */
    @Test
    public void testF_MisApuntes(){
        //Crear minimo
        listViewApuntes=lookup("#listViewApuntes").queryListView();
        clickOn("#listViewApuntes");
        push(KeyCode.DOWN);
        push(KeyCode.UP);
        buscarApunte(tituloApunte);
        clickOn("#btnModificar");
        //minimos
        push(KeyCode.CONTROL,KeyCode.A);
        write("1");
        clickOn("#textFieldPrecio");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("0.20");
        clickOn("#comboBoxMateriaMod");
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        clickOn("#btnModificarMod");
        push(KeyCode.SPACE);
        verifyThat("#labelPrecio",hasText(this.fraseErrorPrecioMinimo));
        Label labelPrecio=lookup("#labelPrecio").queryAs(Label.class);
        assertThat(labelPrecio.getTextFill(),is(Color.web("red")));
        verifyThat("#labelTituloMod",hasText(this.fraseErrorTitulo));
        Label labelTitulo=lookup("#labelTituloMod").queryAs(Label.class);
        assertThat(labelPrecio.getTextFill(),is(Color.web("red")));
        
    }
    /**
     * Comprueba los valores superiores al maximo en la modificación de un apunte.
     */
    @Test
    public void testG_MisApuntes(){
        clickOn("#textFieldPrecio");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("9999999999999999");
        clickOn("#textFieldTitulo");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(MAX_TEXT);
        clickOn("#btnModificarMod");
        push(KeyCode.SPACE);
        verifyThat("#labelPrecio",hasText(this.fraseErrorPrecioMaximo));
        Label labelPrecio=lookup("#labelPrecio").queryAs(Label.class);
        assertThat(labelPrecio.getTextFill(),is(Color.web("red")));
        verifyThat("#labelTituloMod",hasText(this.fraseErrorTitulo));
        Label labelTitulo=lookup("#labelTituloMod").queryAs(Label.class);
        assertThat(labelPrecio.getTextFill(),is(Color.web("red")));
        
    }
    @Test
    public void testH_MisApuntes(){
        clickOn("#textFieldTitulo");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        clickOn("#textFieldPrecio");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("MI SALDO");
        clickOn("#btnModificarMod");
        push(KeyCode.SPACE);
        verifyThat("#labelPrecio",hasText(this.fraseErrorPrecioNoNumerico));
        Label labelPrecio=lookup("#labelPrecio").queryAs(Label.class);
        assertThat(labelPrecio.getTextFill(),is(Color.web("red")));
        clickOn("#textFieldPrecio");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("5.59");
        clickOn("#btnModificarMod");
        push(KeyCode.SPACE);
        verifyThat("#labelPrecio",hasText(this.frasePrecio));
        assertThat(labelPrecio.getTextFill(),is(Color.web("black")));
        clickOn("#textFieldPrecio");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("5,59");
        clickOn("#btnModificarMod");
        push(KeyCode.SPACE);
        verifyThat("#labelPrecio",hasText(this.frasePrecio));
        assertThat(labelPrecio.getTextFill(),is(Color.web("black")));
        
    }
    /**
     * Cre al apunte
     */
    @Test
    public void testI_MisApuntes(){
        clickOn("#textFieldTitulo");
        write(tituloEditado);
        clickOn("#btnModificarMod");
        push(KeyCode.SPACE);
        push(KeyCode.SPACE);
        listViewApuntes=lookup("#listViewApuntes").queryListView();
        clickOn("#listViewApuntes");
        push(KeyCode.SPACE);
        push(KeyCode.DOWN);
        push(KeyCode.UP);
        buscarApunte(tituloEditado);
        clickOn("#btnModificar");
        clickOn("#btnEliminar");
        push(KeyCode.SPACE);
        push(KeyCode.SPACE);
        clickOn("#btnRefrescar");
        
        
    }
    private void applyPath(String filePath){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(filePath);
        clipboard.setContents(stringSelection, stringSelection);
        press(KeyCode.CONTROL).press(KeyCode.V).release(KeyCode.V).release(KeyCode.CONTROL);
        push(KeyCode.ENTER);
    }
    private void buscarApunte(String frase) {
        boolean encontrado=false;
        while(!encontrado){
            ApunteBean apunteSeleccionado=(ApunteBean) listViewApuntes.getSelectionModel().getSelectedItem();
            if(apunteSeleccionado.getTitulo().equals(frase)){
                break;
            }else{
                push(KeyCode.DOWN);
            }
        }
    }
    
    
    
}