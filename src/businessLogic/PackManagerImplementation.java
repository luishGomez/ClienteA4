/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import java.util.Set;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;
import service.PackRESTClient;
import transferObjects.PackBean;

/**
 *
 * @author 2dam
 */
public class PackManagerImplementation implements PackManager{
    private PackRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("BusinessLogic.PackManagerImplementation");
    
    public PackManagerImplementation() {
        webClient= new PackRESTClient();
    }
    
    @Override
    public void createPack(PackBean pack) throws BusinessLogicException {
         try{
            webClient.create(pack);
        }catch(Exception e){
            LOGGER.severe("ERROR! PackManagerImpl -> CreatePack: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }

    @Override
    public void editPack(PackBean pack) throws BusinessLogicException {
        try{
            webClient.edit(pack);
        }catch(Exception e){
            LOGGER.severe("ERROR! PackManagerImpl -> EditPack: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }

    @Override
    public void removePack(PackBean pack) throws BusinessLogicException {
        try{
            webClient.remove(pack.getIdPack().toString());
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.severe("ERROR! PackManagerImpl -> DeletePack: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }

    @Override
    public PackBean findPack(PackBean pack) throws BusinessLogicException {
        PackBean paquete = null;
        try{
            paquete = webClient.find(PackBean.class, pack.getIdPack().toString());
        }catch(Exception e){
            LOGGER.severe("ERROR! PackManagerImpl -> FindPack: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
        return paquete;
    }

    @Override
    public Set<PackBean> findAllPack() throws BusinessLogicException {
        Set<PackBean> packs = null;
        try{
            packs = webClient.findAll(new GenericType<Set<PackBean>>() {});
        }catch(Exception e){
            LOGGER.severe("ERROR! PackManagerImpl -> FindAllPack: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
        return packs;
    }

    @Override
    public void addApunte(PackBean pack, String idApunte) throws BusinessLogicException {
        
    }

    @Override
    public void removeApunte(PackBean pack, String idApunte) throws BusinessLogicException {
        
    }
    
}
