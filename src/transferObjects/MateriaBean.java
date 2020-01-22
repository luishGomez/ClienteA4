package transferObjects;

import java.io.Serializable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis
 */
@XmlRootElement(name="materia")
public class MateriaBean implements Serializable {
    private static final Long serialVersionUID = 1L;
    
    private final SimpleIntegerProperty idMateria;
    private final SimpleStringProperty titulo;
    private final SimpleStringProperty descripcion;
    
    public MateriaBean(Integer idMateria, String titulo, String descripcion){
        this.idMateria = new SimpleIntegerProperty(idMateria);
        this.titulo = new SimpleStringProperty(titulo);
        this.descripcion = new SimpleStringProperty(descripcion);
    }
    
    public MateriaBean(){
        this.idMateria = new SimpleIntegerProperty();
        this.titulo = new SimpleStringProperty();
        this.descripcion = new SimpleStringProperty();
    }
    
    public Integer getIdMateria() {
        return this.idMateria.get();
    }
    
    public void setIdMateria(Integer idMateria) {
        this.idMateria.set(idMateria);
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
    
    @Override
    public String toString() {
        return titulo.get();
    }
}
