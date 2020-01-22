package businessLogic;

/**
 *
 * @author Luis
 */
public class PackManagerFactory {
    public static final String TEST="TEST";
    
    public static PackManager createPackManager(String type) {
        PackManager packManager = null;
        switch(type){
            case TEST:
                break;
            default:
                packManager = new PackManagerImplementation();
        }
        return packManager;
    }
}
