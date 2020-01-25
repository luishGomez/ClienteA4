/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package view;

import businessLogic.ApunteManager;
import static businessLogic.ApunteManagerFactory.createApunteManager;
import businessLogic.BusinessLogicException;
import businessLogic.ClienteManager;
import static businessLogic.ClienteManagerFactory.createClienteManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import transferObjects.ApunteBean;
import transferObjects.ClienteBean;
import static view.ControladorGeneral.showErrorAlert;
import static view.TiendaApuntesFXController.setResultadoTiendaApuntes;

/**
 * La clase que controla la ventana de comprar un apunte.
 * @author Ricardo Peinado Lastra
 */
public class CompraApunteFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.CompraApunteFXController");
    private ClienteBean cliente=null;
    private ApunteBean apunte=null;
    private ApunteManager apunteLogic = createApunteManager("real");
    private ClienteManager clienteLogic = createClienteManager("real");
    private Stage stage;
    private float resultado;
    
    @FXML
    private Label labelTituloApunte;
    @FXML
    private Label labelSaldo;
    @FXML
    private Label labelPrecio;
    @FXML
    private Label labelResultado;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnComprarApunte;
    @FXML
    private Text textDescApunte;
    
    /**
     * El metodo que inicializa la ventana.
     * @param root El nodo raiz.
     */
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando la CopraApunteFXController");
            Scene scene=new Scene(root);
            stage=new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Comprar Apunte");
            stage.setResizable(false);
            btnComprarApunte.setMnemonicParsing(true);
            btnComprarApunte.setText("C_omprar");
            btnCancelar.setMnemonicParsing(true);
            btnCancelar.setText("_Cancelar");
            if(cliente.getSaldo()>=apunte.getPrecio())
                btnComprarApunte.setDisable(false);
            else
                btnComprarApunte.setDisable(true);
            this.labelPrecio.setText(apunte.getPrecio()+"€");
            this.labelSaldo.setText(cliente.getSaldo()+"€");
            resultado=cliente.getSaldo()-apunte.getPrecio();
            this.labelResultado.setText(resultado+"€");
            this.labelTituloApunte.setText(apunte.getTitulo());
            this.textDescApunte.setText(apunte.getDescripcion());
            stage.showAndWait();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    /**
     * Añade los datos de cliente y apunte a la ventana de compra.
     * @param cliente Los datos del cliente.
     * @param apunte Los datos del apunte.
     */
    public void setDatos(ClienteBean cliente,ApunteBean apunte){
        this.apunte=apunte;
        this.cliente=cliente;
    }
    /**
     * Metodo que cancela la compra.
     * @param event El evento de pulsación.
     */
    @FXML
    private void onActionCancelarCompra(ActionEvent event){
        stage.hide();
        setResultadoTiendaApuntes(0);
    }
    /**
     * Metodo que permite comprar el apunte.
     * @param event El evento de pulsación.
     */
    @FXML
    private void onActionComprarApunte(ActionEvent event){
        Alert alertCerrarSesion = new Alert(AlertType.CONFIRMATION);
        alertCerrarSesion.setTitle("Comprar apunte");
        alertCerrarSesion.setHeaderText("Una vez que compres el apunte "+apunte.getTitulo()+" no se podra devolver el producto");
        alertCerrarSesion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    clienteLogic.comprarApunte(cliente, apunte.getIdApunte());
                    cliente.setSaldo(resultado);
                    clienteLogic.edit(cliente);
                    ensenarAlertaInfo("Apunte comprado","Ve a la ventana tu biblioteca para poder descargarlo.");
                    stage.hide();
                    setResultadoTiendaApuntes(1);
                } catch (BusinessLogicException ex) {
                    LOGGER.severe("ERROR en la compra de apunte: "+ex.getMessage());
                    showErrorAlert("A ocurrio un error en la compra del apunte.");
                }
            }
        });
    }
    /**
     * Enseña una alerta de información por pantalla.
     * @param titulo El titulo de la alerta.
     * @param elMensaje El mensaje de la alerta.
     */
    private void ensenarAlertaInfo(String titulo, String elMensaje) {
        Alert alertCerrarSesion = new Alert(Alert.AlertType.INFORMATION);
        alertCerrarSesion.setTitle(titulo);
        alertCerrarSesion.setHeaderText(elMensaje);
        alertCerrarSesion.show();
    }
}
