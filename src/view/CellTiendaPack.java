package view;

import businessLogic.BusinessLogicException;
import businessLogic.PackManager;
import businessLogic.PackManagerFactory;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import transferObjects.OfertaBean;
import transferObjects.PackBean;

/**
 *
 * @author Luis
 */
public class CellTiendaPack extends ListCell<PackBean> {
    private HBox hBox = new HBox();
    private VBox vBox = new VBox();
    private Label lblTitulo = new Label("");
    private Label lblDescripcion = new Label("");
    private Label lblApuntes = new Label("");
    private Label lblPrecio = new Label("");
    private PackManager managerPack = PackManagerFactory.createPackManager("real");
    private OfertaBean oferta = null;
    
    public CellTiendaPack() {
        super();
        hBox.getChildren().addAll(lblTitulo);
        vBox.getChildren().addAll(lblDescripcion,hBox,lblApuntes,lblPrecio);
    }
    
    public void updateItem(PackBean pack,boolean vacio){
        super.updateItem(pack, vacio);
        setText(null);
        setGraphic(null);
        float precio = 0;
        if(pack!=null && !vacio){
            lblTitulo.setText("Titulo: " + pack.getTitulo());
            lblDescripcion.setText("Descripcion: " + pack.getDescripcion());
            lblApuntes.setText("Apuntes: ");
            pack.getApuntes().stream().forEach(a -> {
                lblApuntes.setText(lblApuntes.getText()+a.getTitulo()+"  ");
            });
            try{
                oferta = managerPack.getOferta(pack);
            } catch (BusinessLogicException e) {
                e.printStackTrace();
            }
            Date date = new Date();
            if(oferta != null && oferta.getFechaInicio().before(date) && oferta.getFechaFin().after(date)){
                precio = pack.getPrecio()*(1 - (oferta.getRebaja()/100));
            }else{
                precio = pack.getPrecio();
            }
            lblPrecio.setText(precio+"€");
            setGraphic(vBox);
        }
    }
}
