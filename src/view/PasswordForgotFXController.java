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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.ws.rs.NotFoundException;
import static view.ControladorGeneral.showErrorAlert;

/**
 *
 * @author Ricardo Peinado
 */
public class PasswordForgotFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.PasswordForgotFXController");
    @FXML 
    private TextField textFieldLogin2;
    @FXML
    private Button btnCancelar2;
    @FXML
    private Button btnAceptar2;
    private Stage stage;
    public ClienteManager clienteLogic = createClienteManager("real");
    @FXML
    public void initStage(Parent root) {
        try{
            LOGGER.info("Iniciando la ventana SubirApunteFXController");
            Scene scene=new Scene(root);
            stage=new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Subir Apunte");
            stage.setResizable(false);
            btnCancelar2.setOnAction(this::cerrar);
            btnAceptar2.setOnAction(this::enviar);
            stage.show();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
        
    }
    
    public void cerrar(ActionEvent event){
        stage.hide();
    }
    public void enviar(ActionEvent event){
        if(textFieldLogin2.getText().trim().length()<3 || textFieldLogin2.getText().length()>250){
            showErrorAlert("El login no es correcto reviselo otra vez.");
        }else{
            try {
                clienteLogic.passwordForgot(textFieldLogin2.getText().trim());
                Alert alert=new Alert(Alert.AlertType.INFORMATION,
                            "Revise su correo asociado a la cuenta, para saber tu nueva contraseña",
                            ButtonType.OK);
                    alert.showAndWait();
            }catch (NotFoundException ex) {
                showErrorAlert("Ese login no existe");
            } catch (BusinessLogicException ex) {
                showErrorAlert("A sucedido un error");
            }
        }
    }
}
