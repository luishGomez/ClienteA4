/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package service;

import java.util.ResourceBundle;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Jersey REST client generated for REST resource:OfertaFacadeREST [oferta]<br>
 * USAGE:
 * <pre>
 *        OfertaRESTClient client = new OfertaRESTClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author 2dam
 */
public class OfertaRESTClient {
    private static ResourceBundle configFile=ResourceBundle.getBundle("service.configService");
    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = configFile.getString("base_uri");
    
    public OfertaRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("oferta");
    }
    
    public <T> T findAllOfertas(GenericType<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
    
    public void eliminarPack(Object requestEntity, String idPack) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("eliminarPack/{0}", new Object[]{idPack})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }
    
    public void createOferta(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }
    
    public <T> T findOfertaById(Class<T> responseType, String idOferta) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{idOferta}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
    
    public void deleteOferta(Integer idOferta) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{idOferta})).request().delete();
    }
    
    public void insertarPack(Object requestEntity, String idPack) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("insertarPack/{0}", new Object[]{idPack})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }
    
    public void updateOferta(Object requestEntity) throws ClientErrorException {
        webTarget.path("actualizar").request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }
    
    public void close() {
        client.close();
    }
    
}
