/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transferObjects;

import java.io.Serializable;
import java.util.Date;
import javafx.beans.property.SimpleObjectProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@XmlRootElement(name="compra")
public class CompraBean implements Serializable {
    private static final long serialVersionUID=1L;
    
    private final SimpleObjectProperty<CompraId> idCompra;
    private final SimpleObjectProperty<ClienteBean> propietario;
    private final SimpleObjectProperty<ApunteBean> apunte;
    private final SimpleObjectProperty<Date> fecha;

    /**
     * @return the idCompra
     */
    public CompraId getIdCompra() {
        return idCompra.get();
    }

    /**
     * @param idCompra the idCompra to set
     */
    public void setIdCompra(CompraId idCompra) {
        this.idCompra.set(idCompra);
    }

    /**
     * @return the propietario
     */
    public ClienteBean getPropietario() {
        return propietario.get();
    }

    /**
     * @param propietario the propietario to set
     */
    public void setPropietario(ClienteBean propietario) {
        this.propietario.set(propietario);
    }

    /**
     * @return the apunte
     */
    public ApunteBean getApunte() {
        return apunte.get();
    }

    /**
     * @param apunte the apunte to set
     */
    public void setApunte(ApunteBean apunte) {
        this.apunte.set(apunte);
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha.get();
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha.set(fecha);
    }
    public CompraBean() {
        this.idCompra= new SimpleObjectProperty();
        this.propietario = new SimpleObjectProperty();
        this.apunte = new SimpleObjectProperty();
        this.fecha = new SimpleObjectProperty();
    }
    public CompraBean(CompraId idCompra, ClienteBean propietario,ApunteBean apunte, Date fecha) {
        this.idCompra= new SimpleObjectProperty(idCompra);
        this.propietario = new SimpleObjectProperty(propietario);
        this.apunte = new SimpleObjectProperty(apunte);
        this.fecha = new SimpleObjectProperty(fecha);
    }
    
    
    
    
}
