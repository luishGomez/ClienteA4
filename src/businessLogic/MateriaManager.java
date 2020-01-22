/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import java.util.Set;
import transferObjects.MateriaBean;

/**
 *
 * @author Luis
 */
public interface MateriaManager {
    public void createMateria(MateriaBean materia) throws BusinessLogicException;
    public void editMateria(MateriaBean materia) throws BusinessLogicException;
    public void removeMateria(MateriaBean materia) throws BusinessLogicException;
    public MateriaBean findMateria(MateriaBean materia) throws BusinessLogicException;
    public Set<MateriaBean> findAllMateria() throws BusinessLogicException;
}
