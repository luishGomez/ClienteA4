/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import clientea4.ClienteA4;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.Label;
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
import transferObjects.PackBean;

/**
 *
 * @author Luis
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TiendaPackFXControllerIT extends ApplicationTest{
    
    private ListView list;
    private PackBean pack;
    private float precio;

    
    public TiendaPackFXControllerIT() {
    }
    
    @Override
    public void start(Stage stage) throws Exception{
        new ClienteA4().start(stage);
    }
    
    /**
     * Inicialización de ventana.
     */
    @Test
    public void testA_Inicializacion(){
        //Iniciar sesión.
        verifyThat("#btnAcceder",isDisabled());
        clickOn("#tfNombreUsuario");
        write("test19993");
        clickOn("#tfContra");
        write("123");
        verifyThat("#btnAcceder",isEnabled());
        clickOn("#btnAcceder");
        //Tienda Pack
        push(KeyCode.ALT);
        push(KeyCode.RIGHT);
        for(int i = 0; i < 4; i++){
            push(KeyCode.DOWN);
        }
        push(KeyCode.SPACE);
        verifyThat("#btnBuscarTiendaPack",isEnabled());
        verifyThat("#btnComprarTiendaPack",isEnabled());
        verifyThat("#cbFiltrado",isEnabled());
    }
}
