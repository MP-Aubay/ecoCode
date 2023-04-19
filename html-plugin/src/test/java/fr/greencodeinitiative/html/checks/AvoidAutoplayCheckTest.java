package fr.greencodeinitiative.html.checks;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.sonar.plugins.html.visitor.HtmlSourceCode;

public class AvoidAutoplayCheckTest {
    
    @Test
    public void should_detect_on_html_documents() {
        HtmlSourceCode sourceCode = TestHelper.scan(new File("src/test/resources/checks/AvoidAutoplayCheck/document.html"), new AvoidAutoplayCheck());
        
        CheckMessagesVerifier.verify(sourceCode.getIssues())
            .next().atLine(7).withMessage("Avoid using autoplay attribute in audio element")
            .next().atLine(9).withMessage("Avoid using autoplay attribute in video element")
            .noMore();
    }
    
    @Test
    public void should_detect_on_php_documents() {
        HtmlSourceCode sourceCode = TestHelper.scan(new File("src/test/resources/checks/AvoidAutoplayCheck/document.php"), new AvoidAutoplayCheck());
        
        CheckMessagesVerifier.verify(sourceCode.getIssues())
            .next().atLine(6).withMessage("Avoid using autoplay attribute in audio element")
            .next().atLine(9).withMessage("Avoid using autoplay attribute in video element")
            .noMore();
    }
    
    @Test
    public void should_detect_on_jsp_documents() {
        HtmlSourceCode sourceCode = TestHelper.scan(new File("src/test/resources/checks/AvoidAutoplayCheck/document.jsp"), new AvoidAutoplayCheck());
        
        CheckMessagesVerifier.verify(sourceCode.getIssues())
            .next().atLine(7).withMessage("Avoid using autoplay attribute in audio element")
            .next().atLine(9).withMessage("Avoid using autoplay attribute in video element")
            .noMore();
    }
    
}
