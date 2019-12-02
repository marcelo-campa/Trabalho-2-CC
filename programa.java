import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class Monitor{

    public int lendo,escrevendo;
    public String nome;
    public int numleitura,numescritura,leiturasseguidas,escriturasseguidas;
    int maiorleituraseguida,maiorescrituraseguida;
    PrintWriter printWriter;
    public  Monitor(int nl,int ne, String nomearq) throws IOException {
        lendo=0;
        escrevendo=0;
        numleitura= nl;
        numescritura=ne;
        leiturasseguidas=0;
        escriturasseguidas=0;
        FileWriter fileWriter= new FileWriter(nomearq+".txt",false);

        printWriter= new PrintWriter(fileWriter);

    }

    public synchronized void Entraleitor() throws InterruptedException {
       // printWriter.println("EntraLe()");
        while(leiturasseguidas>=10)
        {
            System.out.println("Esperando por conta de mais leituras seguidas "+leiturasseguidas);
            printWriter.println("Esperaleiseguidas()");

            wait();
        }
        while (escrevendo>0 )
        {
            System.out.println("Leitor espera");
            printWriter.println("Espera()");
            wait();
        }
        lendo++;
        leiturasseguidas++;
        escriturasseguidas=0;
        printWriter.println("Le()");
        maiorleituraseguida= leiturasseguidas>maiorleituraseguida ?leiturasseguidas:maiorleituraseguida;



    }
    public synchronized  void SaiLeitor()
    {
        lendo--;
        printWriter.println("SaiLe()");
        if(lendo==0&&escrevendo==0)
        {
            notifyAll();
            printWriter.println("Libero()");
            System.out.println("Sai leitor");
        }
        decreaseLeituras();

    }
    public  synchronized  int getNumleitura()
    {
        return  numleitura;
    }
    public  synchronized  int getNumescritura()
    {
        return  numleitura;
    }

    public synchronized int getEscriturasseguidas() {
        return escriturasseguidas;
    }

    public synchronized int getLeiturasseguidas() {
        return leiturasseguidas;
    }

    public synchronized void EntraEscritor() throws InterruptedException {
        //printWriter.println("EntraEscreve()");
        while(escriturasseguidas>=10)
        {
            System.out.println("Esperando por conta de mais leituras seguidas "+escriturasseguidas);
            printWriter.println("Esperaescseguidas()");

            wait();
        }
        while (escrevendo>0||lendo>0)
        {
            System.out.println("Escritor espera");
            printWriter.println("Espera()");
            wait();
        }
        escrevendo++;
        printWriter.println("Escreve()");
        escriturasseguidas++;
        leiturasseguidas=0;
        maiorescrituraseguida= escriturasseguidas>maiorescrituraseguida?escriturasseguidas:maiorescrituraseguida;
    }
    public synchronized  void SaiEscritor()
    {
        escrevendo--;
        printWriter.println("SaiEscreve()");
        if(lendo==0&&escrevendo==0)
        {
            notifyAll();
            printWriter.println("Libero()");

            System.out.println("Sai escritor ");

        }
        descreaseEscrituras();
    }
    public synchronized void decreaseLeituras()
    {
        numleitura--;
        if(leiturasseguidas>10)
        {
            try {

                printWriter.println("protegeseguidaLeitura()");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public synchronized void descreaseEscrituras()
    {
        numescritura--;
        if(escriturasseguidas>10)
        {
            try {
                printWriter.println("protegeseguidaEscrita()");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}

class  Escritor extends  Thread{
    public int id;
    Monitor monit;
    public Escritor(int idthread,Monitor m)
    {
        id=idthread;
        monit=m;
    }
    @Override
    public void run() {
        while(monit.getNumescritura()>0)
        {
            try {
                monit.EntraEscritor();
                programa.bufferescrita=id;
                System.out.println("Thread ["+id+"] Escreveu no buffer :  "+programa.bufferescrita);
                System.out.println("Escrituras seguidas:  "+monit.getEscriturasseguidas());
                monit.SaiEscritor();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
class Leitor extends  Thread{
    public int id;
    Monitor monit;
    PrintWriter printWriter;


    public Leitor(int idthread,Monitor m) throws IOException {
        id=idthread;
        monit=m;
        FileWriter fileWriter= new FileWriter(id+".txt",false);
        printWriter= new PrintWriter(fileWriter);

    }

    @Override
    public void run() {
        while (monit.getNumleitura()>0) {
            try {
                monit.Entraleitor();
                System.out.println("Thread [" + id + "] leu o valor : " + programa.bufferescrita);
                System.out.println("Leituras seguidas:  "+monit.getLeiturasseguidas());

                printWriter.println(programa.bufferescrita);
                monit.SaiLeitor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        printWriter.close();

    }
}


public class programa {
    public  static  int bufferescrita;
    public static void main(String[] args) throws IOException, InterruptedException {
        //quantidade de threads leitoras, quantidade de threads escritoras, numero de leituras (das ´
        //threads leitoras), numero de escritas (das threads escritoras), e o nome do arquivo de ´
        int qntleitor=Integer.parseInt(args[0]);
        int qntescritor=Integer.parseInt(args[1]);
        int numleitura=Integer.parseInt(args[2]);
        int numescritura=Integer.parseInt(args[3]);
        String nomearq=args[4];
        Monitor monitor= new Monitor(numescritura,numleitura, nomearq);
        Escritor[] escritores = new Escritor[qntescritor];
        Leitor[] leitores = new Leitor[qntleitor];
        for(int i=0;i<escritores.length ;i++)
        {
            escritores[i]= new Escritor((i+1),monitor);

        }
        for(int i=0;i<leitores.length ;i++)
        {
            leitores[i]= new Leitor((i+1),monitor);

        }
        int indexescrito=0,indexleitor=0;
        while (indexescrito<escritores.length||indexleitor<leitores.length)
        {
            if(indexescrito<escritores.length)
            {
                escritores[indexescrito].start();
                indexescrito++;
            }
            if(indexleitor<leitores.length)
            {
                leitores[indexleitor].start();
                indexleitor++;

            }
        }
        for(int i=0;i<escritores.length;i++)
        {
            escritores[i].join();
        }
        for(int i=0;i<leitores.length;i++)
        {
            leitores[i].join();
        }
        monitor.printWriter.close();
        //System.out.println("Maior leitura seguida "+monitor.maiorleituraseguida);
        //System.out.println("Maior escritura seguida "+monitor.maiorescrituraseguida);



    }
}
