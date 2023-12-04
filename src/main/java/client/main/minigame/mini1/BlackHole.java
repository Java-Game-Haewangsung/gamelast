import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BlackHole extends JLabel {
    private List<Image> blackHoleImages;
    private int currentImageIndex;
    private int setW = 512, setH = 256;
    private String blackHoleImgPath = "img/blackHole";

    public BlackHole(int x, int y) {
        this.blackHoleImages = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Image image = new ImageIcon(getClass().getResource(blackHoleImgPath + (i + 1) + ".png")).getImage();
            Image scaledImage = image.getScaledInstance(setW, setH, Image.SCALE_SMOOTH);
            this.blackHoleImages.add(scaledImage);
        } this.currentImageIndex = 0;

        appear();
        setBounds(x, y, setW, setH);
        setSize(setW, setH);

        disappear();
    }

    public void appear() {
        currentImageIndex = 5;
        Timer appearTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentImageIndex > 0) {
                    currentImageIndex--;
                    setIcon(new ImageIcon(blackHoleImages.get(currentImageIndex)));
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();  // 타이머 중지
                }
            }
        });
        appearTimer.start();
        setIcon(new ImageIcon(blackHoleImages.get(currentImageIndex)));
    }


    public void disappear() {
        new Thread(() -> {
            try {
                Thread.sleep(6000);
                currentImageIndex = 0;
                while (currentImageIndex < 5) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    currentImageIndex++;
                    setIcon(new ImageIcon(blackHoleImages.get(currentImageIndex)));
                    repaint();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public Image getImage() {
        return blackHoleImages.get(currentImageIndex);
    }
}
