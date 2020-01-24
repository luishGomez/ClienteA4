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
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
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
        clickOn("#btnSubirApunte");
        write(tituloApunte);
        clickOn("#comboBoxMateria");
        push(KeyCode.DOWN);
        push(KeyCode.ENTER);
        clickOn("#textDesc");
        write(descApunte);
        clickOn("#btnSeleccionarArchivo");
        applyPath("C:\\Workspace\\hi.pdf");
        clickOn("#textFieldPrecio");
        write(precio);
        clickOn("#btnSubirElApunte");
        push(KeyCode.SPACE);
        
    }
    @Test
    public void testB_MisApuntes(){
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
        verifyThat("#labelTituloMod",hasText(this.fraseErrorTitulo));
        
        
    }
    @Test
    public void testC_MisApuntes(){
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
        verifyThat("#labelTituloMod",hasText(this.fraseErrorTitulo));
        
    }
    @Test
    public void testD_MisApuntes(){
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
        clickOn("#textFieldPrecio");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("5.59");
        clickOn("#btnModificarMod");
        push(KeyCode.SPACE);
        verifyThat("#labelPrecio",hasText(this.frasePrecio));
        clickOn("#textFieldPrecio");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("5,59");
        clickOn("#btnModificarMod");
        push(KeyCode.SPACE);
        verifyThat("#labelPrecio",hasText(this.frasePrecio));
        
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
