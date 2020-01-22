/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakeBusinessLogic;

/**
 *
 * @author 2dam
 */
public class TestCompra {
    private int idCliente;
    private int idApunte;

    public TestCompra(int idCliente, int idApunte) {
        this.idCliente = idCliente;
        this.idApunte = idApunte;
    }

    /**
     * @return the idCliente
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * @return the idApunte
     */
    public int getIdApunte() {
        return idApunte;
    }

    /**
     * @param idApunte the idApunte to set
     */
    public void setIdApunte(int idApunte) {
        this.idApunte = idApunte;
    }
    
}
