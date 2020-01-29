package businessLogic;

import java.util.Set;
import transferObjects.MateriaBean;

/**
 * Interfaz con la lógica de negocio de Materia.
 * @author Luis
 */
public interface MateriaManager {
    public void createMateria(MateriaBean materia) throws BusinessLogicException;
    public void editMateria(MateriaBean materia) throws BusinessLogicException;
    public void removeMateria(MateriaBean materia) throws BusinessLogicException;
    public MateriaBean findMateria(MateriaBean materia) throws BusinessLogicException;
    public Set<MateriaBean> findAllMateria() throws BusinessLogicException;
}
