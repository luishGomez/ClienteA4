/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import businessLogic.ApunteManager;
import businessLogic.ApunteManagerFactory;
import businessLogic.BusinessLogicException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transferObjects.ApunteBean;

/**
 *
 * @author Luis
 */
public class AddApuntePackFXController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.AddApuntePackFXController");
    private Stage stage;
    private ModificarPackFXController fxModificarPack = null;
    private ObservableList<ApunteBean> apuntesObv = null;
    private ApunteManager managerApunte = ApunteManagerFactory.createApunteManager("real");
    
    @FXML
    private Button btnBuscarAddApunte;
    @FXML
    private TextField tfFiltarAddApunte;
    @FXML
    private TableView tvApuntesAddApunte;
    @FXML
    private TableColumn cIncluido;
    @FXML
    private TableColumn cTitulo;
    @FXML
    private TableColumn cDescripcion;
    @FXML
    private TableColumn cMateria;
    @FXML
    private Button btnModificarAddApunte;
    
    public void setFXModificarPack(ModificarPackFXController fxController){
        this.fxModificarPack = fxController;
    }
    
    @FXML
    public void initStage(Parent root) {
        try{
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Modificar Pack");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setOnShowing(this::handleWindowShowing);
            //Tabla
            cTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            cDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            cMateria.setCellValueFactory(new PropertyValueFactory<>("materia"));
            cargarDatos();
            stage.showAndWait();
        }catch(Exception e){
            e.printStackTrace();
            ControladorGeneral.showErrorAlert("Ha ocurrido un error.");
        }
    }
    
    private void handleWindowShowing(WindowEvent event){
        try{
            LOGGER.info("handlWindowShowing --> Gestor de Pack MODIFICAR Añadir apunte");
            tfFiltarAddApunte.requestFocus();
        }catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
    
    @FXML
    private void onActionBtnBuscarAddApunte(){
        if(!tfFiltarAddApunte.getText().trim().isEmpty()){
            cargarDatos(tfFiltarAddApunte.getText().trim());
        }else{
            cargarDatos();
        }
    }
    
    @FXML
    private void onActionBtnModificarAddApunte(){
        fxModificarPack.setOpcion(1);
        stage.hide();
    }
    
    @FXML
    private void onActionBtnSalirAddApunte(){
        fxModificarPack.setOpcion(0);
        stage.hide();
    }
    
    private void cargarDatos(){
        try {
            Set<ApunteBean> apuntes = managerApunte.findAll();
            apuntesObv = FXCollections.observableArrayList(new ArrayList<>(apuntes.stream().sorted(Comparator.comparing(ApunteBean::getTitulo)).collect(Collectors.toList())));
            tvApuntesAddApunte.setItems(apuntesObv);
        }catch (BusinessLogicException e) {
            e.printStackTrace();
        }
    }
    
    private void cargarDatos(String string){
        try {
            Set<ApunteBean> apuntes = managerApunte.findAll();
            apuntesObv = FXCollections.observableArrayList(new ArrayList<>(apuntes.stream().filter(apunte -> apunte.getTitulo().contains(string)).sorted(Comparator.comparing(ApunteBean::getTitulo)).collect(Collectors.toList())));
            tvApuntesAddApunte.setItems(apuntesObv);
        }catch (BusinessLogicException e) {
            e.printStackTrace();
        }
    }
}
