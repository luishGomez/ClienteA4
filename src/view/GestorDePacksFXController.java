package view;

import businessLogic.BusinessLogicException;
import businessLogic.PackManager;
import businessLogic.PackManagerFactory;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import transferObjects.ApunteBean;
import transferObjects.PackBean;
import transferObjects.UserBean;
import static view.ControladorGeneral.showErrorAlert;
import static view.ControladorGeneral.showWarningAlert;

/**
 * El controlador de la ventana GestorPackFx para gestionar los packs.
 *
 * @author Luis
 */
public class GestorDePacksFXController {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("view.GestorDePacksFXController");
    private UserBean user;
    private Stage stage;
    private PackManager manager = PackManagerFactory.createPackManager("real");
    private Set<PackBean> packs = null;
    private ObservableList<PackBean> packsObv = null;
    private int opcion;
    private ApunteBean apunte = null;
    private PackBean pack = null;
    private Stage stageVentanaAyuda;
    private boolean eliminar = false;

    @FXML
    private Menu menuCuenta;
    @FXML
    private MenuItem menuCuentaCerrarSesion;
    @FXML
    private MenuItem menuCuentaSalir;
    @FXML
    private Menu menuVentanas;
    @FXML
    private MenuItem menuVentanasGestorApuntes;
    @FXML
    private MenuItem menuVentanasGestorPacks;
    @FXML
    private MenuItem menuVentanasGestorOfertas;
    @FXML
    private MenuItem menuVentanasGestorMaterias;
    @FXML
    private Menu menuHelp;
    @FXML
    private MenuItem menuHelpAbout;
    @FXML
    private Button btnCrearGestorPack;
    @FXML
    private Button btnInformeGestorPack;
    @FXML
    private TextField tfFiltrarGestorPack;
    @FXML
    private Button btnBuscarGestorPack;
    @FXML
    private TableView tablaPack;
    @FXML
    private TableColumn cId;
    @FXML
    private TableColumn cTitulo;
    @FXML
    private TableColumn cDescripcion;
    @FXML
    private TextField tfTituloGestorPack;
    @FXML
    private TextField tfDescripcionGestorPack;
    @FXML
    private DatePicker dpDate;
    @FXML
    private Button btnModificarApunteGestorPack;
    @FXML
    private Button btnModificarGestorPack;
    @FXML
    private Button btnEliminarGestorPack;

    /**
     * Método que le da un valor a stage.
     *
     * @param stage Valor de stage.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Método que le da un valor a user.
     *
     * @param user Valor de usuario.
     */
    public void setUser(UserBean user) {
        this.user = user;
    }

    /**
     * Método que le da un valor a opcion.
     *
     * @param opcion Valor de opcion.
     */
    public void setOpc(int opcion) {
        this.opcion = opcion;
    }

    /**
     * Método que le da un valor a apunte.
     *
     * @param apunte Valor de apunte.
     */
    public void setApunte(ApunteBean apunte) {
        this.apunte = apunte;
    }

    /**
     * Método que inicializa la ventana.
     *
     * @param root Nodo raiz.
     */
    @FXML
    public void initStage(Parent root) {
        try {
            LOGGER.info("Iniciando la ventana Gestor De Packs");
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Gestor de Pack");
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.setOnShowing(this::handleWindowShowing);
            //Menu
            menuCuenta.setMnemonicParsing(true);
            menuCuenta.setText("_Cuenta");
            menuVentanas.setMnemonicParsing(true);
            menuVentanas.setText("_Ventanas");
            menuHelp.setMnemonicParsing(true);
            menuHelp.setText("_Help");
            menuHelpAbout.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+A"));
            menuCuentaCerrarSesion.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+C"));
            menuCuentaSalir.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+S"));
            menuVentanasGestorApuntes.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+A"));
            menuVentanasGestorMaterias.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+M"));
            menuVentanasGestorOfertas.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+O"));
            menuVentanasGestorPacks.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+P"));
            //Tabla
            cId.setCellValueFactory(new PropertyValueFactory<>("idPack"));
            cTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            cDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            cargarDatos();
            tablaPack.getSelectionModel().selectedItemProperty().addListener(this::packClicked);
            stage.show();
        } catch (Exception e) {
            LOGGER.severe("Error(initStage)" + e.getMessage());
        }
    }

    /**
     * Método que se ejecuta cuando se muestra la ventana.
     *
     * @param event Evento que se ha lanzado.
     */
    private void handleWindowShowing(WindowEvent event) {
        try {
            LOGGER.info("handleWindowShowing -> Gestor De Packs");
            tfFiltrarGestorPack.requestFocus();
        } catch (Exception e) {
            LOGGER.severe("Error(handleWindowShowing)" + e.getMessage());
        }
    }

