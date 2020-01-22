/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import java.util.Set;
import transferObjects.PackBean;

/**
 *
 * @author 2dam
 */
public interface PackManager {
    public void createPack(PackBean pack) throws BusinessLogicException;
    public void editPack(PackBean pack) throws BusinessLogicException;
    public void removePack(PackBean pack) throws BusinessLogicException;
    public PackBean findPack(PackBean pack) throws BusinessLogicException;
    public Set<PackBean> findAllPack() throws BusinessLogicException;
    public void addApunte(PackBean pack, String idApunte) throws BusinessLogicException;
    public void removeApunte(PackBean pack, String idApunte) throws BusinessLogicException;
}
