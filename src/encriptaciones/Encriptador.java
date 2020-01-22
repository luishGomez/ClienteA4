package encriptaciones;




import static com.google.common.io.BaseEncoding.base64;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;
import javax.crypto.Cipher;
import service.ClienteRESTClient;


/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 * Esta clase permite encriptar y resumir datos.
 * @author Ricardo Peinado Lastra
 */
public class Encriptador {
    private static ResourceBundle configFile=ResourceBundle.getBundle("encriptaciones.encriptadorConfig");
    private final String rutaPublica=configFile.getString("llavePublica");
    private static final Logger LOGGER =
            Logger.getLogger("Encriptador");
    
    /**
     * Encripta en mensaje en claro que metas.
     * @param mensaje El mensaje en claro.
     * @return El mensaje encriptado.
     * @throws EncriptarException  Salta si ocurre un error al encriptar.
     */
    public String encriptar(String mensaje) throws EncriptarException{
        String encriptado=null;
        InputStream in = null;
        //byte[] bytes=null; //semibien
        //ZipInputStream zin=null;
        FileInputStream fis=null;
        Path path=null;
        try {
            /*
            LOGGER.severe("infor path "+this.getClass().getResource("/encriptaciones/public.key"));
            fis=new FileInputStream("/encriptaciones/public.key");
            bytes=new byte[fis.available()];
            fis.read(bytes);
            */
            /*
            URL url=this.getClass().getResource("/encriptaciones/public.key");
            in= getClass().getResourceAsStream(url.getFile());
            bytes=new byte[in.available()];
            in.read(bytes);
            */
            //SEMI BIEN
            /*
            in = getClass().getResourceAsStream("/encriptaciones/public.key");
            bytes=new byte[in.available()];
            in.read(bytes);
            */
            //byte[] bytes=Files.readAllBytes(Paths.get("/encriptaciones/public.key"));
            //
            
            //URL url=this.getClass().getResource("/encriptaciones/public.key");
            //new FileInputStream(url.getFile())
            /*
            zin = new ZipInputStream(Encriptador.class.getClassLoader().getResourceAsStream("/src/public.key"));
            bytes=new byte[zin.available()];
            zin.read(bytes);
            */
            /*        
            File file=new File("/encriptaciones/public.key");//se tiene que cambiar
            byte[] bytes=Files.readAllBytes(file.toPath());*/
            
            
            //version netbeans
            /*
            path=Paths.get("public.key");
            byte[] bytes=Files.readAllBytes(Paths.get("public.key"));
            */
            //byte[] bytes=Files.readAllBytes(Paths.get(rutaPublica));
            ClienteRESTClient clienteRest=new ClienteRESTClient();
            byte[] bytes=hexStringToByteArray(clienteRest.getPublicKey());
            
                    
            EncodedKeySpec publicKeySpec = new  X509EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            
            Cipher cipher = Cipher.getInstance("RSA");
            //Cipher cipher = Chiper.getInstance("RSA/ECB/OAEPwithSHA-256andMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherText = cipher.doFinal(mensaje.getBytes());
            
            encriptado= hexadecimal(cipherText);
            
            
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage()+" "+ex.getCause());
            throw new EncriptarException(ex.getMessage());
        }finally{
            if(fis!=null)
                try {
                    fis.close();
            } catch (IOException ex) {
                Logger.getLogger(Encriptador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return encriptado;
    }
    
    /**
     * Convierte una lista de bytes a Hexadecimal.
     * @param resumen La colección de bytes.
     * @return Los bytes en hexadecimal.
     */
    static String hexadecimal(byte[] resumen) {
        String HEX = "";
        for (int i = 0; i < resumen.length; i++) {
            String h = Integer.toHexString(resumen[i] & 0xFF);
            if (h.length() == 1)
                HEX += "0";
            HEX += h;
        }
        return HEX.toUpperCase();
    }
    
    /**
     * Convierte un texto en hexadeciaml en una lista de bytes.
     * @param s El texto en hexadecimal.
     * @return La coleccion en bytes.
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    
}
