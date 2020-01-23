package transferObjects;

import java.util.Date;
import java.util.Set;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis
 */
@XmlRootElement(name="pack")
public class PackBean {
    private static final Long serialVersionUID = 1L;
    
    private final SimpleIntegerProperty idPack;
    private final SimpleStringProperty titulo;
    private final SimpleStringProperty descripcion;
    private final SimpleObjectProperty<Date> fechaModificacion;
    private Set<ApunteBean> apuntes;
    
    public PackBean(Integer idPack, String titulo, String descripcion, Date fecha, Set apuntes){
        this.idPack = new SimpleIntegerProperty(idPack);
        this.titulo = new SimpleStringProperty(titulo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.fechaModificacion = new SimpleObjectProperty(fecha);
        this.apuntes = apuntes;
    }
    
    public PackBean(){
        this.idPack = new SimpleIntegerProperty();
        this.titulo = new SimpleStringProperty();
        this.descripcion = new SimpleStringProperty();
        this.fechaModificacion = new SimpleObjectProperty();
    }

    public Integer getIdPack() {
        return this.idPack.get();
    }

    public void setIdPack(Integer idPack) {
        this.idPack.set(idPack);
    }

    public String getTitulo() {
        return this.titulo.get();
    }

    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    public String getDescripcion() {
        return this.descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public Date getFechaModificacion() {
        return this.fechaModificacion.get();
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion.set(fechaModificacion);
    }
 
    public Set<ApunteBean> getApuntes() {
        return apuntes;
    }

    public void setApuntes(Set<ApunteBean> apuntes) {
        this.apuntes = apuntes;
    }
    
    public float getPrecio(){
        float precio = 0;
        for(ApunteBean a : getApuntes()){
            precio += a.getPrecio();
        }
        return precio;
    }
}
