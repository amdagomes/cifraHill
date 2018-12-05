package cifrahill;

import util.CriptHill;

public class CifraHill {

    public static void main(String[] args) {
        CriptHill cifrar = new CriptHill();
        double[][] chave = new double[][]{{2, 3}, {1, 5}};
        cifrar.setChave(chave);
        System.out.println(cifrar.encripar("HelloWorld"));
        System.out.println("determinante: " + cifrar.determinate(chave));;
        double[][] inversa = cifrar.inversa(chave);
        System.out.println("inversa");
        for (int i = 0; i < inversa.length; i++) {
            for (int j = 0; j < inversa[0].length; j++) {
                System.out.print(inversa[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("Decripta");
        System.out.println(cifrar.decriptar());
    }

}
