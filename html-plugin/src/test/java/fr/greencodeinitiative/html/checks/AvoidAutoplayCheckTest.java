package fr.greencodeinitiative.html.checks;

import java.io.File;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.sonar.plugins.html.visitor.HtmlSourceCode;

@EnableRuleMigrationSupport
public class AvoidAutoplayCheckTest {
    
    @Rule
    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

    @Test
    public void should_detect_on_html_documents() {
        HtmlSourceCode sourceCode = TestHelper.scan(new File("src/test/resources/checks/AvoidAutoplayCheck/document.html"), new AvoidAutoplayCheck());
        
        checkMessagesVerifier.verify(sourceCode.getIssues())
            .next().atLine(7).withMessage("Remove autoplay attribute to this audio")
            .next().atLine(9).withMessage("Remove autoplay attribute to this video");
    }
    
    @Test
    public void should_detect_on_php_documents() {
        HtmlSourceCode sourceCode = TestHelper.scan(new File("src/test/resources/checks/AvoidAutoplayCheck/document.php"), new AvoidAutoplayCheck());
        
        checkMessagesVerifier.verify(sourceCode.getIssues())
            .next().atLine(6).withMessage("Remove autoplay attribute to this audio")
            .next().atLine(9).withMessage("Remove autoplay attribute to this video");
    }
    
    @Test
    public void should_detect_on_jsp_documents() {
        HtmlSourceCode sourceCode = TestHelper.scan(new File("src/test/resources/checks/AvoidAutoplayCheck/document.jsp"), new AvoidAutoplayCheck());
        
        checkMessagesVerifier.verify(sourceCode.getIssues())
            .next().atLine(7).withMessage("Remove autoplay attribute to this audio")
            .next().atLine(9).withMessage("Remove autoplay attribute to this video");
    }
    
}
