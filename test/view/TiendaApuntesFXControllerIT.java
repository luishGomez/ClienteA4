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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
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
import transferObjects.ApunteBean;
import transferObjects.MateriaBean;

/**
 *
 * @author Usuario
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TiendaApuntesFXControllerIT  extends ApplicationTest{
    private final String tituloApunteCaro ="El apunte caro";
    private final String tituloApunteBarato ="El apunte barato";
    private final String descApunte="La descripción del apunte test";
    private final String precioCaro="99999";
    private final String precioBarato="1";
    private ListView listViewApuntes;
    private ListView listViewMateria;
    private ComboBox comboBoxOrdenar;
    public TiendaApuntesFXControllerIT() {
    }
    @Override
    public void start(Stage stage) throws Exception{
        new ClienteA4().start(stage);
    }
    //@Test
    public void testA_TiendaApuntesFXControllerIT() {
        verifyThat("#btnAcceder",isDisabled());
        clickOn("#tfNombreUsuario");
        write("test11888");
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
        //Crear el apunte caro
        clickOn("#btnSubirApunte");
        write(tituloApunteCaro);
        clickOn("#comboBoxMateria");
        push(KeyCode.DOWN);
        push(KeyCode.ENTER);
        clickOn("#textDesc");
        write(descApunte);
        clickOn("#btnSeleccionarArchivo");
        applyPath("F:\\Descargas\\PRUEBAS\\Nueva carpeta\\hi1.pdf");
        clickOn("#textFieldPrecio");
        write(precioCaro);
        clickOn("#btnSubirElApunte");
        push(KeyCode.SPACE);
        //El segundo apunte
        clickOn("#btnSubirApunte");
        write(tituloApunteBarato);
        clickOn("#comboBoxMateria");
        push(KeyCode.DOWN);
        push(KeyCode.ENTER);
        clickOn("#textDesc");
        write(descApunte);
        clickOn("#btnSeleccionarArchivo");
        applyPath("F:\\Descargas\\PRUEBAS\\Nueva carpeta\\hi1.pdf");
        clickOn("#textFieldPrecio");
        write(precioBarato);
        clickOn("#btnSubirElApunte");
        push(KeyCode.SPACE);
        push(KeyCode.CONTROL,KeyCode.ALT,KeyCode.C);
        push(KeyCode.SPACE);
        
    }
    @Test
    public void testB_TiendaApuntesFXControllerIT() {
        
        clickOn("#tfNombreUsuario");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write("test19993");
        clickOn("#tfContra");
        write("123");
        verifyThat("#btnAcceder",isEnabled());
        clickOn("#btnAcceder");
        //Acceder a tienda de apuntes
        push(KeyCode.ALT);
        push(KeyCode.RIGHT);
        for(int i=0;i<3;i++){
            push(KeyCode.DOWN);
        }
        push(KeyCode.SPACE);
        
    }
    @Test
    public void testC_TiendaApuntesFXControllerIT() throws Exception {
        //Por materia
        listViewApuntes=lookup("#listViewApuntes").queryListView();
        listViewMateria=lookup("#listViewMateria").queryListView();
        clickOn("#listViewMateria");
        push(KeyCode.DOWN);
        String materia="";
        listViewApuntes.getSelectionModel().select(0);
        int numeroMaterias=listViewMateria.getItems().size();
        for(int i=1;i<numeroMaterias;i++){
            //listViewMateria.getSelectionModel().select(i);
            push(KeyCode.DOWN);
            materia=listViewMateria.getSelectionModel().toString();
            for(Object a:listViewApuntes.getItems()){
                ApunteBean apunte =(ApunteBean)a;
                if(apunte.getTitulo().equals(materia)){
                    throw new Exception("No tendria que haber ningun apunte de esa materia");
                }
            }
        }
        //Por nombre
        int i=0;
        //listViewMateria.getSelectionModel().select(0);
        while(listViewMateria.getSelectionModel().getSelectedIndex()!=0){
            push(KeyCode.UP);
        }
        clickOn("#textFieldBuscar");
        write("El apunte");
        push(KeyCode.ENTER);
        if(listViewApuntes.getItems().size()<2){
            throw new Exception("Tendrian que aparecer los dos apuntes.");
        }
        //Por orden
        comboBoxOrdenar=lookup("#comboBoxOrdenar").queryComboBox();
        clickOn("#comboBoxOrdenar");
        while(comboBoxOrdenar.getSelectionModel().getSelectedIndex()!=3){
           push(KeyCode.DOWN);
        }
        push(KeyCode.SPACE);
        //comboBoxOrdenar.getSelectionModel().select(3);
        float precioTop=(float) 0.0;
        for(Object a:listViewApuntes.getItems()){
            ApunteBean apunte =(ApunteBean)a;
            if(precioTop<=apunte.getPrecio()){
                precioTop=apunte.getPrecio();
            }else{
                throw new Exception("Tendrian que tener otro orden");
            }
        }
        clickOn("#comboBoxOrdenar");
        while(comboBoxOrdenar.getSelectionModel().getSelectedIndex()!=4){
           push(KeyCode.DOWN);
        }
        push(KeyCode.SPACE);
        float precioMin=(float) 99999999999999999999999.0;
        for(Object a:listViewApuntes.getItems()){
            ApunteBean apunte =(ApunteBean)a;
            if(precioMin>=apunte.getPrecio()){
                precioMin=apunte.getPrecio();
            }else{
                throw new Exception("Tendrian que tener otro orden");
            }
        }
        
        
    }
    @Test
    public void testD_TiendaApuntesFXControllerIT() {
        clickOn("#listViewApuntes");
        push(KeyCode.SPACE);
        listViewApuntes=lookup("#listViewApuntes").queryListView();
        for(Object a:listViewApuntes.getItems()){
            ApunteBean apunte =(ApunteBean)listViewApuntes.getSelectionModel().getSelectedItem();
            if(apunte.getTitulo().equals(tituloApunteCaro)){
                clickOn("#btnComprar");
                verifyThat("#btnComprarApunte",isDisabled());
                clickOn("#btnCancelar");
            }else if(apunte.getTitulo().equals(tituloApunteBarato)){
                clickOn("#btnComprar");
                verifyThat("#btnComprarApunte",isEnabled());
                clickOn("#btnCancelar");
            }
            clickOn("#listViewApuntes");
            push(KeyCode.DOWN);
        }
    
    }
    private void applyPath(String filePath){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(filePath);
        clipboard.setContents(stringSelection, stringSelection);
        press(KeyCode.CONTROL).press(KeyCode.V).release(KeyCode.V).release(KeyCode.CONTROL);
        push(KeyCode.ENTER);
    }
}
