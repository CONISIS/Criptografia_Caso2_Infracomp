package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Juan
 */
public class Client
{

    // CADENAS DE CONTROL

    public static final String HOLA = "HOLA";

    public static final String OK = "OK";

    public static final String ERROR = "ERROR";

    // SEPARADOR

    public static final String SEPARADOR = " : ";

    // ALGORITMOS SIMÉTRICOS

    public static final String AES = "AES";

    public static final String BLOWFISH = "BLOWFISH";

    // ALGORITMOS ASIMÉTRICOS

    public static final String RSA = "RSA";

    // ALGORITMOS HMAC

    public static final String SHA1 = "HMACSHA1";

    public static final String SHA256 = "HMACSHA256";

    public static final String SHA384 = "HMACSHA384";

    public static final String SHA512 = "HMACSHA512";

    // ATRIBUTOS

    private Socket canal;

    private BufferedReader in;

    private PrintWriter out;

    // CONSTRUCTOR

    public Client(int pPuerto)
    {
        try
        {
            canal = new Socket("localhost", pPuerto);
            in = new BufferedReader(new InputStreamReader(canal.getInputStream()));
            out = new PrintWriter(canal.getOutputStream(), true);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    // MÉTODOS

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Ingrese el puerto por el que se desea conectar");
            int puerto = Integer.parseInt(input.readLine());
            Client cliente = new Client(puerto);
            cliente.comunicacion();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void comunicacion()
    {
        try
        {
            out.println("HOLA");
            System.out.println(in.readLine());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
