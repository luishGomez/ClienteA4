/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package transferObjects;

import java.io.Serializable;
import java.util.Date;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@XmlRootElement(name="apunte")
public class ApunteBean implements Serializable {
    private static final long serialVersionUID=1L;
    
    private final SimpleIntegerProperty idApunte;
    private final SimpleStringProperty titulo;
    private final SimpleStringProperty descripcion;
    //private final SimpleObjectProperty<byte[]> archivo; //el archivo
    private byte[] archivo;
    private final SimpleObjectProperty<Date> fechaValidacion;
    private final SimpleIntegerProperty likeCont;
    private final SimpleIntegerProperty dislikeCont;
    private final SimpleFloatProperty precio;
    private final SimpleObjectProperty<ClienteBean> creador;
    private final SimpleObjectProperty<MateriaBean> materia;
    
    public ApunteBean(int idApunte, String titulo, String descripcion, byte[] archivo, Date fechaValidacion, int likeCont, int dislikeCont, Float precio, ClienteBean creador, MateriaBean materia) {
        this.idApunte = new SimpleIntegerProperty(idApunte);
        this.titulo = new SimpleStringProperty(titulo);
        this.descripcion = new SimpleStringProperty(descripcion);
        // this.archivo = new SimpleObjectProperty(archivo);
        this.archivo=archivo;
        this.fechaValidacion = new SimpleObjectProperty(fechaValidacion);
        this.likeCont = new SimpleIntegerProperty(likeCont);
        this.dislikeCont = new SimpleIntegerProperty(dislikeCont);
        this.precio = new SimpleFloatProperty(precio);
        this.creador = new SimpleObjectProperty(creador);
        this.materia = new SimpleObjectProperty(materia);
    }
    
    public ApunteBean() {
        this.idApunte = new SimpleIntegerProperty();
        this.titulo = new SimpleStringProperty();
        this.descripcion = new SimpleStringProperty();
        // this.archivo = new SimpleObjectProperty(archivo);
        
        this.fechaValidacion = new SimpleObjectProperty();
        this.likeCont = new SimpleIntegerProperty();
        this.dislikeCont = new SimpleIntegerProperty();
        this.precio = new SimpleFloatProperty();
        this.creador = new SimpleObjectProperty();
        this.materia = new SimpleObjectProperty();
    }
    
    
    /**
     * @return the idApunte
     */
    public int getIdApunte() {
        return idApunte.get();
    }
    
    /**
     * @param idApunte the idApunte to set
     */
    public void setIdApunte(int idApunte) {
        this.idApunte.set(idApunte);
    }
    
    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo.get();
    }
    
    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }
    
    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion.get();
    }
    
    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }
    
    /**
     * @return the archivo
     */
    public byte[] getArchivo() {
        //return archivo.get();
        return archivo;
    }
    
    /**
     * @param archivo the archivo to set
     */
    public void setArchivo(byte[] archivo) {
        //this.archivo.set(archivo);
        this.archivo=archivo;
    }
    
    /**
     * @return the fechaValidacion
     */
    public Date getFechaValidacion() {
        return fechaValidacion.get();
    }
    
    /**
     * @param fechaValidacion the fechaValidacion to set
     */
    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion.set(fechaValidacion);
    }
    
    /**
     * @return the likeCont
     */
    public int getLikeCont() {
        return likeCont.get();
    }
    
    /**
     * @param likeCont the likeCont to set
     */
    public void setLikeCont(int likeCont) {
        this.likeCont.set(likeCont);
    }
    
    /**
     * @return the dislikeCont
     */
    public int getDislikeCont() {
        return dislikeCont.get();
    }
    
    /**
     * @param dislikeCont the dislikeCont to set
     */
    public void setDislikeCont(int dislikeCont) {
        this.dislikeCont.set(dislikeCont);
    }
    
    
    
    /**
     * @return the precio
     */
    public Float getPrecio() {
        return precio.get();
    }
    
    /**
     * @param precio the precio to set
     */
    public void setPrecio(Float precio) {
        this.precio.set(precio);
    }
    
    /**
     * @return the creador
     */
    public ClienteBean getCreador() {
        return creador.get();
    }
    
    /**
     * @param creador the creador to set
     */
    public void setCreador(ClienteBean creador) {
        this.creador.set(creador);
    }
    
    
    
    /**
     * @return the materia
     */
    public MateriaBean getMateria() {
        return materia.get();
    }
    
    /**
     * @param materia the materia to set
     */
    public void setMateria(MateriaBean materia) {
        this.materia.set(materia);
    }
    
     @Override
    public String toString() {
        return creador.get().getNombreCompleto();
    }
    
    public String getTituloMin(){
        return titulo.get().toLowerCase();
    }
    
}
