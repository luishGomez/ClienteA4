/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;
import service.OfertaRESTClient;
import transferObjects.OfertaBean;

/**
 *
 * @author Sergio
 */
public class OfertaManagerImplementation implements OfertaManager {
    private OfertaRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("BusinessLogic.ClienteManagerImplementation");
    
    public OfertaManagerImplementation() {
        webClient= new OfertaRESTClient();
    }

    @Override
    public void createOferta(OfertaBean oferta) throws BusinessLogicException {
        try{
            webClient.createOferta(oferta);
        }catch(Exception e){
            LOGGER.severe("ERROR! OfertaManagerImplementation -> CreateOferta: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }    
    }


    @Override
    public void borrarOferta(Integer idOferta) throws BusinessLogicException {
        try{
            webClient.deleteOferta(idOferta);
        }catch(Exception e){
            LOGGER.severe("ERROR! OfertaManagerImplementation -> CreateOferta: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }

    @Override
    public void actualizarOferta(OfertaBean oferta) throws BusinessLogicException {
        try{
            webClient.updateOferta(oferta);
        }catch(Exception e){
            LOGGER.severe("ERROR! OfertaManagerImplementation -> CreateOferta: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }

    @Override
    public Collection todasOfertas() throws BusinessLogicException {

        try{
            return webClient.findAllOfertas(new GenericType<Set<OfertaBean>>() {} );
        }catch(Exception e){
            LOGGER.severe("ERROR! OfertaManagerImplementation -> CreateOferta: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }

    }
    

    
    
    
    
}
