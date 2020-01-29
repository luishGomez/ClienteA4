package view;

import javafx.scene.control.TableView;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;

/**
 * Clase 
 * @author Luis
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GestorDeMateriasFXControllerIT extends ApplicationTest{
    
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
    public GestorDeMateriasFXControllerIT() {
    }
    @Override
    public void start(Stage stage) throws Exception{
        new ClienteA4().start(stage);
    }
    
}
