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
import java.util.ResourceBundle;
import javafx.scene.control.ComboBox;
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
 * Esta clase prueba la ventana de mis apuntes y el proceso de modificar y subir un apunte.
 * @author Ricardo Peinado Lastra
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MisApuntesClienteFXControllerIT extends ApplicationTest{
    private static ResourceBundle configFile=ResourceBundle.getBundle("view.testConfig");
    private static final String urlFichero = configFile.getString("fichero");
    private final int MAX_PRECIO=100000;
    private final float MIN_PRECIO=(float) 0.30;
    private final String tituloApunte="El apunte test";
    private final String descApunte="La descripción del apunte test";
    private final String tituloEditado="Titulo del apunte editado";
    private final String precio="12";
    private ListView listViewApuntes;
    private ListView listViewMateria;
    private final String fraseTitulo="Titulo";
    private ComboBox comboBoxOrdenar;
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
     * Comprueba los valores menores al minimo.
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
    /**
     * Comprueba los valores mayores al maximo.
     */
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
        applyPath(urlFichero);
        clickOn("#textFieldPrecio");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        write(precio);
        clickOn("#btnSubirElApunte");
        push(KeyCode.SPACE);
        
    }
    /**
     * Comprueba la función de filtrado de las materias, nombre y orden que se le diga a la ventana.
     * @throws Exception
     */
    @Test
    public void testF_MisApuntes() throws Exception {
        //Por materia
        listViewApuntes=lookup("#listViewApuntes").queryListView();
        listViewMateria=lookup("#listViewMateria").queryListView();
        clickOn("#listViewMateria");
        push(KeyCode.SPACE);
        subirAlPrimeraMateria();
        push(KeyCode.DOWN);
        String materia="";
        listViewApuntes.getSelectionModel().select(0);
        int numeroMaterias=listViewMateria.getItems().size();
        for(int i=1;i<numeroMaterias;i++){
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
        while(listViewMateria.getSelectionModel().getSelectedIndex()!=0){
            push(KeyCode.UP);
        }
        clickOn("#textFieldBuscar");
        write(tituloApunte);
        push(KeyCode.ENTER);
        if(listViewApuntes.getItems().size()<1){
            throw new Exception("Tendrian que aparecer los dos apuntes.");
        }
        //Por orden
        comboBoxOrdenar=lookup("#comboBoxOrdenar").queryComboBox();
        clickOn("#comboBoxOrdenar");
        while(comboBoxOrdenar.getSelectionModel().getSelectedIndex()!=3){
            push(KeyCode.DOWN);
        }
        push(KeyCode.SPACE);
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
        clickOn("#comboBoxOrdenar");
        while(comboBoxOrdenar.getSelectionModel().getSelectedIndex()!=0){
            push(KeyCode.UP);
        }
        push(KeyCode.SPACE);
        clickOn("#listViewMateria");
        while(listViewMateria.getSelectionModel().getSelectedIndex()!=0){
            push(KeyCode.UP);
        }
        push(KeyCode.SPACE);
        clickOn("#textFieldBuscar");
        push(KeyCode.CONTROL,KeyCode.A);
        push(KeyCode.BACK_SPACE);
        push(KeyCode.ENTER);
        
        
    }
    /**
     * Comprueba los valores menores al minimo en la modificación del apunte.
     */
    @Test
    public void testG_MisApuntes(){
        //Crear minimo
        listViewApuntes=lookup("#listViewApuntes").queryListView();
        clickOn("#listViewApuntes");
        push(KeyCode.SPACE);
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
    public void testH_MisApuntes(){
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
    /**
     * Comprueba los valores accesibles del precio.
     */
    @Test
    public void testI_MisApuntes(){
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
     * Modifica bien un apunte y acontinuación lo borra.
     */
    @Test
    public void testJ_MisApuntes(){
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
    /**
     * Permite insertar la ruta para leer el fichero.
     * @param filePath
     */
    private void applyPath(String filePath){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(filePath);
        clipboard.setContents(stringSelection, stringSelection);
        press(KeyCode.CONTROL).press(KeyCode.V).release(KeyCode.V).release(KeyCode.CONTROL);
        push(KeyCode.ENTER);
    }
    /**
     * Busca un apunte en la lista de apuntes.
     * @param frase El titulo del apunte a buscar.
     */
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
    private void subirAlPrimeraMateria() {
         while(this.listViewMateria.getSelectionModel().getSelectedIndex()!=0){
            push(KeyCode.UP);
        }
    }
    
    
}
