package Challenge;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * SpriteConverter is an abstract class used to edit sprites before
 * they are rendered into the game, resizing and rotating etc
 * @author George Carpenter
 * @version 2.0
 */
abstract class SpriteConverter {

    /**
     * Resize a Sprite
     * @param image the sprite to resize
     * @param height the height of the new sprite
     * @param width the width of the new sprite
     * @return the resized sprite
     */
    static Image resize(Image image, int height, int width) {

        // Read Image
        ImageView imageView = new ImageView(image);

        // Resize
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);

        // Capture it? I think
        SnapshotParameters param = new SnapshotParameters();

        param.setFill(Color.TRANSPARENT);

        return imageView.snapshot(param, null);

    }

    /**
     * Rotate a Sprite
     * @param image the sprite to rotate
     * @param direction the amount of rotation
     * @return the rotated sprite
     */
    static Image rotate(Image image, int direction) {

        // Read Image
        ImageView imageView = new ImageView(image);

        // Rotate
        imageView.setRotate(90 * direction);

        // Capture it? I think
        SnapshotParameters param = new SnapshotParameters();

        param.setFill(Color.TRANSPARENT);

        return imageView.snapshot(param, null);
    }

    // This does not work but I've left it for posterity - Gnome
    static Image flip(Image image) {

        // Read Image
        ImageView imageView = new ImageView(image);

        // Flip - nope
        imageView.setScaleX(-1);

        // Capture it? I think
        SnapshotParameters param = new SnapshotParameters();

        param.setFill(Color.TRANSPARENT);

        return imageView.snapshot(param, null);
    }

}
