package org.jas.util;

import java.io.Serializable;

import javax.swing.ImageIcon;

/**
 *
 *
 *
 *
 * @author í£Å@äwåR
 * @version 1.0
 */

public class ImageManager implements Serializable {

	public static ImageIcon createImageIcon(String filename) {
	    String dirPath = "/org/jas/util/resource/images/";
	    return createImageIcon(dirPath, filename);
    }

    public static ImageIcon createImageIcon(String dirName, String filename) {
	    String path = dirName + filename;
	    return new ImageIcon(ImageManager.class.getResource(path));
    }
}