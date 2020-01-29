package view;

import businessLogic.BusinessLogicException;
import businessLogic.PackManager;
import businessLogic.PackManagerFactory;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import transferObjects.OfertaBean;
import transferObjects.PackBean;

/**
 * Celda personalizada para el ListView de TiendaPackFXController.
 * @author Luis
 */
public class CellTiendaPack extends ListCell<PackBean> {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.CellTiendaPack");
    private HBox hBox = new HBox();
    private VBox vBox = new VBox();
    private Label lblTitulo = new Label("");
    private Label lblDescripcion = new Label("");
    private Label lblApuntes = new Label("");
    private Label lblPrecio = new Label("");
    private PackManager managerPack = PackManagerFactory.createPackManager("real");
    private OfertaBean oferta = null;
    
    /**
     * Constructor que añade Label y HBox a VBox.
     */
    public CellTiendaPack() {
        super();
        hBox.getChildren().addAll(lblTitulo);
        vBox.getChildren().addAll(hBox,lblDescripcion,lblApuntes,lblPrecio);
    }
    
    /**
     * Método que se ejecuta para cargar una fila del ListView.
     * @param pack Objeto pack a mostrar.
     * @param vacio whether or not this cell represents data from the list. If it
     *        is empty, then it does not represent any domain data, but is a cell
     *        being used to render an "empty" row.
     */
    @Override
    public void updateItem(PackBean pack, boolean vacio){
        super.updateItem(pack, vacio);
        setText(null);
        setGraphic(null);
        float precio = 0;
        if(pack != null && !vacio){
            lblTitulo.setText("Titulo: " + pack.getTitulo());
            lblDescripcion.setText("Descripcion: " + pack.getDescripcion());
            lblApuntes.setText("Apuntes: ");
            pack.getApuntes().stream().forEach(a -> {
                lblApuntes.setText(lblApuntes.getText() + a.getTitulo() + "  ");
            });
            try{
                oferta = managerPack.getOferta(pack);
            }catch(BusinessLogicException e) {
                LOGGER.severe("Error al intentar cargar la oferta de un pack(updateItem): " + e.getMessage());
            }
            if(oferta != null){
                precio = pack.getPrecio() * (1 - (oferta.getRebaja() / 100));
            }else{
                precio = pack.getPrecio();
            }
            lblPrecio.setText(precio + "€");
            setGraphic(vBox);
        }
    }
}
