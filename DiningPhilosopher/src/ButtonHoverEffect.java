
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonHoverEffect extends MouseAdapter {

    private Color originalColor;

    public ButtonHoverEffect() {
        super();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Save the original color
        originalColor = e.getComponent().getBackground();

        // Lighten the background color, hsb means "Hue, Saturation, and Brightness"
        float[] hsb = Color.RGBtoHSB(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(), null);
        Color lighterColor = Color.getHSBColor(hsb[0], hsb[1], Math.min(1.0f, hsb[2] + 0.2f));
        e.getComponent().setBackground(lighterColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Restore the original color
        e.getComponent().setBackground(originalColor);
    }
}

