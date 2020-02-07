/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import clientea4.ClienteA4;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import transferObjects.PackBean;

/**
 *
 * @author Luis
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GestorDePacksFXControllerIT extends ApplicationTest{
    
    private final String tituloPack = "Pack Basico Programación";
    private final String descripcionPack = "Programación Basica de Java y JavaFX";
    private final String tituloModificadoPack = "Pack Avanzado Programación";
    private final String descripcionModificadaPack = "Programación Avanzado de Java y JavaFX";
    private TableView table;
    private TextField tfTitulo;
    private TextField tfDescripcion;
    private DatePicker datePicker;
    private PackBean pack;
    private Integer idPack;
    private final String fechaModificada = "26/01/2020";
    private final String fechaEsperada = "2020-01-26";
    
    public GestorDePacksFXControllerIT() {
    }
    
    @Override
    public void start(Stage stage) throws Exception{
        new ClienteA4().start(stage);
    }
    
    @Test
    public void testA_CrearPack(){
        verifyThat("#btnAcceder",isDisabled());
        clickOn("#tfNombreUsuario");
        write("admin");
        clickOn("#tfContra");
        write("abcd*1234");
        verifyThat("#btnAcceder",isEnabled());
        clickOn("#btnAcceder");
        //Gestor Pack
        push(KeyCode.ALT);
        push(KeyCode.RIGHT);
        for(int i = 0; i < 3; i++){
            push(KeyCode.DOWN);
        }
        push(KeyCode.SPACE);
        verifyThat("#btnCrearGestorPack",isEnabled());
        verifyThat("#btnInformeGestorPack",isEnabled());
        verifyThat("#btnBuscarGestorPack",isEnabled());
        verifyThat("#btnModificarApunteGestorPack",isEnabled());
        verifyThat("#btnModificarGestorPack",isEnabled());
        verifyThat("#btnEliminarGestorPack",isEnabled());
        clickOn("#btnCrearGestorPack");
        write(tituloPack);
        clickOn("#tfDescripcionCrearPack");
        write(descripcionPack);
        clickOn("#btnCrearCrearPack");
        clickOn("#tfFiltrarGestorPack");
        write(tituloPack);
        clickOn("#btnBuscarGestorPack");
        push(KeyCode.SPACE);
        table = lookup("#tablaPack").queryTableView();
        push(KeyCode.DOWN);
        push(KeyCode.SPACE);
        pack = (PackBean) table.getSelectionModel().getSelectedItem();
        idPack = pack.getIdPack();
        tfTitulo = lookup("#tfTituloGestorPack").queryAs(TextField.class);
        tfDescripcion = lookup("#tfDescripcionGestorPack").queryAs(TextField.class);
        datePicker = lookup("#dpDate").queryAs(DatePicker.class);
        verifyThat(tfTitulo.getText().trim(),is(tituloPack));
        verifyThat(tfDescripcion.getText().trim(),is(descripcionPack));
        verifyThat(datePicker.getValue().toString(),is(dateToLocalDate(new Date()).toString()));
    }
    
    @Test
    public void testB_ModificarMateria() {
        //Gestor Pack
        push(KeyCode.ALT);
        push(KeyCode.RIGHT);
        for(int i = 0; i < 3; i++){
            push(KeyCode.DOWN);
        }
        push(KeyCode.SPACE);
        verifyThat("#btnCrearGestorPack",isEnabled());
        verifyThat("#btnInformeGestorPack",isEnabled());
        verifyThat("#btnBuscarGestorPack",isEnabled());
        verifyThat("#btnModificarApunteGestorPack",isEnabled());
        verifyThat("#btnModificarGestorPack",isEnabled());
        verifyThat("#btnEliminarGestorPack",isEnabled());
        clickOn("#tfFiltrarGestorPack");
        write(tituloPack);
        clickOn("#btnBuscarGestorPack");
        push(KeyCode.SPACE);
        table = lookup("#tablaPack").queryTableView();
        push(KeyCode.DOWN);
        push(KeyCode.SPACE);
        clickOn("#tfTituloGestorPack");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(tituloModificadoPack);
        clickOn("#tfDescripcionGestorPack");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(descripcionModificadaPack);
        clickOn("#dpDate");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(fechaModificada);
        clickOn("#btnModificarGestorPack");
        clickOn("#tfFiltrarGestorPack");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(tituloModificadoPack);
        clickOn("#btnBuscarGestorPack");
        table = lookup("#tablaPack").queryTableView();
        push(KeyCode.DOWN);
        push(KeyCode.SPACE);
        tfTitulo = lookup("#tfTituloGestorPack").queryAs(TextField.class);
        tfDescripcion = lookup("#tfDescripcionGestorPack").queryAs(TextField.class);
        datePicker = lookup("#dpDate").queryAs(DatePicker.class);
        verifyThat(tfTitulo.getText().trim(),is(tituloModificadoPack));
        verifyThat(tfDescripcion.getText().trim(),is(descripcionModificadaPack));
        verifyThat(datePicker.getValue().toString(),is(fechaEsperada));
    }
    
    @Test
    public void testC_EliminarMateria() throws Exception{
        //Gestor Pack
        push(KeyCode.ALT);
        push(KeyCode.RIGHT);
        for(int i = 0; i < 3; i++){
            push(KeyCode.DOWN);
        }
        push(KeyCode.SPACE);
        verifyThat("#btnCrearGestorPack",isEnabled());
        verifyThat("#btnInformeGestorPack",isEnabled());
        verifyThat("#btnBuscarGestorPack",isEnabled());
        verifyThat("#btnModificarApunteGestorPack",isEnabled());
        verifyThat("#btnModificarGestorPack",isEnabled());
        verifyThat("#btnEliminarGestorPack",isEnabled());
        clickOn("#tfFiltrarGestorPack");
        write(tituloModificadoPack);
        clickOn("#btnBuscarGestorPack");
        table = lookup("#tablaPack").queryTableView();
        push(KeyCode.DOWN);
        push(KeyCode.SPACE);
        tfTitulo = lookup("#tfTituloGestorPack").queryAs(TextField.class);
        tfDescripcion = lookup("#tfDescripcionGestorPack").queryAs(TextField.class);
        datePicker = lookup("#dpDate").queryAs(DatePicker.class);
        verifyThat(tfTitulo.getText().trim(),is(tituloModificadoPack));
        verifyThat(tfDescripcion.getText().trim(),is(descripcionModificadaPack));
        verifyThat(datePicker.getValue().toString(),is(fechaEsperada));
        clickOn("#btnEliminarGestorPack");
        push(KeyCode.SPACE);
        verifyThat(tfTitulo.getText().trim(),is(""));
        verifyThat(tfDescripcion.getText().trim(),is(""));
        verifyThat(datePicker.getValue().toString(),is(dateToLocalDate(new Date()).toString()));
        //Gestor Pack
        push(KeyCode.ALT);
        push(KeyCode.RIGHT);
        for(int i = 0; i < 3; i++){
            push(KeyCode.DOWN);
        }
        push(KeyCode.SPACE);
        table = lookup("#tablaPack").queryTableView();
        tfTitulo = lookup("#tfTituloGestorPack").queryAs(TextField.class);
        for(int i = 0; i < table.getItems().size(); i++){
            push(KeyCode.DOWN);
            push(KeyCode.SPACE);
            if(tfTitulo.getText().trim() == tituloModificadoPack){
                throw new Exception("No se ha borrado el pack.");
            }
        }
    }
    
    public LocalDate dateToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
