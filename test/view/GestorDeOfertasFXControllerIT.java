/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package view;

import clientea4.ClienteA4;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import transferObjects.OfertaBean;

/**
 *
 * @author 2dam
 */
public class GestorDeOfertasFXControllerIT extends ApplicationTest{
    
    private final String FECHA_INICIO = "31/01/2020";
    private final String FECHA_FIN = "10/02/2020";
    private final String NOMBRE_OFERTA = "testOferta";
    private final String DESCUENTO = "20";
    private DatePicker dateInicio;
    private DatePicker dateFin;
    private TextField txtOfertaNombre;
    private TextField txtDescuento;
    private Button btnAceptar;
    private Button btnBuscar;
    private Button btnModificar;
    private Button btnBorrar;
    private Button btnInsertar;
    private TextField txtBuscar;
    private TableView tablaGestorOfertas;
    @Override
    public void start(Stage stage) throws Exception{
        new ClienteA4().start(stage);
        dateInicio = lookup("#dateInicio").query();
        dateFin = lookup("#dateFin").query();
        txtOfertaNombre =  lookup("#txtOfertaNombre").query();
        txtDescuento =  lookup("#txtDescuento").query();
        btnAceptar = lookup("#btnAceptar").queryButton();
        btnBuscar = lookup("#btnBuscar").query();
        btnModificar = lookup("#btnModificar").query();
        btnBorrar = lookup("#btnBorrar").query();
        txtBuscar = lookup("#txtBuscar").query();
        btnInsertar = lookup("#btnInsertar").query();
        tablaGestorOfertas = lookup("#tablaGestorOfertas").queryTableView();
    }
    @Test
    public void testA_Login() {
        clickOn("#tfNombreUsuario");
        write("admin");
        clickOn("#tfContra");
        write("abcd*1234");
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
    public void testB_verificacionDeCampos(){
        
        Assert.assertFalse(dateInicio.isVisible());
        Assert.assertFalse(dateFin.isVisible());
        Assert.assertFalse(txtDescuento.isVisible());
        Assert.assertFalse(txtOfertaNombre.isVisible());
        Assert.assertFalse(btnAceptar.isVisible());
        
    }
    @Test
    public void testC_verificarInsertarCorrectamente() throws Exception{
        int rowCount = tablaGestorOfertas.getItems().size();
        
        clickOn(btnInsertar);
        Assert.assertTrue(dateInicio.isVisible());
        Assert.assertTrue(dateFin.isVisible());
        Assert.assertTrue(txtDescuento.isVisible());
        Assert.assertTrue(txtOfertaNombre.isVisible());
        
        clickOn(dateInicio);
        write(FECHA_INICIO);
        clickOn(dateFin);
        write(FECHA_FIN);
        clickOn(txtDescuento);
        write(DESCUENTO);
        clickOn(txtOfertaNombre);
        write(NOMBRE_OFERTA);
        clickOn(btnAceptar);
        
        if(rowCount == tablaGestorOfertas.getItems().size())
            throw new Exception("no se ha podido actualizar");
        
        Assert.assertFalse(dateInicio.isVisible());
        Assert.assertFalse(dateFin.isVisible());
        Assert.assertFalse(txtDescuento.isVisible());
        Assert.assertFalse(txtOfertaNombre.isVisible());
    }
    @Test
    public void testD_verificarModificarOferta() throws Exception{
        Node row=lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        clickOn(btnModificar);
        
        Assert.assertTrue(dateInicio.isVisible());
        Assert.assertTrue(dateFin.isVisible());
        Assert.assertTrue(txtDescuento.isVisible());
        Assert.assertTrue(txtOfertaNombre.isVisible());
        
        OfertaBean oferta = (OfertaBean)tablaGestorOfertas.getSelectionModel()
                .getSelectedItem();
        
        doubleClickOn(txtDescuento);
        write(DESCUENTO);
        doubleClickOn(txtOfertaNombre);
        write(NOMBRE_OFERTA);
        clickOn(btnAceptar);
        
        if(!oferta.getTitulo().trim().equals(txtOfertaNombre.getText().trim()) && oferta.getRebaja() != Float.valueOf(DESCUENTO))
            throw new Exception("No se ha actualizado");
        Assert.assertFalse(dateInicio.isVisible());
        Assert.assertFalse(dateFin.isVisible());
        Assert.assertFalse(txtDescuento.isVisible());
        Assert.assertFalse(txtOfertaNombre.isVisible());
    }
    @Test
    public void testE_borrarOferta() throws Exception{
        int rowCountSearch;
        int rowCount=tablaGestorOfertas.getItems().size();
        Node row=lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        clickOn(btnBorrar);
        press(KeyCode.ENTER);
        rowCountSearch =tablaGestorOfertas.getItems().size();
        if(rowCount == rowCountSearch)
            throw new Exception("No ha Borrado.");
    }
    
}
