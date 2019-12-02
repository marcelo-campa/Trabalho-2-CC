import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class verificador {
    static int lendo=0,escrevendo=0,lendoseguidos=0,escrevendoseguidos=0;

    public static void main(String[] args) throws IOException {
        String nomearq=args[0];
        FileReader reader = new FileReader(nomearq+".txt");
        BufferedReader leitor = new BufferedReader(reader);
        String linha;
        // loop que percorrerá todas as  linhas do arquivo.txt que eu quero ler
        while ((linha = leitor.readLine()) != null) {
            //No metodo StringTokenizer passo os parametros que quero ler, em seguida o que eu quero descartar no meu caso ( - ) e ( ; ).
            // Aqui determino que enquanto existir tokens que ele faça a leitura
            switch (linha)
            {
                case "Le()":
                    Le();
                    break;
                case "Escreve()":
                    Escreve();
                    break;
                case "Libero()":
                    Libero();
                    break;
                case "Espera()":
                    Espera();
                    break;
                case "SaiEscreve()":
                    SaiEscreve();
                    break;
                case "SaiLe()":
                    SaiLeitor();
                    break;
                case "protegeseguidaEscrita()":
                    protegeseguidasEscritura();
                 break;
                case "protegeseguidaLeitura()":
                    preotegeseguidasLeitura();
                    break;
                case "Esperaescseguidas()":
                    Esperaescseguidas();
                    break;
                case "Esperaleiseguidas()":
                    Esperaleiseguidas();
                    break;

                default:
                    exit("Erro, string não tratada! "+linha);
                    break;
            }
            System.out.println(linha);
        }

        leitor.close();
        reader.close();
        System.out.println("Tudo OK com o programa");


    }
    static void Esperaescseguidas()
    {
        if(escrevendoseguidos<10)
        {
            exit("Não há motivos para esperar escritas seguidas "+escrevendoseguidos);
        }
    }
    static void Esperaleiseguidas()
    {
        if(lendoseguidos<10)
        {
            exit("Não há motivos para esperar leituras seguidas "+lendoseguidos);
        }
    }

    static void Le()
    {
        if(escrevendo!=0)
        {
            exit("Não é possivel ler quando existe alguem escrevendo");
        }
        if(lendoseguidos>15)
        {
            exit("Lendo apos 15 leituras seguidas erro " +lendoseguidos);
        }
        lendo++;
        lendoseguidos++;
        escrevendoseguidos=0;
    }static void Escreve()
    {
        if(lendo!=0||escrevendo!=0)
        {
            exit("Não é possivel escrever quando existe alguem escrevendo/lendo!");
        }
        if(escrevendoseguidos>15)
        {
            exit("Escrevendo apos 15 escrituras seguidas erro ");
        }
        escrevendo++;
        escrevendoseguidos++;
        lendoseguidos=0;
    }
    static void Libero()
    {
        if(escrevendo!=0&&lendo!=0)
        {
            exit("Não é possivel liberar quando existe alguem escrevendo/lendo");
        }
    }
    static void Espera()
    {
        if(escrevendo==0&&lendo==0)
        {
            if(escrevendoseguidos<=10&& lendoseguidos<=10)
            exit("Não é necessario esperar quando possui ninguem escrevendo / lendo");
        }
    }
    public static void SaiEscreve()
    {
        escrevendo--;
    } public static void SaiLeitor()
    {
        lendo--;
    }
    public static void exit(String text)
    {
        System.out.println(text);
        System.exit(0);

    }
    public  static void preotegeseguidasLeitura()
    {
        if(escrevendoseguidos>0 )
        {
            exit("Estratégia de unanição na hora errada leitor "+ lendoseguidos);
        }

    }
    public  static void protegeseguidasEscritura()
    {
        if(lendoseguidos>0)
        {
            exit("Estratégia de unanição na hora errada escritor "+escrevendoseguidos );
        }

    }


}
