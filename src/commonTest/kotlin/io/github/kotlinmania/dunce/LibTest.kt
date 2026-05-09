// port-lint: source src/lib.rs
package io.github.kotlinmania.dunce

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LibTest {

    @Test
    fun trimTest() {
        assertEquals("a", rightTrim("a."))
        assertEquals("ą", rightTrim("ą."))
        assertEquals("a", rightTrim("a "))
        assertEquals("ąą", rightTrim("ąą "))
        assertEquals("a", rightTrim("a. . . ....   "))
        assertEquals("a. . . ..ź", rightTrim("a. . . ..ź..   "))
        assertEquals(" b", rightTrim(" b"))
        assertEquals(" べ", rightTrim(" べ"))
        assertEquals("c. c", rightTrim("c. c."))
        assertEquals("。", rightTrim("。"))
        assertEquals("", rightTrim(""))
    }

    @Test
    fun reserved() {
        assertTrue(isReserved("CON"))
        assertTrue(isReserved("con"))
        assertTrue(isReserved("con.con"))
        assertTrue(isReserved("COM4"))
        assertTrue(isReserved("COM4.txt"))
        assertTrue(isReserved("COM4 .txt"))
        assertTrue(isReserved("con."))
        assertTrue(isReserved("con ."))
        assertTrue(isReserved("con  "))
        assertTrue(isReserved("con . "))
        assertTrue(isReserved("con . .txt"))
        assertTrue(isReserved("con.....txt"))
        assertTrue(isReserved("PrN....."))

        assertFalse(isReserved(" PrN....."))
        assertFalse(isReserved(" CON"))
        assertFalse(isReserved("COM0"))
        assertFalse(isReserved("COM77"))
        assertFalse(isReserved(" CON "))
        assertFalse(isReserved(".CON"))
        assertFalse(isReserved("@CON"))
        assertFalse(isReserved("not.CON"))
        assertFalse(isReserved("CON。"))
    }

    @Test
    fun len() {
        assertEquals(1, windowsCharLen("a"))
        assertEquals(1, windowsCharLen("€"))
        assertEquals(1, windowsCharLen("本"))
        assertEquals(2, windowsCharLen("🧐"))
        assertEquals(2, windowsCharLen("®®"))
    }

    @Test
    fun valid() {
        assertFalse(isValidFilename(".."))
        assertFalse(isValidFilename("."))
        assertFalse(isValidFilename("aaaaaaaaaa:"))
        assertFalse(isValidFilename("ą:ą"))
        assertFalse(isValidFilename(""))
        assertFalse(isValidFilename("a "))
        assertFalse(isValidFilename(" a. "))
        assertFalse(isValidFilename("a/"))
        assertFalse(isValidFilename("/a"))
        assertFalse(isValidFilename("/"))
        assertFalse(isValidFilename("\\"))
        assertFalse(isValidFilename("\\a"))
        assertFalse(isValidFilename("<x>"))
        assertFalse(isValidFilename("a*"))
        assertFalse(isValidFilename("?x"))
        assertFalse(isValidFilename("a\u0000a"))
        assertFalse(isValidFilename("\u001F"))
        assertFalse(isValidFilename("a".repeat(257)))

        assertTrue(isValidFilename("®".repeat(254)))
        assertTrue(isValidFilename("ファイル"))
        assertTrue(isValidFilename("a"))
        assertTrue(isValidFilename("a.aaaaaaaa"))
        assertTrue(isValidFilename("a........a"))
        assertTrue(isValidFilename("       b"))
    }

    @Test
    fun strip() {
        assertEquals("C:\\foo\\😀", simplified("\\\\?\\C:\\foo\\😀"))
        assertEquals("\\\\?\\serv\\", simplified("\\\\?\\serv\\"))
        assertEquals("\\\\.\\C:\\notdisk", simplified("\\\\.\\C:\\notdisk"))
        assertEquals(
            "\\\\?\\GLOBALROOT\\Device\\ImDisk0\\path\\to\\file.txt",
            simplified("\\\\?\\GLOBALROOT\\Device\\ImDisk0\\path\\to\\file.txt"),
        )
    }

    @Test
    fun safe() {
        assertTrue(isSafeToStripUnc("\\\\?\\C:\\foo\\bar"))
        assertTrue(isSafeToStripUnc("\\\\?\\Z:\\foo\\bar\\"))
        assertTrue(isSafeToStripUnc("\\\\?\\Z:\\😀\\🎃\\"))
        assertTrue(isSafeToStripUnc("\\\\?\\c:\\foo"))

        val long = "®".repeat(160)
        assertTrue(isSafeToStripUnc("\\\\?\\c:\\$long"))
        assertFalse(isSafeToStripUnc("\\\\?\\c:\\$long\\$long"))

        assertFalse(isSafeToStripUnc("\\\\?\\C:\\foo\\.\\bar"))
        assertFalse(isSafeToStripUnc("\\\\?\\C:\\foo\\..\\bar"))
        assertFalse(isSafeToStripUnc("\\\\?\\c\\foo"))
        assertFalse(isSafeToStripUnc("\\\\?\\c\\foo/bar"))
        assertFalse(isSafeToStripUnc("\\\\?\\c:foo"))
        assertFalse(isSafeToStripUnc("\\\\?\\cc:foo"))
        assertFalse(isSafeToStripUnc("\\\\?\\c:foo\\bar"))
    }
}
