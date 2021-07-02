package org.oppia.android.scripts.testfile

import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.oppia.android.scripts.common.TEST_FILE_CHECK_FAILED_OUTPUT_INDICATOR
import org.oppia.android.scripts.common.TEST_FILE_CHECK_PASSED_OUTPUT_INDICATOR
import org.oppia.android.testing.assertThrows
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/** Tests for [TestFileCheck]. */
class TestFileCheckTest {
  private val outContent: ByteArrayOutputStream = ByteArrayOutputStream()
  private val originalOut: PrintStream = System.out

  @Rule
  @JvmField
  var tempFolder = TemporaryFolder()

  @Before
  fun setUp() {
    tempFolder.newFolder("testfiles")
    System.setOut(PrintStream(outContent))
  }

  @After
  fun restoreStreams() {
    System.setOut(originalOut)
  }

  @Test
  fun testTestFileCheck_prodFileWithCorrespondingTestFile_testFileIsPresent() {
    tempFolder.newFile("testfiles/ProdFile.kt")
    tempFolder.newFile("testfiles/ProdFileTest.kt")

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(TEST_FILE_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testTestFileCheck_missTestFile_testFileIsNotPresent() {
    tempFolder.newFile("testfiles/ProdFile1.kt")
    tempFolder.newFile("testfiles/ProdFile1Test.kt")
    tempFolder.newFile("testfiles/ProdFile2.kt")

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(TEST_FILE_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      No test file found for:
      - ${retrieveTestFilesDirectoryPath()}/ProdFile2.kt
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testTestFileCheck_missTestFilesForMultipleProdFiles_testFileIsNotPresent() {
    tempFolder.newFile("testfiles/ProdFile1.kt")
    tempFolder.newFile("testfiles/ProdFile1Test.kt")
    tempFolder.newFile("testfiles/ProdFile2.kt")
    tempFolder.newFile("testfiles/ProdFile3.kt")

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(TEST_FILE_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      No test file found for:
      - ${retrieveTestFilesDirectoryPath()}/ProdFile3.kt
      - ${retrieveTestFilesDirectoryPath()}/ProdFile2.kt
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  /** Retrieves the absolute path of testfiles directory. */
  private fun retrieveTestFilesDirectoryPath(): String {
    return "${tempFolder.root}/testfiles"
  }

  /** Runs the test_file_check. */
  private fun runScript() {
    main(retrieveTestFilesDirectoryPath())
  }
}