    //Parte comun
    @FXML
    private void onActionCerrarSesion(ActionEvent event) {
        try {
            //Creamos la alerta del tipo confirmación.
            Alert alertCerrarSesion = new Alert(Alert.AlertType.CONFIRMATION);
            //Ponemos titulo de la ventana como titulo para la alerta.
            alertCerrarSesion.setTitle("Cerrar sesión");
            alertCerrarSesion.setHeaderText("¿Quieres cerrar sesión?");
            //Si acepta se cerrara esta ventana.
            alertCerrarSesion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    LOGGER.info("Cerrando sesión.");
                    stage.hide();
                }
            });
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    @FXML
    private void onActionSalir(ActionEvent event) {
        try {
            //Creamos la alerta con el tipo confirmación, con su texto y botones de
            //aceptar y cancelar.
            Alert alertCerrarAplicacion = new Alert(Alert.AlertType.CONFIRMATION, "Si sale de la aplicación cerrara\nautomáticamente la sesión.", ButtonType.NO, ButtonType.YES);
            //Añadimos titulo a la ventana como el alert.
            alertCerrarAplicacion.setTitle("Cerrar la aplicación");
            alertCerrarAplicacion.setHeaderText("¿Quieres salir de la aplicación?");
            //Si acepta cerrara la aplicación.
            alertCerrarAplicacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    LOGGER.info("Cerrando la aplicación.");
                    Platform.exit();
                }
            });
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    //Inicio de los metodos de navegación de la aplicación
    @FXML
    private void onActionAbrirGestorApuntes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("gestor_de_apuntes.fxml"));
            Parent root = (Parent) loader.load();
            GestorDeApuntesFXController controller
                    = ((GestorDeApuntesFXController) loader.getController());
            controller.setUser(user);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        } catch (Exception e) {
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor." + e.getMessage());
        }
    }

    @FXML
    private void onActionAbrirGestorPacks(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("gestor_de_packs.fxml"));
            Parent root = (Parent) loader.load();
            GestorDePacksFXController controller
                    = ((GestorDePacksFXController) loader.getController());
            controller.setUser(user);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        } catch (Exception e) {
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor." + e.getMessage());
        }
    }

    @FXML
    private void onActionAbrirGestorOfertas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("gestor_de_ofertas.fxml"));
            Parent root = (Parent) loader.load();
            GestorDeOfertasFXController controller
                    = ((GestorDeOfertasFXController) loader.getController());
            controller.setUser(user);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        } catch (Exception e) {
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor." + e.getMessage());
        }
    }

    @FXML
    private void onActionAbrirGestorMaterias(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("gestor_de_materias.fxml"));
            Parent root = (Parent) loader.load();
            GestorDeMateriasFXController controller
                    = ((GestorDeMateriasFXController) loader.getController());
            controller.setUser(user);
            controller.setStage(stage);
            controller.initStage(root);
            stage.hide();
        } catch (Exception e) {
            showErrorAlert("A ocurrido un error, reinicie la aplicación por favor.");
        }
    }
    //Fin de los metodos de navegación de la aplicación

    @FXML
    private void onActionAbout(ActionEvent event) {
        try {
            final WebView browser = new WebView();
            final WebEngine webEngine = browser.getEngine();

            URL url = this.getClass().getResource("/ayuda/ayuda_gestor_packs.html");
            webEngine.load(url.toString());

            stageVentanaAyuda = new Stage();
            stageVentanaAyuda.setTitle(webEngine.getTitle());

            Button btnAyudaCerrar = new Button("Cerrar");
            btnAyudaCerrar.setOnAction(this::cerrarVentanaAyuda);
            btnAyudaCerrar.setMnemonicParsing(true);
            btnAyudaCerrar.setText("_Cerrar");
            btnAyudaCerrar.setOnKeyPressed((key) -> {
                if (key.getCode().equals(KeyCode.ENTER)) {
                    btnAyudaCerrar.fire();
                }
            });
            stageVentanaAyuda.setOnShowing(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    btnAyudaCerrar.requestFocus();
                }
            });
            VBox root = new VBox();
            root.getChildren().addAll(browser, btnAyudaCerrar);

            Scene escenaAyuda = new Scene(root);
            stageVentanaAyuda.setScene(escenaAyuda);
            stageVentanaAyuda.initModality(Modality.APPLICATION_MODAL);
            stageVentanaAyuda.show();
        } catch (Exception e) {
            LOGGER.severe("Error al abrir la ventana de ayuda: " + e.getMessage());
            showErrorAlert("A ocurrido un error al abrir la ventana de ayuda");
        }
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón
     * btnCrearGestorPack.
     *
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionCrearGestorPack(ActionEvent event) {
        PackBean pack = new PackBean();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("crear_pack.fxml"));
            Parent root = (Parent) loader.load();
            CrearPackFXController controller
                    = ((CrearPackFXController) loader.getController());
            controller.setFXPack(this);
            controller.setPack(pack);
            controller.setOpcion(opcion);
            controller.initStage(root);
            if (opcion == 1) {
                manager.createPack(pack);
                cargarDatos();
                tablaPack.refresh();
            }
        } catch (BusinessLogicException e) {
            LOGGER.severe("Error al intentar crear un pack(createPack): "
                    + e.getMessage());
            showErrorAlert("Ha ocurrido un error.");
        } catch (IOException e) {
            LOGGER.severe("Error al intentar abrir la "
                    + "ventana(onActionCrearGestorPack): " + e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor.");
        }
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón
     * btnInformeGestorPack.
     *
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionInformeGestorPack(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport(getClass()
                    .getResourceAsStream("/view/informePack.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<ApunteBean>) this.tablaPack.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);

        } catch (JRException e) {
            LOGGER.severe("Error iniciar el informe: " + e.getMessage());
            showErrorAlert("No se ha podido generar el informe.");
        }
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón
     * btnBuscarGestorPack.
     *
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionBuscarGestorPack(ActionEvent event) {
        try {
            if (!tfFiltrarGestorPack.getText().trim().isEmpty()) {
                cargarDatos(tfFiltrarGestorPack.getText().trim());
            } else {
                cargarDatos();
            }
        } catch (Exception e) {
            LOGGER.severe("Error al intentar filtrar la busqueda de "
                    + "packs(onActionBuscarGestorPack): " + e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor.");
        }
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón
     * btnModificarApunteGestorPack.
     *
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionBtnModificarApunteGestorPack(ActionEvent event) {
        try {
            if (pack != null) {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("apunte_pack.fxml"));
                Parent root = (Parent) loader.load();
                ApuntePackFXController controller
                        = ((ApuntePackFXController) loader.getController());
                controller.setFXModificarPack(this);
                controller.setApuntes(pack.getApuntes());
                controller.initStage(root);
                if (opcion == 1) {
                    manager.removeApunte(pack, Integer.toString(apunte.getIdApunte()));
                    cargarDatos();
                    tablaPack.refresh();
                    tfTituloGestorPack.setText("");
                    tfDescripcionGestorPack.setText("");
                    this.dpDate.setValue(dateToLocalDate(new Date()));
                    pack = null;
                } else if (opcion == 2) {
                    manager.addApunte(pack, Integer.toString(apunte.getIdApunte()));
                    cargarDatos();
                    tablaPack.refresh();
                    tfTituloGestorPack.setText("");
                    tfDescripcionGestorPack.setText("");
                    this.dpDate.setValue(dateToLocalDate(new Date()));
                    pack = null;
                }
            } else {
                showWarningAlert("Seleccione un pack, después, vuelva a pulsar el botón.");
            }
        } catch (BusinessLogicException e) {
            LOGGER.severe("Error al intentar modificar un apunte de un "
                    + "pack(onActionBtnModificarApunteGestorPack): " + e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor. "
                    + e.getMessage());
        } catch (IOException e) {
            LOGGER.severe("Error al intentar abrir la "
                    + "ventana(onActionBtnModificarApunteGestorPack): " + e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor.");
        } catch (Exception e) {
            LOGGER.severe("Error al intentar modificar un "
                    + "pack(onActionBtnModificarGestorPack): " + e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor.");
        }
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón
     * btnModificarGestorPack.
     *
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionBtnModificarGestorPack(ActionEvent event) {
        try {
            if (pack != null) {
                if (tfTituloGestorPack.getText().trim().isEmpty()
                        || tfDescripcionGestorPack.getText().trim().isEmpty()) {
                    showErrorAlert("Los campos de texto no pueden estar vacios.");
                } else {
                    PackBean p = new PackBean(pack.getIdPack(),
                            tfTituloGestorPack.getText().trim(),
                            tfDescripcionGestorPack.getText().trim(),
                            localDateToDate(dpDate.getValue()), pack.getApuntes());
                    if (!(p.getTitulo().equals(pack.getTitulo())
                            && p.getDescripcion().equals(pack.getDescripcion())
                            && p.getFechaModificacion().equals(pack.getFechaModificacion()))) {
                        manager.editPack(p);
                        cargarDatos();
                        tablaPack.refresh();
                        tfTituloGestorPack.setText("");
                        tfDescripcionGestorPack.setText("");
                        this.dpDate.setValue(null);
                        pack = null;
                    }
                }
            } else {
                showWarningAlert("Seleccione un pack, después, vuelva a pulsar el botón.");
            }
        } catch (BusinessLogicException e) {
            LOGGER.severe("Error al intentar editar un "
                    + "pack(onActionBtnModificarGestorPack): " + e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la "
                    + "aplicación porfavor. " + e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Error al intentar modificar un "
                    + "pack(onActionBtnModificarGestorPack): " + e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor.");
        }
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón
     * btnEliminarGestorPack.
     *
     * @param event Evento que se ha lanzado.
     */
    @FXML
    private void onActionBtnEliminarGestorPack(ActionEvent event) {
        try {
            if (pack != null) {
                Alert alertCerrarAplicacion = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.NO, ButtonType.YES);
                //Añadimos titulo a la ventana como el alert.
                alertCerrarAplicacion.setTitle("Eliminar");
                alertCerrarAplicacion.setHeaderText("¿Estas seguro que lo deseas eliminar?");
                //Si acepta cerrara la aplicación.
                alertCerrarAplicacion.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        eliminar = true;
                    }
                });
                if (eliminar) {
                    manager.removePack(pack);
                    tablaPack.getItems().remove(this.pack);
                    tablaPack.refresh();
                    tfTituloGestorPack.setText("");
                    tfDescripcionGestorPack.setText("");
                    this.dpDate.setValue(dateToLocalDate(new Date()));
                    pack = null;
                    eliminar = false;
                }
            } else {
                showWarningAlert("Seleccione un pack, después, vuelva a pulsar el botón.");
            }
        } catch (BusinessLogicException e) {
            LOGGER.severe("Error al intentar borrar un "
                    + "pack(onActionBtnEliminarGestorPack): "
                    + e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la "
                    + "aplicación porfavor. " + e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Error al intentar eliminar un "
                    + "pack(onActionBtnEliminarGestorPack): " + e.getMessage());
            showErrorAlert("A ocurrido un error, reinicie la aplicación porfavor.");
        }
    }

    /**
     * Método para cargar todos los packs.
     */
    private void cargarDatos() {
        try {
            packs = manager.findAllPack();
            List<PackBean> packList = packs.stream().sorted(Comparator.
                    comparing(PackBean::getIdPack)).collect(Collectors.toList());
            packsObv = FXCollections.observableArrayList(new ArrayList<>(packList));
            tablaPack.setItems(packsObv);
        } catch (BusinessLogicException e) {
            LOGGER.severe("Error al intentar cargar los packs(cargarDatos()): "
                    + e.getMessage());
            showErrorAlert("Ha ocurrido un error cargando los packs.");
        }
    }

    /**
     * Método para cargar todos los packs y los filtra según el parámetro.
     *
     * @param string Cadena de caracteres a filtrar.
     */
    private void cargarDatos(String string) {
        try {
            packs = manager.findAllPack();
            List<PackBean> packList = packs.stream().filter(pack -> pack.getTitulo().
                    toLowerCase().contains(string.toLowerCase())).
                    sorted(Comparator.comparing(PackBean::getIdPack)).
                    collect(Collectors.toList());
            packsObv = FXCollections.observableArrayList(new ArrayList<>(packList));
            tablaPack.setItems(packsObv);
        } catch (BusinessLogicException e) {
            LOGGER.severe("Error al intentar cargar los "
                    + "packs(cargarDatos(String s)): " + e.getMessage());
            showErrorAlert("Ha ocurrido un error cargando los packs.");
        }
    }

    /**
     * Cierra la ventana de ayuda.
     *
     * @param event Evento que se ha lanzado.
     */
    public void cerrarVentanaAyuda(ActionEvent event) {
        stageVentanaAyuda.hide();
    }

    /**
     * Método que se ejecuta cuando cambia algún valor de una fila de la tabla.
     *
     * @param obvservable Valor observable.
     * @param oldValue Valor antiguo.
     * @param newValue Valor nuevo.
     */
    private void packClicked(ObservableValue obvservable, Object oldValue, Object newValue) {
        if (newValue != null) {
            pack = (PackBean) newValue;
            tfTituloGestorPack.setText(pack.getTitulo());
            tfDescripcionGestorPack.setText(pack.getDescripcion());
            dpDate.setValue(dateToLocalDate(pack.getFechaModificacion()));
            LOGGER.info("Método packClicked");
        }
    }

    /**
     * Método que cambia un objeto LocalDate a Date.
     *
     * @param date Objeto LocalDate.
     * @return Objeto Date.
     */
    public Date localDateToDate(LocalDate date) {
        return java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    /**
     * Método que cambia un objeto Date a LocalDate.
     *
     * @param date Objeto Date.
     * @return Objeto LocalDate.
     */
    public LocalDate dateToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
