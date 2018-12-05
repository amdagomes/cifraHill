package cifrahill;

import util.CifraHill;

public class Loader {

    public static void main(String[] args) {
        String mensagem = "helloworld";

        double[][] chave = new double[][]{{2, 3}, {1, 2}};

        int tamanhoBloco = 2;

        CifraHill encriptador = new CifraHill(chave, tamanhoBloco);
        String msgCifrada = encriptador.encriptar(mensagem);

        System.out.println(msgCifrada);
    }

}
