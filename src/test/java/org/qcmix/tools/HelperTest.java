package org.qcmix.tools;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class HelperTest {

	@Test
	public void testGetFileExtentionOds() {
		File file = new File("/foo/bar/My file name.ods");
		String extention = Helper.getFileExtention(file);
		assertEquals("ods", extention);
	}

	@Test
	public void testGetFileExtentionXls() {
		File file = new File("/foo/bar/My file name.xls");
		String extention = Helper.getFileExtention(file);
		assertEquals("xls", extention);
	}

	@Test
	public void testGetFileExtentionXlsx() {
		File file = new File("/foo/bar/My file name.xlsx");
		String extention = Helper.getFileExtention(file);
		assertEquals("xlsx", extention);
	}
}