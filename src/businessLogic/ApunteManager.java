/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import java.util.Set;
import transferObjects.ApunteBean;

/**
 *  La interfaz del manager de apuntes.
 * @author Ricardo Peinado Lastra
 */
public interface ApunteManager {
    /**
     * Crea un apunte.
     * @param apunte Los datos del apunte.
     * @throws BusinessLogicException  Salta si ocurre algun error.
     */
    public void create(ApunteBean apunte) throws BusinessLogicException;
    /**
     * Modifica un apunte.
     * @param apunte Los datos del apunte.
     * @throws BusinessLogicException  Salta si ocurre algun error.
     */
    public void edit( ApunteBean apunte) throws BusinessLogicException;
    /**
     * Borra un apunte.
     * @param id El identificador del apunte.
     * @throws BusinessLogicException  Salta si ocurre algun error.
     */
    public void remove(Integer id) throws BusinessLogicException;
    /**
     * Encuntra los datos de un determinado apunte.
     * @param id El iddentificador del apunte.
     * @return Los datos del apunte.
     * @throws BusinessLogicException Salta si ocurre algun error. 
     */
    public ApunteBean find(Integer id) throws BusinessLogicException;
    /**
     * Devuelve todos los apuntes existentes.
     * @return Retorna una lista con los datos de los apuntes,
     * @throws BusinessLogicException  Salta si ocurre algun error.
     */
    public Set<ApunteBean> findAll()throws BusinessLogicException;
    /**
     * Devuelve los apuntes creados por un cliente.
     * @param id El identificador del cliente.
     * @return Retorna la lista de apuntes creados.
     * @throws BusinessLogicException  Salta si ocurre algun error.
     */
    public Set<ApunteBean> getApuntesByCreador(Integer id) throws BusinessLogicException;
    /**
     * Devuelve los apuntes comprados por un determinado cliente.
     * @param id El identificador del cliente.
     * @return Retorna la lista de apuntes comprados por el cliente.
     * @throws BusinessLogicException  Salta si ocurre algun error.
     */
    public Set<ApunteBean> getApuntesByComprador(Integer id) throws BusinessLogicException;
    /**
     * Permite votar un apunte.
     * @param idCliente El identificador del cliente
     * @param like Like 1 dislike -1
     * @param apunte Los datos del apunte.
     * @throws BusinessLogicException  Salta si ocurre algun error.
     */
    public void votacion(Integer idCliente,Integer like, ApunteBean apunte) throws BusinessLogicException;
    /**
     * Devuelve el numero de compras de un determinado apunte.
     * @param id El identificador del apunte.
     * @return El numero de compras.
     * @throws BusinessLogicException Salta si ocurre algun error. 
     */
    public int cuantasCompras(Integer id) throws BusinessLogicException;
}
