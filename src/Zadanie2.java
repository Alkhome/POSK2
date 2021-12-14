import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;


public class Zadanie2 implements ActionListener {
    static JFrame ramka = new JFrame();
    JButton audio_played = new JButton("Przycisk");
    int number_of_rounds = -1;
    long beggining_time = -1;
    long pressed_time = -1;
    long[] reaction_times = new long[5];
    long visual_reaction_time = 0;
    public Zadanie2()
    {
        ramka.setTitle("Testowanie reakcji");
        ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ramka.setSize(600, 600);
        ramka.setResizable(false);
        ramka.setVisible(true);
        ramka.setLocationRelativeTo(null);
        ramka.getContentPane().setBackground(new Color(107, 184, 202));
        ramka.setLayout(null);

        audio_played.setOpaque(true);
        audio_played.setBounds(20,480,560, 70);
        audio_played.addActionListener(this);
        ramka.add(audio_played);

        JOptionPane.showMessageDialog(null, """
                Zadanie 2 Dziala bardzo podobnie do 1. jednak tutaj zamiast zmiany tla,
                grany jest krotki dzwiek, po ktorego uslyszeniu nalezy niezwlocznie nacisnac przycisk,
                ktory znajduje się u dolu ekranu
                """,
                "Uwaga!", JOptionPane.INFORMATION_MESSAGE);

        playing_audio();

    }
    public void czy_lepszy_wynik(long obecny_wynik)
    {
        String end_message = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("scores.txt"));
            String line = reader.readLine();
            java.util.List<String> temp_array = new ArrayList<>();

            while (line != null)
            {
                temp_array.add(line);
                line = reader.readLine();
            }
            String[] tempsArray = temp_array.toArray(new String[0]);
            System.out.println(tempsArray[0]);
            System.out.println(tempsArray[1]);

            if(Integer.parseInt(tempsArray[0]) > obecny_wynik)
            {
                if(Integer.parseInt(tempsArray[0]) == 2147483647){
                    end_message += "Ustanowiłeś nowy najwyższy wynik!\n";
                }
                else
                {
                    end_message += "Gratulacje! Pobiłeś swój obecny wynik o: " + (Integer.parseInt(tempsArray[0])-obecny_wynik)  + " ms\n";
                }
                try {
                FileWriter myWriter = new FileWriter("scores.txt", false);
                tempsArray[0] = String.valueOf(obecny_wynik);
                    for (String s : tempsArray) {
                        myWriter.write(s + "\n");
                        System.out.println(s);
                    }
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
            else if (Integer.parseInt(tempsArray[0]) == obecny_wynik)
            {
                end_message += "Wyrównałeś do najwyższego wyniku\n";
            }
            else
            {
                end_message += "Nie udało Ci się pobić najwyższego wyniku\n";
            }

            if(Integer.parseInt(tempsArray[1]) > obecny_wynik) {
                if (Integer.parseInt(tempsArray[1]) != 2147483647) {
                    end_message += "Gratulacje! Pobiłeś swój poprzedni wynik o: " + ((Integer.parseInt(tempsArray[1])) - obecny_wynik) + " ms\n";
                }
                else {
                    end_message += "";
                }
            }
            else if (Integer.parseInt(tempsArray[1]) == obecny_wynik)
            {
                end_message += "Osiągnałeś taki sam wynik jak ostatnio\n";
            }
            else
            {
                end_message += "Nie udało Ci się pobić ostatniego wyniku\n";
            }
            try {
                FileWriter myWriter = new FileWriter("scores.txt", false);
                tempsArray[1] = String.valueOf(obecny_wynik);
                for (String s : tempsArray) {
                    myWriter.write(s + "\n");
                    System.out.println(s);
                }
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(obecny_wynik);
        JOptionPane.showMessageDialog(null, end_message,
                "Podsumowanie", JOptionPane.INFORMATION_MESSAGE);

    }

    public void playing_audio()
    {
        beggining_time = -1;
        Random rng = new Random();
        Timer timer = new Timer(rng.nextInt(1000) + 3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("beep.wav").getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    beggining_time = System.currentTimeMillis();
                } catch(Exception ex) {
                    System.out.println("Error while trying to play the song.");
                    ex.printStackTrace();
                }
            }
        });

        timer.setRepeats(false);
        timer.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == audio_played) {
            if (number_of_rounds == -1) {
                //runda testowa na poczatku
                if (beggining_time == -1) {
                    JOptionPane.showMessageDialog(null, """
                                    Nacisnales przycisk za szybko!
                                    Pamietaj, zeby nacisnac przycisk dopiero,
                                    kiedy uslyszysz dzwiek.""",
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
                    playing_audio();
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
                        long avg_overall = (average_of_times+visual_reaction_time)/2;

                        JOptionPane.showMessageDialog(null, "Twoj sredni czas reakcji to: " +
                                        average_of_times + " ms\n",
                                "Koniec poziomu!", JOptionPane.INFORMATION_MESSAGE);
                        JOptionPane.showMessageDialog(null, "Twoj sredni czas reakcji " +
                                        "podczas calego testu wynisol:\n" +
                                        avg_overall + " ms\n",
                                "Koniec testu!", JOptionPane.INFORMATION_MESSAGE);
                        czy_lepszy_wynik(avg_overall);
                        System.exit(0);
                    }
                    number_of_rounds++;
                    playing_audio();
                }
            }
        }
    }
}
