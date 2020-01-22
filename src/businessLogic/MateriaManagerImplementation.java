package businessLogic;

import java.util.Set;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;
import service.MateriaRESTClient;
import transferObjects.MateriaBean;

/**
 *
 * @author Luis
 */
public class MateriaManagerImplementation implements MateriaManager{
    private MateriaRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("BusinessLogic.MateriaManagerImplementation");
    
    public MateriaManagerImplementation() {
        webClient= new MateriaRESTClient();
    }
    
    @Override
    public void createMateria(MateriaBean materia) throws BusinessLogicException {
        try{
            webClient.create(materia);
        }catch(Exception e){
            LOGGER.severe("ERROR! MateriaManagerImpl -> CreateMateria: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }

    @Override
    public void editMateria(MateriaBean materia) throws BusinessLogicException {
        try{
            webClient.edit(materia);
        }catch(Exception e){
            LOGGER.severe("ERROR! MateriaManagerImpl -> EditMateria: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }

    @Override
    public void removeMateria(MateriaBean materia) throws BusinessLogicException {
        try{
            webClient.remove(materia.getIdMateria().toString());
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.severe("ERROR! MateriaManagerImpl -> DeleteMateria: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }

    @Override
    public MateriaBean findMateria(MateriaBean materia) throws BusinessLogicException {
        MateriaBean mat = null;
        try{
            mat = webClient.find(MateriaBean.class, materia.getIdMateria().toString());
        }catch(Exception e){
            LOGGER.severe("ERROR! MateriaManagerImpl -> DeleteMateria: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
        return materia;
    }

    @Override
    public Set<MateriaBean> findAllMateria() throws BusinessLogicException {
        Set<MateriaBean> mat = null;
        try{
            mat = webClient.findAll(new GenericType<Set<MateriaBean>>() {});
        }catch(Exception e){
            LOGGER.severe("ERROR! MateriaManagerImpl -> DeleteMateria: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
        return mat;
    }
}
