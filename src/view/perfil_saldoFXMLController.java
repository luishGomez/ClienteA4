/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package view;

import businessLogic.BusinessLogicException;
import businessLogic.ClienteManager;
import static businessLogic.ClienteManagerFactory.createClienteManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import transferObjects.ClienteBean;
import static view.ControladorGeneral.showErrorAlert;


/**
 *
 * @author Sergio
 */
public class perfil_saldoFXMLController{
    private static final Logger LOGGER = Logger.getLogger("escritorio.view.Perfil_saldo");
    private ClienteManager clientLogic = createClienteManager("REAL");
    private Stage stage;
    private ClienteBean user;
    @FXML private TextField txtSaldo;
    public void setUser(ClienteBean user){
        this.user = user;
    }
    public ClienteBean getUser(){
        return user;
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }
    public void initStage(Parent root){
        
    }
    
    @FXML public void aceptarSaldo(){
        if(txtSaldo.getText()!=""){
            try{
                Float elPrecio=Float.parseFloat(txtSaldo.getText());
                LOGGER.info("He ingresado mi saldo");
                Alert alertIngresarSaldo = new Alert(Alert.AlertType.CONFIRMATION,"¨Seguro de ingresar esta cantidad?.",ButtonType.NO,ButtonType.YES);
                //Añadimos titulo a la ventana como el alert.
                alertIngresarSaldo.setTitle("Ingresar Saldo");
                alertIngresarSaldo.setHeaderText("¿Quieres ingresar saldo?.");
                //Si acepta ingresa saldo
                alertIngresarSaldo.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        try {
                            user.setSaldo(user.getSaldo()+elPrecio);
                            clientLogic.edit(user);
                        } catch (BusinessLogicException ex) {
                            Logger.getLogger(perfil_saldoFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                stage.hide();
            }catch(Exception e){
                LOGGER.severe("Error al ingresar el saldo");
                showErrorAlert("Ese saldo no es valido");
            }
        }else{
            showErrorAlert("Inserte el saldo que quieras ingresar.");
        }
    }
    @FXML public void cancelarSaldo(){
        LOGGER.info("He cancelado mi saldo");
        stage.hide();
    }
}
