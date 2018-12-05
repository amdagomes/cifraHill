package util;

import java.util.ArrayList;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class CifraHill {

    double[][] chave;
    double[][] inversa;
    int tamanhoBloco;

    public CifraHill(double[][] chave, int tamanho) {
        this.chave = chave;
        this.tamanhoBloco = tamanho;
    }

    public String encriptar(String mensagem) {
        
        String[] blocos = dividirMensagem(mensagem);

        ArrayList<double[][]> valores = gerarMatriz(blocos);

        ArrayList<double[][]> criptValores = multiplicaMatrizes(chave, valores);

        String[] criptSubPalavras = toLetras(criptValores);

        String msgCriptografada = formarPalavra(criptSubPalavras);

        return msgCriptografada;
    }
    
    private String[] dividirMensagem(String mensagem) {
        String palavraCompleta = completarMensagem(mensagem);

        int quantBloco = palavraCompleta.length() / this.tamanhoBloco;
        String[] blocos = new String[quantBloco];

        int inicioIndex = 0;
        int fimIndex = tamanhoBloco;
        for (int i = 0; i < quantBloco; i++) {
            blocos[i] = palavraCompleta.substring(inicioIndex, fimIndex);
            inicioIndex += tamanhoBloco;
            fimIndex += tamanhoBloco;
        }

        return blocos;
    }

    private String completarMensagem(String mensagem) {
        int mod = mensagem.length() % this.tamanhoBloco;
        if (mod == 0) {
            return mensagem;
        }
        String novaPalavra = mensagem;
        for (int i = 0; i < this.tamanhoBloco - mod; i++) {
            novaPalavra += "a";
        }
        return novaPalavra;
    }

    private ArrayList<double[][]> gerarMatriz(String[] blocos) {
        ArrayList<double[][]> result = new ArrayList<>();
        for (String palavra : blocos) {
            result.add(gerarMatriz(palavra.toCharArray()));
        }
        return result;
    }

    public static double[][] gerarMatriz(char[] letras) {
        double[][] matriz = new double[letras.length][1];
        for (int i = 0; i < letras.length; i++) {
            matriz[i][0] = asValor(letras[i]);
        }
        return matriz;
    }

    public static char asLetra(double valor) {
        return (char) ((char) (valor % 26) + 97);
    }

    public static int asValor(char letra) {
        return letra - 97;
    }

    public double[][] multiplicar(double[][] chave, double[][] msgOrdem) {
        RealMatrix matriz = new Array2DRowRealMatrix(chave);
        RealMatrix mensagem = new Array2DRowRealMatrix(msgOrdem);
        RealMatrix produto = matriz.multiply(mensagem);
        double[][] resultado = produto.getData();

        return resultado;
    }

    public ArrayList<double[][]> multiplicaMatrizes(double[][] matrizA, ArrayList<double[][]> matrizesB) {
        ArrayList<double[][]> result = new ArrayList<>();
        for (double[][] matrizB : matrizesB) {
            result.add(multiplicar(matrizA, matrizB));
        }
        return result;
    }

    public static char[] toLetras(double[][] matriz) {
        char[] letras = new char[matriz.length];
        for (int i = 0; i < matriz.length; i++) {
            letras[i] = asLetra(matriz[i][0]);
        }
        return letras;
    }

    public static String[] toLetras(ArrayList<double[][]> matrizes) {
        String[] subPalavras = new String[matrizes.size()];
        for (int i = 0; i < matrizes.size(); i++) {
            char[] letras = toLetras(matrizes.get(i));
            subPalavras[i] = String.copyValueOf(letras);
        }
        return subPalavras;
    }

    private String formarPalavra(String[] subPalavras) {
        String result = "";
        for (String subPalavra : subPalavras) {
            result += subPalavra;
        }
        return result;
    }
}
