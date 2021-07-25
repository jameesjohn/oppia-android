package org.oppia.android.scripts.docs

import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.oppia.android.scripts.proto.KDocValidityExemptions
import org.oppia.android.testing.assertThrows
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

/** Tests for [KDocValidityCheck]. */
class KDocValidityCheckTest {
  private val outContent: ByteArrayOutputStream = ByteArrayOutputStream()
  private val originalOut: PrintStream = System.out
  private val KDOC_CHECK_PASSED_OUTPUT_INDICATOR = "KDOC VALIDITY CHECK PASSED"
  private val KDOC_CHECK_FAILED_OUTPUT_INDICATOR = "KDOC VALIDITY CHECK FAILED"
  private val pathToProtoBinary = "scripts/assets/kdoc_validity_exemptions.pb"

  @Rule
  @JvmField
  var tempFolder = TemporaryFolder()

  @Before
  fun setUp() {
    tempFolder.newFolder("testfiles")
    tempFolder.newFolder("scripts", "assets")
    tempFolder.newFile(pathToProtoBinary)
    System.setOut(PrintStream(outContent))
  }

  @After
  fun restoreStreams() {
    System.setOut(originalOut)
  }

  @Test
  fun testKDoc_functionWithKDoc_checkShouldPass() {
    val testContent =
      """
      /** 
       * Returns the string corresponding to this error's string resources, or null if there 
       * is none.
       */
      fun getErrorMessageFromStringRes(context: Context): String? {
        return error?.let(context::getString)
      }
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_privateMemberWithoutKDoc_checkShouldPass() {
    val testContent =
      """
      private fun getErrorMessageFromStringRes(context: Context): String? {
        return error?.let(context::getString)
      }
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_memberWithOverrideModifierWithoutKDoc_checkShouldPass() {
    val testContent =
      """
      override fun getErrorMessageFromStringRes(context: Context): String? {
        return error?.let(context::getString)
      }
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_functionWithTestAnnotationWithoutKDoc_checkShouldPass() {
    val testContent =
      """
      @Test
      fun getErrorMessageFromStringRes(context: Context): String? {
        return error?.let(context::getString)
      }
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_functionWithBeforeAnnotationWithoutKDoc_checkShouldPass() {
    val testContent =
      """
      @Before
      fun getErrorMessageFromStringRes(context: Context): String? {
        return error?.let(context::getString)
      }
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_functionWithBeforeClassAnnotationWithoutKDoc_checkShouldPass() {
    val testContent =
      """
      @BeforeClass
      fun getErrorMessageFromStringRes(context: Context): String? {
        return error?.let(context::getString)
      }
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_functionWithAfterAnnotationWithoutKDoc_checkShouldPass() {
    val testContent =
      """
      @After
      fun getErrorMessageFromStringRes(context: Context): String? {
        return error?.let(context::getString)
      }
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_functionWithAfterClassAnnotationWithoutKDoc_checkShouldPass() {
    val testContent =
      """
      @AfterClass
      fun getErrorMessageFromStringRes(context: Context): String? {
        return error?.let(context::getString)
      }
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_fieldWithRuleAnnotationWithoutKDoc_checkShouldPass() {
    val testContent =
      """
      @Rule
      @JvmField
      val mockitoRule: MockitoRule = MockitoJUnit.rule()
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_fieldWithMockAnnotationWithoutKDoc_checkShouldPass() {
    val testContent =
      """
      @Mock
      lateinit var mockCellularDataObserver: Observer<AsyncResult<CellularDataPreference>>
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_fieldWithCaptorAnnotationWithoutKDoc_checkShouldPass() {
    val testContent =
      """
      @Captor
      lateinit var regionClickedEvent: ArgumentCaptor<RegionClickedEvent>
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_functionWithProvidesAnnotationWithoutKDoc_checkShouldPass() {
    val testContent =
      """
      @Provides
      @ExplorationStorageDatabaseSize
      fun provideExplorationStorageDatabaseSize(): Int = 150
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_fieldWithInjectAnnotationWithoutKDoc_checkShouldPass() {
    val testContent =
      """
      @Inject
      lateinit var administratorControlsViewModel: AdministratorControlsViewModel
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_constructorWithInjectAnnotationWithoutKDoc_checkShouldPass() {
    val testContent =
      """
      /** Test Class. */
      class SelectionFragmentModel{

          @Inject
          constructor(channelInfosRepository: ChannelInfosRepository) : super() {
              this.channelInfosRepository = channelInfosRepository
          }
      }
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_injectPrimaryConstructor_noSpecialBehaviour_checkShouldPass() {
    val testContent =
      """
      /** Test Class. */
      class SelectionFragmentModel @Inject constructor(){
      }
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_classMissingKDoc_checkShouldFail() {
    val testContent =
      """
      class TestClass {}  
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:1
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_nestedClassMissingKDoc_checkShouldFail() {
    val testContent =
      """
      /** Test KDoc 1. */
      class TestClass {
        /** Test KDoc 2. */
        class NestedClass {
          class NestedLevel2Class {}
        }
      }  
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:5
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_classMembersMissingKDoc_checkShouldFail() {
    val testContent =
      """
      /** Test KDoc 1. */
      class TestClass {
        val testVal = "test"
        
        fun testFunc(){}
      }  
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:3
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:5
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_fieldsMissingKDoc_checkShouldFail() {
    val testContent =
      """
      val testVal = "dddd"
      var testVar = true
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:1
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:2
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_blockCommentInsteadOfKDoc_checkShouldFail() {
    val testContent =
      """
      // Test block comment.
      val testVal = "test content"
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:2
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_objectMissingKdoc_checkShouldFail() {
    val testContent =
      """
      object TestObject {
          const val answer1 = 42
          const val answer2 = 43
      }
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:1
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:2
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:3
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_constantsMissingKdoc_checkShouldFail() {
    val testContent =
      """
      const val TABLE_USER_ATTRIBUTE_EMPID = "_id"
      
      const val TABLE_USER_ATTRIBUTE_DATA = "data"
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:1
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:3
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_functionMissingKDoc_checkShouldFail() {
    val testContent =
      """
      fun getErrorMessageFromStringRes(context: Context): String? {
        return error?.let(context::getString)
      }  
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:1
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_constructorMissingKDoc_checkShouldFail() {
    val testContent =
      """
      /** Test KDoc. */
      class TestClass {
        constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
      }
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:3
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_enumMissingKDoc_checkShouldFail() {
    val testContent =
      """
      enum class WalkthroughPages(val value: Int) {
        WELCOME(0),
        TOPIC_LIST(1),
        FINAL(2)
      }  
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:1
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:2
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:3
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:4
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_nestedInterfaceMissingKDoc_checkShouldFail() {
    val testContent =
      """
      interface ChapterSelector {
        
        fun chapterSelected(chapterIndex: Int, nextStoryIndex: Int, explorationId: String)
      
        fun chapterUnselected(chapterIndex: Int, nextStoryIndex: Int)
        
        interface ChildInterface {
          fun testFunction()
        }
      }  
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:1
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:3
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:5
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:7
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:8
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_companionObjectMissingKDoc_checkShouldFail() {
    val testContent =
      """
      /** Test KDoc. */
      class TestClass {
        companion object {
          val pos = 1
          
          fun incrementedPosition(position: Int): Int {
            return position+1
          }
          
          fun decrementedPosition(position: Int): Int {
            return position-1
          }
        }
      }
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:4
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:6
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:10
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_annotationMissingKDoc_checkShouldFail() {
    val testContent =
      """
      import javax.inject.Qualifier
      
      @Qualifier
      annotation class DelayShowAdditionalHintsFromWrongAnswerMillis
      """.trimIndent()
    val tempFile = tempFolder.newFile("testfiles/TempFile.kt")
    tempFile.writeText(testContent)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile.kt:4
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_multipleFilesMissingKDoc_multipleFailures_logsShouldBeLexicographicallySorted() {
    val testContent1 =
      """
      import javax.inject.Qualifier
      
      @Qualifier
      annotation class DelayShowAdditionalHintsFromWrongAnswerMillis
      """.trimIndent()
    val testContent2 =
      """
      class TestClass {
        fun testFunc(){}
        
        private val testVal = "test"
        
        val testVal2 = "test2"
      }
      """.trimIndent()
    val testContent3 =
      """
      val testVal = "abc"
      """.trimIndent()
    val tempFile1 = tempFolder.newFile("testfiles/TempFile1.kt")
    val tempFile2 = tempFolder.newFile("testfiles/TempFile2.kt")
    val tempFile3 = tempFolder.newFile("testfiles/TempFile3.kt")
    tempFile1.writeText(testContent1)
    tempFile2.writeText(testContent2)
    tempFile3.writeText(testContent3)

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/TempFile1.kt:4
      - ${retrieveTestFilesDirectoryPath()}/TempFile2.kt:1
      - ${retrieveTestFilesDirectoryPath()}/TempFile2.kt:2
      - ${retrieveTestFilesDirectoryPath()}/TempFile2.kt:6
      - ${retrieveTestFilesDirectoryPath()}/TempFile3.kt:1
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_exemptedFileMissingKDocs_checkShouldPass() {
    val testContent =
      """
      fun getErrorMessageFromStringRes(context: Context): String? {
        return error?.let(context::getString)
      }
      """.trimIndent()
    tempFolder.newFolder(
      "testfiles", "app", "src", "main", "java", "org", "oppia", "android", "app", "home"
    )
    val exemptedFile = tempFolder.newFile(
      "testfiles/app/src/main/java/org/oppia/android/app/home/HomeActivity.kt"
    )
    exemptedFile.writeText(testContent)
    val exemptionFile = File("${tempFolder.root}/$pathToProtoBinary")
    val builder = KDocValidityExemptions.newBuilder()
    builder.addExemptedFilePath("app/src/main/java/org/oppia/android/app/home/HomeActivity.kt")
    val exemptions = builder.build()
    exemptions.writeTo(exemptionFile.outputStream())

    runScript()

    assertThat(outContent.toString().trim()).isEqualTo(KDOC_CHECK_PASSED_OUTPUT_INDICATOR)
  }

  @Test
  fun testKDoc_redundantExemption_checkShouldFail() {
    val testContent =
      """
      /** test Kdoc. */
      fun getErrorMessageFromStringRes(context: Context): String? {
        return error?.let(context::getString)
      }
      """.trimIndent()
    tempFolder.newFolder(
      "testfiles", "app", "src", "main", "java", "org", "oppia", "android", "app", "home"
    )
    val exemptedFile = tempFolder.newFile(
      "testfiles/app/src/main/java/org/oppia/android/app/home/HomeActivity.kt"
    )
    exemptedFile.writeText(testContent)
    val exemptionFile = File("${tempFolder.root}/$pathToProtoBinary")
    val builder = KDocValidityExemptions.newBuilder()
    builder.addExemptedFilePath("app/src/main/java/org/oppia/android/app/home/HomeActivity.kt")
    val exemptions = builder.build()
    exemptions.writeTo(exemptionFile.outputStream())

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      Redundant exemptions:
      - app/src/main/java/org/oppia/android/app/home/HomeActivity.kt
      Please remove them from scripts/assets/kdoc_validity_exemptions.textproto
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  @Test
  fun testKDoc_addRedundantExemption_missingKdoc_allFailuresShouldLog() {
    val testContent =
      """
      fun getErrorMessageFromStringRes(context: Context): String? {
        return error?.let(context::getString)
      }
      """.trimIndent()
    val exemptedFile = tempFolder.newFile(
      "testfiles/HomeActivity.kt"
    )
    exemptedFile.writeText(testContent)
    val exemptionFile = File("${tempFolder.root}/$pathToProtoBinary")
    val builder = KDocValidityExemptions.newBuilder()
    builder.addExemptedFilePath("app/src/main/java/org/oppia/android/app/splash/SplashActivity.kt")
    val exemptions = builder.build()
    exemptions.writeTo(exemptionFile.outputStream())

    val exception = assertThrows(Exception::class) {
      runScript()
    }

    assertThat(exception).hasMessageThat().contains(KDOC_CHECK_FAILED_OUTPUT_INDICATOR)
    val failureMessage =
      """
      Redundant exemptions:
      - app/src/main/java/org/oppia/android/app/splash/SplashActivity.kt
      Please remove them from scripts/assets/kdoc_validity_exemptions.textproto
      
      KDoc missing for files:
      - ${retrieveTestFilesDirectoryPath()}/HomeActivity.kt:1
      """.trimIndent()
    assertThat(outContent.toString().trim()).isEqualTo(failureMessage)
  }

  /** Retrieves the absolute path of testfiles directory. */
  private fun retrieveTestFilesDirectoryPath(): String {
    return "${tempFolder.root}/testfiles"
  }

  /** Runs the kdoc_validity_check. */
  private fun runScript() {
    main(retrieveTestFilesDirectoryPath(), "${tempFolder.root}/$pathToProtoBinary")
  }
}
