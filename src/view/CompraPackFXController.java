package view;

import businessLogic.ApunteManager;
import static businessLogic.ApunteManagerFactory.createApunteManager;
import businessLogic.BusinessLogicException;
import businessLogic.ClienteManager;
import static businessLogic.ClienteManagerFactory.createClienteManager;
import businessLogic.PackManager;
import static businessLogic.PackManagerFactory.createPackManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import transferObjects.ApunteBean;
import transferObjects.PackBean;
import transferObjects.ClienteBean;
import transferObjects.OfertaBean;
import static view.ControladorGeneral.showErrorAlert;

/**
 * Clase controladora de compra pack.
 * @author Luis
 */
public class CompraPackFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.CompraPackFXController");
    private ClienteBean cliente = null;
    private PackBean pack = null;
    private ClienteManager managerCliente = createClienteManager("real");
    private PackManager managerPack = createPackManager("real");
    private ApunteManager managerApunte = createApunteManager("real");
    private Stage stage;
    private float resultado;
    private TiendaPackFXController fxTienda;
    private float precio;
    private Set<ApunteBean> apuntesComprado;
    private PackBean packActualizado;
    
    @FXML
    private Label lblTituloPack;
    @FXML
    private Label lblDescripcionPack;
    @FXML
    private Label lblApuntesPack;
    @FXML
    private Label lblSaldoActual;
    @FXML
    private Label lblPrecioPack;
    @FXML
    private Label lblSaldoFinal;
    @FXML
    private Button btnCancelarComprarPack;
    @FXML
    private Button btnComprarComprarPack;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setCliente(ClienteBean cliente){
        this.cliente = cliente;
    }
    
    public void setPack(PackBean pack){
        this.pack = pack;
    }
    
    public void setFXController(TiendaPackFXController fxTienda){
        this.fxTienda = fxTienda;
    }
    
    /**
     * El metodo que inicializa la ventana.
     * @param root El nodo raiz.
     */
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando CompraPackFXController");
            Scene scene=new Scene(root);
            stage=new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Comprar Pack");
            stage.setResizable(false);
            btnComprarComprarPack.setMnemonicParsing(true);
            btnComprarComprarPack.setText("C_omprar");
            btnCancelarComprarPack.setMnemonicParsing(true);
            btnCancelarComprarPack.setText("_Cancelar");
            lblTituloPack.setText(pack.getTitulo());
            lblDescripcionPack.setText(pack.getDescripcion());
            comprobarCompras();
            if(packActualizado.getApuntes().isEmpty()){
                Alert al = new Alert(AlertType.INFORMATION);
                al.setTitle("Comprar pack");
                al.setHeaderText("Tienes todos los apuntes de este pack comprados.");
                al.getModality();
                al.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        stage.hide();
                    }
                    if(response == ButtonType.CLOSE) {
                        stage.hide();
                    }
                });
            }else{
                lblApuntesPack.setText("Apuntes:");
                for(ApunteBean a : pack.getApuntes()){
                    lblApuntesPack.setText(lblApuntesPack.getText() + "\nTítulo: " + a.getTitulo());
                }
                mostrarPrecio();
                if(cliente.getSaldo()>precio){
                    btnComprarComprarPack.setDisable(false);
                }else{
                    btnComprarComprarPack.setDisable(true);
                }
                lblSaldoActual.setText(cliente.getSaldo() + "€");
                resultado = cliente.getSaldo() - precio;
                lblSaldoFinal.setText(resultado + "€");
                stage.showAndWait();
            }
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.severe(e.getMessage());
        }
    }
    /**
     * Metodo que cancela la compra.
     * @param event El evento de pulsación.
     */
    @FXML
    private void onActionCancelarCompraPack(ActionEvent event){
        fxTienda.setOpcion(0);
        stage.hide();
    }
    /**
     * Metodo que permite comprar el apunte.
     * @param event El evento de pulsación.
     */
    @FXML
    private void onActionComprarComprarPack(ActionEvent event){
        Alert alertCerrarSesion = new Alert(AlertType.CONFIRMATION);
        alertCerrarSesion.setTitle("Comprar pack");
        alertCerrarSesion.setHeaderText("Una vez que compres el pack no se podra devolver el producto.");
        alertCerrarSesion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                for(ApunteBean a : pack.getApuntes()){
                    try {
                        managerCliente.comprarApunte(cliente, a.getIdApunte());
                    }catch (BusinessLogicException e) {
                        LOGGER.severe("ERROR en la compra de pack: "+e.getMessage());
                        showErrorAlert("A ocurrio un error en la compra del pack.");
                    }
                }
                try{
                    cliente.setSaldo(resultado);
                    managerCliente.edit(cliente);
                    alertInfo("Pack comprado","Ve a la ventana tu biblioteca para poder descargarlo.");
                }catch (BusinessLogicException e) {
                    LOGGER.severe("ERROR en la compra de pack: "+e.getMessage());
                    showErrorAlert("A ocurrio un error en la compra del pack.");
                }
                fxTienda.setOpcion(1);
                stage.hide();
            }
        });
    }
    /**
     * Alert de información sobre la compra.
     * @param titulo Titulo de la alerta.
     * @param mensaje Mensaje de la alerta.
     */
    private void alertInfo(String titulo, String mensaje) {
        Alert alertCerrarSesion = new Alert(Alert.AlertType.INFORMATION);
        alertCerrarSesion.setTitle(titulo);
        alertCerrarSesion.setHeaderText(mensaje);
        alertCerrarSesion.show();
    }

    private void mostrarPrecio() {
        OfertaBean oferta = null;
        try{
            oferta = managerPack.getOferta(pack);
        } catch (BusinessLogicException e) {
            e.printStackTrace();
        }
        Date date = new Date();
        if(oferta != null){
            precio = packActualizado.getPrecio()*(1 - (oferta.getRebaja()/100));
        }else{
            precio = packActualizado.getPrecio();
        }
        lblPrecioPack.setText(precio + "€.");
    }

    private void comprobarCompras() {
        packActualizado = pack;
        System.out.println(pack.getApuntes().size());
        System.out.println(packActualizado.getApuntes().size());
        Set<ApunteBean> apuntes = new HashSet<ApunteBean>();
        try{
            apuntesComprado = managerApunte.getApuntesByComprador(cliente.getId());
            if(apuntesComprado != null){
                for(ApunteBean a : apuntesComprado){
                    for(ApunteBean ap : pack.getApuntes()){
                        if(a.getIdApunte() == ap.getIdApunte()){
                            apuntes.add(ap);
                        }
                    }
                }
            }
            if(apuntes != null){
                for(ApunteBean a : apuntes){
                    if(packActualizado.getApuntes().contains(a)){
                        packActualizado.getApuntes().remove(a);
                    }
                }
            }
        } catch (BusinessLogicException e) {
            e.printStackTrace();
        }
    }
}
