package util;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author Amand
 */
public class CriptHill {

    private char[] alfabeto = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'x', 'y', 'z', 'w'};
    private double[] valorAlfabeto = new double[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
    private double[][] chave;
    private double[][] matrizMsg;
    private String msgCifrada;
    private String msgDecriptada;

    public CriptHill() {
        this.chave = new double[][]{{1, 2}, {2, 3}};
        this.msgCifrada = "";
        this.msgDecriptada = "";
    }

    public String encripar(String msg) {
        substituirMensagem(msg);
        if (this.chave[0].length != matrizMsg.length) {
            return "As matrizes são incopativeis";
        }
        double[][] novaOrdemMsg = codificar(this.chave, this.matrizMsg);

        for (int i = 0; i < novaOrdemMsg.length; i++) {
            for (int j = 0; j < novaOrdemMsg[0].length; j++) {
                encripta(novaOrdemMsg, i, j, 0);
            }
        }
        return this.msgCifrada;
    }

    public void setChave(double[][] chave) {
        if (chave.length != chave[0].length) {
            System.out.println("Chave precisa ser uma matriz quadrada");
        }
        this.chave = chave;
    }

    public String decriptar() {
        double[][] inversa = inversa(chave);
        double[][] produto = multiplicaMatrizes(inversa, matrizMsg);
        double[][] matrizDescifrada = new double[produto.length][produto[0].length];
        for (int i = 0; i < matrizDescifrada.length; i++) {
            for (int j = 0; j < matrizDescifrada[0].length; j++) {
                matrizDescifrada[i][j] = produto[i][j] % alfabeto.length;
            }
        }
        System.out.println("tamanho: " + alfabeto.length);
        System.out.println("matriz");
        for (int i = 0; i < matrizDescifrada.length; i++) {
            for (int j = 0; j < matrizDescifrada[0].length; j++) {
                System.out.print(matrizDescifrada[i][j] + " ");
            }
            System.out.println("");
        }
        for (int i = 0; i < matrizDescifrada.length; i++) {
            for (int j = 0; j < matrizDescifrada[0].length; j++) {
                decripta(matrizDescifrada[i][j], 0);
            }
        }
        return this.msgDecriptada;
    }

    private void substituirMensagem(String mensagem) {
        char[][] msg = gerarMatrizMsg(mensagem.toLowerCase());
        this.matrizMsg = new double[msg.length][msg[0].length];
        for (int i = 0; i < msg.length; i++) {
            for (int j = 0; j < msg[0].length; j++) {
                trocaValorLetra(msg[i][j], 0, i, j);
            }
        }
    }

    //gera a matriz da mensagem
    private char[][] gerarMatrizMsg(String mensagem) {
        //int tamanho = (int) Math.sqrt(mensagem.length());
        int tamanho = 5;
        char[] msg = mensagem.toCharArray();
        //char[][] matrizMsg = new char[tamanho][tamanho];
        char[][] matrizMsg = new char[2][tamanho];
        int indiceAux = 0;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < tamanho; j++) {
                matrizMsg[i][j] = msg[indiceAux];
                indiceAux++;
            }
        }

        return matrizMsg;
    }

    //substitui os caracteres da mensagem por numeros
    private void trocaValorLetra(char letra, int aux, int linha, int coluna) {
        if (aux == this.alfabeto.length - 1) {
            this.matrizMsg[linha][coluna] = this.valorAlfabeto[aux];
        }
        if (letra == this.alfabeto[aux]) {
            this.matrizMsg[linha][coluna] = this.valorAlfabeto[aux];
        } else {
            aux++;
            trocaValorLetra(letra, aux, linha, coluna);
        }
    }

    //pega a matriz resultante da multiplacação e cria uma nova com os valores do resto da divisão pelo tamanho do alfabeto
    private double[][] codificar(double[][] chave, double[][] msgOrdem) {
        double[][] produtoMatriz = multiplicaMatrizes(chave, msgOrdem);
        double[][] resultado = new double[produtoMatriz.length][produtoMatriz[0].length];
        int tamanho = this.alfabeto.length;

        for (int i = 0; i < resultado.length; i++) {
            for (int j = 0; j < resultado[0].length; j++) {
                resultado[i][j] = produtoMatriz[i][j] % tamanho;
            }
        }

        return resultado;
    }

    private double[][] multiplicaMatrizes(double[][] chave, double[][] msgOrdem) {
        double[][] resultado = new double[chave.length][msgOrdem[0].length];
        int soma = 0;
        for (int k = 0; k < chave.length; k++) {
            for (int i = 0; i < msgOrdem[0].length; i++) {
                for (int j = 0; j < chave[0].length; j++) {
                    soma += chave[k][j] * msgOrdem[j][i];
                }
                resultado[k][i] = soma;
                soma = 0;
            }
        }

        return resultado;
    }

    private void encripta(double[][] matriz, int linha, int coluna, int aux) {
        if (aux == this.alfabeto.length - 1) {
            this.msgCifrada += this.alfabeto[aux];
        }
        if (matriz[linha][coluna] == this.valorAlfabeto[aux]) {
            this.msgCifrada += this.alfabeto[aux];
        } else {
            aux++;
            encripta(matriz, linha, coluna, aux);
        }
    }

    private void decripta(double valor, int aux) {
        if (aux == this.alfabeto.length - 1) {
            this.msgDecriptada += this.alfabeto[aux];
        }
        if (valor == this.valorAlfabeto[aux]) {
            this.msgDecriptada += this.alfabeto[aux];
        } else {
            aux++;
            decripta(valor, aux);
        }
    }
    
//    private void decripta(double valor, int linha, int coluna, int aux) {
//        if (valor == this.matrizMsg[linha][coluna]) {
//            msgDecriptada += String.valueOf(this.alfabeto[aux]);
//        } else if (coluna == this.matrizMsg[0].length && linha == this.matrizMsg.length) {
//            msgDecriptada += String.valueOf(this.alfabeto[aux]);
//        } else if (coluna < this.matrizMsg[0].length - 1) {
//            coluna++;
//            decripta(valor, linha, coluna, aux);
//        } else {
//            linha++;
//            coluna = 0;
//            decripta(valor, linha, coluna, aux);
//        }
//    }

    public double determinate(double[][] matriz) {
        RealMatrix relmatriz = new Array2DRowRealMatrix(matriz);
        LUDecomposition lu = new LUDecomposition(relmatriz);
        return lu.getDeterminant();
    }

    public double[][] inversa(double[][] matriz) {
        RealMatrix relmatriz = new Array2DRowRealMatrix(matriz);
        LUDecomposition lu = new LUDecomposition(relmatriz);
        RealMatrix inversa = lu.getSolver().getInverse();
        return inversa.getData();
    }

}
