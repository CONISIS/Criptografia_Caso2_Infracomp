package cliente;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

/**
 * @author Juan
 */
public class Client
{
    // CADENAS DE CONTROL

    public static final String HOLA = "HOLA";

    public static final String OK = "OK";

    public static final String ERROR = "ERROR";

    public static final String ALGORITMOS = "ALGORITMOS";

    // SEPARADOR

    public static final String SEPARADOR = ":";

    // ALGORITMOS SIMÉTRICOS

    public static final String AES = "AES";

    public static final String BLOWFISH = "Blowfish";

    // ALGORITMOS ASIMÉTRICOS

    public static final String RSA = "RSA";

    // ALGORITMOS HMAC

    public static final String SHA1 = "HMACSHA1";

    public static final String SHA256 = "HMACSHA256";

    public static final String SHA384 = "HMACSHA384";

    public static final String SHA512 = "HMACSHA512";

    // ATRIBUTOS

    private Socket canal;

    private BufferedReader inServidor;

    private BufferedReader inUsuario;

    private PrintWriter out;

    // CONSTRUCTOR

    public Client(BufferedReader in, int pPuerto)
    {
        try
        {
            canal = new Socket("localhost", pPuerto);
            inServidor = new BufferedReader(new InputStreamReader(canal.getInputStream()));
            inUsuario = in;
            out = new PrintWriter(canal.getOutputStream(), true);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    // MÉTODOS

    public static void main(String[] args)
    {
        try
        {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Ingrese el puerto por el que se desea conectar");
            int puerto = Integer.parseInt(input.readLine());
            Client cliente = new Client(input, puerto);
            cliente.comunicacion1();
            String certificado = cliente.getInServidor().readLine();
            cliente.comunicacion2(certificado);
            cliente.comunicacion3();
            cliente.comunicacion4();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Socket getCanal()
    {
        return canal;
    }

    public void setCanal(Socket canal)
    {
        this.canal = canal;
    }

    public BufferedReader getInServidor()
    {
        return inServidor;
    }

    public void setInServidor(BufferedReader inServidor)
    {
        this.inServidor = inServidor;
    }

    public BufferedReader getInUsuario()
    {
        return inUsuario;
    }

    public void setInUsuario(BufferedReader inUsuario)
    {
        this.inUsuario = inUsuario;
    }

    public PrintWriter getOut()
    {
        return out;
    }

    public void setOut(PrintWriter out)
    {
        this.out = out;
    }

    /**
     * Etapa 1 de la comunicación con el servidor en que se seleccionan los algoritmos a usar.
     *
     * @throws Exception En caso de que el servidor mande un mensaje de ERROR.
     */
    public void comunicacion1() throws Exception
    {
        try
        {
            getOut().println(HOLA);
            if (ERROR.equals(getInServidor().readLine()))
                throw new Exception("Hubo un error al iniciar la comunicación con el servidor");
            System.out.println();
            System.out.println("Seleccione el algoritmo simetrico que quiere :usar (1,2) \n" +
                                       "1) AES\n" +
                                       "2) Blowfish");
            int res1 = Integer.parseInt(getInUsuario().readLine());
            String simetrico = "";
            simetrico = res1 == 1 ? AES : BLOWFISH;
            System.out.println("Seleccione el algoritmo HMAC que quiere :usar (1,2,3,4) \n" +
                                       "1) HmacSHA1\n" +
                                       "2) HmacSHA256\n" +
                                       "3) HmacSHA384\n" +
                                       "4) HmacSHA512");
            int res2 = Integer.parseInt(getInUsuario().readLine());
            String hmac = "";
            if (1 == res2)
                hmac = SHA1;
            else if (2 == res2)
                hmac = SHA256;
            else if (3 == res2)
                hmac = SHA384;
            else
                hmac = SHA512;
            System.out.println(ALGORITMOS + SEPARADOR + simetrico + SEPARADOR + RSA + SEPARADOR + hmac);
            getOut().println(ALGORITMOS + SEPARADOR + simetrico + SEPARADOR + RSA + SEPARADOR + hmac);
            if (ERROR.equals(getInServidor().readLine()))
                throw new Exception("Hubo un error al enviar algoritmos de cifrado al servidor");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Etapa 2 del protocolo de comunicación, se autenticará al servidor.
     *
     * @param certificado certificado del servidor web
     */
    public void comunicacion2(String certificado)
    {
    	System.out.println(certificado);
    	byte[] certEntryBytes = certificado.getBytes();
    	InputStream in = new ByteArrayInputStream(certEntryBytes);
    	try {
    		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
    		X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
			cert.checkValidity();
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println("Certificado valido");  
    }

    /**
     * Etapa 3 del protocolo de comunicación, se autenticará al cliente.
     */
    public void comunicacion3()
    {
    }

    /**
     * Etapa 4 del protooolo de comunicación, se solicita información y se valifa la respuesta.
     */
    public void comunicacion4()
    {
    }
}
