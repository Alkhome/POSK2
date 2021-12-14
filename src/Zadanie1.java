import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class Zadanie1 implements ActionListener {
    static JFrame ramka = new JFrame();
    JButton bg_changed = new JButton("Przycisk");
    JLabel wait = new JLabel();
    JLabel press = new JLabel();
    int number_of_rounds = -1;
    long beggining_time = -1;
    long pressed_time = -1;
    long[] reaction_times = new long[5];
    public Zadanie1()
    {
        ramka.setTitle("Testowanie reakcji");
        ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ramka.setSize(600, 600);
        ramka.setResizable(false);
        ramka.setVisible(true);
        ramka.setLocationRelativeTo(null);
        ramka.getContentPane().setBackground(new Color(107, 184, 202));
        ramka.setLayout(null);

        wait.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 40));
        wait.setText("Nie naciskaj przycisku");
        wait.setHorizontalAlignment(SwingConstants.CENTER);
        wait.setBounds(20,180,560,70);
        wait.setVisible(false);
        ramka.add(wait);

        press.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 40));
        press.setText("Nacisnij przycisk!");
        press.setHorizontalAlignment(SwingConstants.CENTER);
        press.setBounds(20,180,560,70);
        press.setVisible(false);
        ramka.add(press);

        bg_changed.setOpaque(true);
        bg_changed.setBounds(20,480,560, 70);
        bg_changed.addActionListener(this);
        bg_changed.setHorizontalAlignment(SwingConstants.CENTER);
        bg_changed.setVisible(true);
        ramka.add(bg_changed);

        try {
            File wyniki = new File("scores.txt");
            if (wyniki.createNewFile()) {
                System.out.println("Stworzono plik: " + wyniki.getName());
                FileWriter myWriter = new FileWriter("scores.txt");
                myWriter.write("2147483647\n2147483647");
                myWriter.close();
            }
        } catch (IOException e) {
            System.out.println("Wystapil blad z plikiem.");
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, """
                Witaj w grze, mierzacej czas reakcji!
                Zostaniesz postawiony przez 2 zadaniami, jedno z nich badajace Twoj czas reakcji,
                na bodzce optyczne, a drugie na bodzce akustyczne.
                Przed rozpoczeciem kazdego cwiczenia, bedziesz mial runde probna,
                zeby moc sie sprawdzic i zapoznac z zadaniem.
                
                Program na biezace informuje o wynikach, oraz robi zbiorcze podsumowanie, zarowno
                po ukonczeniu poziomu, jak i calej gry.
                """,
                "Witaj!", JOptionPane.INFORMATION_MESSAGE);

        JOptionPane.showMessageDialog(null, """
                Zad 1. polega na naciesnieciu przycisku znajdujacego sie w dolnej czesci pola gry,
                kiedy kolor tla zmieni sie z czerwonego na zielony. Dodatkowo, w celu ulatwienia
                mozliwosci korzystania z tych testow dla osob, majacych problem z rozroznianiem
                kolorw, na srodku ekranu gry wyswietla sie informacja, mowiaca,
                kiedy nalezy naciskac przycisk
                """,
                "Uwaga!", JOptionPane.INFORMATION_MESSAGE);

        changing_bg();
    }

    public void changing_bg()
    {
        beggining_time = -1;
        press.setVisible(false);
        wait.setVisible(true);
        ramka.getContentPane().setBackground(Color.RED);
        Random rng = new Random();
        Timer timer = new Timer(rng.nextInt(1000) + 2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ramka.getContentPane().setBackground(Color.GREEN);
                beggining_time = System.currentTimeMillis();
                press.setVisible(true);
                wait.setVisible(false);
            }
        });

        timer.setRepeats(false);
        timer.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bg_changed) {
            if (number_of_rounds == -1) {
                //runda testowa na poczatku
                if (beggining_time == -1) {
                    JOptionPane.showMessageDialog(null, """
                                    Nacisnales przycisk za szybko!
                                    Pamietaj, zeby nacisnac przycisk dopiero,
                                    kiedy tlo zmieni sie na zielone""",
                            "Uwaga!", JOptionPane.INFORMATION_MESSAGE);
                }
                else if (beggining_time > 0) {
                    pressed_time = System.currentTimeMillis();
                    long reaction_time = pressed_time - beggining_time;
                    JOptionPane.showMessageDialog(null, "Bardzo dobrze!\n" +
                                    "Teraz przejdziesz do testow, w ktorych bedzie liczony Twoj czas reakcji\n"+
                            "Twoj czas reakcji w tej probie wyniosl: " + reaction_time + " ms",
                            "Dobrze!!", JOptionPane.INFORMATION_MESSAGE);
                    number_of_rounds++;
                    changing_bg();
                }
            }

            else
            {
                if (beggining_time == -1) {
                    JOptionPane.showMessageDialog(null, "Nacisnales przycisk za szybko!",
                            "Uwaga!", JOptionPane.INFORMATION_MESSAGE);
                } else if (beggining_time > 0) {
                    System.out.println(number_of_rounds);
                    pressed_time = System.currentTimeMillis();
                    long reaction_time = pressed_time - beggining_time;
                    reaction_times[number_of_rounds] = reaction_time;
                    JOptionPane.showMessageDialog(null, "Twoj czas reakcji to: " +
                                    reaction_time + " ms",
                            "Dobrze!", JOptionPane.INFORMATION_MESSAGE);

                    if (number_of_rounds == 4) {
                        long sum_of_times = 0;
                        for (long reactionTime : reaction_times) {
                            sum_of_times += reactionTime;
                        }
                        long average_of_times = sum_of_times / reaction_times.length;

                        JOptionPane.showMessageDialog(null, "Twoj sredni czas reakcji to: " +
                                        average_of_times + " ms\n" +
                                        "Przejedziesz teraz do kolejnego testu",
                                "Koniec poziomu!", JOptionPane.INFORMATION_MESSAGE);
                        Zadanie2 zad2 = new Zadanie2();
                        zad2.visual_reaction_time = average_of_times;
                        ramka.dispose();
                    }
                    number_of_rounds++;
                    changing_bg();
                }
            }
        }
    }
}
