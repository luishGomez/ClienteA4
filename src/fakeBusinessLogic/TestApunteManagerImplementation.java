/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakeBusinessLogic;

import businessLogic.ApunteManager;
import businessLogic.BusinessLogicException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import transferObjects.ApunteBean;
import transferObjects.ClienteBean;
import transferObjects.MateriaBean;
import transferObjects.UserPrivilege;

/**
 *
 * @author 2dam
 */
public class TestApunteManagerImplementation implements ApunteManager {
    private Set<ApunteBean> apuntes;
    private ArrayList <TestCompra> compras = new ArrayList<TestCompra>();

    public TestApunteManagerImplementation() {
        //Clientes
        ClienteBean cliente1=new ClienteBean();
        cliente1.setId(1);
        cliente1.setNombreCompleto("Kevin Garcia Goikoextxea");
        cliente1.setPrivilegio(UserPrivilege.USER);
        ClienteBean cliente2=new ClienteBean();
        cliente2.setId(2);
        cliente2.setNombreCompleto("Nerea Bujedo Fernandez");
        cliente2.setPrivilegio(UserPrivilege.USER);
        ClienteBean cliente3=new ClienteBean();
        cliente3.setId(3);
        cliente3.setNombreCompleto("Adrian Villanueva Ereida");
        cliente3.setPrivilegio(UserPrivilege.USER);
        //Materias
        MateriaBean materia1=new MateriaBean();
        materia1.setIdMateria(1);
        materia1.setTitulo("Procesos");
        materia1.setDescripcion("Los programas en segundo plano.");
        MateriaBean materia2=new MateriaBean();
        materia2.setIdMateria(1);
        materia2.setTitulo("Bilogia");
        materia2.setDescripcion("La ciencia de los seres vivos.");
        MateriaBean materia3=new MateriaBean();
        materia3.setIdMateria(1);
        materia3.setTitulo("Matematicas");
        materia3.setDescripcion("La ciencia de los numeros y sus formulas.");
        MateriaBean materia4=new MateriaBean();
        materia4.setIdMateria(1);
        materia4.setTitulo("Euskera");
        materia4.setDescripcion("El idioma del Pais Vasco.");
        //Los apuntes
        apuntes=new HashSet<ApunteBean>();
        ApunteBean ap1=new ApunteBean();
        ap1.setIdApunte(1);
        ap1.setTitulo("Crea tu propio protocolo");
        ap1.setDescripcion("Con este apunte podras crear tu propio protocolo para comunicar tu servidor con tu aplicación cliente.0");
        ap1.setCreador(cliente1);
        ap1.setMateria(materia1);
        ap1.setLikeCont(0);
        ap1.setDislikeCont(0);
        ap1.setPrecio(Float.parseFloat("5.85"));
        ApunteBean ap2=new ApunteBean();
        ap2.setIdApunte(2);
        ap2.setTitulo("Aprende a utilizar Retrofit");
        ap2.setDescripcion("Aprenderas a utiliar el api de retrofit para la comunicación entre aplicaciones");
        ap2.setCreador(cliente1);
        ap2.setMateria(materia1);
        ap2.setLikeCont(0);
        ap2.setDislikeCont(0);
        ap2.setPrecio(Float.parseFloat("10.99"));
        ApunteBean ap3=new ApunteBean();
        ap3.setIdApunte(3);
        ap3.setTitulo("Composición quimica del sistema linfatico");
        ap3.setDescripcion("Aprendera los compuestos quimicos y las reacciones del sistema linfactico.");
        ap3.setCreador(cliente2);
        ap3.setMateria(materia2);
        ap3.setLikeCont(0);
        ap3.setDislikeCont(0);
        ap3.setPrecio(Float.parseFloat("1.20"));
        ApunteBean ap4=new ApunteBean();
        ap4.setIdApunte(4);
        ap4.setTitulo("Aprende threads en Java");
        ap4.setDescripcion("Aprenderas a utilizar los hilos en java.");
        ap4.setCreador(cliente1);
        ap4.setMateria(materia1);
        ap4.setLikeCont(0);
        ap4.setDislikeCont(0);
        ap4.setPrecio(Float.parseFloat("7.12"));
        apuntes.add(ap4);
        apuntes.add(ap3);
        apuntes.add(ap2);
        apuntes.add(ap1);
        //Compras
        compras.add(new TestCompra(3,1));
        compras.add(new TestCompra(3,2));
        compras.add(new TestCompra(3,4));
        
        
    }
    

    @Override
    public void create(ApunteBean apunte) throws BusinessLogicException {
        apunte.setIdApunte(apuntes.size()+1);
        apuntes.add(apunte);
    }

    @Override
    public void edit(ApunteBean apunte) throws BusinessLogicException {
        for(ApunteBean a:apuntes){
            if(a.getIdApunte()==apunte.getIdApunte()){
                a=apunte;
                break;
            }
        }
    }

    @Override
    public void remove(Integer id) throws BusinessLogicException {
        for(ApunteBean a:apuntes){
            if(a.getIdApunte()==id){
                apuntes.remove(a);
                break;
            }
        }
    }

    @Override
    public ApunteBean find(Integer id) throws BusinessLogicException {
        ApunteBean resultado=null;
        for(ApunteBean a:apuntes){
            if(a.getIdApunte()==id){
                resultado=a;
                break;
            }
        }
        return resultado;
    }

    @Override
    public Set<ApunteBean> findAll() throws BusinessLogicException {
        return apuntes;
    }

    @Override
    public Set<ApunteBean> getApuntesByCreador(Integer id) throws BusinessLogicException {
        Set<ApunteBean> apuntesDeCreador=new HashSet<ApunteBean>();
        for(ApunteBean a:apuntes){
            if(a.getCreador().getId()==id){
                apuntesDeCreador.add(a);
            }
        }
        return apuntesDeCreador;
    }

    @Override
    public Set<ApunteBean> getApuntesByComprador(Integer id) throws BusinessLogicException {
        Set<ApunteBean> apuntesDeCreador=new HashSet<ApunteBean>();
        for(TestCompra a:compras){
            if(a.getIdCliente()==id){
                for(ApunteBean apunte:apuntes){
                    if(a.getIdApunte()==apunte.getIdApunte()){
                        apuntesDeCreador.add(apunte);
                    }
                }
            }
        }
        return apuntesDeCreador;
    }

    @Override
    public void votacion(Integer idCliente, Integer like, ApunteBean apunte) throws BusinessLogicException {
       //
    }

    @Override
    public int cuantasCompras(Integer id) throws BusinessLogicException {
        int resultado=0;
        for(TestCompra compra:compras){
            if(compra.getIdApunte()==id){
                resultado++;
            }
        }
        return resultado;
    }
    
}
