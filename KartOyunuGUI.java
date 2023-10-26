package Arrays;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class KartOyunuGUI extends JFrame {
    private Kart[][] kartlar = new Kart[4][4];
    private JButton[][] kartButtons = new JButton[4][4];
    private Kart ilkTahmin = null;
    private boolean ikinciTahmin = false;

    public KartOyunuGUI() {
        setTitle("Kart Oyunu");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        Random rand = new Random();

        char[] alfabe = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        int kartSayisi = 16;
        char[] karakterler = new char[kartSayisi];

        // Rastgele 8 karakter seçme
        for (int i = 0; i < kartSayisi / 2; i++) {
            char rastgeleKarakter;
            do {
                rastgeleKarakter = alfabe[rand.nextInt(alfabe.length)];
            } while (containsCharacter(karakterler, rastgeleKarakter, i));
            karakterler[i] = karakterler[i + kartSayisi / 2] = rastgeleKarakter;
        }

        // Karakterleri rastgele yerleştirme
        for (int i = 0; i < kartSayisi; i++) {
            int randomRow, randomCol;
            do {
                randomRow = rand.nextInt(4);
                randomCol = rand.nextInt(4);
            } while (kartlar[randomRow][randomCol] != null);

            kartlar[randomRow][randomCol] = new Kart(karakterler[i]);
        }


        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                kartButtons[i][j] = new JButton("");
                kartButtons[i][j].setFont(new Font("Arial", Font.PLAIN, 24));
                kartButtons[i][j].setBackground(Color.WHITE);
                kartButtons[i][j].setOpaque(true);

// Kartların boyutunu ayarlamak için setPreferredSize kullanabilirsiniz.
                Dimension buttonSize = new Dimension(100, 150); // Genişlik ve yükseklik değerlerini ayarlayabilirsiniz.
                kartButtons[i][j].setPreferredSize(buttonSize);

                kartButtons[i][j].addActionListener(new KartButtonListener(i, j));
                kartlar[i][j].setRow(i);
                kartlar[i][j].setCol(j);
                c.gridx = j;
                c.gridy = i;
                add(kartButtons[i][j], c);
            }
        }
    }
    private boolean containsCharacter(char[] array, char character, int maxIndex) {
        for (int i = 0; i < maxIndex; i++) {
            if (array[i] == character) {
                return true;
            }
        }
        return false;
    }
    private class KartButtonListener implements ActionListener {
        private int row;
        private int col;

        public KartButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!kartlar[row][col].isTahmin() && !ikinciTahmin) {
                kartButtons[row][col].setText(kartlar[row][col].getDeger() + "");
                kartlar[row][col].setTahmin(true);
                if (ilkTahmin == null) {
                    ilkTahmin = kartlar[row][col];
                } else {
                    if (ilkTahmin.getDeger() == kartlar[row][col].getDeger()) {
                        ilkTahmin = null;
                    } else {
                        ikinciTahmin = true;
                        Timer timer = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                kartButtons[row][col].setText("");
                                kartButtons[row][col].setBackground(Color.WHITE);
                                kartButtons[row][col].setOpaque(true);
                                kartButtons[ilkTahmin.getRow()][ilkTahmin.getCol()].setText("");
                                kartButtons[ilkTahmin.getRow()][ilkTahmin.getCol()].setBackground(Color.WHITE);
                                kartButtons[ilkTahmin.getRow()][ilkTahmin.getCol()].setOpaque(true);
                                kartlar[row][col].setTahmin(false);
                                kartlar[ilkTahmin.getRow()][ilkTahmin.getCol()].setTahmin(false);
                                ikinciTahmin = false;
                                ilkTahmin = null;
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }

                if (OyunBittiMi()) {
                    JOptionPane.showMessageDialog(KartOyunuGUI.this, "Tebrikler! Oyunu Bitirdiniz!");
                    System.exit(0);
                }
            }
        }
    }

    public boolean OyunBittiMi() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!kartlar[i][j].isTahmin()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KartOyunuGUI oyun = new KartOyunuGUI();
            oyun.setVisible(true);
        });
    }
}
