/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transferObjects;

import java.io.Serializable;
import java.util.Date;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableSet;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@XmlRootElement(name="oferta")
public class OfertaBean implements Serializable {
    //private static final long serialVersionUID=1L;

    private final SimpleIntegerProperty idOferta;
    private final SimpleStringProperty titulo;
    private final SimpleObjectProperty<Date> fechaInicio;
    private final SimpleObjectProperty<Date> fechaFin;
    private final SimpleFloatProperty rebaja;
    private final SimpleSetProperty<PackBean> packs;
    
    public OfertaBean() {
        this.idOferta=new SimpleIntegerProperty();
        this.titulo=new SimpleStringProperty();
        this.fechaInicio=new SimpleObjectProperty();
        this.fechaFin=new SimpleObjectProperty();
        this.packs=new SimpleSetProperty();
        this.rebaja=new SimpleFloatProperty();
    }
    public OfertaBean(Integer idOferta,String titulo, Date fechaInicio, Date fechaFin, ObservableSet<PackBean> packs, Float rebaja) {
        this.idOferta = new SimpleIntegerProperty(idOferta);
        this.titulo = new SimpleStringProperty(titulo);
        this.fechaInicio = new SimpleObjectProperty(fechaInicio);
        this.fechaFin = new SimpleObjectProperty(fechaFin);
        this.packs = new SimpleSetProperty(packs);
        this.rebaja = new SimpleFloatProperty(rebaja);     
    }
    
    /**
     * @return the idOferta
     */
    public Integer getIdOferta() {
        return this.idOferta.get();
    }

    /**
     * @param idOferta the idOferta to set
     */
    public void setIdOferta(Integer idOferta) {
        this.idOferta.set(idOferta);
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return  this.titulo.get();
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    /**
     * @return the fechaInicio
     */
    public Date getFechaInicio() {
        return this.fechaInicio.get();
    }

    /**
     * @param fechaInicio the fechaInicio to set
     */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio.set(fechaInicio);
    }

    /**
     * @return the fechaFin
     */
    public Date getFechaFin(){
        return this.fechaFin.get();
    }
    /**
     * @param fechaFin the fechaFin to set
     */
    public void setFechaFin(Date fechaFin) {
        this.fechaFin.set(fechaFin);
    }

    /**
     * @return the packs
     */
    //@XmlTransient
    public ObservableSet<PackBean> getPacks() {
        return packs.get();
    }

    /**
     * @param packs the packs to set
     */
    public void setPacks(SetProperty<PackBean> packs) {
        this.packs.set(packs);
    }
       /**
     * @return the rebaja
     */
    public float getRebaja() {
        return this.rebaja.get();
    }

    /**
     * @param rebaja the rebaja to set
     */
    public void setRebaja(float rebaja) {
        this.rebaja.set(rebaja);
    }
     @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOferta != null ? idOferta.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OfertaBean)) {
            return false;
        }
        OfertaBean other = (OfertaBean) object;
        if ((this.idOferta == null && other.idOferta != null) || (this.idOferta != null && !this.idOferta.equals(other.idOferta))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "serverApuntes4.entity.Oferta[ idOferta=" + idOferta + " ]";
    }

 

    
    
}
