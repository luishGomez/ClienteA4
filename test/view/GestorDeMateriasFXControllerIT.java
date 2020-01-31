package view;

import clientea4.ClienteA4;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.hamcrest.CoreMatchers.is;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;

/**
 * 
 * @author Luis
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GestorDeMateriasFXControllerIT extends ApplicationTest{
    
    private final String tituloMateria = "Programación Basica";
    private final String descripcionMateria = "Programación Basica de Java.";
    private final String tituloModificadoMateria = "Programación Avanzada";
    private final String descripcionModificadaMateria = "Programación Avanzada de Java.";
    private TableView table;
    private TextField tfTitulo;
    private TextField tfDescripcion;
    
    public GestorDeMateriasFXControllerIT() {
    }
    
    @Override
    public void start(Stage stage) throws Exception{
        new ClienteA4().start(stage);
    }
    
    @Test
    public void testA_CrearMateria(){
        verifyThat("#btnAcceder",isDisabled());
        clickOn("#tfNombreUsuario");
        write("admin");
        clickOn("#tfContra");
        write("abcd*1234");
        verifyThat("#btnAcceder",isEnabled());
        clickOn("#btnAcceder");
        //Gestor Materia
        push(KeyCode.ALT);
        push(KeyCode.RIGHT);
        for(int i = 0; i < 5; i++){
            push(KeyCode.DOWN);
        }
        push(KeyCode.SPACE);
        verifyThat("#btnCrearGestorMateria",isEnabled());
        verifyThat("#btnInformeGestorMateria",isEnabled());
        verifyThat("#btnBuscarGestorMateria",isEnabled());
        verifyThat("#btnModificarGestorMateria",isEnabled());
        verifyThat("#btnEliminarGestorMateria",isEnabled());
        clickOn("#btnCrearGestorMateria");
        write(tituloMateria);
        clickOn("#tfDescripcionCrearMateria");
        write(descripcionMateria);
        clickOn("#btnCrearCrearMateria");
        clickOn("#tfFiltrarGestorMateria");
        write(tituloMateria);
        clickOn("#btnBuscarGestorMateria");
        push(KeyCode.SPACE);
        table = lookup("#tablaMateria").queryTableView();
        push(KeyCode.DOWN);
        push(KeyCode.SPACE);
        tfTitulo = lookup("#tfTituloGestorMateria").queryAs(TextField.class);
        tfDescripcion = lookup("#tfDescripcionGestorMateria").queryAs(TextField.class);
        verifyThat(tfTitulo.getText().trim(),is(tituloMateria));
        verifyThat(tfDescripcion.getText().trim(),is(descripcionMateria));
    }
    
    @Test
    public void testB_ModificarMateria(){
        //Gestor Materia
        push(KeyCode.ALT);
        push(KeyCode.RIGHT);
        for(int i = 0; i < 5; i++){
            push(KeyCode.DOWN);
        }
        push(KeyCode.SPACE);
        verifyThat("#btnCrearGestorMateria",isEnabled());
        verifyThat("#btnInformeGestorMateria",isEnabled());
        verifyThat("#btnBuscarGestorMateria",isEnabled());
        verifyThat("#btnModificarGestorMateria",isEnabled());
        verifyThat("#btnEliminarGestorMateria",isEnabled());
        clickOn("#tfFiltrarGestorMateria");
        write(tituloMateria);
        clickOn("#btnBuscarGestorMateria");
        push(KeyCode.SPACE);
        table = lookup("#tablaMateria").queryTableView();
        push(KeyCode.DOWN);
        push(KeyCode.SPACE);
        clickOn("#tfTituloGestorMateria");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(tituloModificadoMateria);
        clickOn("#tfDescripcionGestorMateria");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(descripcionModificadaMateria);
        clickOn("#btnModificarGestorMateria");
        clickOn("#tfFiltrarGestorMateria");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(tituloModificadoMateria);
        clickOn("#btnBuscarGestorMateria");
        push(KeyCode.SPACE);
        table = lookup("#tablaMateria").queryTableView();
        push(KeyCode.DOWN);
        push(KeyCode.SPACE);
        tfTitulo = lookup("#tfTituloGestorMateria").queryAs(TextField.class);
        tfDescripcion = lookup("#tfDescripcionGestorMateria").queryAs(TextField.class);
        verifyThat(tfTitulo.getText().trim(),is(tituloModificadoMateria));
        verifyThat(tfDescripcion.getText().trim(),is(descripcionModificadaMateria));
    }
    
    @Test
    public void testC_EliminarMateria(){
        //Gestor Materia
        push(KeyCode.ALT);
        push(KeyCode.RIGHT);
        for(int i = 0; i < 5; i++){
            push(KeyCode.DOWN);
        }
        push(KeyCode.SPACE);
        verifyThat("#btnCrearGestorMateria",isEnabled());
        verifyThat("#btnInformeGestorMateria",isEnabled());
        verifyThat("#btnBuscarGestorMateria",isEnabled());
        verifyThat("#btnModificarGestorMateria",isEnabled());
        verifyThat("#btnEliminarGestorMateria",isEnabled());
        clickOn("#tfFiltrarGestorMateria");
        write(tituloModificadoMateria);
        clickOn("#btnBuscarGestorMateria");
        table = lookup("#tablaMateria").queryTableView();
        push(KeyCode.DOWN);
        push(KeyCode.SPACE);
        tfTitulo = lookup("#tfTituloGestorMateria").queryAs(TextField.class);
        tfDescripcion = lookup("#tfDescripcionGestorMateria").queryAs(TextField.class);
        verifyThat(tfTitulo.getText().trim(),is(tituloModificadoMateria));
        verifyThat(tfDescripcion.getText().trim(),is(descripcionModificadaMateria));
        clickOn("#btnEliminarGestorMateria");
        push(KeyCode.SPACE);
        verifyThat(tfTitulo.getText().trim(),is(""));
        verifyThat(tfDescripcion.getText().trim(),is(""));
    }
}
