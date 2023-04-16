package fr.greencodeinitiative.html.checks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.plugins.html.analyzers.ComplexityVisitor;
import org.sonar.plugins.html.analyzers.PageCountLines;
import org.sonar.plugins.html.api.HtmlConstants;
import org.sonar.plugins.html.lex.PageLexer;
import org.sonar.plugins.html.lex.VueLexer;
import org.sonar.plugins.html.visitor.DefaultNodeVisitor;
import org.sonar.plugins.html.visitor.HtmlAstScanner;
import org.sonar.plugins.html.visitor.HtmlSourceCode;

public class TestHelper {

    private TestHelper() {
    }

    public static HtmlSourceCode scan(File file, DefaultNodeVisitor visitor) {
      FileReader fileReader;
      try {
        fileReader = new FileReader(file);
      } catch (FileNotFoundException e) {
        throw new IllegalStateException(e);
      }

      HtmlSourceCode result = new HtmlSourceCode(
        new TestInputFileBuilder("key", file.getPath())
          .setLanguage(HtmlConstants.LANGUAGE_KEY)
          .setType(InputFile.Type.MAIN)
          .setModuleBaseDir(new File(".").toPath())
          .setCharset(StandardCharsets.UTF_8)
          .build()
      );

      HtmlAstScanner walker = new HtmlAstScanner(List.of(new PageCountLines(), new ComplexityVisitor()));
      PageLexer lexer = file.getName().endsWith(".vue") ? new VueLexer() : new PageLexer();
      walker.addVisitor(visitor);
      walker.scan(
        lexer.parse(fileReader),
        result
      );

      return result;
    }
}