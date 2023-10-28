import java.io.File;


public class App {
    public static void main(String[] args) throws Exception {
        File f =new File("Libros.xml");
        Acceso a =new Acceso();
        a.abrirXMLaDOM(f); //Carga el árbol DOM en memoria
        a.insertarLibroEnDOM("LIBRO DE FOL", "PEPI", "2022");
        a.recorreDOMyMuestra();//Recorre y muestra la informacion del DOM 
         a.guardarDOMcomoArchivo("LibrosNuevo.xml");
        a.deleteNode("LIBRO DE FOL");
      
    }

    
}
