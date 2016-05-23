package org.qcmix.post_processing;

/**
 * Theses are flags (search what flag fields are).
 * They must be powers of 2.
 * When calling a method, they can be combined with |
 * <pre>
 * {@code
 * myMethod(REMOVE_CELLS_STYLE | REMOVE_ALL_COLORS);
 *
 * public myMethod(PostProcessingOptions flags) {
 *     if ((optionFlags & PostProcessingOptions.REMOVE_ALL_COLORS) != 0) {
 *         removeAllColors();
 *     }
 * }
 * }
 * </pre>
 */
public class PostProcessingOptions {
	/**
	 * Remove style applied to an *entire* cell.
	 * Example: There are cells that bold, passing this to the post processor
	 * will make it remove the boldness.
	 */
	public static int REMOVE_CELLS_STYLE = 1;

	/**
	 * Change all the colors defined in styles.xml to black.
	 * See the "ODS-format" wiki page for more details.
	 */
	public static int REMOVE_ALL_COLORS = 2;
}
