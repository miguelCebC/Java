import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

public class Acceso {
    Document doc;

    public void abrirXMLaDOM(File f) {

        try {
            System.out.println("Abriendo archivo");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);// Ignorar comentarios
            factory.setIgnoringElementContentWhitespace(true);// Ignorar espacios en blanco
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(f);
            System.out.println("DOM CREADO CON ÉXITO");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void recorreDOMyMuestra() {
        String[] datos = new String[3];// lo usamos para la información de cada libro
        Node nodo = null;
        Node root = doc.getFirstChild();
        NodeList nodelist = root.getChildNodes(); // (1)Ver dibujo del árbol
        // recorrer el árbol DOM. El 1er nivel de nodos hijos del raíz
        for (int i = 0; i < nodelist.getLength(); i++) {
            nodo = nodelist.item(i);// node toma el valor de los hijos de raíz
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {// miramos nodos de tipo Element

                Node ntemp = null;
                int contador = 1;
                // sacamos el valor del atributo publicado
                datos[0] = nodo.getAttributes().item(0).getNodeValue();
                // sacamos los valores de los hijos de nodo, Titulo y Autor
                NodeList nl2 = nodo.getChildNodes();// obtenemos lista de hijos (2)
                for (int j = 0; j < nl2.getLength(); j++) {// iteramos en esa lista
                    ntemp = nl2.item(j);
                    if (ntemp.getNodeType() == Node.ELEMENT_NODE) {
                        
                            // para conseguir el texto de titulo y autor, se //puedo hacer con
                            // getNodeValue(), también con //getTextContent() si es ELEMENT
                            datos[contador] = ntemp.getTextContent(); // también
                                                                      // datos[contador]=ntemp.getChildNodes().item(0).getNodeValue();

                            contador++;
                        
                    }
                    // el array de String datos[] tiene los valores que necesitamos

                    
                }System.out.println(datos[0] + "--" + datos[2] + "--" + datos[1]);
            }
        }
    }
    public void insertarLibroEnDOM(String titulo, String autor,String fecha) {
        try{
            System.out.println("Añadir libro al árbol DOM:"+titulo+";"+autor+";"+fecha);
            //crea los nodos=>los añade al padre desde las hojas a la raíz
            //CREATE TITULO con el texto en medio
            Node ntitulo=doc.createElement("Titulo");//crea etiquetas <Titulo>...</Titulo>
            Node ntitulo_text=doc.createTextNode(titulo);//crea el nodo texto para el Titulo
            ntitulo.appendChild(ntitulo_text);//añade el titulo a las etiquetas=><Titulo>titulo</Titulo>
            //Node nautor=doc.createElement("Autor").appendChild(doc.createTextNode(autor));//one line doesn't work
            //CREA AUTOR
            Node nautor=doc.createElement("Autor");
            Node nautor_text=doc.createTextNode(autor); 
            nautor.appendChild(nautor_text);
            //CREA LIBRO, con atributo y nodos Título y Autor 
            Node nLibro=doc.createElement("Libro");
            ((Element)nLibro).setAttribute("publicado",fecha);
            nLibro.appendChild(ntitulo); 
            nLibro.appendChild(nautor); 
            //APPEND LIBRO TO THE ROOT
    
    nLibro.appendChild(doc.createTextNode("\n"));//para insertar saltos de línea
    
            Node raiz=doc.getFirstChild();//tb. doc.getChildNodes().item(0)
            raiz.appendChild(nLibro);
            System.out.println("Libro insertado en DOM.");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public int deleteNode(String tit) {
		System.out.println("Buscando el Libro "+tit+" para borrarlo");
			try{
				//Node root=doc.getFirstChild();
				Node raiz= doc.getDocumentElement();
				NodeList nl1=doc.getElementsByTagName("Titulo");
				Node n1;
				for(int i=0;i<nl1.getLength();i++){
					n1=nl1.item(i);
					if(n1.getNodeType()==Node.ELEMENT_NODE){//redundante por getElementsByTagName, no lo es si buscamos getChildNodes()
						if (n1.getChildNodes().item(0).getNodeValue().equals(tit)){
							System.out.println("Borrando el nodo <Libro> con título "+tit);
							//n1.getParentNode().removeChild(n1); //borra <Titulo> tit </Titulo>, pero deja Libro y Autor
							n1.getParentNode().getParentNode().removeChild(n1.getParentNode());
						}
						
					}
				}
				System.out.println("Nodo borrado");
				//Guardar el arbol DOM en un nuevo archivo para mantener nuestro archivo original
				//guardarDOMcomoArchivo("LibrosBorrados.xml");
				
				return 0;
			}catch(Exception e){
				System.out.println(e);
				e.printStackTrace();
				return -1;
			}
		}
        
        void guardarDOMcomoArchivo(String nuevoArchivo) {
            try {
                Source src = new DOMSource(doc); // Definimos el origen
                StreamResult rst = new StreamResult(new File(nuevoArchivo)); // Definimos el resultado
                // Declaramos el Transformer que tiene el método .transform() que necesitamos.
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
        
                // Opción para indentar el archivo
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                
                transformer.transform(src, (javax.xml.transform.Result) rst);
                System.out.println("Archivo creado del DOM con éxito\n");
            }
            catch (Exception e) {
                e.printStackTrace();}
        }

}