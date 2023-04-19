package fr.greencodeinitiative.html.core;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.html.api.HtmlConstants;
import org.sonar.plugins.html.checks.AbstractPageCheck;
import org.sonar.plugins.html.checks.HtmlIssue;
import org.sonar.plugins.html.checks.PreciseHtmlIssue;
import org.sonar.plugins.html.lex.PageLexer;
import org.sonar.plugins.html.lex.VueLexer;
import org.sonar.plugins.html.visitor.HtmlAstScanner;
import org.sonar.plugins.html.visitor.HtmlSourceCode;

import fr.greencodeinitiative.html.rules.CheckClasses;
import fr.greencodeinitiative.html.rules.HtmlRulesDefinition;

public class HtmlSensor implements Sensor {
    private static final Logger LOG = Loggers.get(HtmlSensor.class);
    private static final String[] OTHER_FILE_SUFFIXES = {"php", "php3", "php4", "php5", "phtml", "inc", "vue"};

    private final Checks<Object> checks;

    public HtmlSensor(CheckFactory checkFactory) {
      this.checks = checkFactory.create(HtmlRulesDefinition.REPOSITORY_KEY).addAnnotatedChecks((Iterable<Class<?>>) CheckClasses.getCheckClasses());
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
      descriptor
        .name("ecoCodeHtmlSensor")
        .onlyOnLanguage(HtmlConstants.LANGUAGE_KEY)
        .createIssuesForRuleRepository(HtmlRulesDefinition.REPOSITORY_KEY);
    }

    @Override
    public void execute(SensorContext sensorContext) {
      FileSystem fileSystem = sensorContext.fileSystem();

      // configure page scanner and the visitors
      final HtmlAstScanner scanner = setupScanner(sensorContext);

      FilePredicates predicates = fileSystem.predicates();
      Iterable<InputFile> inputFiles = fileSystem.inputFiles(
        predicates.and(
          predicates.hasType(InputFile.Type.MAIN),
          predicates.or(
            predicates.hasLanguages(HtmlConstants.LANGUAGE_KEY, HtmlConstants.JSP_LANGUAGE_KEY),
            predicates.or(Stream.of(OTHER_FILE_SUFFIXES).map(predicates::hasExtension).toArray(FilePredicate[]::new))
            )
      ));

      for (InputFile inputFile : inputFiles) {
        if (sensorContext.isCancelled()) {
          return;
        }

        HtmlSourceCode sourceCode = new HtmlSourceCode(inputFile);

        try (Reader reader = new InputStreamReader(inputFile.inputStream(), inputFile.charset())) {
          PageLexer lexer = inputFile.filename().endsWith(".vue") ? new VueLexer() : new PageLexer();
          scanner.scan(lexer.parse(reader), sourceCode);
          saveMetrics(sensorContext, sourceCode);

        } catch (Exception e) {
          LOG.error("Cannot analyze file " + inputFile, e);
          sensorContext.newAnalysisError()
            .onFile(inputFile)
            .message(e.getMessage())
            .save();
        }
      }
    }

    private static void saveMetrics(SensorContext context, HtmlSourceCode sourceCode) {
      InputFile inputFile = sourceCode.inputFile();

      for (HtmlIssue issue : sourceCode.getIssues()) {
        NewIssue newIssue = context.newIssue()
          .forRule(issue.ruleKey())
          .gap(issue.cost());
        NewIssueLocation location = locationForIssue(inputFile, issue, newIssue);
        newIssue.at(location);
        newIssue.save();
      }
    }

    private static NewIssueLocation locationForIssue(InputFile inputFile, HtmlIssue issue, NewIssue newIssue) {
      NewIssueLocation location = newIssue.newLocation()
        .on(inputFile)
        .message(issue.message());
      Integer line = issue.line();
      if (issue instanceof PreciseHtmlIssue) {
        PreciseHtmlIssue preciseHtmlIssue = (PreciseHtmlIssue) issue;
        location.at(inputFile.newRange(issue.line(),
          preciseHtmlIssue.startColumn(),
          preciseHtmlIssue.endLine(),
          preciseHtmlIssue.endColumn()));
      } else if (line != null) {
        location.at(inputFile.selectLine(line));
      }
      return location;
    }

    /**
     * Create PageScanner with Visitors.
     */
    private HtmlAstScanner setupScanner(SensorContext context) {
      HtmlAstScanner scanner = new HtmlAstScanner(new ArrayList<>());

      for (Object check : checks.all()) {
        ((AbstractPageCheck) check).setRuleKey(checks.ruleKey(check));
        scanner.addVisitor((AbstractPageCheck) check);
      }
      return scanner;
    }
}
