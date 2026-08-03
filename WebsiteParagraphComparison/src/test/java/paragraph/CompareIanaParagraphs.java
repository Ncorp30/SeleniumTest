package paragraph;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CompareIanaParagraphs {
    @Test
    public void compareParagraphs() {
        // Set path to ChromeDriver if not in system PATH
        // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        WebDriver driver = null;

        try {
            driver = new ChromeDriver();

            // Navigate to the website
            String url = "https://www.iana.org/help/example-domains";
            driver.get(url);

            // Locate the paragraph element using XPath
            WebElement paragraphElement = driver.findElement(By.xpath("//p[contains(text(),'We provide a web service on the example domain hos')]"));

            // Get actual paragraph text from the website
            String actualText = paragraphElement.getText().trim();

            // Read expected text from file
            String expectedText = readTextFromFile("expected_paragraphs.txt").trim();

            // Compare paragraphs and highlight differences
            if (actualText.equals(expectedText)) {
                System.out.println("✅ Paragraph matches expected text.");
            } else {
                System.out.println("❌ Mismatch found:");
                compareSentences(expectedText, actualText);  // ✅ Use sentence/word-level comparison
            }
            assertEquals(expectedText, actualText);

        } catch (Exception e) {
            fail(e);
        } finally {
            if (driver != null) {
                driver.quit();  // Always close the browser
            }
        }
    }

    // Read entire file content
    private static String readTextFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder contentBuilder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append(" ");
            }
            return contentBuilder.toString().trim();
        } finally {
            reader.close();
        }
    }

    // Compare sentence by sentence
    private static void compareSentences(String expected, String actual) {
        // Normalize spacing
        expected = expected.trim().replaceAll("\\s+", " ");
        actual = actual.trim().replaceAll("\\s+", " ");

        String[] expectedSentences = expected.split("(?<=[.!?])\\s+");
        String[] actualSentences = actual.split("(?<=[.!?])\\s+");

        int length = Math.max(expectedSentences.length, actualSentences.length);

        for (int i = 0; i < length; i++) {
            String expectedSentence = i < expectedSentences.length ? expectedSentences[i] : "[Missing]";
            String actualSentence = i < actualSentences.length ? actualSentences[i] : "[Missing]";

            if (!expectedSentence.equalsIgnoreCase(actualSentence)) {
                System.out.println("❌ Mismatch at sentence " + (i + 1) + ":");
                highlightMismatchWords(expectedSentence, actualSentence);
                System.out.println();  // Blank line for readability
            }
        }
    }

    // Compare word-by-word and print mismatches only
    private static void highlightMismatchWords(String expected, String actual) {
        String[] expectedWords = expected.trim().split("\\s+");
        String[] actualWords = actual.trim().split("\\s+");

        int length = Math.max(expectedWords.length, actualWords.length);
        boolean anyMismatch = false;

        for (int i = 0; i < length; i++) {
            String expectedWord = i < expectedWords.length ? expectedWords[i] : "[Missing]";
            String actualWord = i < actualWords.length ? actualWords[i] : "[Missing]";

            if (!expectedWord.equalsIgnoreCase(actualWord)) {
                anyMismatch = true;
                System.out.println("🔸 Word mismatch at position " + (i + 1) + ":");
                System.out.println("    ➤ Expected: \"" + expectedWord + "\"");
                System.out.println("    ➤ Actual:   \"" + actualWord + "\"");
            }
        }

        if (!anyMismatch) {
            System.out.println("⚠ Sentence differs only by punctuation or case.");
        }
    }
}
